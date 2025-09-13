package org.example.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


public class LoginDTO {
    @NotBlank
    @Id
    private String email;
    @NotBlank
    private String password;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
