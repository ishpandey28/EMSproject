package com.ish.capstone.controllerImpl;

import com.ish.capstone.controller.EmployeeRequestController;
import com.ish.capstone.service.EmployeeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/employee-requests")
@RequiredArgsConstructor
public class EmployeeRequestControllerImpl implements EmployeeRequestController {

    private final EmployeeRequestService employeeRequestService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<String> createRequest(@RequestParam Integer managerId, @RequestParam Integer projectId, @RequestBody List<Integer> employeeIds) {
        return employeeRequestService.createRequest(managerId, projectId, employeeIds);
    }

    @PutMapping("/approve/{requestId}")
    @Override
    public ResponseEntity<String> approveRequest(@PathVariable Integer requestId) {
        return employeeRequestService.approveRequest(requestId);
    }

    @PutMapping("/reject/{requestId}")
    @Override
    public ResponseEntity<String> rejectRequest(@PathVariable Integer requestId) {
        return employeeRequestService.rejectRequest(requestId);
    }

    @PutMapping("/unassign/{employeeId}")
    @Override
    public ResponseEntity<String> unassignEmployee(@PathVariable Integer employeeId) {
        return employeeRequestService.unassignEmployee(employeeId);
    }
}