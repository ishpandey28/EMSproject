package com.ish.capstone;

import com.ish.capstone.controllerImpl.ResourceRequestControllerImpl;
import com.ish.capstone.dto.ResourceRequestDTO;
import com.ish.capstone.service.ResourceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ResourceRequestControllerImplTest {

    @Mock
    private ResourceRequestService resourceRequestService;

    @InjectMocks
    private ResourceRequestControllerImpl resourceRequestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resourceRequestController).build();
    }

    @Test
    @WithMockUser
    public void testSendRequest() throws Exception {
        doNothing().when(resourceRequestService).sendRequest(any(ResourceRequestDTO.class));

        mockMvc.perform(post("/requests/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"managerId\": 1, \"employeeId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Request sent successfully"));
    }

    @Test
    @WithMockUser
    public void testApproveRequest() throws Exception {
        doNothing().when(resourceRequestService).approveRequest(anyInt());

        mockMvc.perform(post("/requests/approve/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Request approved successfully"));
    }

    @Test
    @WithMockUser
    public void testDeclineRequest() throws Exception {
        doNothing().when(resourceRequestService).declineRequest(anyInt());

        mockMvc.perform(put("/requests/declineRequest/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Request declined successfully"));
    }

    @Test
    @WithMockUser
    public void testUnassignEmployee() throws Exception {
        doNothing().when(resourceRequestService).unassignEmployee(anyInt());

        mockMvc.perform(post("/requests/unassign/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee unassigned successfully."));
    }
}
