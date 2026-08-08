package com.HRMS.QuickDines.AuditLogs.repo;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByEmployee_Id(String employeeId);

    List<ActivityLog> findByActivityModule(String activityModule);

    List<ActivityLog> findByActivityStatus(ActivityStatus activityStatus);
}
