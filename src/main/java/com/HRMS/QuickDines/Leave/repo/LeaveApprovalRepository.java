package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    List<LeaveApproval> findByApprovalRoleAndStatus(
            String approvalRole,
            String status
    );

    List<LeaveApproval> findByLeaveRequestId(
            Long leaveRequestId
    );

    Optional<LeaveApproval> findByLeaveRequestIdAndApprovalRoleAndStatus(
            Long leaveRequestId,
            String approvalRole,
            String status
    );
}
