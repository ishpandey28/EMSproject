package com.ish.capstone.controller;

import com.ish.capstone.dto.ResourceRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public interface ResourceRequestController {
    @PostMapping("/send")
    ResponseEntity<String> sendRequest(@RequestBody ResourceRequestDTO requestDTO);

    @PostMapping("/approve/{requestId}")
    ResponseEntity<String> approveRequest(@PathVariable Integer requestId);

    @GetMapping("/all")
    ResponseEntity<List<ResourceRequestDTO>> getAllRequests();
    @PutMapping("/declineRequest/{requestId}")
    ResponseEntity<String> declineRequest(@PathVariable Integer requestId);

    @PostMapping("/unassign/{employeeId}")
    ResponseEntity<String> unassignEmployee(@PathVariable Integer employeeId);
}
