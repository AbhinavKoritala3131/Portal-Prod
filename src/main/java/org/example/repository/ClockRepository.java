package org.example.repository;

import org.example.entity.Clock;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClockRepository extends JpaRepository<Clock, Long>{

    @Query("SELECT c FROM Clock c WHERE c.user.id = :userId AND c.date = :date AND c.end IS NULL ORDER BY c.start DESC")
    Optional<Clock> findLastOpenClockByUserAndDate(@Param("userId") Long userId, @Param("date") String date);

}