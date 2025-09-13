package org.example.repository;

import org.example.entity.AuthorizeUsers;
import org.example.entity.Clock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizeUsersRepository extends JpaRepository<AuthorizeUsers, Long> {
    Optional<AuthorizeUsers> findByEmail(String email);
    Optional<AuthorizeUsers> getByEmail(String email);

}
