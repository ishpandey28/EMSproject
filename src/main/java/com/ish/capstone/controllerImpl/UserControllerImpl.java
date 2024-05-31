package com.ish.capstone.controllerImpl;

import com.ish.capstone.dto.UserDTO;
import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.controller.UserController;
import com.ish.capstone.entity.User;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final JwtFilter jwtFilter;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signup(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return userService.forgotPassword(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> updateUserDetails(Map<String, String> requestMap) {
        try {
            return userService. updateUserDetails(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            return userService.deleteUser(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        try {
            String email = jwtFilter.getCurrentUser();
            User user = userRepository.findByEmailId(email);
            if (user != null) {
                UserDTO userDTO = new UserDTO(user);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
