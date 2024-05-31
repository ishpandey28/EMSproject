package com.ish.capstone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "employee")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    private String name;
    private String contactNumber;
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    private String designation;
    private String projectStatus;
    private boolean assigned;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResourceRequest> resourceRequests = new HashSet<>();
    @Override
    public int hashCode() {
        return Objects.hash(empId, name, contactNumber, email, designation, projectStatus, assigned);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return assigned == employee.assigned &&
                Objects.equals(empId, employee.empId) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(contactNumber, employee.contactNumber) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(designation, employee.designation) &&
                Objects.equals(projectStatus, employee.projectStatus);
    }
}