package com.ish.capstone.serviceImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.User;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            List<EmployeeDTO> employeeDTOs = employees.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateEmployee(Map<String, String> requestMap) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(Integer.parseInt(requestMap.get("empId")));
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                if (requestMap.containsKey("name")) {
                    employee.setName(requestMap.get("name"));
                }
                if (requestMap.containsKey("contactNumber")) {
                    employee.setContactNumber(requestMap.get("contactNumber"));
                }
                if (requestMap.containsKey("email")) {
                    employee.setEmail(requestMap.get("email"));
                }
                if (requestMap.containsKey("designation")) {
                    employee.setDesignation(requestMap.get("designation"));
                }
                if (requestMap.containsKey("projectStatus")) {
                    employee.setProjectStatus(requestMap.get("projectStatus"));
                }
                employeeRepository.save(employee);
                return AppConfig.getResponseEntity("Employee details updated successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("Employee not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<EmployeeDTO> findEmployeeById(Integer empId) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(empId);
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                EmployeeDTO employeeDTO = convertToDTO(employee);
                return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public List<EmployeeDTO> getAssignableEmployees() throws Exception {
        try {
            return employeeRepository.findByAssignedFalse().stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("Failed to fetch assignable employees");
        }
    }
    @Override
    public List<EmployeeDTO> getEmployeesBySkill(String skill) {
        List<Employee> employees = employeeRepository.findBySkill(skill);
        return employees.stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
    }

    private EmployeeDTO convertToDTO(Employee employee) {
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
    @Override
    public Employee getLoggedInEmployee() {
        String username = jwtFilter.getCurrentUser();
        User user = userRepository.findByEmail(username);
        return employeeRepository.findByUser(user);
    }
}
