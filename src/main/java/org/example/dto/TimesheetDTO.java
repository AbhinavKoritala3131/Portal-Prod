package org.example.dto;

import java.util.List;

public class TimesheetDTO {
    private String week;
    private String weekTotal;
    private String weekType;
    private List<TimesheetDTOEntries> entries;

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public String getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(String weekTotal) {
        this.weekTotal = weekTotal;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<TimesheetDTOEntries> getEntries() {
        return entries;
    }

    public void setEntries(List<TimesheetDTOEntries> entries) {
        this.entries = entries;
    }
}



