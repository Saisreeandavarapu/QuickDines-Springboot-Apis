package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Payroll.model.TdsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TdsDetailsRepository extends JpaRepository<TdsDetails, Long> {
    boolean existsByEmployee(Employee employee);

    Optional<TdsDetails> findByEmployee(Employee employee);
}
