package com.ish.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequestDTO {
    private Integer requestId;
    private Integer managerId;
    private Integer employeeId;
    private String status;
    private LocalDateTime requestDate;
}