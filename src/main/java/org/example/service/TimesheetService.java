package org.example.service;
import jakarta.persistence.EntityManager;
import org.example.dto.ClockDTO;
import org.example.dto.TimesheetDTO;
import org.example.dto.TimesheetDTOEntries;
import org.example.entity.Clock;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.entity.Timesheet;
import org.example.exception.UserNotFound;
import org.example.repository.ClockRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TimesheetRepository;
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


    public ResponseEntity<Void> submitApproval(TimesheetDTO dto) {


        List<TimesheetDTOEntries> records = dto.getEntries();
        if (records != null && !records.isEmpty()) {
            for (TimesheetDTOEntries entry : records) {
                User u = em.getReference(User.class, entry.getUserId());

                Timesheet temp = new Timesheet();
                temp.setUser(u);
                temp.setProject(entry.getProject());
                temp.setWeek(entry.getWeek());
                temp.setTotal(entry.getTotal());
                temp.setDate(entry.getDate());
                temp.setEnd(entry.getEnd());
                temp.setStart(entry.getStart());
//                 Timesheet timesheet =
                timesheetRepository.save(temp);

            }
            Long emp = records.get(0).getUserId();
            String weekGiven=dto.getWeekType();
            if("CURRENT".equals(weekGiven)){
             statusRepository.findById(emp)
                    .map(w->{w.setCurrentWeek("SUBMITTED");
                        double decimalHours = dto.getWeekTotal();
                        long totalSeconds = (long) (decimalHours * 3600);
                        long hours = totalSeconds / 3600;
                        long minutes = (totalSeconds % 3600) / 60;
                        long seconds = totalSeconds % 60;
                        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        w.setCurrentTotal(formatted);
                       return statusRepository.save(w);});


            }else if("PREVIOUS".equals(weekGiven)){
                statusRepository.findById(emp)
                        .map(p -> {
                            p.setPreviousWeek("SUBMITTED");

                            double decimalHours = dto.getWeekTotal();
                            long totalSeconds = (long) (decimalHours * 3600);
                            long hours = totalSeconds / 3600;
                            long minutes = (totalSeconds % 3600) / 60;
                            long seconds = totalSeconds % 60;
                            String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                            p.setPreviousTotal(formatted);

                            return statusRepository.save(p);
                        });


            }}
         else {
                        throw new UserNotFound("User timesheet not found");
        }
         return ResponseEntity.ok().build();
    }

//    public Optional<Clock> getClockByUserAndDate(User user, String date) {
//        return clockRepository.findByUserAndDate(user, date);
//    }

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
    private void UpdateStatus(User u, String s) {
        Optional<Status> st = statusRepository.findById(u.getId());
        if (st.isPresent()) {
            Status temp_status=st.get();
            temp_status.setStatus(s);
            statusRepository.save(temp_status);
        } else {
            Status st1 = new Status();
            st1.setStatus(s);
            st1.setUser(u);
            statusRepository.save(st1);
        }
    }

    public String userStat(Long id){

        Optional<Status> st=statusRepository.findById(id);
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














