package com.ish.capstone.service;

import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Project;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ResponseEntity<String> addProject(Map<String, String> requestMap);
    ResponseEntity<List<ProjectDTO>> getAllProjectDTOs();
    ResponseEntity<Project> getProjectById(Map<String, Integer> requestMap);
    ResponseEntity<String> updateProject(Map<String, String> requestMap);
    ResponseEntity<String> deleteProject(Map<String, Integer> requestMap);
    ResponseEntity<String> assignProjectToEmployee(Map<String, Integer> requestMap);

    ResponseEntity<String> assignProjectToManager(Map<String, Integer> requestMap);
}