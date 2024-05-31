package com.ish.capstone.controllerImpl;

import com.ish.capstone.controller.ResourceRequestController;
import com.ish.capstone.dto.ResourceRequestDTO;
import com.ish.capstone.service.ResourceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ResourceRequestControllerImpl implements ResourceRequestController {

    private final ResourceRequestService resourceRequestService;

    @Override
    public ResponseEntity<String> sendRequest(@RequestBody ResourceRequestDTO requestDTO) {
        try {
            resourceRequestService.sendRequest(requestDTO);
            return new ResponseEntity<>("Request sent successfully", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Failed to send request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> approveRequest(@PathVariable Integer requestId) {
        try {
            resourceRequestService.approveRequest(requestId);
            return new ResponseEntity<>("Request approved successfully", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Failed to approve request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ResourceRequestDTO>> getAllRequests() {
        try {
            return new ResponseEntity<>(resourceRequestService.getAllRequests(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<String> declineRequest(@PathVariable Integer requestId) { // New method
        try {
            resourceRequestService.declineRequest(requestId);
            return new ResponseEntity<>("Request declined successfully", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Failed to decline request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    @PostMapping("/unassign/{employeeId}")
    public ResponseEntity<String> unassignEmployee(@PathVariable Integer employeeId) {
        try {
            resourceRequestService.unassignEmployee(employeeId);
            return ResponseEntity.ok("Employee unassigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
