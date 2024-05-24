package com.ish.capstone.serviceImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.Project;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public ResponseEntity<String> addProject(Map<String, String> requestMap) {
        try {
            Project project = new Project();
            project.setProjectName(requestMap.get("projectName"));
            projectRepository.save(project);
            return AppConfig.getResponseEntity("Project added successfully", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            return new ResponseEntity<>(projectRepository.findAll(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Project> getProjectById(Map<String, Integer> requestMap) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(requestMap.get("projectId"));
            return projectOptional.map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProject(Map<String, String> requestMap) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(requestMap.get("projectId")));
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if (requestMap.containsKey("projectName")) {
                    project.setProjectName(requestMap.get("projectName"));
                }
                projectRepository.save(project);
                return AppConfig.getResponseEntity("Project updated successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProject(Map<String, Integer> requestMap) {
        try {
            if (projectRepository.existsById(requestMap.get("projectId"))) {
                projectRepository.deleteById(requestMap.get("projectId"));
                return AppConfig.getResponseEntity("Project deleted successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> assignManagersToProject(Map<String, Object> requestMap) {
        try {
            Optional<Project> projectOptional = projectRepository.findById((Integer) requestMap.get("projectId"));
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                List<Integer> managerIds = (List<Integer>) requestMap.get("managerIds");
                Set<Manager> managers = new HashSet<>(managerRepository.findAllById(managerIds));
                project.setManagers(managers);
                projectRepository.save(project);
                return AppConfig.getResponseEntity("Managers assigned successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
