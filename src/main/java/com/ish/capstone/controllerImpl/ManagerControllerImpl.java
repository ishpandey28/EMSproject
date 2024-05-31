package com.ish.capstone.controllerImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.controller.ManagerController;
import com.ish.capstone.dto.ManagerDTO;

import com.ish.capstone.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ManagerControllerImpl implements ManagerController {
    @Autowired
    private ManagerService managerService;

    @Override
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        try {
            return managerService.getAllManagers();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateManager(Map<String, String> requestMap) {
        try {
            return managerService.updateManager(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<ManagerDTO> findManagerById(@PathVariable Integer id) {
        return managerService.findManagerById(id);
    }
    @Override
    public ResponseEntity<String> assignManagerToEmployee(@RequestBody Map<String, Integer> requestMap) {
        try {
            return managerService.assignManagerToEmployee(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> unassignProjectFromManager(Map<String, Integer> requestMap) {
        return managerService.unassignProjectFromManager(requestMap);
    }
}

