package com.ish.capstone.service;

import com.ish.capstone.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    public ResponseEntity<String> signup(Map<String, String> requestMap);

    public ResponseEntity<String> login(Map<String, String> requestMap);

    public ResponseEntity<List<UserDTO>> getAllUsers();

    public ResponseEntity<String> update(Map<String, String> requestMap);

    public ResponseEntity<String> checkToken();
    public ResponseEntity<String> changePassword(Map<String, String> requestMap);

    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap);
    ResponseEntity<String> updateUserDetails(Map<String, String> requestMap);
    ResponseEntity<String> deleteUser(Integer id);
}
