package org.example.controller;


import org.example.dto.ApprovalStatusDTO;
import org.example.entity.HR;
import org.example.entity.Status;
import org.example.entity.Timesheet;
import org.example.entity.User;
import org.example.repository.HRRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TimesheetRepository;
import org.example.repository.UserRepository;
import org.example.security.SSNEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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


    @GetMapping("/timesheets-by-week")

    public ResponseEntity<List<Timesheet>> getTimesheetsWithNoRemarks(@RequestParam String week) {
        List<Timesheet> entries = timesheetRepository.findByWeekAndEmpIdIn(
                week,
                statusRepository.findEmpIdsByWeekWithNullRemarks(week)
        );
        return ResponseEntity.ok(entries);
    }

// BELOW API CURRENTLY NOT REQUIRED
//
//    @PutMapping("/update-timesheet")
//    public ResponseEntity<?> updateTimesheet(@RequestBody Timesheet updated) {
//        Optional<Timesheet> optional = timesheetRepository.findById(updated.getId());
//
//        if (optional.isPresent()) {
//            Timesheet existing = optional.get();
//            existing.setStart(updated.getStart());
//            existing.setEnd_time(updated.getEnd_time());
//            existing.setTotal(updated.getTotal());
//            existing.setProject(updated.getProject());
//
//            timesheetRepository.save(existing);
//            return ResponseEntity.ok("Updated successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Timesheet not found");
//        }
//    }


    @GetMapping("/status-by-week")
    public ResponseEntity<List<Status>> getPendingStatusByWeek(@RequestParam String week) {
        List<Status> pending = statusRepository.findByWeekAndRemarksIsNull(week);
        return ResponseEntity.ok(pending);
    }



    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody ApprovalStatusDTO request) {
        Optional<Status> statusOpt = statusRepository.findByEmpIdAndWeek(request.getEmpId(), request.getWeek());

        if (statusOpt.isPresent()) {
            Status status = statusOpt.get();

            if ("APPROVED".equalsIgnoreCase(request.getRem()) || "REJECTED".equalsIgnoreCase(request.getRem())) {
                status.setRemarks(request.getRem().toUpperCase());
                statusRepository.save(status);
                if ("APPROVED".equalsIgnoreCase(request.getRem())){
                    HR hr = new HR();
                    hr.setEmployee_id(request.getEmpId());
                    hr.setWeek(request.getWeek());
                    hr.setHours(status.getTotal());
                    Optional<User> u = userRepository.findById(request.getEmpId());
                    if (u.isPresent()) {
                        User user = u.get();
                        hr.setEmail(user.getEmail());
                        try {
                            String decryptedSsn = SSNEncryptor.decrypt(user.getSsn());
                            hr.setSsn(decryptedSsn);
                        } catch (Exception e) {
                            throw new RuntimeException("Problem decrypting SSN");
                        }
                        hr.setName(user.getName());
                        hr.setEmail(user.getEmail());
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




}
