package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.Entity.EmployeeTransferStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.model.EmployeeTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeTransferRepository extends JpaRepository<EmployeeTransfer, Long> {
    void deleteByEmployee(Employee employee);
    List<EmployeeTransfer> findByEmployeeEmployeeId(String employeeId);

    List<EmployeeTransfer> findByManagerEmployeeIdAndStatus(
            String employeeId,
            EmployeeTransferStatus status
    );

    List<EmployeeTransfer> findByHrEmployeeIdAndStatus(
            String employeeId,
            EmployeeTransferStatus status
    );

    List<EmployeeTransfer> findByStatus(
            EmployeeTransferStatus status
    );

}

