package org.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class TimesheetDTOEntries {
        private Long UserId;
        private String date;
        private String start;

        private String end;
        private String total;
        private String project;
        private String week;

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public void setTotal(String total) {
        this.total = total;
    }



    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
