package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflow, Long> {
    List<ApprovalWorkflow> findByStatus(WorkflowStatus workflowStatus);

    List<ApprovalWorkflow> findByWorkflowType(String type);
}
