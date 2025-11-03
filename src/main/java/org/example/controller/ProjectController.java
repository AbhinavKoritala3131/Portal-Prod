package org.example.controller;


import org.example.dto.ProjectDTO;
import org.example.entity.ProjectGroups;
import org.example.entity.ProjectsList;
import org.example.repository.ProjectGroupsRepository;
import org.example.repository.ProjectsListRepository;
import org.example.repository.UserRepository;
import org.example.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RestController
@RequestMapping("/projects")

public class ProjectController {


    private ProjectService projectService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectsListRepository projectsListRepository;
    @Autowired
    private ProjectGroupsRepository groupsRepo;

    public ProjectController(ProjectService projectsService) {

        this.projectService = projectsService;

    }

    @GetMapping("/employee/{empId}")
    public List<ProjectsList> getEmployeeProjects(@PathVariable Long empId) {
        return projectService.getProjectsByEmpId(empId);

    }



    @GetMapping("/employee/names/{empId}")
    public ResponseEntity<List<String>> ProjectNamesDisp(@PathVariable Long empId){
        List<String> projectNames =projectService.NamesReturn(empId);

        if (projectNames.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(projectNames);
    }

    // ✅ Get all projects
    @GetMapping("/listAll")
    public List<ProjectDTO> listAll() {
        return projectService.getAllProjects()
                .stream().map(ProjectDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ Add project
    @PostMapping
    public ProjectDTO addProject(@RequestBody ProjectsList project) {
        ProjectsList saved = projectService.addProject(project);
        return new ProjectDTO(saved);
    }

    // Get all users/employees
    @GetMapping("/employees")
    public List<User> getAllEmployees() {
        return userRepository.findAll();
    }

    // ✅ Delete project by name
    @DeleteMapping("/name/{name}")
    public void deleteProject(@PathVariable String name) {
        projectService.deleteProjectByName(name);
    }

    // ✅ Delete group by project type
    @DeleteMapping("/type/{type}")
    public void deleteGroup(@PathVariable String type) {
        projectService.deleteGroup(type);
    }

    @GetMapping("/types")
    public List<String> getProjectTypes() {
        // Types from projects
        List<String> projectTypes = projectsListRepository.findAll()
                .stream()
                .map(ProjectsList::getProjectType)
                .collect(Collectors.toList());

        // Types from project groups
        List<String> groupTypes = groupsRepo.findAll()
                .stream()
                .map(ProjectGroups::getProjectType)
                .collect(Collectors.toList());

        // Combine and remove duplicates
        return Stream.concat(projectTypes.stream(), groupTypes.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Update project by name
    @PutMapping("/name/{projectName}")
    public ResponseEntity<?> updateProject(
            @PathVariable String projectName,
            @RequestBody ProjectDTO projectDto) {

        boolean updated = projectService.updateProject(projectName, projectDto);
        if (updated) {
            return ResponseEntity.ok("Project updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Project name already exists or update failed");
        }
    }


}

