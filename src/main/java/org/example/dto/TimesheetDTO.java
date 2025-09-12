package org.example.dto;

import java.time.Duration;
import java.util.List;

public class TimesheetDTO {
    private Long userId;
    private String week;
    private Double weekTotal;
    private List<TimesheetDTOEntries> entries;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Double getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(Double weekTotal) {
        this.weekTotal = weekTotal;
    }

    public List<TimesheetDTOEntries> getEntries() {
        return entries;
    }

    public void setEntries(List<TimesheetDTOEntries> entries) {
        this.entries = entries;
    }
}



