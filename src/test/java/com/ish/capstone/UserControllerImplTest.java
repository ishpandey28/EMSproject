package com.ish.capstone;

import com.ish.capstone.controllerImpl.UserControllerImpl;
import com.ish.capstone.service.UserService;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.config.AppConfig;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.dto.UserDTO;
import com.ish.capstone.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserControllerImpl userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser
    public void testSignUp() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", "testuser");
        requestMap.put("password", "password");

        when(userService.signup(any(Map.class))).thenReturn(new ResponseEntity<>("User signed up successfully", HttpStatus.OK));

        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testLogin() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", "testuser");
        requestMap.put("password", "password");

        when(userService.login(any(Map.class))).thenReturn(new ResponseEntity<>("Login successful", HttpStatus.OK));

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }



    @Test
    @WithMockUser
    public void testUpdate() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", "1");
        requestMap.put("status", "ACTIVE");

        when(userService.update(any(Map.class))).thenReturn(new ResponseEntity<>("User status updated successfully", HttpStatus.OK));

        mockMvc.perform(post("/user/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"1\", \"status\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testCheckToken() throws Exception {
        when(userService.checkToken()).thenReturn(new ResponseEntity<>("Token is valid", HttpStatus.OK));

        mockMvc.perform(get("/user/check-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testChangePassword() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPassword", "oldpassword");
        requestMap.put("newPassword", "newpassword");

        when(userService.changePassword(any(Map.class))).thenReturn(new ResponseEntity<>("Password changed successfully", HttpStatus.OK));

        mockMvc.perform(post("/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\": \"oldpassword\", \"newPassword\": \"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testForgotPassword() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "testuser@example.com");

        when(userService.forgotPassword(any(Map.class))).thenReturn(new ResponseEntity<>("Password reset link sent", HttpStatus.OK));

        mockMvc.perform(post("/user/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"testuser@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testUpdateUserDetails() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", "1");
        requestMap.put("name", "New Name");

        when(userService.updateUserDetails(any(Map.class))).thenReturn(new ResponseEntity<>("User details updated successfully", HttpStatus.OK));

        mockMvc.perform(put("/user/update-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"1\", \"name\": \"New Name\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(new ResponseEntity<>("User deleted successfully", HttpStatus.OK));

        mockMvc.perform(delete("/user/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

//    @Test
//    @WithMockUser
//    public void testGetCurrentUser() throws Exception {
//        User user = new User();
//        user.setEmailId("testuser@example.com");
//
//        when(jwtFilter.getCurrentUser()).thenReturn("testuser@example.com");
//        when(userRepository.findByEmailId("testuser@example.com")).thenReturn(user);
//
//        mockMvc.perform(get("/user/me")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.emailId").value("testuser@example.com"));
//    }
}
