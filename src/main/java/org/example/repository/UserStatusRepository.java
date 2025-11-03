package org.example.repository;

import org.example.entity.Status;
import org.example.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    Optional<UserStatus> findById(Long id);
    List<UserStatus> findByStatus(String clockIn);


}
