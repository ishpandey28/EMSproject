package com.ish.capstone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface SkillController {
    public ResponseEntity<String> addSkillsToEmployee(Map<String, Object> requestMap);
    public ResponseEntity<Set<String>> getSkillsByEmployeeId(Integer employeeId);
    public ResponseEntity<String> updateSkillsOfEmployee(Map<String, Object> requestMap);
    public ResponseEntity<String> deleteSkillsFromEmployee(Map<String, Object> requestMap);
}
