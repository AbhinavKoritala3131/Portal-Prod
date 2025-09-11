package org.example.controller;

import org.example.dto.ClockDTO;
import org.example.dto.TimesheetDTO;
import org.example.entity.Status;
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
//SUBMIT FOR APPROVAL TIMESHEET
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitTimesheet(@RequestBody TimesheetDTO dto) {
//        LocalDate date = LocalDate.parse(dto.getDate().toString());
//        Timesheet ts = timesheetService.upsertTimesheet(dto.getUserId(), date, dto);
        timesheetService.submitApproval(dto);
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", "TS Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    @GetMapping("/updatedWeeks")
    public ResponseEntity<Map<String, Object>> getUpdatedWeeks(@RequestParam Long id){

        Optional<Status> res=timesheetService.showWeeks(id);
        if(res.isPresent()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("currentWeek", res.get().getCurrentWeek());
            stat.put("previousWeek", res.get().getPreviousWeek());
            return ResponseEntity.status(HttpStatus.OK).body(stat);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}




