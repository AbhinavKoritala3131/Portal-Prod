package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsBySsn(String ssn);
    boolean existsByMobile(String mobile);
    User findByEmail(String email);
  boolean existsByPassword(String password);
}