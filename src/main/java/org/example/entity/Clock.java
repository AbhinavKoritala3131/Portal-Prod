package org.example.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "clock")

public class Clock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s_no;
    @Column(name="clockIn")
    private String start;
    @Column(name="clockOut")
    private String end;
    @Column(name="log_date")
    private String date;
    private String shiftTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Emp_ID")
    private User user;

    public String getShiftTotal() {
        return shiftTotal;
    }

    public void setShiftTotal(String shiftTotal) {
        this.shiftTotal = shiftTotal;
    }

    public Long getS_no() {
        return s_no;
    }

    public void setS_no(Long s_no) {
        this.s_no = s_no;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
