package com.ish.capstone.serviceImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.Project;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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
    @Transactional
    public ResponseEntity<List<ProjectDTO>> getAllProjectDTOs() {
        try {
            List<ProjectDTO> projects = projectRepository.findAllProjectDTOs();
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
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
    public ResponseEntity<String> assignProjectToEmployee(Map<String, Integer> requestMap) {
        try {
            Integer projectId = requestMap.get("projectId");
            Integer empId = requestMap.get("empId");

            Optional<Project> projectOptional = projectRepository.findById(projectId);
            Optional<Employee> employeeOptional = employeeRepository.findById(empId);

            if (projectOptional.isPresent() && employeeOptional.isPresent()) {
                Project project = projectOptional.get();
                Employee employee = employeeOptional.get();

                employee.setProject(project);
                employee.setAssigned(true);

                employeeRepository.save(employee);
                return AppConfig.getResponseEntity("Project assigned to employee successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project or Employee not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> assignProjectToManager(Map<String, Integer> requestMap) {
        try {
            Integer projectId = requestMap.get("projectId");
            Integer mgrId = requestMap.get("mgrId");

            Optional<Project> projectOptional = projectRepository.findById(projectId);
            Optional<Manager> managerOptional = managerRepository.findById(mgrId);

            if (projectOptional.isPresent() && managerOptional.isPresent()) {
                Project project = projectOptional.get();
                Manager manager = managerOptional.get();

                manager.getProjects().add(project);

                managerRepository.save(manager);
                return AppConfig.getResponseEntity("Project assigned to manager successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project or Manager not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
