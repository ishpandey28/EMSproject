package com.ish.capstone;

import com.ish.capstone.controllerImpl.ManagerControllerImpl;
import com.ish.capstone.dto.ManagerDTO;
import com.ish.capstone.service.ManagerService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ManagerControllerImplTest {

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerControllerImpl managerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    @WithMockUser
    public void testGetAllManagers() throws Exception {
        List<ManagerDTO> managers = Collections.singletonList(new ManagerDTO());
        when(managerService.getAllManagers()).thenReturn(ResponseEntity.ok(managers));

        mockMvc.perform(get("/manager/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void testUpdateManager() throws Exception {
        when(managerService.updateManager(any(Map.class))).thenReturn(ResponseEntity.ok("Manager details updated successfully"));

        mockMvc.perform(put("/manager/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mgrId\": \"1\", \"name\": \"John Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager details updated successfully"));
    }

    @Test
    @WithMockUser
    public void testFindManagerById() throws Exception {
        ManagerDTO managerDTO = new ManagerDTO(1, "John Doe", "1234567890", "john.doe@example.com", "Manager", new HashSet<>(), new HashSet<>());
        when(managerService.findManagerById(anyInt())).thenReturn(ResponseEntity.ok(managerDTO));

        mockMvc.perform(get("/manager/find/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser
    public void testAssignManagerToEmployee() throws Exception {
        when(managerService.assignManagerToEmployee(any(Map.class))).thenReturn(ResponseEntity.ok("Manager assigned to employee successfully"));

        mockMvc.perform(post("/manager/assignManagerToEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mgrId\": 1, \"empId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager assigned to employee successfully"));
    }

    @Test
    @WithMockUser
    public void testUnassignProjectFromManager() throws Exception {
        when(managerService.unassignProjectFromManager(any(Map.class))).thenReturn(ResponseEntity.ok("Project unassigned from manager successfully"));

        mockMvc.perform(put("/manager/unassign-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mgrId\": 1, \"projectId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project unassigned from manager successfully"));
    }
}