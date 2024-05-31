package com.ish.capstone.dto;

import com.ish.capstone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;
    private String role;
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.contactNumber = user.getContactNumber();
        this.status = user.getStatus();
        this.role = user.getRole();
    }
}
