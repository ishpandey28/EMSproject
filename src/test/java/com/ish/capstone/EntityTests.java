package com.ish.capstone;


import com.ish.capstone.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTests {

    @Test
    public void testUserEntity() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");
        user.setContactNumber("1234567890");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setStatus("active");
        user.setRole("admin");

        User sameUser = new User();
        sameUser.setId(1);
        sameUser.setName("John Doe");
        sameUser.setContactNumber("1234567890");
        sameUser.setEmail("john.doe@example.com");
        sameUser.setPassword("password");
        sameUser.setStatus("active");
        sameUser.setRole("admin");

        assertEquals(user, sameUser);
        assertEquals(user.hashCode(), sameUser.hashCode());

        User differentUser = new User();
        differentUser.setId(2);

        assertNotEquals(user, differentUser);
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }

    @Test
    public void testResourceRequestEntity() {
        ResourceRequest request = new ResourceRequest();
        request.setRequestId(1);
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());

        ResourceRequest sameRequest = new ResourceRequest();
        sameRequest.setRequestId(1);
        sameRequest.setStatus("PENDING");
        sameRequest.setRequestDate(request.getRequestDate());

        assertEquals(request, sameRequest);
        assertEquals(request.hashCode(), sameRequest.hashCode());

        ResourceRequest differentRequest = new ResourceRequest();
        differentRequest.setRequestId(2);

        assertNotEquals(request, differentRequest);
        assertNotEquals(request.hashCode(), differentRequest.hashCode());
    }

    @Test
    public void testProjectEntity() {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Project A");

        Project sameProject = new Project();
        sameProject.setProjectId(1);
        sameProject.setProjectName("Project A");

        assertEquals(project, sameProject);
        assertEquals(project.hashCode(), sameProject.hashCode());

        Project differentProject = new Project();
        differentProject.setProjectId(2);

        assertNotEquals(project, differentProject);
        assertNotEquals(project.hashCode(), differentProject.hashCode());
    }

    @Test
    public void testManagerEntity() {
        Manager manager = new Manager();
        manager.setMgrId(1);
        manager.setName("Jane Doe");
        manager.setContactNumber("0987654321");
        manager.setEmail("jane.doe@example.com");
        manager.setDesignation("Manager");

        Manager sameManager = new Manager();
        sameManager.setMgrId(1);
        sameManager.setName("Jane Doe");
        sameManager.setContactNumber("0987654321");
        sameManager.setEmail("jane.doe@example.com");
        sameManager.setDesignation("Manager");

        assertEquals(manager, sameManager);
        assertEquals(manager.hashCode(), sameManager.hashCode());

        Manager differentManager = new Manager();
        differentManager.setMgrId(2);

        assertNotEquals(manager, differentManager);
        assertNotEquals(manager.hashCode(), differentManager.hashCode());
    }

    @Test
    public void testEmployeeEntity() {
        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setName("Alice Doe");
        employee.setContactNumber("1112223333");
        employee.setEmail("alice.doe@example.com");
        employee.setDesignation("Developer");
        employee.setProjectStatus("Active");
        employee.setAssigned(true);

        Employee sameEmployee = new Employee();
        sameEmployee.setEmpId(1);
        sameEmployee.setName("Alice Doe");
        sameEmployee.setContactNumber("1112223333");
        sameEmployee.setEmail("alice.doe@example.com");
        sameEmployee.setDesignation("Developer");
        sameEmployee.setProjectStatus("Active");
        sameEmployee.setAssigned(true);

        assertEquals(employee, sameEmployee);
        assertEquals(employee.hashCode(), sameEmployee.hashCode());

        Employee differentEmployee = new Employee();
        differentEmployee.setEmpId(2);

        assertNotEquals(employee, differentEmployee);
        assertNotEquals(employee.hashCode(), differentEmployee.hashCode());
    }

    @Test
    public void testEntityRelationships() {
        // Test User-Employee relationship
        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setUser(user);
        user.setEmployee(employee);

        assertEquals(user.getEmployee(), employee);
        assertEquals(employee.getUser(), user);

        // Test User-Manager relationship
        Manager manager = new Manager();
        manager.setMgrId(1);
        manager.setUser(user);
        user.setManager(manager);

        assertEquals(user.getManager(), manager);
        assertEquals(manager.getUser(), user);

        // Test Project-Employee and Project-Manager relationships
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Project A");

        Set<Employee> employees = new HashSet<>();
        employees.add(employee);
        project.setEmployees(employees);

        Set<Manager> managers = new HashSet<>();
        managers.add(manager);
        project.setManagers(managers);

        assertTrue(project.getEmployees().contains(employee));
        assertTrue(project.getManagers().contains(manager));
    }
}
