package org.example.service;
import org.example.dto.TimesheetDTO;
import org.example.entity.User;

import org.example.entity.Timesheet;
import org.example.repository.TimesheetRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.repository.util.ClassUtils.ifPresent;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private UserRepository userRepository;
    // Upsert logic: find existing by userId + date, update if exists, else create new
    public Timesheet upsertTimesheet(Long userId, LocalDate date, TimesheetDTO dto) {
        Optional<Timesheet> existingOpt = timesheetRepository.findByUserIdAndDate(userId, date);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Timesheet ts;
        if (existingOpt.isPresent()) {
            ts = existingOpt.get();
            // Update fields
            ts.setStart(dto.getStart());
            ts.setEnd(dto.getEnd());
            ts.setTotal(dto.getTotal());
            ts.setProject(dto.getProject());
            ts.setWeek(dto.getWeek());
            // User and Date do not change
        } else {
            ts = new Timesheet();
            ts.setUser(user);
            ts.setDate(date);
            ts.setStart(dto.getStart());
            ts.setEnd(dto.getEnd());
            ts.setTotal(dto.getTotal());
            ts.setProject(dto.getProject());
            ts.setWeek(dto.getWeek());
        }
        return timesheetRepository.save(ts);
    }
}

//    public void submitTS(TimesheetDTO dto){
//
//            Timesheet ts = new Timesheet();
//
//
//       ts.setStart(dto.getStart());
//         ts.setEnd(dto.getEnd());
//         ts.setTotal(dto.getTotal());
//        ts.setWeek(dto.getWeek());
//         ts.setProject(dto.getProject());
//
//        timesheetRepository.save(ts);
//
//            }



