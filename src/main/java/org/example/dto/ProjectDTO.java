package org.example.dto;

import org.example.entity.ProjectsList;

import java.time.LocalDate;

public class ProjectDTO {
    private Long id;
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private LocalDate deadline;
    private String projectType;

    public ProjectDTO(){

    }

    public ProjectDTO(ProjectsList pl) {
        this.id = pl.getId();
        this.projectName = pl.getProjectName();
        this.projectDescription = pl.getProjectDescription();
        this.projectStatus = pl.getProjectStatus();
        this.deadline = pl.getDeadline();
        this.projectType = pl.getProjectType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    // getters/setters
}
