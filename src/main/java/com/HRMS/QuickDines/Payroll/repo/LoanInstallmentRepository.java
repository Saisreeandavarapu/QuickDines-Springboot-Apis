package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
}
