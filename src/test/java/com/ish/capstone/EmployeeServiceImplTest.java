package com.ish.capstone;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.User;
import com.ish.capstone.jwt.JwtFilter;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.EmployeeService;
import com.ish.capstone.serviceImpl.EmployeeServiceImpl;
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

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        ResponseEntity<List<EmployeeDTO>> response = employeeService.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setEmpId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("empId", "1");
        requestMap.put("name", "John Doe");

        ResponseEntity<String> response = employeeService.updateEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Employee details updated successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("empId", "1");

        ResponseEntity<String> response = employeeService.updateEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Employee not found\"}", response.getBody());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testFindEmployeeById() {
        Employee employee = new Employee();
        employee.setEmpId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<EmployeeDTO> response = employeeService.findEmployeeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getEmpId());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDTO> response = employeeService.findEmployeeById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testGetAssignableEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findByAssignedFalse()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getAssignableEmployees();

        assertEquals(2, employeeDTOs.size());
        verify(employeeRepository, times(1)).findByAssignedFalse();
    }
}
