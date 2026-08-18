package com.HRMS.QuickDines.Sales.Controller;

import com.HRMS.QuickDines.Sales.Entity.*;
import com.HRMS.QuickDines.Sales.Service.AuditService;
import com.HRMS.QuickDines.Sales.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService service;

    // =====================================================
    // AUDIT
    // =====================================================

    @PostMapping("/audits")
    public ResponseEntity<?> createAudit(
            @RequestBody AuditReports audit) {

        return ResponseEntity.ok(service.createAudit(audit));
    }

    @GetMapping("/audits")
    public ResponseEntity<?> getAllAudits() {
        return ResponseEntity.ok(service.getAllAudits());
    }

    @GetMapping("/audits/{id}")
    public ResponseEntity<?> getAudit(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getAudit(id));
    }

    @PutMapping("/audits/{id}")
    public ResponseEntity<?> updateAudit(
            @PathVariable Long id,
            @RequestBody AuditReports audit) {

        return ResponseEntity.ok(
                service.updateAudit(id, audit)
        );
    }

    @DeleteMapping("/audits/{id}")
    public ResponseEntity<?> deleteAudit(
            @PathVariable Long id) {

        service.deleteAudit(id);
        return ResponseEntity.ok("Audit deleted successfully");
    }


    // =====================================================
    // AUDIT - EMPLOYEE
    // =====================================================

    @GetMapping("/audits/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeAudits(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeAudits(employeeId)
        );
    }

    @GetMapping("/audits/employee/{employeeId}/pending")
    public ResponseEntity<?> getEmployeePendingAudits(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeePendingAudits(employeeId)
        );
    }

    @GetMapping("/audits/employee/{employeeId}/submitted")
    public ResponseEntity<?> getEmployeeSubmittedAudits(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeSubmittedAudits(employeeId)
        );
    }

    @GetMapping("/audits/employee/{employeeId}/completed")
    public ResponseEntity<?> getEmployeeCompletedAudits(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeCompletedAudits(employeeId)
        );
    }

    @GetMapping("/audits/employee/{employeeId}/overdue")
    public ResponseEntity<?> getEmployeeOverdueAudits(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeOverdueAudits(employeeId)
        );
    }

    @GetMapping("/audits/employee/{employeeId}/date/{date}")
    public ResponseEntity<?> getEmployeeAuditsByDate(
            @PathVariable String employeeId,
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                service.getEmployeeAuditsByDate(
                        employeeId,
                        date
                )
        );
    }


    // =====================================================
    // AUDIT - TYPE / TARGET
    // =====================================================

    @GetMapping("/audits/type/{auditType}")
    public ResponseEntity<?> getAuditsByType(
            @PathVariable AuditType auditType) {

        return ResponseEntity.ok(
                service.getAuditsByType(auditType)
        );
    }

    @GetMapping("/audits/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantAudits(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getRestaurantAudits(restaurantId)
        );
    }

    @GetMapping("/audits/bus/{busId}")
    public ResponseEntity<?> getBusAudits(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getBusAudits(busId)
        );
    }


    // =====================================================
    // AUDIT - STATUS
    // =====================================================

    @GetMapping("/audits/status/{status}")
    public ResponseEntity<?> getAuditsByStatus(
            @PathVariable AuditStatus status) {

        return ResponseEntity.ok(
                service.getAuditsByStatus(status)
        );
    }

    @GetMapping("/audits/scheduled")
    public ResponseEntity<?> getScheduledAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.SCHEDULED)
        );
    }

    @GetMapping("/audits/in-progress")
    public ResponseEntity<?> getInProgressAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.IN_PROGRESS)
        );
    }

    @GetMapping("/audits/submitted")
    public ResponseEntity<?> getSubmittedAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.SUBMITTED)
        );
    }

    @GetMapping("/audits/completed")
    public ResponseEntity<?> getCompletedAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.COMPLETED)
        );
    }

    @GetMapping("/audits/overdue")
    public ResponseEntity<?> getOverdueAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.OVERDUE)
        );
    }

    @GetMapping("/audits/cancelled")
    public ResponseEntity<?> getCancelledAudits() {
        return ResponseEntity.ok(
                service.getAuditsByStatus(AuditStatus.CANCELLED)
        );
    }


    // =====================================================
    // AUDIT WORKFLOW
    // =====================================================

    @PutMapping("/audits/{id}/start")
    public ResponseEntity<?> startAudit(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.startAudit(id)
        );
    }

    @PutMapping("/audits/{id}/submit")
    public ResponseEntity<?> submitAudit(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.submitAudit(id)
        );
    }

    @PutMapping("/audits/{id}/approve")
    public ResponseEntity<?> approveAudit(
            @PathVariable Long id,
            @RequestParam String approverId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                service.approveAudit(
                        id,
                        approverId,
                        remarks
                )
        );
    }

    @PutMapping("/audits/{id}/reject")
    public ResponseEntity<?> rejectAudit(
            @PathVariable Long id,
            @RequestParam String approverId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                service.rejectAudit(
                        id,
                        approverId,
                        remarks
                )
        );
    }

    @PutMapping("/audits/{id}/return")
    public ResponseEntity<?> returnAudit(
            @PathVariable Long id,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                service.returnAudit(id, remarks)
        );
    }

    @PutMapping("/audits/{id}/cancel")
    public ResponseEntity<?> cancelAudit(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.cancelAudit(id)
        );
    }

    @PutMapping("/audits/{id}/complete")
    public ResponseEntity<?> completeAudit(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.completeAudit(id)
        );
    }


    // =====================================================
    // APPROVAL
    // =====================================================

    @GetMapping("/audits/approval/{approvalStatus}")
    public ResponseEntity<?> getAuditsByApprovalStatus(
            @PathVariable AuditApprovalStatus approvalStatus) {

        return ResponseEntity.ok(
                service.getAuditsByApprovalStatus(
                        approvalStatus
                )
        );
    }

    @GetMapping("/audits/pending-approval")
    public ResponseEntity<?> getPendingApproval() {

        return ResponseEntity.ok(
                service.getAuditsByApprovalStatus(
                        AuditApprovalStatus.PENDING
                )
        );
    }

    @GetMapping("/audits/approved")
    public ResponseEntity<?> getApprovedAudits() {

        return ResponseEntity.ok(
                service.getAuditsByApprovalStatus(
                        AuditApprovalStatus.APPROVED
                )
        );
    }

    @GetMapping("/audits/rejected")
    public ResponseEntity<?> getRejectedAudits() {

        return ResponseEntity.ok(
                service.getAuditsByApprovalStatus(
                        AuditApprovalStatus.REJECTED
                )
        );
    }

    @GetMapping("/audits/returned")
    public ResponseEntity<?> getReturnedAudits() {

        return ResponseEntity.ok(
                service.getAuditsByApprovalStatus(
                        AuditApprovalStatus.RETURNED
                )
        );
    }


    // =====================================================
    // SCHEDULE
    // =====================================================

    @PostMapping("/audit-schedules")
    public ResponseEntity<?> createSchedule(
            @RequestBody AuditSchedule schedule) {

        return ResponseEntity.ok(
                service.createSchedule(schedule)
        );
    }

    @GetMapping("/audit-schedules")
    public ResponseEntity<?> getAllSchedules() {

        return ResponseEntity.ok(
                service.getAllSchedules()
        );
    }

    @GetMapping("/audit-schedules/{id}")
    public ResponseEntity<?> getSchedule(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSchedule(id)
        );
    }

    @PutMapping("/audit-schedules/{id}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable Long id,
            @RequestBody AuditSchedule schedule) {

        return ResponseEntity.ok(
                service.updateSchedule(id, schedule)
        );
    }

    @DeleteMapping("/audit-schedules/{id}")
    public ResponseEntity<?> deleteSchedule(
            @PathVariable Long id) {

        service.deleteSchedule(id);
        return ResponseEntity.ok(
                "Schedule deleted successfully"
        );
    }


    // =====================================================
    // SCHEDULE - EMPLOYEE
    // =====================================================

    @GetMapping("/audit-schedules/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeSchedules(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeSchedules(employeeId)
        );
    }

    @GetMapping("/audit-schedules/employee/{employeeId}/active")
    public ResponseEntity<?> getActiveEmployeeSchedules(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getActiveEmployeeSchedules(employeeId)
        );
    }

    @GetMapping("/audit-schedules/employee/{employeeId}/inactive")
    public ResponseEntity<?> getInactiveEmployeeSchedules(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getInactiveEmployeeSchedules(employeeId)
        );
    }

    @GetMapping("/audit-schedules/employee/{employeeId}/upcoming")
    public ResponseEntity<?> getUpcomingEmployeeSchedules(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getUpcomingEmployeeSchedules(employeeId)
        );
    }


    // =====================================================
    // SCHEDULE - TARGET
    // =====================================================

    @GetMapping("/audit-schedules/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantSchedules(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getRestaurantSchedules(restaurantId)
        );
    }

    @GetMapping("/audit-schedules/bus/{busId}")
    public ResponseEntity<?> getBusSchedules(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getBusSchedules(busId)
        );
    }

    @GetMapping("/audit-schedules/target-type/{auditType}")
    public ResponseEntity<?> getSchedulesByTargetType(
            @PathVariable AuditType auditType) {

        return ResponseEntity.ok(
                service.getSchedulesByTargetType(auditType)
        );
    }


    // =====================================================
    // SCHEDULE - TYPE
    // =====================================================

    @GetMapping("/audit-schedules/automatic")
    public ResponseEntity<?> getAutomaticSchedules() {

        return ResponseEntity.ok(
                service.getSchedulesByType("AUTOMATIC")
        );
    }

    @GetMapping("/audit-schedules/manual")
    public ResponseEntity<?> getManualSchedules() {

        return ResponseEntity.ok(
                service.getSchedulesByType("MANUAL")
        );
    }

    @GetMapping("/audit-schedules/schedule-type/{scheduleType}")
    public ResponseEntity<?> getSchedulesByScheduleType(
            @PathVariable String scheduleType) {

        return ResponseEntity.ok(
                service.getSchedulesByType(scheduleType)
        );
    }

    @GetMapping("/audit-schedules/frequency/{days}")
    public ResponseEntity<?> getSchedulesByFrequency(
            @PathVariable Integer days) {

        return ResponseEntity.ok(
                service.getSchedulesByFrequency(days)
        );
    }


    // =====================================================
    // SCHEDULE - DATES
    // =====================================================

    @GetMapping("/audit-schedules/upcoming")
    public ResponseEntity<?> getUpcomingSchedules() {

        return ResponseEntity.ok(
                service.getUpcomingSchedules()
        );
    }

    @GetMapping("/audit-schedules/today")
    public ResponseEntity<?> getTodaySchedules() {

        return ResponseEntity.ok(
                service.getTodaySchedules()
        );
    }

    @GetMapping("/audit-schedules/tomorrow")
    public ResponseEntity<?> getTomorrowSchedules() {

        return ResponseEntity.ok(
                service.getTomorrowSchedules()
        );
    }

    @GetMapping("/audit-schedules/overdue")
    public ResponseEntity<?> getOverdueSchedules() {

        return ResponseEntity.ok(
                service.getOverdueSchedules()
        );
    }


    // =====================================================
    // CHECKLIST
    // =====================================================

    @PostMapping("/audits/{auditId}/checklist")
    public ResponseEntity<?> createChecklist(
            @PathVariable Long auditId,
            @RequestBody AuditChecklistItem checklist) {

        return ResponseEntity.ok(
                service.createChecklist(
                        auditId,
                        checklist
                )
        );
    }

    @GetMapping("/audits/{auditId}/checklist")
    public ResponseEntity<?> getChecklist(
            @PathVariable Long auditId) {

        return ResponseEntity.ok(
                service.getChecklist(auditId)
        );
    }

    @GetMapping("/audit-checklist/{id}")
    public ResponseEntity<?> getChecklistById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getChecklistById(id)
        );
    }

    @PutMapping("/audit-checklist/{id}")
    public ResponseEntity<?> updateChecklist(
            @PathVariable Long id,
            @RequestBody AuditChecklistItem checklist) {

        return ResponseEntity.ok(
                service.updateChecklist(
                        id,
                        checklist
                )
        );
    }

    @DeleteMapping("/audit-checklist/{id}")
    public ResponseEntity<?> deleteChecklist(
            @PathVariable Long id) {

        service.deleteChecklist(id);

        return ResponseEntity.ok(
                "Checklist deleted successfully"
        );
    }

    @PutMapping("/audit-checklist/{id}/status")
    public ResponseEntity<?> updateChecklistStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                service.updateChecklistStatus(
                        id,
                        status
                )
        );
    }

    @PutMapping("/audit-checklist/{id}/photo")
    public ResponseEntity<?> updateChecklistPhoto(
            @PathVariable Long id,
            @RequestParam String photoUrl) {

        return ResponseEntity.ok(
                service.updateChecklistPhoto(
                        id,
                        photoUrl
                )
        );
    }


    // =====================================================
    // ISSUES
    // =====================================================

    @PostMapping("/audits/{auditId}/issues")
    public ResponseEntity<?> createIssue(
            @PathVariable Long auditId,
            @RequestBody AuditIssue issue) {

        return ResponseEntity.ok(
                service.createIssue(
                        auditId,
                        issue
                )
        );
    }

    @GetMapping("/audits/{auditId}/issues")
    public ResponseEntity<?> getAuditIssues(
            @PathVariable Long auditId) {

        return ResponseEntity.ok(
                service.getAuditIssues(auditId)
        );
    }

    @GetMapping("/audit-issues/{id}")
    public ResponseEntity<?> getIssue(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getIssue(id)
        );
    }

    @PutMapping("/audit-issues/{id}")
    public ResponseEntity<?> updateIssue(
            @PathVariable Long id,
            @RequestBody AuditIssue issue) {

        return ResponseEntity.ok(
                service.updateIssue(id, issue)
        );
    }

    @DeleteMapping("/audit-issues/{id}")
    public ResponseEntity<?> deleteIssue(
            @PathVariable Long id) {

        service.deleteIssue(id);

        return ResponseEntity.ok(
                "Issue deleted successfully"
        );
    }

    @PutMapping("/audit-issues/{id}/resolve")
    public ResponseEntity<?> resolveIssue(
            @PathVariable Long id,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                service.resolveIssue(id, remarks)
        );
    }

    @PutMapping("/audit-issues/{id}/reopen")
    public ResponseEntity<?> reopenIssue(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.reopenIssue(id)
        );
    }

    @GetMapping("/audit-issues/status/{status}")
    public ResponseEntity<?> getIssuesByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                service.getIssuesByStatus(status)
        );
    }

    @GetMapping("/audit-issues/severity/{severity}")
    public ResponseEntity<?> getIssuesBySeverity(
            @PathVariable String severity) {

        return ResponseEntity.ok(
                service.getIssuesBySeverity(severity)
        );
    }

    @GetMapping("/audit-issues/open")
    public ResponseEntity<?> getOpenIssues() {

        return ResponseEntity.ok(
                service.getOpenIssues()
        );
    }

    @GetMapping("/audit-issues/resolved")
    public ResponseEntity<?> getResolvedIssues() {

        return ResponseEntity.ok(
                service.getResolvedIssues()
        );
    }

    @GetMapping("/audit-issues/overdue")
    public ResponseEntity<?> getOverdueIssues() {

        return ResponseEntity.ok(
                service.getOverdueIssues()
        );
    }

    @GetMapping("/audit-issues/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeIssues(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeIssues(employeeId)
        );
    }

    @GetMapping("/audit-issues/restaurant/{restaurantId}")
    public ResponseEntity<?> getRestaurantIssues(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getRestaurantIssues(restaurantId)
        );
    }

    @GetMapping("/audit-issues/bus/{busId}")
    public ResponseEntity<?> getBusIssues(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getBusIssues(busId)
        );
    }


    // =====================================================
    // RESTAURANT
    // =====================================================

    @GetMapping("/audits/restaurants")
    public ResponseEntity<?> getRestaurantsForAudit() {
        return ResponseEntity.ok(
                service.getRestaurantsForAudit()
        );
    }

    @GetMapping("/audits/restaurant/{restaurantId}/latest")
    public ResponseEntity<?> getLatestRestaurantAudit(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getLatestRestaurantAudit(
                        restaurantId
                )
        );
    }

    @GetMapping("/audits/restaurant/{restaurantId}/history")
    public ResponseEntity<?> getRestaurantAuditHistory(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getRestaurantAuditHistory(
                        restaurantId
                )
        );
    }

    @GetMapping("/audits/restaurant/{restaurantId}/overdue")
    public ResponseEntity<?> getRestaurantOverdueAudits(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                service.getRestaurantOverdueAudits(
                        restaurantId
                )
        );
    }


    // =====================================================
    // BUS
    // =====================================================

    @GetMapping("/audits/buses")
    public ResponseEntity<?> getBusesForAudit() {

        return ResponseEntity.ok(
                service.getBusesForAudit()
        );
    }

    @GetMapping("/audits/bus/{busId}/latest")
    public ResponseEntity<?> getLatestBusAudit(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getLatestBusAudit(busId)
        );
    }

    @GetMapping("/audits/bus/{busId}/history")
    public ResponseEntity<?> getBusAuditHistory(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getBusAuditHistory(busId)
        );
    }

    @GetMapping("/audits/bus/{busId}/overdue")
    public ResponseEntity<?> getBusOverdueAudits(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                service.getBusOverdueAudits(busId)
        );
    }


    // =====================================================
    // DASHBOARD
    // =====================================================

    @GetMapping("/audits/dashboard/employee/{employeeId}")
    public ResponseEntity<?> employeeDashboard(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.employeeDashboard(employeeId)
        );
    }

    @GetMapping("/audits/dashboard/management")
    public ResponseEntity<?> managementDashboard() {

        return ResponseEntity.ok(
                service.managementDashboard()
        );
    }


    // =====================================================
    // NOTIFICATIONS
    // =====================================================

    @PostMapping("/audit-notifications/{auditId}/send")
    public ResponseEntity<?> sendAuditNotification(
            @PathVariable Long auditId) {

        return ResponseEntity.ok(
                service.sendAuditNotification(auditId)
        );
    }

    @GetMapping("/audit-notifications/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeNotifications(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeNotifications(employeeId)
        );
    }

    @GetMapping("/audit-notifications/employee/{employeeId}/upcoming")
    public ResponseEntity<?> getUpcomingNotifications(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getUpcomingNotifications(employeeId)
        );
    }

    @GetMapping("/audit-notifications/employee/{employeeId}/overdue")
    public ResponseEntity<?> getOverdueNotifications(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getOverdueNotifications(employeeId)
        );
    }
}