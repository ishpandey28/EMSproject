package com.ish.capstone.serviceImpl;

import com.google.common.base.Strings;
import com.ish.capstone.config.EmailConfig;
import com.ish.capstone.dto.UserDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.jwt.CustomerUsersDetailsService;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.jwt.JwtUtil;
import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.entity.User;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final JwtFilter jwtFilter;
    @Autowired
    private EmailConfig emailConfig;

//    @Override
//    public ResponseEntity<String> signup(Map<String, String> requestMap) {
//        log.info("Inside signup {}", requestMap);
//        try {
//            if (this.validateSignUpMap(requestMap)) {
//                User user = userRepository.findByEmailId(requestMap.get("email"));
//                if (Objects.isNull(user)) {
//                    User newUser = this.getUserFromMap(requestMap);
//                    userRepository.save(newUser);
//
//                    String role = requestMap.get("role");
//                    if ("employee".equalsIgnoreCase(role)) {
//                        saveEmployee(newUser, requestMap);
//                    } else if ("manager".equalsIgnoreCase(role)) {
//                        saveManager(newUser, requestMap);
//                    }
//
//                    return AppConfig.getResponseEntity("Successfully Registered", HttpStatus.OK);
//                } else {
//                    return AppConfig.getResponseEntity(AppConstants.EMAIL_EXISTS, HttpStatus.BAD_REQUEST);
//                }
//            } else {
//                return AppConfig.getResponseEntity(AppConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
@Override
public ResponseEntity<String> signup(Map<String, String> requestMap) {
    log.info("Inside signup {}", requestMap);
    try {
        if (this.validateSignUpMap(requestMap)) {
            User user = userRepository.findByEmailId(requestMap.get("email"));
            if (Objects.isNull(user)) {
                userRepository.save(this.getUserFromMap(requestMap));
                return AppConfig.getResponseEntity("Successfully Registered", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity(AppConstants.EMAIL_EXISTS, HttpStatus.BAD_REQUEST);
            }
        } else {
            return AppConfig.getResponseEntity(AppConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}
    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")
                && requestMap.containsKey("role");
    }
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole(requestMap.get("role"));

        if ("employee".equalsIgnoreCase(requestMap.get("role"))) {
            Employee employee = new Employee();
            employee.setUser(user);
            employee.setName(user.getName());
            employee.setContactNumber(user.getContactNumber());
            employee.setEmail(user.getEmail());
            user.setEmployee(employee);
        } else if ("manager".equalsIgnoreCase(requestMap.get("role"))) {
            Manager manager = new Manager();
            manager.setUser(user);
            manager.setName(user.getName());
            manager.setContactNumber(user.getContactNumber());
            manager.setEmail(user.getEmail());
            user.setManager(manager);
        }

        return user;
    }
//    private void saveEmployee(User user, Map<String, String> requestMap) {
//        Employee employee = new Employee();
//        employee.setUserId(user.getId());
//        employee.setName(user.getName());
//        employee.setContactNumber(user.getContactNumber());
//        employee.setEmail(user.getEmail());
//        employee.setDesignation(requestMap.get("designation"));
//        employee.setProjectStatus("not assigned");
//        employeeRepository.save(employee);
//    }
//    private void saveManager(User user, Map<String, String> requestMap) {
//        Manager manager = new Manager();
//        manager.setUserId(user.getId());
//        manager.setName(user.getName());
//        manager.setContactNumber(user.getContactNumber());
//        manager.setEmail(user.getEmail());
//        manager.setDesignation(requestMap.get("designation"));
//        managerRepository.save(manager);
//    }
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if(authentication.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),customerUsersDetailsService.getUserDetail().getRole()) + "\"}",HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval." + "\"}", HttpStatus.BAD_REQUEST);
                }
            }

        } catch (Exception exception){
            log.error("{}", exception);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad credentials." + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            if(jwtFilter.isAdmin()) return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
            else return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> user = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(user.isPresent()){
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    this.sendMailToAllAdmin(requestMap.get("status"), user.get().getEmail(), userRepository.getAllAdmin());
                    return AppConfig.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else {
                    return AppConfig.getResponseEntity("User id does not exist", HttpStatus.NO_CONTENT);
                }
            } else return AppConfig.getResponseEntity(AppConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> checkToken() {
        return AppConfig.getResponseEntity("true", HttpStatus.OK);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true"))
            emailConfig.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved","User:- " + user + "\n is approved by \nADMIN:- " +jwtFilter.getCurrentUser(), allAdmin);
        else
            emailConfig.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled","User:- " + user + "\n is disabled by \nADMIN:- " +jwtFilter.getCurrentUser(), allAdmin);
    }
    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(user != null){
                if(user.getPassword().equalsIgnoreCase(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                    return AppConfig.getResponseEntity("Password has been updated successfully", HttpStatus.OK);
                }
                return AppConfig.getResponseEntity("Old password is not true", HttpStatus.BAD_REQUEST);
            }
            return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailConfig.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
            return AppConfig.getResponseEntity("Check your email for credentials", HttpStatus.BAD_GATEWAY);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> updateUserDetails(Map<String, String> requestMap) {
        log.info("Inside updateUserDetails {}", requestMap);
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optionalUser = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (requestMap.containsKey("name")) {
                        user.setName(requestMap.get("name"));
                    }
                    if (requestMap.containsKey("contactNumber")) {
                        user.setContactNumber(requestMap.get("contactNumber"));
                    }
                    if (requestMap.containsKey("email")) {
                        user.setEmail(requestMap.get("email"));
                    }
                    userRepository.save(user);
                    return AppConfig.getResponseEntity("User details updated successfully", HttpStatus.OK);
                } else {
                    return AppConfig.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return AppConfig.getResponseEntity(AppConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @Override
//    public ResponseEntity<String> deleteUser(Integer id) {
//        log.info("Inside deleteUser with id {}", id);
//        try {
//            if (jwtFilter.isAdmin()) {
//                Optional<User> optionalUser = userRepository.findById(id);
//                if (optionalUser.isPresent()) {
//                    userRepository.deleteById(id);
//                    return AppConfig.getResponseEntity("User deleted successfully", HttpStatus.OK);
//                } else {
//                    return AppConfig.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
//                }
//            } else {
//                return AppConfig.getResponseEntity(AppConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
@Override
public ResponseEntity<String> deleteUser(Integer id) {
    log.info("Inside deleteUser with id {}", id);
    try {
        if (jwtFilter.isAdmin()) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                userRepository.deleteById(id);
                return AppConfig.getResponseEntity("User deleted successfully", HttpStatus.OK);
            } else {
                return AppConfig.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return AppConfig.getResponseEntity(AppConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}
}
