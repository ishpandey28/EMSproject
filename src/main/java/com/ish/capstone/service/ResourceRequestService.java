package com.ish.capstone.service;

import com.ish.capstone.dto.ResourceRequestDTO;

import java.util.List;

public interface ResourceRequestService {
    void sendRequest(ResourceRequestDTO requestDTO) throws Exception;
    void approveRequest(Integer requestId) throws Exception;
    List<ResourceRequestDTO> getAllRequests() throws Exception;
    void declineRequest(Integer requestId) throws Exception; //
    void unassignEmployee(Integer employeeId) throws Exception;

}
