package com.ish.capstone.controller;

import com.ish.capstone.dto.ManagerDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/manager")
@CrossOrigin(origins = "*")
public interface ManagerController {

    @GetMapping("/getAll")
    public ResponseEntity<List<ManagerDTO>> getAllManagers();

    @PutMapping("/update")
    public ResponseEntity<String> updateManager(@RequestBody Map<String, String> requestMap);
    @GetMapping("/find/{id}")
    public ResponseEntity<ManagerDTO> findManagerById(@PathVariable Integer id);
    @PostMapping("/assignManagerToEmployee")
    public ResponseEntity<String> assignManagerToEmployee(@RequestBody Map<String, Integer> requestMap);
    @PutMapping("/unassign-project")
    ResponseEntity<String> unassignProjectFromManager(@RequestBody Map<String, Integer> requestMap);

}

