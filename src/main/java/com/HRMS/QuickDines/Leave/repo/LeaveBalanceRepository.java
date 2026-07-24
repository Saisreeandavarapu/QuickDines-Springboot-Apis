package com.HRMS.QuickDines.Leave.repo;

import com.HRMS.QuickDines.Leave.model.LeaveBalance;
import com.HRMS.QuickDines.Leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<Object> findByEmployeeId(String employeeId);
}
