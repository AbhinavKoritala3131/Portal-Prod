package org.example.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "authorized_users")
public class AuthorizedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    private String username;



    @Column(nullable = false)
    private String role;  // E.g. "USER" or "ADMIN"

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
