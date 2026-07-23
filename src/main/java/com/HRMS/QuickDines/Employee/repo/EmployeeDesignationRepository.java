package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.EmployeeDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDesignationRepository extends JpaRepository<EmployeeDesignation, Long> {
    List<EmployeeDesignation> findByEmployeeId(String employeeId);
}
