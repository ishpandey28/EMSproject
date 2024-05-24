package com.ish.capstone.controller;

import com.ish.capstone.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserController {
    @CrossOrigin(origins = "http://localhost:5500")
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @GetMapping("/get")
    public ResponseEntity<List<UserDTO>> getAllUsers();
    @CrossOrigin(origins = "http://localhost:5500")
    @PostMapping("/updateStatus")
    public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @GetMapping("/check-token")
    public ResponseEntity<String> checkToken();
    @CrossOrigin(origins = "http://localhost:5500")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @PutMapping("/update-details")
    public ResponseEntity<String> updateUserDetails(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "http://localhost:5500")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id);

}
