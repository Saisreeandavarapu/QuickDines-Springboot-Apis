package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Object findByEmployeeEmployeeId(String employeeId);

    Object findByStatus(String pending);
}
