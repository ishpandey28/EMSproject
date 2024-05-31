package com.ish.capstone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
@NoArgsConstructor
public class ProjectDTO {
    private Integer projectId;
    private String projectName;
    public ProjectDTO(Integer projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    // Getters and Setters
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
