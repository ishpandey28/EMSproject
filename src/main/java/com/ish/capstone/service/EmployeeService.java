package com.ish.capstone.service;

import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    ResponseEntity<List<EmployeeDTO>> getAllEmployees();
    ResponseEntity<String> updateEmployee(Map<String, String> requestMap);
    ResponseEntity<EmployeeDTO> findEmployeeById(Integer empId);
    List<EmployeeDTO> getAssignableEmployees() throws Exception;
    List<EmployeeDTO> getEmployeesBySkill(String skill);
    Employee getLoggedInEmployee();
}
