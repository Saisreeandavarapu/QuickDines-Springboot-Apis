package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long> {
}
