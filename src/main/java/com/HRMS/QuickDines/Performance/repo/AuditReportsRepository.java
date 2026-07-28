package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.AuditReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditReportsRepository extends JpaRepository<AuditReports, Long> {
    List<AuditReports> findByEmployeeEmployeeId(String employeeId);
    List<AuditReports> findByAuditStatus(String auditStatus);
}
