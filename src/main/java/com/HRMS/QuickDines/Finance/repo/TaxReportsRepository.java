package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.TaxReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxReportsRepository extends JpaRepository<TaxReports, Long> {
    Optional<TaxReports> findByEmployeeEmployeeId(String employeeId);
}
