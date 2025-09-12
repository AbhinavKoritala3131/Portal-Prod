package org.example.controller;

import org.example.dto.ClockDTO;
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

@CrossOrigin(origins = "http://localhost:5173")  // allow your frontend URL
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

//    CURRENT CLOCK STATUS OF USER FOR BUTTON
    @GetMapping("/status/{id}")
    public ResponseEntity<String> getUserStatus(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).
                body(timesheetService.userStat(id));

    }
//    SEND SUBMITTED WEEKS UPDATE
//    @GetMapping("/updatedWeeks")
//    public ResponseEntity<Map<String, String>> getWeekStatus(
//            @RequestParam Long userId,
//            @RequestParam String currentWeek,
//            @RequestParam String previousWeek) {
//
//        Map<String, String> result = new HashMap<>();
//
//        boolean current = statusRepository.findByUserIdAndWeek(userId, currentWeek).isPresent();
//        boolean previous = statusRepository.findByUserIdAndWeek(userId, previousWeek).isPresent();
//
//        result.put("CURRENT", current ? "SUBMITTED" : "PENDING");
//        result.put("PREVIOUS", previous ? "SUBMITTED" : "PENDING");
//
//        return ResponseEntity.ok(result);
//    }

}




