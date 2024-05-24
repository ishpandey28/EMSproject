package com.ish.capstone.serviceImpl;

import com.ish.capstone.entity.*;
import com.ish.capstone.repository.EmployeeRepository;
import com.ish.capstone.repository.EmployeeRequestRepository;
import com.ish.capstone.repository.ManagerRepository;
import com.ish.capstone.repository.ProjectRepository;
import com.ish.capstone.service.EmployeeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeRequestServiceImpl implements EmployeeRequestService {

    private final EmployeeRequestRepository employeeRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ResponseEntity<String> createRequest(Integer managerId, Integer projectId, List<Integer> employeeIds) {
        Optional<Manager> managerOpt = managerRepository.findById(managerId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (managerOpt.isPresent() && projectOpt.isPresent()) {
            EmployeeRequest request = new EmployeeRequest();
            request.setManager(managerOpt.get());
            request.setProject(projectOpt.get());
            request.setStatus(RequestStatus.PENDING);

            Set<Employee> employees = employeeRepository.findAllById(employeeIds).stream().collect(Collectors.toSet());
            request.setEmployees(employees);

            employeeRequestRepository.save(request);
            return new ResponseEntity<>("Request created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Manager or Project not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> approveRequest(Integer requestId) {
        Optional<EmployeeRequest> requestOpt = employeeRequestRepository.findById(requestId);

        if (requestOpt.isPresent()) {
            EmployeeRequest request = requestOpt.get();
            request.setStatus(RequestStatus.APPROVED);
            for (Employee employee : request.getEmployees()) {
                employee.setAssigned(true);
                employee.setProject(request.getProject());
                employeeRepository.save(employee);
            }
            employeeRequestRepository.save(request);
            return new ResponseEntity<>("Request approved successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Request not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> rejectRequest(Integer requestId) {
        Optional<EmployeeRequest> requestOpt = employeeRequestRepository.findById(requestId);

        if (requestOpt.isPresent()) {
            EmployeeRequest request = requestOpt.get();
            request.setStatus(RequestStatus.REJECTED);
            employeeRequestRepository.save(request);
            return new ResponseEntity<>("Request rejected successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Request not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> unassignEmployee(Integer employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.setAssigned(false);
            employee.setProject(null);
            employeeRepository.save(employee);
            return new ResponseEntity<>("Employee unassigned successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }
}