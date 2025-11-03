package org.example.repository;

import org.example.entity.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<HR,Long> {

    List<HR> findByWeek(String week);



}
