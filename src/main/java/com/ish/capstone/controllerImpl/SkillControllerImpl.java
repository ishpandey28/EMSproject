package com.ish.capstone.controllerImpl;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.controller.SkillController;
import com.ish.capstone.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Slf4j
public class SkillControllerImpl implements SkillController {

    private final SkillService skillService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<String> addSkillsToEmployee(@RequestBody Map<String, Object> requestMap) {
        try {
            return skillService.addSkillsToEmployee(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping("/{employeeId}")
    public ResponseEntity<Set<String>> getSkillsByEmployeeId(@PathVariable Integer employeeId) {
        try {
            return skillService.getSkillsByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<String> updateSkillsOfEmployee(@RequestBody Map<String, Object> requestMap) {
        try {
            return skillService.updateSkillsOfEmployee(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSkillsFromEmployee(@RequestBody Map<String, Object> requestMap) {
        try {
            return skillService.deleteSkillsFromEmployee(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AppConfig.getResponseEntity(AppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}