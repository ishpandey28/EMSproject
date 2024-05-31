package com.ish.capstone;


import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.Project;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.service.ProjectService;
import com.ish.capstone.serviceImpl.ProjectServiceImpl;
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

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddProject() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("projectName", "New Project");

        ResponseEntity<String> response = projectService.addProject(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project added successfully\"}", response.getBody());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testGetAllProjectDTOs() {
        List<ProjectDTO> projectDTOs = Arrays.asList(new ProjectDTO(), new ProjectDTO());
        when(projectRepository.findAllProjectDTOs()).thenReturn(projectDTOs);

        ResponseEntity<List<ProjectDTO>> response = projectService.getAllProjectDTOs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(projectRepository, times(1)).findAllProjectDTOs();
    }

    @Test
    void testGetProjectById() {
        Project project = new Project();
        project.setProjectId(1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);

        ResponseEntity<Project> response = projectService.getProjectById(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getProjectId());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);

        ResponseEntity<Project> response = projectService.getProjectById(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateProject() {
        Project project = new Project();
        project.setProjectId(1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("projectId", "1");
        requestMap.put("projectName", "Updated Project");

        ResponseEntity<String> response = projectService.updateProject(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project updated successfully\"}", response.getBody());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testUpdateProjectNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("projectId", "1");

        ResponseEntity<String> response = projectService.updateProject(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Project not found\"}", response.getBody());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteProject() {
        when(projectRepository.existsById(1)).thenReturn(true);

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);

        ResponseEntity<String> response = projectService.deleteProject(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project deleted successfully\"}", response.getBody());
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteProjectNotFound() {
        when(projectRepository.existsById(1)).thenReturn(false);

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);

        ResponseEntity<String> response = projectService.deleteProject(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Project not found\"}", response.getBody());
        verify(projectRepository, times(1)).existsById(1);
    }

    @Test
    void testAssignProjectToEmployee() {
        Project project = new Project();
        Employee employee = new Employee();
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("empId", 1);

        ResponseEntity<String> response = projectService.assignProjectToEmployee(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project assigned to employee successfully\"}", response.getBody());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testAssignProjectToEmployeeNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("empId", 1);

        ResponseEntity<String> response = projectService.assignProjectToEmployee(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Project or Employee not found\"}", response.getBody());
        verify(projectRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testAssignProjectToManager() {
        Project project = new Project();
        Manager manager = new Manager();
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = projectService.assignProjectToManager(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Project assigned to manager successfully\"}", response.getBody());
        verify(managerRepository, times(1)).save(manager);
    }

    @Test
    void testAssignProjectToManagerNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());
        when(managerRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("projectId", 1);
        requestMap.put("mgrId", 1);

        ResponseEntity<String> response = projectService.assignProjectToManager(requestMap);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Project or Manager not found\"}", response.getBody());
        verify(projectRepository, times(1)).findById(1);
        verify(managerRepository, times(1)).findById(1);
    }
}
