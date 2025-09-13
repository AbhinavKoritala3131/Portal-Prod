package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;


    @OneToOne
    @MapsId
    @JoinColumn(name = "emp_no")  // FK to emp_id
    private AuthorizeUsers authorizeUsers;
    @NotBlank(message="name should not be blank")
    private String fname;
    private String lname;
    @Column(name = "full_name")
    private String name;
    @Email(message="invalid format")
    private String email;
    private String password;
    private String country;
    private String role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\+?[0-9]{1,3}[0-9]{10}$", message = "Mobile number must include country code and be 10 digits")
    private String mobile;
    private String ssn;
    private LocalDate dob;

    public AuthorizeUsers getAuthorizeUsers() {
        return authorizeUsers;
    }

    public void setAuthorizeUsers(AuthorizeUsers authorizeUsers) {
        this.authorizeUsers = authorizeUsers;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getName() {
        return name;
    }

    public void updatedName(String fname, String lname) {
        this.name = fname+" "+lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
