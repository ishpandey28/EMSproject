package com.ish.capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email =:email")
@NamedQuery(name = "User.getAllUser", query = "select new com.ish.capstone.dto.UserDTO(u.id, u.name, u.email, u.contactNumber, u.status, u.role) from User u where u.role IN ('admin','employee','manager')")
@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String contactNumber;
    private String email;
    private String password;
    private String status;
    private String role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Employee employee;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Manager manager;
    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactNumber, email, password, status, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(contactNumber, user.contactNumber) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(status, user.status) &&
                Objects.equals(role, user.role);
    }

}
