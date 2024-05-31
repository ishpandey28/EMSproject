package com.ish.capstone.repository;

import com.ish.capstone.entity.Employee;
import com.ish.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Employee findByUser(User user);
    List<Employee> findByAssignedFalse();
    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE LOWER(s) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Employee> findBySkill(@Param("skill") String skill);
}
