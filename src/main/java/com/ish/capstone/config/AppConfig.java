package com.ish.capstone.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AppConfig {
    public static ResponseEntity<String> getResponseEntity(String response, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\"" + response + "\"}", httpStatus);
    }
}
