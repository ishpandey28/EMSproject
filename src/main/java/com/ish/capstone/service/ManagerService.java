package com.ish.capstone.service;

import com.ish.capstone.dto.ManagerDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    ResponseEntity<List<ManagerDTO>> getAllManagers();
    ResponseEntity<String> updateManager(Map<String, String> requestMap);
    ResponseEntity<ManagerDTO> findManagerById(Integer mgrId);
    ResponseEntity<String> assignManagerToEmployee(Map<String, Integer> requestMap);
    ResponseEntity<String> unassignProjectFromManager(Map<String, Integer> requestMap);

}