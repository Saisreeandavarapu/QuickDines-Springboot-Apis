package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflowLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalWorkflowLevelRepository extends JpaRepository<ApprovalWorkflowLevel, Long> {
    boolean existsByWorkflowIdAndLevelNumber( Long workflowId, Integer levelNumber);
    List<ApprovalWorkflowLevel> findByWorkflowIdOrderByLevelNumberAsc(Long workflowId);
    Optional<ApprovalWorkflowLevel> findByWorkflowIdAndLevelNumber(Long workflowId, Integer levelNumber);

    long countByWorkflowId(Long workflowId);

    List<ApprovalWorkflowLevel> findByWorkflowIdAndStatus(Long id, WorkflowStatus workflowStatus);
}
