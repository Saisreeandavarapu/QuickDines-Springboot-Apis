package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Long> {
}
