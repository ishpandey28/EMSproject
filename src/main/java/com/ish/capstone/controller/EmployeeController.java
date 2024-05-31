package com.ish.capstone.controller;

import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.dto.SkillRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/employee")
@CrossOrigin(origins = "*")
public interface EmployeeController {

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees();

    @PutMapping("/update")
    public ResponseEntity<String> updateEmployee(@RequestBody Map<String, String> requestMap);
    @GetMapping("/find/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable Integer id);
    @GetMapping("/assignable")
    ResponseEntity<List<EmployeeDTO>> getAssignableEmployees();
    @PostMapping("/employeesBySkill")
    ResponseEntity<List<EmployeeDTO>> getEmployeesBySkill(@RequestBody SkillRequestDTO skillRequest);

}

