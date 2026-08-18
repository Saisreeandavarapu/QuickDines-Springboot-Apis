package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import com.HRMS.QuickDines.Task.model.EmployeeTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeTimesheetRepository
        extends JpaRepository<EmployeeTimesheet, Long> {

    List<EmployeeTimesheet> findByEmployee_Id(String employeeId);

    List<EmployeeTimesheet> findByWorkDate(LocalDate date);

    Optional<EmployeeTimesheet> findByEmployee_IdAndWorkDate(
            String employeeId,
            LocalDate workDate
    );

    List<EmployeeTimesheet> findByStatus(TimesheetStatus status);

    List<EmployeeTimesheet> findByEmployee_IdAndStatus(
            String employeeId,
            TimesheetStatus status
    );
}