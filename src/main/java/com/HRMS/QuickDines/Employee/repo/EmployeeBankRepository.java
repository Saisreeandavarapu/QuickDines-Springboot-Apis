package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.model.EmployeeBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public interface EmployeeBankRepository extends JpaRepository<EmployeeBankDetails, Long> {
    Optional<EmployeeBankDetails> findByEmployeeId(String employeeId);
}
