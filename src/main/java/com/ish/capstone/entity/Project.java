package com.ish.capstone.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "project")
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;
    private String projectName;

    @OneToMany(mappedBy = "project")
    private Set<Employee> employees;

    @ManyToMany(mappedBy = "projects")
    private Set<Manager> managers;

}