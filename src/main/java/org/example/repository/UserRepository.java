package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsBySsn(String ssn);
    boolean existsByMobile(String mobile);


    Optional<User>  findByEmail(String email);


  boolean existsByPassword(String password);
}