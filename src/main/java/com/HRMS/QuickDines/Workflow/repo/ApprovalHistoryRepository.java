package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.model.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory,Long> {
    List<ApprovalHistory> findByApprovalRequestIdOrderByActionDateAsc(Long requestId);

    List<ApprovalHistory> findByEmployeeId(String employeeId);
    // 

    // History of an approver
    List<ApprovalHistory>
    findByApprover_EmployeeId(
            String employeeId
    );

    // History by workflow level
    List<ApprovalHistory>
    findByWorkflowLevelIdOrderByActionDateAsc(
            Long workflowLevelId
    );

    // History by request and approver
    List<ApprovalHistory>
    findByApprovalRequestIdAndApprover_EmployeeId(
            Long requestId,
            String employeeId
    );
}
