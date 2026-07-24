package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Payroll.model.PfDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PfDetailsRepository extends JpaRepository<PfDetails, Long> {
    boolean existsByEmployee(Employee employee);

    Optional<PfDetails> findByEmployee(Employee employee);
}
