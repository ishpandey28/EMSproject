package com.ish.capstone.controller;

import com.ish.capstone.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@CrossOrigin(origins = "*")
public interface UserController {

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserDTO>> getAllUsers();

    @PostMapping("/updateStatus")
    public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap);

    @GetMapping("/check-token")
    public ResponseEntity<String> checkToken();

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
    @CrossOrigin(origins = "*")
    @PutMapping("/update-details")
    public ResponseEntity<String> updateUserDetails(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id);

}
