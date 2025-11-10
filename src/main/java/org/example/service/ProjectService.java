package org.example.service;

import org.example.dto.ProjectDTO;
import org.example.entity.ProjectGroups;
import org.example.entity.ProjectsList;
import org.example.repository.ProjectGroupsRepository;
import org.example.repository.ProjectsListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectsListRepository projectsRepo;
    private final ProjectGroupsRepository groupsRepo;

    public ProjectService(ProjectsListRepository projectsRepo, ProjectGroupsRepository groupsRepo) {
        this.projectsRepo = projectsRepo;
        this.groupsRepo = groupsRepo;
    }

    //    USERS VIEW ALL THE PROJECTS ASSIGNED TO THEM BY THEIR EMPID
    public List<ProjectsList> getProjectsByEmpId(Long empId) {
        // Step 1: get project types for the employee
        List<ProjectGroups> groups = groupsRepo.findByEmpId(empId);

        List<String> projectTypes = groups.stream()
                .map(ProjectGroups::getProjectType)
                .distinct() // avoid duplicates
                .collect(Collectors.toList());

        // Step 2: fetch all projects matching these types
        return projectsRepo.findByProjectTypeIn(projectTypes);

    }

    // Retrieve all projects to display in Timesheet list
    public List<String> NamesReturn(Long empId) {
        List<ProjectGroups> pg=groupsRepo.findByEmpId(empId);
        List<String> projectTypes = pg.stream()
                .map(ProjectGroups::getProjectType)
                .distinct() // avoid duplicates
                .collect(Collectors.toList());
        List<ProjectsList> names=projectsRepo.findByProjectTypeIn(projectTypes);
        return names.stream()
                .map(ProjectsList::getProjectName)
                .distinct() // avoid duplicates
                .collect(Collectors.toList());
    }


//  ✅ Get all projects TO LIST IN ASSIGN PROJECTS COMPONENT
    public List<ProjectsList> getAllProjects() {
        return projectsRepo.findAll();
    }

    // ✅ Add NEW project
    public ProjectsList addProject(ProjectsList project) {
        return projectsRepo.save(project);
    }

    // ✅ Delete SINGLE project by name
    public void deleteProjectByName(String name) {
        ProjectsList p = projectsRepo.findByProjectName(name);
        if (p != null) projectsRepo.delete(p);
    }

    // ✅ Delete group by project type[GROUP NAME]
    public void deleteGroup(String type) {
        List<ProjectsList> projects = projectsRepo.findByProjectType(type);
        projectsRepo.deleteAll(projects);
        List<ProjectGroups> users = groupsRepo.findByProjectType(type);
        groupsRepo.deleteAll(users);
    }

    // Get users[EMPIDs] by project type/GROUP THEY ARE ASSIGNED TO LIST ALL BY GROUP
    public List<ProjectGroups> getUsersByType(String type) {
        return groupsRepo.findByProjectType(type);
    }

    // Add users to project group/type
    public ProjectGroups addUserToType(ProjectGroups pg) {
        return groupsRepo.save(pg);
    }

    // Remove users to project group/type
    public void removeUserFromType(String type, Long empId) {
        ProjectGroups pg = groupsRepo.findByProjectTypeAndEmpId(type, empId);
        if (pg != null) groupsRepo.delete(pg);
    }

    // TO UPDATE EDITED PROJECT
    public boolean updateProject(String oldName, ProjectDTO dto) {
        ProjectsList project = projectsRepo.findByProjectName(oldName);

        // Check if new name already exists (and is different)
        if (!oldName.equals(dto.getProjectName()) &&
                projectsRepo.existsByProjectName(dto.getProjectName())) {
            return false;
        }

        project.setProjectName(dto.getProjectName());
        project.setProjectDescription(dto.getProjectDescription());
        project.setProjectStatus(dto.getProjectStatus());
        project.setDeadline(dto.getDeadline());
        project.setProjectType(dto.getProjectType());

        projectsRepo.save(project);
        return true;
    }
}
