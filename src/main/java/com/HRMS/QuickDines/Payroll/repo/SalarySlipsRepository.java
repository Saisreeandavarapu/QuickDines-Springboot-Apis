package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.SalarySlips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalarySlipsRepository extends JpaRepository<SalarySlips, Long> {
    Object findByEmployeeEmployeeId(String employeeId);
}
