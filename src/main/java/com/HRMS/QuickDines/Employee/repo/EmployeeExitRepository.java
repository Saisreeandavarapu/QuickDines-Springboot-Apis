package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.EmployeeExitManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeExitRepository extends JpaRepository<EmployeeExitManagement, Long> {
    Optional<EmployeeExitManagement> findByEmployeeId(String employeeId);
}
