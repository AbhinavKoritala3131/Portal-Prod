package org.example.entity;

import jakarta.persistence.*;

@Entity
public class UserStatus {
    @Id
    private Long id;



    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    private String status;

    public User getUser() {
        return user;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
