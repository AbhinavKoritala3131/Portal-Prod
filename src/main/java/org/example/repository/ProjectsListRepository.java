package org.example.repository;

import org.example.entity.ProjectsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsListRepository extends JpaRepository<ProjectsList,Long> {
    List<ProjectsList> findByProjectTypeIn(List<String> projectTypes);
//    List<String> findByProjectNameOnly(@Param(type) )

    ProjectsList findByProjectName(String name);
    List<ProjectsList> findByProjectType(String type);
    boolean existsByProjectName(String projectName);
}
