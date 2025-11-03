package org.example.repository;

import org.example.entity.AuthorizedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUsersRepo extends JpaRepository<AuthorizedUser, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsById(Long id);                          // Check for duplicate employee IDs

    void deleteByUsername(String username);
    Optional<AuthorizedUser> findByUsername(String username);

    AuthorizedUser getByUsername(String username);
}
