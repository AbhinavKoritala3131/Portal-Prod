package org.example.repository;

import org.example.entity.AuthorizedUser;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsBySsn(String ssn);
    boolean existsByMobile(String mobile);


    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    void deleteByUsername(String username);


    User getByUsername(String username);
}
