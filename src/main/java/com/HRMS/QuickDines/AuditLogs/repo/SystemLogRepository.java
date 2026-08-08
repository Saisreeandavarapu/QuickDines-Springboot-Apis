package com.HRMS.QuickDines.AuditLogs.repo;

import com.HRMS.QuickDines.AuditLogs.Entity.SystemLogLevel;
import com.HRMS.QuickDines.AuditLogs.model.SystemLog;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    List<SystemLog> findByLogLevel(SystemLogLevel logLevel);

    List<SystemLog> findByModuleName(String moduleName);

    List<SystemLog> findByServiceName(String serviceName);

    List<SystemLog> findByResponseCode(Integer responseCode);
}
