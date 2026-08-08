package com.HRMS.QuickDines.AuditLogs.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Entity.SystemLogLevel;
import com.HRMS.QuickDines.AuditLogs.model.ActivityLog;
import com.HRMS.QuickDines.AuditLogs.model.AuditLog;
import com.HRMS.QuickDines.AuditLogs.model.SystemLog;
import com.HRMS.QuickDines.AuditLogs.repo.ActivityLogRepository;
import com.HRMS.QuickDines.AuditLogs.repo.AuditLogRepository;
import com.HRMS.QuickDines.AuditLogs.repo.SystemLogRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogsService
{
    private final AuditLogRepository auditLogRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SystemLogRepository systemLogRepository;
    private final EmployeeRepository employeeRepository;


    // =========================================================
    // AUDIT LOG METHODS
    // =========================================================

    /**
     * Create complete audit log.
     */
    public AuditLog createAuditLog(
            String moduleName,
            String referenceId,
            AuditActionType actionType,
            String performedBy,
            String employeeId,
            String description,
            String oldValue,
            String newValue,
            String ipAddress,
            String deviceInfo) {

        Employee performer = null;
        Employee employee = null;

        if (performedBy != null) {
            performer = employeeRepository.findById(performedBy)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Performed by employee not found"));
        }

        if (employeeId != null) {
            employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Employee not found"));
        }

        AuditLog auditLog = new AuditLog();

        auditLog.setModuleName(moduleName);
        auditLog.setReferenceId(referenceId);
        auditLog.setActionType(actionType);
        auditLog.setPerformedBy(performer);
        auditLog.setEmployee(employee);
        auditLog.setDescription(description);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setIpAddress(ipAddress);
        auditLog.setDeviceInfo(deviceInfo);
        auditLog.setCreatedAt(LocalDateTime.now());

        return auditLogRepository.save(auditLog);
    }


    /**
     * Log CREATE operation.
     */
    public AuditLog logCreate(
            String moduleName,
            String referenceId,
            String performedBy,
            String employeeId,
            String description) {

        return createAuditLog(
                moduleName,
                referenceId,
                AuditActionType.CREATE,
                performedBy,
                employeeId,
                description,
                null,
                null,
                null,
                null
        );
    }


    /**
     * Log UPDATE operation.
     */
    public AuditLog logUpdate(
            String moduleName,
            String referenceId,
            String performedBy,
            String employeeId,
            String description,
            String oldValue,
            String newValue) {

        return createAuditLog(
                moduleName,
                referenceId,
                AuditActionType.UPDATE,
                performedBy,
                employeeId,
                description,
                oldValue,
                newValue,
                null,
                null
        );
    }


    /**
     * Log DELETE operation.
     */
    public AuditLog logDelete(
            String moduleName,
            String referenceId,
            String performedBy,
            String employeeId,
            String description) {

        return createAuditLog(
                moduleName,
                referenceId,
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                description,
                null,
                null,
                null,
                null
        );
    }


    /**
     * Log APPROVE operation.
     */
    public AuditLog logApprove(
            String moduleName,
            String referenceId,
            String performedBy,
            String employeeId,
            String description) {

        return createAuditLog(
                moduleName,
                referenceId,
                AuditActionType.APPROVE,
                performedBy,
                employeeId,
                description,
                null,
                null,
                null,
                null
        );
    }


    /**
     * Log REJECT operation.
     */
    public AuditLog logReject(
            String moduleName,
            String referenceId,
            String performedBy,
            String employeeId,
            String description) {

        return createAuditLog(
                moduleName,
                referenceId,
                AuditActionType.REJECT,
                performedBy,
                employeeId,
                description,
                null,
                null,
                null,
                null
        );
    }


    /**
     * Log LOGIN operation.
     */
    public AuditLog logLogin(
            String employeeId,
            String ipAddress,
            String deviceInfo) {

        return createAuditLog(
                "AUTHENTICATION",
                null,
                AuditActionType.LOGIN,
                employeeId,
                employeeId,
                "Employee logged into the system",
                null,
                null,
                ipAddress,
                deviceInfo
        );
    }


    /**
     * Log LOGOUT operation.
     */
    public AuditLog logLogout(
            String employeeId,
            String ipAddress,
            String deviceInfo) {

        return createAuditLog(
                "AUTHENTICATION",
                null,
                AuditActionType.LOGOUT,
                employeeId,
                employeeId,
                "Employee logged out of the system",
                null,
                null,
                ipAddress,
                deviceInfo
        );
    }


    /**
     * Get all audit logs.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {

        return auditLogRepository.findAll();
    }


    /**
     * Get audit log by ID.
     */
    @Transactional(readOnly = true)
    public AuditLog getAuditLogById(Long id) {

        return auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Audit log not found"));
    }


    /**
     * Get audit logs by module.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByModule(
            String moduleName) {

        return auditLogRepository.findByModuleName(moduleName);
    }


    /**
     * Get audit logs by action.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByAction(
            AuditActionType actionType) {

        return auditLogRepository
                .findByActionType(actionType);
    }


    /**
     * Get audit logs by employee.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByEmployee(
            String employeeId) {

        return auditLogRepository
                .findByEmployee_Id(employeeId);
    }


    /**
     * Get audit logs by performed-by employee.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByPerformedBy(
            String employeeId) {

        return auditLogRepository
                .findByPerformedBy_Id(employeeId);
    }


    /**
     * Get audit logs by reference.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByReference(
            Long referenceId) {

        return auditLogRepository
                .findByReferenceId(referenceId);
    }


    // =========================================================
    // ACTIVITY LOG METHODS
    // =========================================================

    /**
     * Create complete activity log.
     */
    public ActivityLog createActivityLog(
            Long employeeId,
            String activityName,
            String activityModule,
            String activityDescription,
            ActivityStatus activityStatus,
            String loginIp,
            String browser,
            String operatingSystem) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found"));

        ActivityLog activityLog = new ActivityLog();

        activityLog.setEmployee(employee);
        activityLog.setActivityName(activityName);
        activityLog.setActivityModule(activityModule);
        activityLog.setActivityDescription(activityDescription);
        activityLog.setActivityStatus(activityStatus);
        activityLog.setLoginIp(loginIp);
        activityLog.setBrowser(browser);
        activityLog.setOperatingSystem(operatingSystem);
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setCreatedAt(LocalDateTime.now());

        return activityLogRepository.save(activityLog);
    }


    /**
     * Generic activity logger.
     */
    public ActivityLog logActivity(
            String employeeId,
            String activityName,
            String activityModule,
            String description,
            ActivityStatus status,
            String ip,
            String browser,
            String operatingSystem) {

        return createActivityLog(
                Long.valueOf(employeeId),
                activityName,
                activityModule,
                description,
                status,
                ip,
                browser,
                operatingSystem
        );
    }


    /**
     * Get all activities.
     */
    @Transactional(readOnly = true)
    public List<ActivityLog> getAllActivities() {

        return activityLogRepository.findAll();
    }


    /**
     * Get activity by ID.
     */
    @Transactional(readOnly = true)
    public ActivityLog getActivityById(Long id) {

        return activityLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Activity log not found"));
    }


    /**
     * Get activities by employee.
     */
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByEmployee(
            String employeeId) {

        return activityLogRepository
                .findByEmployee_Id(employeeId);
    }


    /**
     * Get activities by module.
     */
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByModule(
            String moduleName) {

        return activityLogRepository
                .findByActivityModule(moduleName);
    }


    /**
     * Get activities by status.
     */
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivitiesByStatus(
            ActivityStatus status) {

        return activityLogRepository
                .findByActivityStatus(status);
    }


    /**
     * Delete activity.
     */
    public void deleteActivity(Long id) {

        ActivityLog activityLog =
                getActivityById(id);

        activityLogRepository.delete(activityLog);
    }


    // =========================================================
    // SYSTEM LOG METHODS
    // =========================================================

    /**
     * Create complete system log.
     */
    public SystemLog createSystemLog(
            SystemLogLevel logLevel,
            String moduleName,
            String serviceName,
            String apiEndpoint,
            String requestMethod,
            Integer responseCode,
            String errorMessage,
            String stackTrace,
            String serverName) {

        SystemLog systemLog = new SystemLog();

        systemLog.setLogLevel(logLevel);
        systemLog.setModuleName(moduleName);
        systemLog.setServiceName(serviceName);
        systemLog.setApiEndpoint(apiEndpoint);
        systemLog.setRequestMethod(requestMethod);
        systemLog.setResponseCode(responseCode);
        systemLog.setErrorMessage(errorMessage);
        systemLog.setStackTrace(stackTrace);
        systemLog.setServerName(serverName);
        systemLog.setLoggedAt(LocalDateTime.now());
        systemLog.setCreatedAt(LocalDateTime.now());

        return systemLogRepository.save(systemLog);
    }


    /**
     * Log INFO.
     */
    public SystemLog logInfo(
            String moduleName,
            String serviceName,
            String message) {

        return createSystemLog(
                SystemLogLevel.INFO,
                moduleName,
                serviceName,
                null,
                null,
                null,
                message,
                null,
                null
        );
    }


    /**
     * Log WARNING.
     */
    public SystemLog logWarning(
            String moduleName,
            String serviceName,
            String message) {

        return createSystemLog(
                SystemLogLevel.WARNING,
                moduleName,
                serviceName,
                null,
                null,
                null,
                message,
                null,
                null
        );
    }


    /**
     * Log ERROR.
     */
    public SystemLog logError(
            String moduleName,
            String serviceName,
            String message,
            String stackTrace) {

        return createSystemLog(
                SystemLogLevel.ERROR,
                moduleName,
                serviceName,
                null,
                null,
                null,
                message,
                stackTrace,
                null
        );
    }


    /**
     * Log DEBUG.
     */
    public SystemLog logDebug(
            String moduleName,
            String serviceName,
            String message) {

        return createSystemLog(
                SystemLogLevel.DEBUG,
                moduleName,
                serviceName,
                null,
                null,
                null,
                message,
                null,
                null
        );
    }


    /**
     * Log API request.
     */
    public SystemLog logApiRequest(
            String moduleName,
            String serviceName,
            String endpoint,
            String requestMethod) {

        return createSystemLog(
                SystemLogLevel.INFO,
                moduleName,
                serviceName,
                endpoint,
                requestMethod,
                null,
                "API request received",
                null,
                null
        );
    }


    /**
     * Log API response.
     */
    public SystemLog logApiResponse(
            String moduleName,
            String serviceName,
            String endpoint,
            String requestMethod,
            Integer responseCode) {

        return createSystemLog(
                SystemLogLevel.INFO,
                moduleName,
                serviceName,
                endpoint,
                requestMethod,
                responseCode,
                "API response generated",
                null,
                null
        );
    }


    /**
     * Log exception.
     */
    public SystemLog logException(
            String moduleName,
            String serviceName,
            String errorMessage,
            String stackTrace) {

        return createSystemLog(
                SystemLogLevel.ERROR,
                moduleName,
                serviceName,
                null,
                null,
                500,
                errorMessage,
                stackTrace,
                null
        );
    }


    /**
     * Get all system logs.
     */
    @Transactional(readOnly = true)
    public List<SystemLog> getAllSystemLogs() {

        return systemLogRepository.findAll();
    }


    /**
     * Get system log by ID.
     */
    @Transactional(readOnly = true)
    public SystemLog getSystemLogById(Long id) {

        return systemLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "System log not found"));
    }


    /**
     * Get system logs by level.
     */
    @Transactional(readOnly = true)
    public List<SystemLog> getSystemLogsByLevel(
            SystemLogLevel level) {

        return systemLogRepository
                .findByLogLevel(level);
    }


    /**
     * Get system logs by module.
     */
    @Transactional(readOnly = true)
    public List<SystemLog> getSystemLogsByModule(
            String moduleName) {

        return systemLogRepository
                .findByModuleName(moduleName);
    }


    /**
     * Get system logs by service.
     */
    @Transactional(readOnly = true)
    public List<SystemLog> getSystemLogsByService(
            String serviceName) {

        return systemLogRepository
                .findByServiceName(serviceName);
    }


    /**
     * Get system logs by response code.
     */
    @Transactional(readOnly = true)
    public List<SystemLog> getSystemLogsByResponseCode(
            Integer responseCode) {

        return systemLogRepository
                .findByResponseCode(responseCode);
    }


    /**
     * Delete system log.
     */
    public void deleteSystemLog(Long id) {

        SystemLog systemLog =
                getSystemLogById(id);

        systemLogRepository.delete(systemLog);
    }
}

