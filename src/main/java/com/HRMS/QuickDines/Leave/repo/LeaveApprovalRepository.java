package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
}
