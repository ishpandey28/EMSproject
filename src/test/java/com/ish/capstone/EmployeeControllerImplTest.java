package com.ish.capstone;

import com.ish.capstone.controllerImpl.EmployeeControllerImpl;
import com.ish.capstone.dto.EmployeeDTO;
import com.ish.capstone.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class EmployeeControllerImplTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeControllerImpl employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    @WithMockUser
    public void testGetAllEmployees() throws Exception {
        List<EmployeeDTO> employees = Collections.singletonList(new EmployeeDTO());
        when(employeeService.getAllEmployees()).thenReturn(ResponseEntity.ok(employees));

        mockMvc.perform(get("/employee/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(any(Map.class))).thenReturn(ResponseEntity.ok("Employee updated"));

        mockMvc.perform(put("/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"empId\": \"1\", \"name\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated"));
    }



    @Test
    @WithMockUser
    public void testGetAssignableEmployees() throws Exception {
        List<EmployeeDTO> employees = Collections.singletonList(new EmployeeDTO());
        when(employeeService.getAssignableEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/assignable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void testGetEmployeesBySkill() throws Exception {
        List<EmployeeDTO> employees = Collections.singletonList(new EmployeeDTO());
        when(employeeService.getEmployeesBySkill(any(String.class))).thenReturn(employees);

        mockMvc.perform(post("/employee/employeesBySkill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"skill\": \"Java\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}