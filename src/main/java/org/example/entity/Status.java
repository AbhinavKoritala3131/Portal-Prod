package org.example.entity;

import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class Status {
    @Id
    private long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "status")
    private String status;
    private String currentWeek;
    private String currentTotal;
    private String previousWeek;
    private String previousTotal;

    public User getUser() {
        return user;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(String currentTotal) {
        this.currentTotal = currentTotal;
    }

    public String getPreviousWeek() {
        return previousWeek;
    }

    public void setPreviousWeek(String previousWeek) {
        this.previousWeek = previousWeek;
    }

    public String getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(String previousTotal) {
        this.previousTotal = previousTotal;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
