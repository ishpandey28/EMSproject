package com.ish.capstone.jwt;
//
//import com.ish.capstone.entity.User;
//import com.ish.capstone.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Objects;
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CustomerUsersDetailsService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private User userDetail;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("Inside loadUserByUsername {}", username);
//        userDetail = userRepository.findByEmailId(username);
//        if(!Objects.isNull(userDetail))
//            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(),new ArrayList<>());
//        else throw new UsernameNotFoundException("User not found");
//    }
//    public User getUserDetail(){
//        return userDetail;
//    }
//}

import com.ish.capstone.entity.User;
import com.ish.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUsersDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private User userDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userRepository.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(),new ArrayList<>());
        else throw new UsernameNotFoundException("User not found");
    }
    public User getUserDetail(){
        return userDetail;
    }
}
