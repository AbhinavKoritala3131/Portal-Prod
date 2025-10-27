package org.example.controller;

import org.example.dto.ClockDTO;
import org.example.dto.StatusCheckDTO;
import org.example.dto.TimesheetDTO;
import org.example.entity.Status;
import org.example.repository.StatusRepository;
import org.example.service.TimesheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RestController
@RequestMapping("/tsManage")


public class TimesheetController {


    @Autowired
    private TimesheetService  timesheetService;
    @Autowired
    private StatusRepository statusRepository;
//SUBMIT FOR APPROVAL TIMESHEET
@PostMapping("/submit")
public ResponseEntity<?> submitTimesheet(@RequestBody TimesheetDTO submissionDTO) {
    try {
        timesheetService.submitTimesheetWeek(submissionDTO);
        return ResponseEntity.ok("Timesheet submitted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Failed to submit timesheet");
    }
}

//    SETS CLOCKIN AND CLOCKOUT DATA MADE BY USER
    @PostMapping("/clock")
    public ResponseEntity clockData(@RequestBody ClockDTO cdt){

        timesheetService.saveClock(cdt);
        return ResponseEntity.status(HttpStatus.OK).build();


    }



//    FETCH STATUS TO UPDATE THE SUBMITTED WEEKS
    @PostMapping("/status-check")
    public ResponseEntity<Map<String, String>> checkStatus(@RequestBody StatusCheckDTO request) {
        Long empId = request.getEmpId();
        List<String> weeks = request.getWeeks();

        if (weeks == null || weeks.size() != 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "Exactly two weeks must be provided"));
        }

        List<Status> statuses = statusRepository.findByEmpIdAndWeekIn(empId, weeks);

        Map<String, String> result = new HashMap<>();
        result.put("previous", null);
        result.put("current", null);

        for (Status status : statuses) {
            if (status.getWeek().equals(weeks.get(0))) {
                result.put("previous", status.getStatus());
            } else if (status.getWeek().equals(weeks.get(1))) {
                result.put("current", status.getStatus());
            }
        }

        return ResponseEntity.ok(result);
    }
}




