package org.example.dto;

import java.time.LocalDate;

public class TimesheetDTO {

    private Long userId;
    private String start;
    private String end;
    private LocalDate date;
    private String total;
    private String week;
    private String project;

    public TimesheetDTO(Long userId, LocalDate date, String start, String end,
                        String total, String week, String project) {
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.date=date;
        this.total = total;
        this.week = week;
        this.project = project;

    }


    public Long getUserId() {
        return userId;
    }



    public void setUserId(Long user_id) {
        this.userId = user_id;
    }

    public void setStart(String start) {
        this.start = start;
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



    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }



    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
