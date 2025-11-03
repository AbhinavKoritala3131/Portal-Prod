package org.example.controller;

import org.example.entity.ProjectGroups;
import org.example.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RequestMapping("/api/project-groups")
public class ProjectGroupsController {

    private final ProjectService projectService;

    public ProjectGroupsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Get users by project type
    @GetMapping("/type/{type}")
    public List<ProjectGroups> getUsersByType(@PathVariable String type) {
        return projectService.getUsersByType(type);
    }

    // Add user to project type
    @PostMapping
    public ProjectGroups addUser(@RequestBody ProjectGroups pg) {
        return projectService.addUserToType(pg);
    }

    // Delete user from project type
    @DeleteMapping("/{type}/{empId}")
    public void deleteUser(@PathVariable String type, @PathVariable Long empId) {
        projectService.removeUserFromType(type, empId);
    }
}
