package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.EmployeeApproval;
import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeApprovalRepository
        extends JpaRepository<EmployeeApproval, Long> {

    Optional<EmployeeApproval> findByEmployee_Id(Long employeeId);

    boolean existsByEmployee_Id(Long employeeId);

    List<EmployeeApproval> findByHrStatus(ApprovalStatus status);

    List<EmployeeApproval> findByAdminStatus(ApprovalStatus status);

    List<EmployeeApproval> findByDepartmentHeadStatus(
            ApprovalStatus status
    );

    List<EmployeeApproval> findByFinalStatus(
            ApprovalStatus status
    );
}