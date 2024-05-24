package com.ish.capstone.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface EmployeeRequestController {

    public ResponseEntity<String> createRequest(Integer managerId, Integer projectId, List<Integer> employeeIds);

    public ResponseEntity<String> approveRequest(Integer requestId);

    public ResponseEntity<String> rejectRequest(Integer requestId);

    public ResponseEntity<String> unassignEmployee(Integer employeeId);
}