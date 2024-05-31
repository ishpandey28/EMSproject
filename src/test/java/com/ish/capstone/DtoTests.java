package com.ish.capstone;


import com.ish.capstone.dto.*;
import com.ish.capstone.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DtoTests {

    @Test
    public void testUserDTO() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setContactNumber("1234567890");
        user.setStatus("active");
        user.setRole("admin");

        UserDTO userDTO = new UserDTO(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getContactNumber(), userDTO.getContactNumber());
        assertEquals(user.getStatus(), userDTO.getStatus());
        assertEquals(user.getRole(), userDTO.getRole());
    }

    @Test
    public void testSkillRequestDTO() {
        SkillRequestDTO skillRequestDTO = new SkillRequestDTO("Java");

        assertEquals("Java", skillRequestDTO.getSkill());

        skillRequestDTO.setSkill("Python");
        assertEquals("Python", skillRequestDTO.getSkill());
    }

    @Test
    public void testResourceRequestDTO() {
        LocalDateTime now = LocalDateTime.now();
        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO(1, 2, 3, "PENDING", now);

        assertEquals(1, resourceRequestDTO.getRequestId());
        assertEquals(2, resourceRequestDTO.getManagerId());
        assertEquals(3, resourceRequestDTO.getEmployeeId());
        assertEquals("PENDING", resourceRequestDTO.getStatus());
        assertEquals(now, resourceRequestDTO.getRequestDate());

        resourceRequestDTO.setStatus("APPROVED");
        assertEquals("APPROVED", resourceRequestDTO.getStatus());
    }

    @Test
    public void testProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO(1, "Project A");

        assertEquals(1, projectDTO.getProjectId());
        assertEquals("Project A", projectDTO.getProjectName());

        projectDTO.setProjectName("Project B");
        assertEquals("Project B", projectDTO.getProjectName());
    }

    @Test
    public void testManagerDTO() {
        Set<Integer> employeeIds = new HashSet<>();
        employeeIds.add(1);
        employeeIds.add(2);

        Set<String> projectNames = new HashSet<>();
        projectNames.add("Project A");
        projectNames.add("Project B");

        ManagerDTO managerDTO = new ManagerDTO(1, "Jane Doe", "0987654321", "jane.doe@example.com", "Manager", employeeIds, projectNames);

        assertEquals(1, managerDTO.getMgrId());
        assertEquals("Jane Doe", managerDTO.getName());
        assertEquals("0987654321", managerDTO.getContactNumber());
        assertEquals("jane.doe@example.com", managerDTO.getEmail());
        assertEquals("Manager", managerDTO.getDesignation());
        assertEquals(employeeIds, managerDTO.getEmployees());
        assertEquals(projectNames, managerDTO.getProjectNames());

        managerDTO.setDesignation("Senior Manager");
        assertEquals("Senior Manager", managerDTO.getDesignation());
    }

    @Test
    public void testEmployeeDTO() {
        Set<String> skills = new HashSet<>();
        skills.add("Java");
        skills.add("Python");

        EmployeeDTO employeeDTO = new EmployeeDTO(1, "Alice Doe", "1112223333", "alice.doe@example.com", "Developer", "Active", true, "John Doe", "Project A", skills);

        assertEquals(1, employeeDTO.getEmpId());
        assertEquals("Alice Doe", employeeDTO.getName());
        assertEquals("1112223333", employeeDTO.getContactNumber());
        assertEquals("alice.doe@example.com", employeeDTO.getEmail());
        assertEquals("Developer", employeeDTO.getDesignation());
        assertEquals("Active", employeeDTO.getProjectStatus());
        assertTrue(employeeDTO.isAssigned());
        assertEquals("John Doe", employeeDTO.getManagerName());
        assertEquals("Project A", employeeDTO.getProjectName());
        assertEquals(skills, employeeDTO.getSkills());

        employeeDTO.setDesignation("Senior Developer");
        assertEquals("Senior Developer", employeeDTO.getDesignation());
    }
}
