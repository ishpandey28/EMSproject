package com.ish.capstone.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "resource_request")
public class ResourceRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "mgrId")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "empId")
    private Employee employee;

    private String status; // e.g. "PENDING", "APPROVED", "REJECTED"
    private LocalDateTime requestDate;
}