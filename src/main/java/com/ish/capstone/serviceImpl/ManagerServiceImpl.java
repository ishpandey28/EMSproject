package com.ish.capstone.serviceImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.dto.ManagerDTO;

import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.Project;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        try {
            List<Manager> managers = managerRepository.findAll();
            List<ManagerDTO> managerDTOs = managers.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(managerDTOs, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateManager(Map<String, String> requestMap) {
        try {
            Optional<Manager> optionalManager = managerRepository.findById(Integer.parseInt(requestMap.get("mgrId")));
            if (optionalManager.isPresent()) {
                Manager manager = optionalManager.get();
                if (requestMap.containsKey("name")) {
                    manager.setName(requestMap.get("name"));
                }
                if (requestMap.containsKey("contactNumber")) {
                    manager.setContactNumber(requestMap.get("contactNumber"));
                }
                if (requestMap.containsKey("email")) {
                    manager.setEmail(requestMap.get("email"));
                }
                if (requestMap.containsKey("designation")) {
                    manager.setDesignation(requestMap.get("designation"));
                }
                managerRepository.save(manager);
                return AppConfig.getResponseEntity("Manager details updated successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Manager not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<ManagerDTO> findManagerById(Integer mgrId) {
        try {
            Optional<Manager> optionalManager = managerRepository.findById(mgrId);
            if (optionalManager.isPresent()) {
                Manager manager = optionalManager.get();
                ManagerDTO managerDTO = convertToDTO(manager);
                return new ResponseEntity<>(managerDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @Transactional
    public ResponseEntity<String> assignManagerToEmployee(Map<String, Integer> requestMap) {
        try {
            Integer empId = requestMap.get("empId");
            Integer mgrId = requestMap.get("mgrId");

            Optional<Employee> employeeOptional = employeeRepository.findById(empId);
            Optional<Manager> managerOptional = managerRepository.findById(mgrId);

            if (employeeOptional.isPresent() && managerOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                Manager manager = managerOptional.get();

                employee.setManager(manager);

                employeeRepository.save(employee);
                return AppConfig.getResponseEntity("Manager assigned to employee successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Employee or Manager not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @Transactional
    public ResponseEntity<String> unassignProjectFromManager(Map<String, Integer> requestMap) {
        try {
            Integer projectId = requestMap.get("projectId");
            Integer mgrId = requestMap.get("mgrId");

            Optional<Project> projectOptional = projectRepository.findById(projectId);
            Optional<Manager> managerOptional = managerRepository.findById(mgrId);

            if (projectOptional.isPresent() && managerOptional.isPresent()) {
                Project project = projectOptional.get();
                Manager manager = managerOptional.get();

                // Explicitly initialize the collections if they are not already loaded
                if (manager.getProjects() != null) {
                    manager.getProjects().remove(project);
                }
                if (project.getManagers() != null) {
                    project.getManagers().remove(manager);
                }

                managerRepository.save(manager);
                projectRepository.save(project);

                return AppConfig.getResponseEntity("Project unassigned from manager successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Project or Manager not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ManagerDTO convertToDTO(Manager manager) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setMgrId(manager.getMgrId());
        managerDTO.setName(manager.getName());
        managerDTO.setContactNumber(manager.getContactNumber());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setDesignation(manager.getDesignation());
        managerDTO.setEmployees(manager.getEmployees().stream().map(Employee::getEmpId).collect(Collectors.toSet()));
        managerDTO.setProjectNames(manager.getProjects().stream().map(Project::getProjectName).collect(Collectors.toSet()));
        return managerDTO;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpId(employee.getEmpId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setContactNumber(employee.getContactNumber());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setDesignation(employee.getDesignation());
        employeeDTO.setProjectStatus(employee.getProjectStatus());
        employeeDTO.setAssigned(employee.isAssigned());
        employeeDTO.setSkills(employee.getSkills());
        if (employee.getManager() != null) {
            employeeDTO.setManagerName(employee.getManager().getName());
        }
        if (employee.getProject() != null) {
            employeeDTO.setProjectName(employee.getProject().getProjectName());
        }
        return employeeDTO;
    }
}

