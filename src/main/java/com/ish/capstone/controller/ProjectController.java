package com.ish.capstone.controller;

import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/projects")
public interface ProjectController {
    @PostMapping("/add")
    ResponseEntity<String> addProject(@RequestBody Map<String, String> requestMap);

    @GetMapping("/all")
    ResponseEntity<List<ProjectDTO>> getAllProjects();
    @PostMapping("/getById")
    ResponseEntity<Project> getProjectById(@RequestBody Map<String, Integer> requestMap);

    @PutMapping("/update")
    ResponseEntity<String> updateProject(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteProject(@RequestBody Map<String, Integer> requestMap);
    @PostMapping("/assignProjectToEmployee")
    public ResponseEntity<String> assignProjectToEmployee(@RequestBody Map<String, Integer> requestMap);

    @PostMapping("/assignProjectToManager")
    public ResponseEntity<String> assignProjectToManager(@RequestBody Map<String, Integer> requestMap);
}