package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.PayrollHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollHistoryRepository extends JpaRepository<PayrollHistory, Long> {
}
