package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.EmployeeLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeLoanRepository extends JpaRepository<EmployeeLoan, Long> {
}
