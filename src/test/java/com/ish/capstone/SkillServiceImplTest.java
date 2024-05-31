package com.ish.capstone;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.serviceImpl.SkillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SkillServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSkillsToEmployee_Success() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java", "Spring");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        Employee employee = new Employee();
        employee.setEmpId(employeeId);
        employee.setSkills(new HashSet<>());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<String> response = skillService.addSkillsToEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Skills added successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testAddSkillsToEmployee_EmployeeNotFound() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java", "Spring");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = skillService.addSkillsToEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Employee not found\"}", response.getBody());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetSkillsByEmployeeId_Success() {
        Integer employeeId = 1;
        Set<String> skills = new HashSet<>(Arrays.asList("Java", "Spring"));

        Employee employee = new Employee();
        employee.setEmpId(employeeId);
        employee.setSkills(skills);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<Set<String>> response = skillService.getSkillsByEmployeeId(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(skills, response.getBody());
    }

    @Test
    void testGetSkillsByEmployeeId_EmployeeNotFound() {
        Integer employeeId = 1;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<Set<String>> response = skillService.getSkillsByEmployeeId(employeeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdateSkillsOfEmployee_Success() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java", "Spring");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        Employee employee = new Employee();
        employee.setEmpId(employeeId);
        employee.setSkills(new HashSet<>());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<String> response = skillService.updateSkillsOfEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Skills updated successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testUpdateSkillsOfEmployee_EmployeeNotFound() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java", "Spring");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = skillService.updateSkillsOfEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Employee not found\"}", response.getBody());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteSkillsFromEmployee_Success() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        Employee employee = new Employee();
        employee.setEmpId(employeeId);
        employee.setSkills(new HashSet<>(Arrays.asList("Java", "Spring")));

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<String> response = skillService.deleteSkillsFromEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Skills deleted successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testDeleteSkillsFromEmployee_EmployeeNotFound() {
        Integer employeeId = 1;
        List<String> skillNames = Arrays.asList("Java");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", employeeId);
        requestMap.put("skillNames", skillNames);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = skillService.deleteSkillsFromEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Employee not found\"}", response.getBody());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
