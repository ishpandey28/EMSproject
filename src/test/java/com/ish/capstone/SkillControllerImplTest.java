package com.ish.capstone;

import com.ish.capstone.controllerImpl.SkillControllerImpl;
import com.ish.capstone.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class SkillControllerImplTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillControllerImpl skillController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(skillController).build();
    }

    @Test
    @WithMockUser
    public void testAddSkillsToEmployee() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", 1);
        requestMap.put("skills", Collections.singletonList("Java"));

        when(skillService.addSkillsToEmployee(any(Map.class))).thenReturn(new ResponseEntity<>("Skills added successfully", HttpStatus.OK));

        mockMvc.perform(post("/skills/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1, \"skills\": [\"Java\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }



    @Test
    @WithMockUser
    public void testUpdateSkillsOfEmployee() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", 1);
        requestMap.put("skills", Collections.singletonList("Java"));

        when(skillService.updateSkillsOfEmployee(any(Map.class))).thenReturn(new ResponseEntity<>("Skills updated successfully", HttpStatus.OK));

        mockMvc.perform(put("/skills/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1, \"skills\": [\"Java\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void testDeleteSkillsFromEmployee() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("employeeId", 1);
        requestMap.put("skills", Collections.singletonList("Java"));

        when(skillService.deleteSkillsFromEmployee(any(Map.class))).thenReturn(new ResponseEntity<>("Skills deleted successfully", HttpStatus.OK));

        mockMvc.perform(delete("/skills/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1, \"skills\": [\"Java\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
