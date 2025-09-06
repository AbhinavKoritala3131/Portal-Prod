package org.example.controller;

import org.example.dto.TimesheetDTO;
import org.example.entity.Timesheet;
import org.example.service.TimesheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // allow your frontend URL
@RestController
@RequestMapping("/tsManage")


public class TimesheetController {


    @Autowired
    private TimesheetService timesheetService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitTimesheet(@RequestBody TimesheetDTO dto) {
        LocalDate date = LocalDate.parse(dto.getDate().toString());
        Timesheet ts = timesheetService.upsertTimesheet(dto.getUserId(), date, dto);
        return ResponseEntity.ok(ts);
    }
}


//    @PostMapping("/submit")
//    public ResponseEntity createUser( @RequestBody TimesheetDTO dto){
//
//        timesheetService.submitTS(dto);
//       return ResponseEntity.status(200).build();
//
//    }




