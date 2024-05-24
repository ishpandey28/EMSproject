package com.ish.capstone.serviceImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<String> addSkillsToEmployee(Map<String, Object> requestMap) {
        Integer employeeId = (Integer) requestMap.get("employeeId");
        List<String> skillNames = (List<String>) requestMap.get("skillNames");

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            Set<String> currentSkills = employee.getSkills();
            currentSkills.addAll(skillNames);
            employee.setSkills(currentSkills);
            employeeRepository.save(employee);
            return AppConfig.getResponseEntity("Skills added successfully", HttpStatus.OK);
        } else {
            return AppConfig.getResponseEntity("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Set<String>> getSkillsByEmployeeId(Integer employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return new ResponseEntity<>(employee.getSkills(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateSkillsOfEmployee(Map<String, Object> requestMap) {
        Integer employeeId = (Integer) requestMap.get("employeeId");
        List<String> skillNames = (List<String>) requestMap.get("skillNames");

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setSkills(new HashSet<>(skillNames));
            employeeRepository.save(employee);
            return AppConfig.getResponseEntity("Skills updated successfully", HttpStatus.OK);
        } else {
            return AppConfig.getResponseEntity("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteSkillsFromEmployee(Map<String, Object> requestMap) {
        Integer employeeId = (Integer) requestMap.get("employeeId");
        List<String> skillNames = (List<String>) requestMap.get("skillNames");

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            Set<String> currentSkills = employee.getSkills();
            currentSkills.removeAll(skillNames);
            employee.setSkills(currentSkills);
            employeeRepository.save(employee);
            return AppConfig.getResponseEntity("Skills deleted successfully", HttpStatus.OK);
        } else {
            return AppConfig.getResponseEntity("Employee not found", HttpStatus.NOT_FOUND);
        }
    }
}