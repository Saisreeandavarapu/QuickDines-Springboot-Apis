package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.Entity.WorkFromHomeStatus;
import com.HRMS.QuickDines.Attendance.model.WorkFromHomeRequest;
import com.HRMS.QuickDines.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkFromHomeRepository
        extends JpaRepository<WorkFromHomeRequest, Long> {

    List<WorkFromHomeRequest> findByEmployee(Employee employee);

    List<WorkFromHomeRequest> findByManager(Employee manager);

    List<WorkFromHomeRequest> findByStatus(WorkFromHomeStatus status);

    List<WorkFromHomeRequest> findByEmployeeAndStatus(
            Employee employee,
            WorkFromHomeStatus status
    );

    List<WorkFromHomeRequest> findByFromDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}