package org.example.repository;

import org.example.entity.ProjectGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectGroupsRepository extends JpaRepository<ProjectGroups, Long> {
    List<ProjectGroups> findByEmpId(Long empId);

}
