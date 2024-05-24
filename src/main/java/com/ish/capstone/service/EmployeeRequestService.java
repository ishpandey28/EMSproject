package com.ish.capstone.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeRequestService {
    ResponseEntity<String> createRequest(Integer managerId, Integer projectId, List<Integer> employeeIds);

    ResponseEntity<String> approveRequest(Integer requestId);

    ResponseEntity<String> rejectRequest(Integer requestId);

    ResponseEntity<String> unassignEmployee(Integer employeeId);
}
