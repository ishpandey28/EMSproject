package com.ish.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Integer empId;
    private String name;
    private String contactNumber;
    private String email;
    private String designation;
    private String projectStatus;
    private boolean assigned;
    private String managerName;
    private String projectName;
    private Set<String> skills;
}
