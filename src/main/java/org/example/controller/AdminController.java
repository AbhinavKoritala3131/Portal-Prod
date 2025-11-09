package org.example.controller;


import org.example.dto.ApprovalStatusDTO;
import org.example.entity.*;
import org.example.repository.*;
import org.example.security.SSNEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HRRepository hrRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;


    //    FETCH TO SHOW ALL THE TIMESHEETS SUBMITTED IN THAT WEEK FOR ALL USERS
    @GetMapping("/timesheets-by-week")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<Timesheet>> getTimesheetsBySubmittedStatus(@RequestParam String week) {
        List<Timesheet> entries = timesheetRepository.findByWeekAndEmpIdIn(
                week,
                statusRepository.findEmpIdsByWeekWithSubmittedStatus(week)
        );
        return ResponseEntity.ok(entries);
    }


    //    FETCH TO SHOW ALL THE AGGREGATED TIMESHEETS SUBMITTED  FOR ALL USERS [ONE RECORD PER USER]
    @GetMapping("/status-by-week")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Status>> getPendingStatusByWeek(@RequestParam String week) {
        List<Status> pending = statusRepository.findByWeekAndStatusIgnoreCase(week, "SUBMITTED");
        return ResponseEntity.ok(pending);
    }

//      ADMIN DECISION[APPROVE/REJECT] IS UPDATED
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody ApprovalStatusDTO request) {
        Optional<Status> statusOpt = statusRepository.findByEmpIdAndWeek(request.getEmpId(), request.getWeek());

        if (statusOpt.isPresent()) {
            Status status = statusOpt.get();
            String newStatus = request.getStatus();
            String remarks = request.getRem();

            if ("APPROVED".equalsIgnoreCase(newStatus) || "REJECTED".equalsIgnoreCase(newStatus)) {
                // ✅ Update both status and remarks
                status.setStatus(newStatus.toUpperCase());
                status.setRemarks(remarks);
                statusRepository.save(status);

                if ("APPROVED".equalsIgnoreCase(newStatus)) {
                    HR hr = new HR();
                    hr.setEmployee_id(request.getEmpId());
                    hr.setWeek(request.getWeek());
                    hr.setHours(status.getTotal());
                    Optional<User> u = userRepository.findById(request.getEmpId());
                    if (u.isPresent()) {
                        User user = u.get();
                        hr.setEmail(user.getUsername());
                        try {
                            String decryptedSsn = SSNEncryptor.decrypt(user.getSsn());
                            hr.setSsn("XXXX-"+decryptedSsn.substring(5, 9));

                        } catch (Exception e) {
                            throw new RuntimeException("Problem decrypting SSN");
                        }
                        hr.setName(user.getName());
                        hr.setEmail(user.getUsername());
                        hr.setWorked(statusOpt.get().getNote());


                        hrRepository.save(hr);
                    }
                }else{
                    //rejection logic.
                }
                return ResponseEntity.ok("Status updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid decision. Must be APPROVED or REJECTED.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Status record not found.");
    }



// ADMIN EDITS THE TIMESHEET ENTRIES
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-multiple")
    public ResponseEntity<?> updateMultipleTimesheets(@RequestBody List<Timesheet> updates) {
        int totalDelta = 0;

        for (Timesheet updated : updates) {
            Optional<Timesheet> optExisting = timesheetRepository.findByEmpIdAndDate(updated.getEmpId(), updated.getDate());

            if (optExisting.isPresent()) {
                Timesheet existing = optExisting.get();

                int oldTotal = existing.getTotal();
                int newTotal = updated.getTotal();

                totalDelta += (newTotal - oldTotal);
                if (updated.getStart() != null) {
                    // If updated.getStart() is a string, parse it manually:
                    LocalTime start = updated.getStart();
                    existing.setStart(start);
                }
                if (updated.getEnd_time() != null) {
                    LocalTime end = updated.getEnd_time();
                    existing.setEnd_time(end);
                }
                if (updated.getProject() != null) existing.setProject(updated.getProject());


                existing.setTotal(newTotal);

                timesheetRepository.save(existing);
            }
        }

        if (!updates.isEmpty()) {
            Timesheet ref = updates.get(0);
            Optional<Status> statusOpt = statusRepository.findByEmpIdAndWeek(ref.getEmpId(), ref.getWeek());
            if (statusOpt.isPresent()) {
                Status status = statusOpt.get();
                int oldWeekTotal = status.getTotal();
                int updtTotal = oldWeekTotal + totalDelta;// adjust by diff
                status.setTotal(updtTotal);

                statusRepository.save(status);
            }
        }

        return ResponseEntity.ok("Timesheet updates and status total adjusted successfully.");
    }

//    ADMIN CAN TRACK THE STATUS OF TIMESHEETS PER WEEK FOR ALL THE USERS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/trackStatus")
    public ResponseEntity<List<Map<String, Object>>> trackStatus(@RequestParam String week) {
        // Find all status entries for that week
        List<Status> statusList = statusRepository.findByWeek(week);

        if (statusList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Prepare the response list
        List<Map<String, Object>> response = new ArrayList<>();

        for (Status s : statusList) {
            Map<String, Object> map = new HashMap<>();
            map.put("empId", s.getEmpId());
            map.put("status", s.getStatus());

            // 🔹 Get employee name from users table (assuming you have a UserRepository)
            userRepository.findById(s.getEmpId()).ifPresentOrElse(user -> {
                map.put("name", user.getFname()+" "+user.getLname()  );
            }, () -> {
                map.put("name", "Unknown");
            });

            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    //    ADMIN CAN FILTER / TRACK THE USER STATUS [ONLINE/OFFLINE]
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userStatus")
    public ResponseEntity<List<Map<String, Object>>> getUserStatus(
            @RequestParam(required = false) String filterStatus) { // optional filter

        List<UserStatus> statuses;

        if ("ONLINE".equalsIgnoreCase(filterStatus)) {
            statuses = userStatusRepository.findByStatus("CLOCK_IN");
        } else if ("OFFLINE".equalsIgnoreCase(filterStatus)) {
            statuses = userStatusRepository.findByStatus("CLOCK_OUT");
        } else {
            statuses = userStatusRepository.findAll();
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (UserStatus s : statuses) {
            Map<String, Object> map = new HashMap<>();
            map.put("empId", s.getUser().getId());
            map.put("name", s.getUser().getFname() + " " + s.getUser().getLname());
            String mod_status= ("CLOCK_IN".equalsIgnoreCase(s.getStatus()) ? "ONLINE" : "OFFLINE");
            map.put("status", mod_status); // FRONTEND maps CLOCK_IN/CLOCK_OUT to dot
            response.add(map);
        }

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }



}
