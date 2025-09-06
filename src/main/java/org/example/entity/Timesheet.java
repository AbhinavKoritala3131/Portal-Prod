package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"Emp_No", "date"}))
@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s_no;
//    @Column(name = "UserId")
//    private Long user_id;
    private LocalDate date;
    @Column(name = "StartTime")
    private String start;
    private String week;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Emp_No")
    @JsonIgnore
    private User user;

    private String total;
    @Column(name = "EndTime")
    private String end;
    @Column(name="Project")
    private String project;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


//
//    public Long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(Long user_id) {
//        this.user_id = user_id;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
