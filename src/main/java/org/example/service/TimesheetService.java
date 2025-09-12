package org.example.service;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.dto.ClockDTO;
import org.example.dto.TimesheetDTO;
import org.example.dto.TimesheetDTOEntries;
import org.example.entity.*;
import org.example.exception.UserNotFound;
import org.example.repository.ClockRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TimesheetRepository;
import org.example.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private ClockRepository clockRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    //SUBMIT TIMESHEET RECORDS
    @Transactional
    public void submitTimesheetWeek(TimesheetDTO submissionDTO) {

        // Save all timesheet entries for the week
        List<TimesheetDTOEntries> entries = submissionDTO.getEntries();

        entries.forEach(entryDTO -> {
            Timesheet timesheet = new Timesheet();
            timesheet.setEmpId(entryDTO.getUserId());
            timesheet.setStart(entryDTO.getStart());
            timesheet.setEnd_time(entryDTO.getEnd());
            timesheet.setDate(entryDTO.getDate());
            timesheet.setWeek(entryDTO.getWeek());
            timesheet.setTotal(entryDTO.getTotal());
            timesheet.setProject(entryDTO.getProject());

            timesheetRepository.save(timesheet);
        });

        // TO UPDATE THE STATUS AS UPDATED
        Status status = statusRepository.findByEmpIdAndWeek(submissionDTO.getUserId(), submissionDTO.getWeek())
                .orElse(new Status());

        status.setEmpId(submissionDTO.getUserId());
        status.setWeek(submissionDTO.getWeek());
        status.setTotal(submissionDTO.getWeekTotal());
        status.setStatus("SUBMITTED");

        statusRepository.save(status);
    }

// USER CLOCKIN/OUT DETAILS EVERYSHIT DURATION
    public void saveClock(ClockDTO dto) {
        User u=em.getReference(User.class,dto.getUserId());

        if("CLOCK_IN".equals(dto.getStatus())){
            Clock c=new Clock();
            c.setStart(dto.getStart());
            c.setDate(dto.getDate());
            c.setUser(u);
            clockRepository.save(c);
            UpdateStatus(u, dto.getStatus());


        }
        else {
            Optional<Clock> record = clockRepository.findLastOpenClockByUserAndDate(u.getId(), dto.getDate());

            if (record.isPresent()) {
                Clock c = record.get();

                LocalTime start = c.getStart();
                LocalTime end = dto.getEnd(); // use directly if it's LocalTime

                c.setEnd(end);

                // Calculate and set the shift total as Duration
                Duration shiftDuration = Duration.between(start, end);
                long seconds = shiftDuration.getSeconds();

                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                String formatted = String.format("%02d:%02d:%02d", hours, minutes, secs);
                c.setShiftTotal(formatted);


                clockRepository.save(c);
                UpdateStatus(u, dto.getStatus());
            }

        }



    }
//    UPDATE USER CLOCKIN/OUT STATUS
    private void UpdateStatus(User u, String s) {
        Optional<UserStatus> st = userStatusRepository.findById(u.getId());
        if (st.isPresent()) {
            UserStatus temp_status=st.get();
            temp_status.setStatus(s);
            userStatusRepository.save(temp_status);
        } else {
            UserStatus st1 = new UserStatus();
            st1.setStatus(s);
            st1.setUser(u);
            userStatusRepository.save(st1);
        }
    }
//TO SEND USER CLOCK STATUS TO REACT TO UPDATE CLOCK STATUS
    public String userStat(Long id){

        Optional<UserStatus> st=userStatusRepository.findById(id);
        if (st.isPresent()) {
            String tmpStat=st.get().getStatus();
            return tmpStat;
        }else{
            throw new UserNotFound("User Status not found");
        }

    }
// SEND WEEK UPDATES TO USE
public Optional<Status> showWeeks(Long id){
        return statusRepository.findById(id);
}







}














