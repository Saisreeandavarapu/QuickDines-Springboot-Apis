package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.Entity.ApprovalRequestStatus;
import com.HRMS.QuickDines.Workflow.model.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByEmployee_EmployeeId(String employeeId);
    List<ApprovalRequest> findByStatus(ApprovalRequestStatus status);

    List<ApprovalRequest> findByEmployeeId(String employeeId);
    // Employee requests
    List<ApprovalRequest>
    findByApproverEmployee_EmployeeId(String employeeId);


    // Requests by type
    List<ApprovalRequest>
    findByRequestType(String requestType);

    // Requests by workflow
    List<ApprovalRequest>
    findByWorkflowId(Long workflowId);

    // Requests by workflow and status
    List<ApprovalRequest>
    findByWorkflowIdAndStatus(
            Long workflowId,
            ApprovalRequestStatus status
    );

    // Requests by employee and status
    List<ApprovalRequest>
    findByApproverEmployee_EmployeeIdAndStatus(
            String employeeId,
            ApprovalRequestStatus status
    );

    // Check duplicate business request
    boolean existsByRequestTypeAndReferenceId(
            String requestType,
            Long referenceId
    );
}
