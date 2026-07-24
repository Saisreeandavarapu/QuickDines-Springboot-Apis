package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.BonusManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonusManagementRepository extends JpaRepository<BonusManagement, Long> {
    List<BonusManagement> findByEmployeeEmployeeId(String employeeId);
}
