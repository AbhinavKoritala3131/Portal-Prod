package org.example.repository;

import org.example.entity.AuthorizedUser;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsBySsn(String ssn);
    boolean existsByMobile(String mobile);




    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User getByUsername(String username);
}
