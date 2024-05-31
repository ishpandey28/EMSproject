package com.ish.capstone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Employee> employees= new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "manager_projects",
            joinColumns = @JoinColumn(name = "mgr_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonBackReference
    private Set<Project> projects= new HashSet<>();

    private String designation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return mgrId.equals(manager.mgrId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mgrId);
    }

}
