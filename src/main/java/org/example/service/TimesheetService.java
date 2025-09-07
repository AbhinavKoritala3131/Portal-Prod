package org.example.service;
import jakarta.persistence.EntityManager;
import org.example.dto.TimesheetDTO;
import org.example.entity.User;

import org.example.entity.Timesheet;
import org.example.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EntityManager em;
    public void upsertTimesheet(TimesheetDTO dto) {

        Optional<Timesheet> r= timesheetRepository.findByUser_IdAndDate(dto.getUserId(), dto.getDate());
        r.ifPresentOrElse(t -> { if(!dto.getStart().
                        equals(t.getStart())){t.setStart(dto.getStart());}
        else if(!dto.getEnd().equals(t.getEnd())){
            t.setEnd(dto.getEnd());
                }
                    else {
//                            Do Nothing
                }
            timesheetRepository.save(t);},

                ()->{ Timesheet ts= new Timesheet();
                        ts.setDate(dto.getDate());
                        ts.setWeek(dto.getWeek());
                        ts.setProject(dto.getProject());
                        ts.setTotal(dto.getTotal());
                        ts.setEnd(dto.getEnd());
                        ts.setStart(dto.getStart());
                        User u =em.getReference(User.class, dto.getUserId());
                        ts.setUser(u);
                        timesheetRepository.save(ts);
        }
                );










    }

}

    // Upsert logic: find existing by userId + date, update if exists, else create new
//    public Timesheet upsertTimesheet(Long userId, LocalDate date, TimesheetDTO dto) {
//        Optional<Timesheet> existingOpt = timesheetRepository.findByUserIdAndDate(userId, date);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        Timesheet ts;
//        if (existingOpt.isPresent()) {
//            ts = existingOpt.get();
//            // Update fields
//            ts.setStart(dto.getStart());
//            ts.setEnd(dto.getEnd());
//            ts.setTotal(dto.getTotal());
//            ts.setProject(dto.getProject());
//            ts.setWeek(dto.getWeek());
//            // User and Date do not change
//        } else {
//            ts = new Timesheet();
//            ts.setUser(user);
//            ts.setDate(date);
//            ts.setStart(dto.getStart());
//            ts.setEnd(dto.getEnd());
//            ts.setTotal(dto.getTotal());
//            ts.setProject(dto.getProject());
//            ts.setWeek(dto.getWeek());
//        }
//        return timesheetRepository.save(ts);
//    }
//}

