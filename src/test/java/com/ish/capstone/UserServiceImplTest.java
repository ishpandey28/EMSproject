package com.ish.capstone;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.config.EmailConfig;
import com.ish.capstone.dto.UserDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.User;
import com.ish.capstone.jwt.CustomerUsersDetailsService;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.jwt.JwtUtil;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.mail.MessagingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomerUsersDetailsService customerUsersDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private JwtFilter jwtFilter;
    @Mock
    private EmailConfig emailConfig;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testSignup_InvalidData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "John Doe");
        requestMap.put("contactNumber", "1234567890");
        requestMap.put("password", "password");
        requestMap.put("role", "employee");

        ResponseEntity<String> response = userService.signup(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Invalid data.\"}", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "john.doe@example.com");
        requestMap.put("password", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setRole("employee");
        user.setStatus("true");

        when(customerUsersDetailsService.getUserDetail()).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"token\":\"token\"}", response.getBody());
    }

    @Test
    void testLogin_WaitForAdminApproval() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "john.doe@example.com");
        requestMap.put("password", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setRole("employee");
        user.setStatus("false");

        when(customerUsersDetailsService.getUserDetail()).thenReturn(user);

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Wait for admin approval.\"}", response.getBody());
    }


    @Test
    void testGetAllUsers_Unauthorized() {
        when(jwtFilter.isAdmin()).thenReturn(false);

        ResponseEntity<List<UserDTO>> response = userService.getAllUsers();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testUpdate_Unauthorized() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "1");
        requestMap.put("status", "true");

        when(jwtFilter.isAdmin()).thenReturn(false);

        ResponseEntity<String> response = userService.update(requestMap);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\":\"Unauthorized access.\"}", response.getBody());
        verify(userRepository, never()).updateStatus(anyString(), anyInt());
        verify(emailConfig, never()).sendSimpleMessage(anyString(), anyString(), anyString(), anyList());
    }

    @Test
    void testCheckToken() {
        ResponseEntity<String> response = userService.checkToken();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"true\"}", response.getBody());
    }


    @Test
    void testUpdateUserDetails_Unauthorized() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "1");

        when(jwtFilter.isAdmin()).thenReturn(false);

        ResponseEntity<String> response = userService.updateUserDetails(requestMap);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\":\"Unauthorized access.\"}", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Unauthorized() {
        int userId = 1;

        when(jwtFilter.isAdmin()).thenReturn(false);

        ResponseEntity<String> response = userService.deleteUser(userId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\":\"Unauthorized access.\"}", response.getBody());
        verify(userRepository, never()).deleteById(anyInt());
    }
}