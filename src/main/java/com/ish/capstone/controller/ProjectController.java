package com.ish.capstone.controller;

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
    ResponseEntity<List<Project>> getAllProjects();

    @PostMapping("/getById")
    ResponseEntity<Project> getProjectById(@RequestBody Map<String, Integer> requestMap);

    @PutMapping("/update")
    ResponseEntity<String> updateProject(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteProject(@RequestBody Map<String, Integer> requestMap);

    @PutMapping("/assign-managers")
    ResponseEntity<String> assignManagersToProject(@RequestBody Map<String, Object> requestMap);
}