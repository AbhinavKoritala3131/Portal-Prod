package org.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class ClockDTO {
    private Long userId;
    private LocalDate date;
    public LocalTime start;
    public LocalTime end;
    public Duration Total;
    public String status;

    public Duration getTotal() {
        return Total;
    }

    public void setTotal(Duration total) {
        Total = total;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
