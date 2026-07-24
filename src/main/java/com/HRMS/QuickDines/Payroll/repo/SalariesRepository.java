package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Payroll.model.Salaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalariesRepository extends JpaRepository<Salaries,Long> {
    List<Salaries> findByEmployeeEmployeeId(String employeeId);

    Optional<Object> findByEmployee(Employee employee);
}
