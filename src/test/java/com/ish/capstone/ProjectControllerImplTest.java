package com.ish.capstone;


import com.ish.capstone.controllerImpl.ProjectControllerImpl;
import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Project;
import com.ish.capstone.service.ProjectService;
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
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ProjectControllerImplTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectControllerImpl projectController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    @WithMockUser
    public void testAddProject() throws Exception {
        when(projectService.addProject(any(Map.class))).thenReturn(ResponseEntity.ok("Project added successfully"));

        mockMvc.perform(post("/projects/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectName\": \"New Project\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project added successfully"));
    }

    @Test
    @WithMockUser
    public void testGetAllProjects() throws Exception {
        List<ProjectDTO> projects = Collections.singletonList(new ProjectDTO(1, "Project 1"));
        when(projectService.getAllProjectDTOs()).thenReturn(new ResponseEntity<>(projects, HttpStatus.OK));

        mockMvc.perform(get("/projects/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectName").value("Project 1"));
    }

    @Test
    @WithMockUser
    public void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Project 1");
        when(projectService.getProjectById(any(Map.class))).thenReturn(new ResponseEntity<>(project, HttpStatus.OK));

        mockMvc.perform(post("/projects/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectName").value("Project 1"));
    }

    @Test
    @WithMockUser
    public void testUpdateProject() throws Exception {
        when(projectService.updateProject(any(Map.class))).thenReturn(ResponseEntity.ok("Project updated successfully"));

        mockMvc.perform(put("/projects/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\": \"1\", \"projectName\": \"Updated Project\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project updated successfully"));
    }

    @Test
    @WithMockUser
    public void testDeleteProject() throws Exception {
        when(projectService.deleteProject(any(Map.class))).thenReturn(ResponseEntity.ok("Project deleted successfully"));

        mockMvc.perform(delete("/projects/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted successfully"));
    }

    @Test
    @WithMockUser
    public void testAssignProjectToEmployee() throws Exception {
        when(projectService.assignProjectToEmployee(any(Map.class))).thenReturn(ResponseEntity.ok("Project assigned to employee successfully"));

        mockMvc.perform(post("/projects/assignProjectToEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\": 1, \"empId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project assigned to employee successfully"));
    }

    @Test
    @WithMockUser
    public void testAssignProjectToManager() throws Exception {
        when(projectService.assignProjectToManager(any(Map.class))).thenReturn(ResponseEntity.ok("Project assigned to manager successfully"));

        mockMvc.perform(post("/projects/assignProjectToManager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\": 1, \"mgrId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Project assigned to manager successfully"));
    }
}
