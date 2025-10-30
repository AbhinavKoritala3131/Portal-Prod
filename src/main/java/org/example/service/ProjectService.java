package org.example.service;

import org.example.entity.ProjectGroups;
import org.example.entity.ProjectsList;
import org.example.repository.ProjectGroupsRepository;
import org.example.repository.ProjectsListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectGroupsRepository projectGroupsRepository;
    private ProjectsListRepository projectsRepository;

    public ProjectService(ProjectGroupsRepository projectGroupsRepository,
                          ProjectsListRepository projectsRepository) {
        this.projectGroupsRepository = projectGroupsRepository;
        this.projectsRepository = projectsRepository;
    }

    public List<ProjectsList> getProjectsByEmpId(Long empId) {
        // Step 1: get project types for the employee
        List<ProjectGroups> groups = projectGroupsRepository.findByEmpId(empId);
        List<String> projectTypes = groups.stream()
                .map(ProjectGroups::getProjectType)
                .distinct() // avoid duplicates
                .collect(Collectors.toList());

        // Step 2: fetch all projects matching these types
        return projectsRepository.findByProjectTypeIn(projectTypes);
    }
}
