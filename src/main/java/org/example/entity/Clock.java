package org.example.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clock")

public class Clock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s_no;
    @Column(name="clockIn")
    private LocalTime start;
    @Column(name="clockOut")
    private LocalTime  end;
    @Column(name="log_date")
    private LocalDate date;
    private String shiftTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Emp_ID")
    private User user;

    public String  getShiftTotal() {
        return shiftTotal;
    }

    public void setShiftTotal(String  shiftTotal) {
        this.shiftTotal = shiftTotal;
    }

    public Long getS_no() {
        return s_no;
    }

    public void setS_no(Long s_no) {
        this.s_no = s_no;
    }

    public LocalTime  getStart() {
        return start;
    }

    public void setStart(LocalTime  start) {
        this.start = start;
    }

    public LocalTime  getEnd() {
        return end;
    }

    public void setEnd(LocalTime  end) {
        this.end = end;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
