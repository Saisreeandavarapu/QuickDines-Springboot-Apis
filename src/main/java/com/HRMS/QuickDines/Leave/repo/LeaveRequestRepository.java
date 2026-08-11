package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    LeaveRequest findByEmployeeEmployeeId(String employeeId);

    List<LeaveRequest> findByStatus(String pending);
    List<LeaveRequest> findByLeaveTypeId(Long leaveTypeId);
}
