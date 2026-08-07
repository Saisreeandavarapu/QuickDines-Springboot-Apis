package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflow, Long> {
}
