package org.example.repository;

import org.example.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    public Optional<Timesheet> findByUser_IdAndDate(Long userId, LocalDate date);

}