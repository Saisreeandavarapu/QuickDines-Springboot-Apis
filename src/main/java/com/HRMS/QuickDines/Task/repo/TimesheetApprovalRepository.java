package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import com.HRMS.QuickDines.Task.model.TimesheetApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetApprovalRepository
        extends JpaRepository<TimesheetApproval, Long> {

    List<TimesheetApproval> findByTimesheet_Id(Long timesheetId);

    List<TimesheetApproval> findByStatus(TimesheetStatus status);

    List<TimesheetApproval> findByApprover_IdAndStatus(
            String employeeId,
            TimesheetStatus status
    );

    List<TimesheetApproval> findByTimesheet_Employee_Id(
            String employeeId
    );
}