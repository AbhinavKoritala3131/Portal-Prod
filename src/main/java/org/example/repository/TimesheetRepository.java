package org.example.repository;

import org.example.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByWeekAndEmpIdIn(String week, List<Long> empIds);
    Optional<Timesheet> findByEmpIdAndDate(Long empId, LocalDate date);

}