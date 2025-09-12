package org.example.repository;

import org.example.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>{

    Optional<Status> findByEmpIdAndWeek(Long empId, String week);
    List<Status> findByEmpIdAndWeekIn(Long empId, List<String> weeks);


}