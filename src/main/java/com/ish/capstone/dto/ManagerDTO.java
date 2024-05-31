package com.ish.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDTO {
    private Integer mgrId;
    private String name;
    private String contactNumber;
    private String email;
    private String designation;
    private Set<Integer> employees;
    private Set<String> projectNames;

}

