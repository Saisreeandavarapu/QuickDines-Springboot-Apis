package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.Entity.ApprovalRequestStatus;
import com.HRMS.QuickDines.Workflow.model.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByEmployeeEmployeeId(String employeeId);
    List<ApprovalRequest> findByStatus(ApprovalRequestStatus status);

    List<ApprovalRequest> findByEmployeeId(String employeeId);
}
