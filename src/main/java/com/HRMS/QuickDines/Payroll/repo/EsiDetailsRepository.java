package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Payroll.model.EsiDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EsiDetailsRepository extends JpaRepository<EsiDetails, Long> {
    boolean existsByEmployee(Employee employee);

    Optional<EsiDetails> findByEmployee(Employee employee);
}
