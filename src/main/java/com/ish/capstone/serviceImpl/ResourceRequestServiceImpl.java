package com.ish.capstone.serviceImpl;

import com.ish.capstone.constants.AppConstants;
import com.ish.capstone.dto.ResourceRequestDTO;
import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.Manager;
import com.ish.capstone.entity.ResourceRequest;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ResourceRequestRepository;
import com.ish.capstone.service.ResourceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {

    private final ResourceRequestRepository resourceRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    public void sendRequest(ResourceRequestDTO requestDTO) throws Exception {
        if (requestDTO.getManagerId() == null || requestDTO.getEmployeeId() == null) {
            throw new IllegalArgumentException("Manager ID and Employee ID must not be null!");
        }

        Manager manager = managerRepository.findById(requestDTO.getManagerId())
                .orElseThrow(() -> new Exception("Manager not found"));
        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new Exception("Employee not found"));

        // Check if the employee is already assigned
        if (employee.isAssigned()) {
            throw new Exception("Employee is already assigned to a project and cannot be reassigned.");
        }

        ResourceRequest request = new ResourceRequest();
        request.setManager(manager);
        request.setEmployee(employee);
        request.setStatus(AppConstants.REQUEST_PENDING);
        request.setRequestDate(LocalDateTime.now());

        resourceRequestRepository.save(request);
    }
    @Override
    @Transactional
    public void approveRequest(Integer requestId) throws Exception {
        ResourceRequest request = resourceRequestRepository.findById(requestId).orElseThrow(() -> new Exception("Request not found"));
        request.setStatus(AppConstants.REQUEST_APPROVED);
        resourceRequestRepository.save(request);

        Employee employee = request.getEmployee();
        employee.setAssigned(true);
        employee.setManager(request.getManager());
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public List<ResourceRequestDTO> getAllRequests() throws Exception {
        return resourceRequestRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override
    public void declineRequest(Integer requestId) throws Exception {
        ResourceRequest request = resourceRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Request not found"));
        request.setStatus(AppConstants.REQUEST_DECLINED);
        resourceRequestRepository.save(request);
    }
    @Override
    @Transactional
    public void unassignEmployee(Integer employeeId) throws Exception {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new Exception("Employee not found"));

        employee.setAssigned(false);
        employee.setManager(null);
        employeeRepository.save(employee);
    }
    private ResourceRequestDTO convertToDTO(ResourceRequest request) {
        ResourceRequestDTO dto = new ResourceRequestDTO();
        dto.setRequestId(request.getRequestId());
        if (request.getManager() != null) {
            dto.setManagerId(request.getManager().getMgrId());
        }
        if (request.getEmployee() != null) {
            dto.setEmployeeId(request.getEmployee().getEmpId());
        }
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        return dto;
    }

//    private ResourceRequestDTO convertToDTO(ResourceRequest request) {
//        ResourceRequestDTO dto = new ResourceRequestDTO();
//        dto.setRequestId(request.getRequestId());
//        dto.setManagerId(request.getManager().getMgrId());
//        dto.setEmployeeId(request.getEmployee().getEmpId());
//        dto.setStatus(request.getStatus());
//        dto.setRequestDate(request.getRequestDate());
//        return dto;
//    }
}

