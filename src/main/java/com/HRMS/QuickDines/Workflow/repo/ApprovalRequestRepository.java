package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.model.ApprovalRequest;
import com.HRMS.QuickDines.Workflow.Entity.ApprovalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRequestRepository
        extends JpaRepository<ApprovalRequest, Long> {

    // Employee requests
    List<ApprovalRequest> findByEmployee_EmployeeId(
            String employeeId
    );

    // Pending requests
    List<ApprovalRequest> findByStatus(
            ApprovalRequestStatus status
    );

    // Requests by status
    List<ApprovalRequest> findByStatusOrderByCreatedAtDesc(
            ApprovalRequestStatus status
    );

    // Requests by request type
    List<ApprovalRequest> findByRequestType(
            String requestType
    );

    // Employee + status
    List<ApprovalRequest> findByEmployee_EmployeeIdAndStatus(
            String employeeId,
            ApprovalRequestStatus status
    );

    // Workflow requests
    List<ApprovalRequest> findByWorkflow_Id(
            Long workflowId
    );
}
