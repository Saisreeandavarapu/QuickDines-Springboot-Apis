package com.HRMS.QuickDines.AuditLogs.repo;

import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByModuleName(String moduleName);

    List<AuditLog> findByActionType(AuditActionType actionType);

    List<AuditLog> findByEmployee_Id(String employeeId);

    List<AuditLog> findByPerformedBy_Id(String employeeId);

    List<AuditLog> findByReferenceId(Long referenceId);
}
