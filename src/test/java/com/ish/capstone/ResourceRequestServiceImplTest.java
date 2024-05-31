package com.ish.capstone;

import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.ResourceRequestDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.ResourceRequest;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ResourceRequestRepository;
import com.ish.capstone.serviceImpl.ResourceRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ResourceRequestServiceImplTest {

    @Mock
    private ResourceRequestRepository resourceRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ResourceRequestServiceImpl resourceRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendRequest() throws Exception {
        ResourceRequestDTO requestDTO = new ResourceRequestDTO();
        requestDTO.setManagerId(1);
        requestDTO.setEmployeeId(1);

        Manager manager = new Manager();
        manager.setMgrId(1);
        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setAssigned(false);

        when(managerRepository.findById(1)).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        resourceRequestService.sendRequest(requestDTO);

        verify(resourceRequestRepository, times(1)).save(any(ResourceRequest.class));
    }

    @Test
    void testApproveRequest() throws Exception {
        ResourceRequest request = new ResourceRequest();
        Employee employee = new Employee();
        request.setEmployee(employee);

        when(resourceRequestRepository.findById(1)).thenReturn(Optional.of(request));

        resourceRequestService.approveRequest(1);

        assertEquals(AppConstants.REQUEST_APPROVED, request.getStatus());
        verify(resourceRequestRepository, times(1)).save(request);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetAllRequests() throws Exception {
        ResourceRequest request1 = new ResourceRequest();
        request1.setRequestId(1);
        request1.setStatus(AppConstants.REQUEST_PENDING);
        request1.setRequestDate(LocalDateTime.now());

        ResourceRequest request2 = new ResourceRequest();
        request2.setRequestId(2);
        request2.setStatus(AppConstants.REQUEST_APPROVED);
        request2.setRequestDate(LocalDateTime.now());

        when(resourceRequestRepository.findAll()).thenReturn(Arrays.asList(request1, request2));

        List<ResourceRequestDTO> requests = resourceRequestService.getAllRequests();

        assertEquals(2, requests.size());
        verify(resourceRequestRepository, times(1)).findAll();
    }

    @Test
    void testDeclineRequest() throws Exception {
        ResourceRequest request = new ResourceRequest();
        when(resourceRequestRepository.findById(1)).thenReturn(Optional.of(request));

        resourceRequestService.declineRequest(1);

        assertEquals(AppConstants.REQUEST_DECLINED, request.getStatus());
        verify(resourceRequestRepository, times(1)).save(request);
    }

    @Test
    void testUnassignEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setEmpId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        resourceRequestService.unassignEmployee(1);

        assertEquals(false, employee.isAssigned());
        verify(employeeRepository, times(1)).save(employee);
    }
}
