package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"Emp_No", "date"}))
@Entity
@Table(name = "Timesheet")
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="emp_id")
    private Long empId;

    private LocalTime start;
    @Column(name = "end_time")
    private LocalTime end_time;

    private LocalDate date;

    private String week; // e.g., "2025-09-07 - 2025-09-13"


    private Double total; // total hours for the day

    private String project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }



    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
