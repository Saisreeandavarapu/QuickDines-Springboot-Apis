package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveEncashment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveEncashmentRepository extends JpaRepository<LeaveEncashment, Long> {
}
