package com.ish.capstone;

import com.ish.capstone.config.AppConfig;
import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.ManagerDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.Project;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.repository.UserRepository;
import com.ish.capstone.service.ManagerService;
import com.ish.capstone.serviceImpl.ManagerServiceImpl;
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

class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllManagers() {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());
        when(managerRepository.findAll()).thenReturn(managers);

        ResponseEntity<List<ManagerDTO>> response = managerService.getAllManagers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(managerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateManager() {
        Manager manager = new Manager();
        manager.setMgrId(1);
        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("mgrId", "1");
        requestMap.put("name", "John Doe");

        ResponseEntity<String> response = managerService.updateManager(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Manager details updated successfully\"}", response.getBody());
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).save(manager);
    }

    @Test
    void testUpdateManagerNotFound() {
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("mgrId", "1");

        ResponseEntity<String> response = managerService.updateManager(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Manager not found\"}", response.getBody());
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testFindManagerById() {
        Manager manager = new Manager();
        manager.setMgrId(1);
        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        ResponseEntity<ManagerDTO> response = managerService.findManagerById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getMgrId());
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testFindManagerByIdNotFound() {
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ManagerDTO> response = managerService.findManagerById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testAssignManagerToEmployee() {
        Employee employee = new Employee();
        Manager manager = new Manager();
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("empId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = managerService.assignManagerToEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Manager assigned to employee successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testAssignManagerToEmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("empId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = managerService.assignManagerToEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Employee or Manager not found\"}", response.getBody());
        verify(employeeRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).findById(1);
    }

    @Test
    void testUnassignProjectFromManager() {
        Project project = new Project();
        Manager manager = new Manager();
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = managerService.unassignProjectFromManager(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project unassigned from manager successfully\"}", response.getBody());
        verify(projectRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).save(manager);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testUnassignProjectFromManagerNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = managerService.unassignProjectFromManager(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Project or Manager not found\"}", response.getBody());
        verify(projectRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).findById(1);
    }
}
