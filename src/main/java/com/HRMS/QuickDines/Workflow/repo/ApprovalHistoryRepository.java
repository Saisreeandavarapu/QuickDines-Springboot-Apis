package com.HRMS.QuickDines.Workflow.repo;

import com.HRMS.QuickDines.Workflow.model.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory,Long> {
    List<ApprovalHistory> findByApprovalRequestIdOrderByActionDateAsc(Long requestId);

    List<ApprovalHistory> findByEmployeeId(String employeeId);
}
