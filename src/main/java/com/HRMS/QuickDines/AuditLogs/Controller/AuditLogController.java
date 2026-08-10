package com.HRMS.QuickDines.AuditLogs.Controller;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Entity.SystemLogLevel;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.model.ActivityLog;
import com.HRMS.QuickDines.AuditLogs.model.AuditLog;
import com.HRMS.QuickDines.AuditLogs.model.SystemLog;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogsService auditLogService;


    // =========================================================
    // AUDIT LOG APIs
    // =========================================================

    /**
     * Create Audit Log
     */
    @PostMapping("/audit-log")
    @PreAuthorize("hasAuthority('AUDIT_LOG_CREATE')")
    public ResponseEntity<AuditLog> createAuditLog(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam AuditActionType actionType,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String oldValue,
            @RequestParam(required = false) String newValue,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String deviceInfo) {

        return ResponseEntity.ok(
                auditLogService.createAuditLog(
                        moduleName,
                        referenceId,
                        actionType,
                        performedBy,
                        employeeId,
                        description,
                        oldValue,
                        newValue,
                        ipAddress,
                        deviceInfo
                )
        );
    }


    /**
     * Get All Audit Logs
     */
    @GetMapping("/audit-log/all")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs()
        );
    }


    /**
     * Get Audit Log By ID
     */
    @GetMapping("/audit-log/{id}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<AuditLog> getAuditLogById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogById(id)
        );
    }


    /**
     * Get Audit Logs By Module
     */
    @GetMapping("/audit-log/module/{moduleName}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByModule(
            @PathVariable String moduleName) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByModule(moduleName)
        );
    }


    /**
     * Get Audit Logs By Action
     */
    @GetMapping("/audit-log/action/{actionType}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(
            @PathVariable AuditActionType actionType) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByAction(actionType)
        );
    }


    /**
     * Get Audit Logs By Employee
     */
    @GetMapping("/audit-log/employee/{employeeId}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEmployee(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByEmployee(employeeId)
        );
    }


    /**
     * Get Audit Logs By Performed By
     */
    @GetMapping("/audit-log/performed-by/{employeeId}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByPerformedBy(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByPerformedBy(employeeId)
        );
    }


    /**
     * Get Audit Logs By Reference ID
     */
    @GetMapping("/audit-log/reference/{referenceId}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_VIEW')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByReference(
            @PathVariable Long referenceId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByReference(referenceId)
        );
    }


    // =========================================================
    // QUICK AUDIT ACTION APIs
    // =========================================================

    /**
     * Log CREATE
     */
    @PostMapping("/audit-log/create")
    @PreAuthorize("hasAuthority('AUDIT_LOG_CREATE')")
    public ResponseEntity<AuditLog> logCreate(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam String description) {

        return ResponseEntity.ok(
                auditLogService.logCreate(
                        moduleName,
                        referenceId,
                        performedBy,
                        employeeId,
                        description
                )
        );
    }


    /**
     * Log UPDATE
     */
    @PostMapping("/audit-log/update")
    @PreAuthorize("hasAuthority('AUDIT_LOG_UPDATE')")
    public ResponseEntity<AuditLog> logUpdate(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam String description,
            @RequestParam(required = false) String oldValue,
            @RequestParam(required = false) String newValue) {

        return ResponseEntity.ok(
                auditLogService.logUpdate(
                        moduleName,
                        referenceId,
                        performedBy,
                        employeeId,
                        description,
                        oldValue,
                        newValue
                )
        );
    }


    /**
     * Log DELETE
     */
    @PostMapping("/audit-log/delete")
    @PreAuthorize("hasAuthority('AUDIT_LOG_DELETE')")
    public ResponseEntity<AuditLog> logDelete(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam String description) {

        return ResponseEntity.ok(
                auditLogService.logDelete(
                        moduleName,
                        referenceId,
                        performedBy,
                        employeeId,
                        description
                )
        );
    }


    /**
     * Log APPROVE
     */
    @PostMapping("/audit-log/approve")
    @PreAuthorize("hasAuthority('AUDIT_LOG_APPROVE')")
    public ResponseEntity<AuditLog> logApprove(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam String description) {

        return ResponseEntity.ok(
                auditLogService.logApprove(
                        moduleName,
                        referenceId,
                        performedBy,
                        employeeId,
                        description
                )
        );
    }


    /**
     * Log REJECT
     */
    @PostMapping("/audit-log/reject")
    @PreAuthorize("hasAuthority('AUDIT_LOG_REJECT')")
    public ResponseEntity<AuditLog> logReject(

            @RequestParam String moduleName,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String employeeId,
            @RequestParam String description) {

        return ResponseEntity.ok(
                auditLogService.logReject(
                        moduleName,
                        referenceId,
                        performedBy,
                        employeeId,
                        description
                )
        );
    }


    /**
     * Log LOGIN
     */
    @PostMapping("/audit-log/login/{employeeId}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_LOGIN')")
    public ResponseEntity<AuditLog> logLogin(

            @PathVariable String employeeId,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String deviceInfo) {

        return ResponseEntity.ok(
                auditLogService.logLogin(
                        employeeId,
                        ipAddress,
                        deviceInfo
                )
        );
    }


    /**
     * Log LOGOUT
     */
    @PostMapping("/audit-log/logout/{employeeId}")
    @PreAuthorize("hasAuthority('AUDIT_LOG_LOGOUT')")
    public ResponseEntity<AuditLog> logLogout(

            @PathVariable String employeeId,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String deviceInfo) {

        return ResponseEntity.ok(
                auditLogService.logLogout(
                        employeeId,
                        ipAddress,
                        deviceInfo
                )
        );
    }


    // =========================================================
    // ACTIVITY LOG APIs
    // =========================================================

    /**
     * Create Activity Log
     */
    @PostMapping("/activity")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_CREATE')")
    public ResponseEntity<ActivityLog> createActivityLog(

            @RequestParam Long employeeId,
            @RequestParam String activityName,
            @RequestParam String activityModule,
            @RequestParam String activityDescription,
            @RequestParam ActivityStatus activityStatus,
            @RequestParam(required = false) String loginIp,
            @RequestParam(required = false) String browser,
            @RequestParam(required = false) String operatingSystem) {

        return ResponseEntity.ok(
                auditLogService.createActivityLog(
                        employeeId,
                        activityName,
                        activityModule,
                        activityDescription,
                        activityStatus,
                        loginIp,
                        browser,
                        operatingSystem
                )
        );
    }


    /**
     * Generic Activity Log
     */
    @PostMapping("/activity/log")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_CREATE')")
    public ResponseEntity<ActivityLog> logActivity(

            @RequestParam String employeeId,
            @RequestParam String activityName,
            @RequestParam String activityModule,
            @RequestParam String description,
            @RequestParam ActivityStatus status,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) String browser,
            @RequestParam(required = false) String operatingSystem) {

        return ResponseEntity.ok(
                auditLogService.logActivity(
                        employeeId,
                        activityName,
                        activityModule,
                        description,
                        status,
                        ip,
                        browser,
                        operatingSystem
                )
        );
    }


    /**
     * Get All Activities
     */
    @GetMapping("/activity/all")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_VIEW')")
    public ResponseEntity<List<ActivityLog>> getAllActivities() {

        return ResponseEntity.ok(
                auditLogService.getAllActivities()
        );
    }


    /**
     * Get Activity By ID
     */
    @GetMapping("/activity/{id}")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_VIEW')")
    public ResponseEntity<ActivityLog> getActivityById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getActivityById(id)
        );
    }


    /**
     * Get Activities By Employee
     */
    @GetMapping("/activity/employee/{employeeId}")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_VIEW')")
    public ResponseEntity<List<ActivityLog>> getActivitiesByEmployee(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                auditLogService.getActivitiesByEmployee(employeeId)
        );
    }


    /**
     * Get Activities By Module
     */
    @GetMapping("/activity/module/{moduleName}")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_VIEW')")
    public ResponseEntity<List<ActivityLog>> getActivitiesByModule(
            @PathVariable String moduleName) {

        return ResponseEntity.ok(
                auditLogService.getActivitiesByModule(moduleName)
        );
    }


    /**
     * Get Activities By Status
     */
    @GetMapping("/activity/status/{status}")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_VIEW')")
    public ResponseEntity<List<ActivityLog>> getActivitiesByStatus(
            @PathVariable ActivityStatus status) {

        return ResponseEntity.ok(
                auditLogService.getActivitiesByStatus(status)
        );
    }


    /**
     * Delete Activity
     */
    @DeleteMapping("/activity/{id}")
    @PreAuthorize("hasAuthority('ACTIVITY_LOG_DELETE')")
    public ResponseEntity<String> deleteActivity(
            @PathVariable Long id) {

        auditLogService.deleteActivity(id);

        return ResponseEntity.ok(
                "Activity log deleted successfully"
        );
    }


    // =========================================================
    // SYSTEM LOG APIs
    // =========================================================

    /**
     * Create System Log
     */
    @PostMapping("/system-log")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> createSystemLog(

            @RequestParam SystemLogLevel logLevel,
            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam(required = false) String apiEndpoint,
            @RequestParam(required = false) String requestMethod,
            @RequestParam(required = false) Integer responseCode,
            @RequestParam(required = false) String errorMessage,
            @RequestParam(required = false) String stackTrace,
            @RequestParam(required = false) String serverName) {

        return ResponseEntity.ok(
                auditLogService.createSystemLog(
                        logLevel,
                        moduleName,
                        serviceName,
                        apiEndpoint,
                        requestMethod,
                        responseCode,
                        errorMessage,
                        stackTrace,
                        serverName
                )
        );
    }


    /**
     * Log INFO
     */
    @PostMapping("/system-log/info")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logInfo(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String message) {

        return ResponseEntity.ok(
                auditLogService.logInfo(
                        moduleName,
                        serviceName,
                        message
                )
        );
    }


    /**
     * Log WARNING
     */
    @PostMapping("/system-log/warning")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logWarning(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String message) {

        return ResponseEntity.ok(
                auditLogService.logWarning(
                        moduleName,
                        serviceName,
                        message
                )
        );
    }


    /**
     * Log ERROR
     */
    @PostMapping("/system-log/error")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logError(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String message,
            @RequestParam(required = false) String stackTrace) {

        return ResponseEntity.ok(
                auditLogService.logError(
                        moduleName,
                        serviceName,
                        message,
                        stackTrace
                )
        );
    }


    /**
     * Log DEBUG
     */
    @PostMapping("/system-log/debug")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logDebug(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String message) {

        return ResponseEntity.ok(
                auditLogService.logDebug(
                        moduleName,
                        serviceName,
                        message
                )
        );
    }


    /**
     * Log API Request
     */
    @PostMapping("/system-log/api-request")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logApiRequest(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String endpoint,
            @RequestParam String requestMethod) {

        return ResponseEntity.ok(
                auditLogService.logApiRequest(
                        moduleName,
                        serviceName,
                        endpoint,
                        requestMethod
                )
        );
    }


    /**
     * Log API Response
     */
    @PostMapping("/system-log/api-response")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logApiResponse(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String endpoint,
            @RequestParam String requestMethod,
            @RequestParam Integer responseCode) {

        return ResponseEntity.ok(
                auditLogService.logApiResponse(
                        moduleName,
                        serviceName,
                        endpoint,
                        requestMethod,
                        responseCode
                )
        );
    }


    /**
     * Log Exception
     */
    @PostMapping("/system-log/exception")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_CREATE')")
    public ResponseEntity<SystemLog> logException(

            @RequestParam String moduleName,
            @RequestParam String serviceName,
            @RequestParam String errorMessage,
            @RequestParam String stackTrace) {

        return ResponseEntity.ok(
                auditLogService.logException(
                        moduleName,
                        serviceName,
                        errorMessage,
                        stackTrace
                )
        );
    }


    /**
     * Get All System Logs
     */
    @GetMapping("/system-log/all")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<List<SystemLog>> getAllSystemLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllSystemLogs()
        );
    }


    /**
     * Get System Log By ID
     */
    @GetMapping("/system-log/{id}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<SystemLog> getSystemLogById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getSystemLogById(id)
        );
    }


    /**
     * Get System Logs By Level
     */
    @GetMapping("/system-log/level/{level}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<List<SystemLog>> getSystemLogsByLevel(
            @PathVariable SystemLogLevel level) {

        return ResponseEntity.ok(
                auditLogService.getSystemLogsByLevel(level)
        );
    }


    /**
     * Get System Logs By Module
     */
    @GetMapping("/system-log/module/{moduleName}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<List<SystemLog>> getSystemLogsByModule(
            @PathVariable String moduleName) {

        return ResponseEntity.ok(
                auditLogService.getSystemLogsByModule(moduleName)
        );
    }


    /**
     * Get System Logs By Service
     */
    @GetMapping("/system-log/service/{serviceName}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<List<SystemLog>> getSystemLogsByService(
            @PathVariable String serviceName) {

        return ResponseEntity.ok(
                auditLogService.getSystemLogsByService(serviceName)
        );
    }


    /**
     * Get System Logs By Response Code
     */
    @GetMapping("/system-log/response/{responseCode}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_VIEW')")
    public ResponseEntity<List<SystemLog>> getSystemLogsByResponseCode(
            @PathVariable Integer responseCode) {

        return ResponseEntity.ok(
                auditLogService.getSystemLogsByResponseCode(responseCode)
        );
    }


    /**
     * Delete System Log
     */
    @DeleteMapping("/system-log/{id}")
    @PreAuthorize("hasAuthority('SYSTEM_LOG_DELETE')")
    public ResponseEntity<String> deleteSystemLog(
            @PathVariable Long id) {

        auditLogService.deleteSystemLog(id);

        return ResponseEntity.ok(
                "System log deleted successfully"
        );
    }
}
