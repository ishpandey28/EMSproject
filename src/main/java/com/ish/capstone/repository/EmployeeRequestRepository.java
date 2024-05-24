package com.ish.capstone.repository;

import com.ish.capstone.entity.EmployeeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRequestRepository extends JpaRepository<EmployeeRequest, Integer> {
}
