package com.ish.capstone.controllerImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.controller.EmployeeController;
import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.dto.SkillRequestDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class EmployeeControllerImpl implements EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            return employeeService.getAllEmployees();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateEmployee(Map<String, String> requestMap) {
        try {
            return employeeService.updateEmployee(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @GetMapping("/find/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable Integer id) {
        return employeeService.findEmployeeById(id);
    }
    @Override
    public ResponseEntity<List<EmployeeDTO>> getAssignableEmployees() {
        try {
            return new ResponseEntity<>(employeeService.getAssignableEmployees(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping("/employeesBySkill")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySkill(@RequestBody SkillRequestDTO skillRequest) {
        try {
            List<EmployeeDTO> employees = employeeService.getEmployeesBySkill(skillRequest.getSkill());
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/current")
    public ResponseEntity<Employee> getCurrentEmployee() {
        Employee employee = employeeService.getLoggedInEmployee();
        return ResponseEntity.ok(employee);
    }
}

