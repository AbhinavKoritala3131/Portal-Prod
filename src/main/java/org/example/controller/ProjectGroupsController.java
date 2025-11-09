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

    // Get users[EMPIDs] by project type/GROUP THEY ARE ASSIGNED TO LIST ALL BY GROUP
    @GetMapping("/type/{type}")
    public List<ProjectGroups> getUsersByType(@PathVariable String type) {
        return projectService.getUsersByType(type);
    }

    // Add users to project group
    @PostMapping
    public ProjectGroups addUser(@RequestBody ProjectGroups pg) {
        return projectService.addUserToType(pg);
    }

    // Remove users to project group/type
    @DeleteMapping("/{type}/{empId}")
    public void deleteUser(@PathVariable String type, @PathVariable Long empId) {
        projectService.removeUserFromType(type, empId);
    }
}
