package org.example.repository;

import org.example.entity.AuthorizedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUsersRepo extends JpaRepository<AuthorizedUser, String> {
    Optional<AuthorizedUser> findByUsername(String username);

    AuthorizedUser getByUsername(String username);
}
