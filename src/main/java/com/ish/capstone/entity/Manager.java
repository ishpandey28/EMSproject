package com.ish.capstone.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name = "manager")
public class Manager implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mgrId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String name;
    private String contactNumber;
    private String email;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> employees;

    @ManyToMany
    @JoinTable(
            name = "manager_projects",
            joinColumns = @JoinColumn(name = "mgr_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;

    private String designation;

}
