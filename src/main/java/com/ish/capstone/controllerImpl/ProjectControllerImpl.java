package com.ish.capstone.controllerImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.controller.ProjectController;
import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Project;
import com.ish.capstone.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectControllerImpl implements ProjectController {

    @Autowired
    private ProjectService projectService;

    @Override
    public ResponseEntity<String> addProject(@RequestBody Map<String, String> requestMap) {
        try {
            return projectService.addProject(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return projectService.getAllProjectDTOs();
    }

    @Override
    public ResponseEntity<Project> getProjectById(@RequestBody Map<String, Integer> requestMap) {
        try {
            return projectService.getProjectById(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProject(@RequestBody Map<String, String> requestMap) {
        try {
            return projectService.updateProject(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProject(@RequestBody Map<String, Integer> requestMap) {
        try {
            return projectService.deleteProject(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> assignProjectToEmployee(@RequestBody Map<String, Integer> requestMap) {
        try {
            return projectService.assignProjectToEmployee(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> assignProjectToManager(@RequestBody Map<String, Integer> requestMap) {
        try {
            return projectService.assignProjectToManager(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
   
}
