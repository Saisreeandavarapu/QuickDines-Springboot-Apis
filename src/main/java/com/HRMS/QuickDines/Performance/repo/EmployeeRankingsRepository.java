package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.EmployeeRankings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRankingsRepository extends JpaRepository<EmployeeRankings, Long> {
    EmployeeRankings findByEmployeeEmployeeId(String employeeId);
}
