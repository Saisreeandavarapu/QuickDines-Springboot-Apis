package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.AuditReports;
import com.HRMS.QuickDines.Performance.model.PerformanceReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReportsRepository extends JpaRepository<PerformanceReports, Long> {
    List<PerformanceReports> findByEmployeeEmployeeId(String employeeId);
    List<PerformanceReports>
    findByPerformanceScoreGreaterThanEqual(Double performanceScore);


    List<PerformanceReports>
    findByPerformanceScoreLessThan(Double performanceScore);
}
