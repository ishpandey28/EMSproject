package com.ish.capstone.service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SkillService {
    ResponseEntity<String> addSkillsToEmployee(Map<String, Object> requestMap);
    ResponseEntity<Set<String>> getSkillsByEmployeeId(Integer employeeId);
    ResponseEntity<String> updateSkillsOfEmployee(Map<String, Object> requestMap);
    ResponseEntity<String> deleteSkillsFromEmployee(Map<String, Object> requestMap);
}