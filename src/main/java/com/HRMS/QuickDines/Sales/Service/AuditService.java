package com.HRMS.QuickDines.Sales.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Sales.Entity.*;
import com.HRMS.QuickDines.Sales.model.*;
import com.HRMS.QuickDines.Sales.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final SalesAuditReportsRepository auditRepository;
    private final AuditScheduleRepository scheduleRepository;
    private final AuditChecklistItemRepository checklistRepository;
    private final AuditIssueRepository issueRepository;
    private final EmployeeRepository employeeRepository;
    private final RestaurantsRepository restaurantRepository;
    private final BusServicesRepository busServiceRepository;


    // =====================================================
    // AUDIT
    // =====================================================

    @Transactional
    public SalesAuditReports createAudit(SalesAuditReports audit) {

        if (audit.getEmployee() == null) {
            throw new RuntimeException("Employee is required");
        }

        if (audit.getAuditType() == null) {
            throw new RuntimeException("Audit type is required");
        }

        audit.setAuditStatus(AuditStatus.SCHEDULED);
        audit.setApprovalStatus(
                AuditApprovalStatus.PENDING
        );
        audit.setOverdue(false);

        if (audit.getAuditDate() == null) {
            audit.setAuditDate(LocalDate.now());
        }

        return auditRepository.save(audit);
    }

    public List<SalesAuditReports> getAllAudits() {
        return auditRepository.findAll();
    }

    public SalesAuditReports getAudit(Long id) {

        return auditRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Audit not found: " + id
                        )
                );
    }

    @Transactional
    public SalesAuditReports updateAudit(
            Long id,
            SalesAuditReports data) {

        SalesAuditReports audit = getAudit(id);

        audit.setAuditType(data.getAuditType());
        audit.setAuditDate(data.getAuditDate());
        audit.setRemarks(data.getRemarks());
        audit.setWarningCount(data.getWarningCount());

        return auditRepository.save(audit);
    }

    @Transactional
    public void deleteAudit(Long id) {

        SalesAuditReports audit = getAudit(id);

        auditRepository.delete(audit);
    }


    // =====================================================
    // EMPLOYEE AUDITS
    // =====================================================

    public List<SalesAuditReports> getEmployeeAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeId(employeeId);
    }

    public List<SalesAuditReports> getEmployeePendingAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeIdAndAuditStatus(
                        employeeId,
                        AuditStatus.SCHEDULED
                );
    }

    public List<SalesAuditReports> getEmployeeSubmittedAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeIdAndAuditStatus(
                        employeeId,
                        AuditStatus.SUBMITTED
                );
    }

    public List<SalesAuditReports> getEmployeeCompletedAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeIdAndAuditStatus(
                        employeeId,
                        AuditStatus.COMPLETED
                );
    }

    public List<SalesAuditReports> getEmployeeOverdueAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeIdAndOverdue(
                        employeeId,
                        true
                );
    }

    public List<SalesAuditReports> getEmployeeAuditsByDate(
            String employeeId,
            LocalDate date) {

        return auditRepository
                .findByEmployee_EmployeeIdAndAuditDate(
                        employeeId,
                        date
                );
    }


    // =====================================================
    // AUDIT TYPE
    // =====================================================

    public List<SalesAuditReports> getAuditsByType(
            AuditType auditType) {

        return auditRepository
                .findByAuditType(auditType);
    }

    public List<SalesAuditReports> getRestaurantAudits(
            Long restaurantId) {

        return auditRepository
                .findByRestaurant_Id(restaurantId);
    }

    public List<SalesAuditReports> getBusAudits(
            Long busId) {

        return auditRepository
                .findByBusService_Id(busId);
    }


    // =====================================================
    // STATUS
    // =====================================================

    public List<SalesAuditReports> getAuditsByStatus(
            AuditStatus status) {

        return auditRepository
                .findByAuditStatus(status);
    }


    // =====================================================
    // WORKFLOW
    // =====================================================

    @Transactional
    public SalesAuditReports startAudit(Long id) {

        SalesAuditReports audit = getAudit(id);

        if (audit.getAuditStatus()
                != AuditStatus.SCHEDULED) {

            throw new RuntimeException(
                    "Only scheduled audits can be started"
            );
        }

        audit.setAuditStatus(
                AuditStatus.IN_PROGRESS
        );

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports submitAudit(Long id) {

        SalesAuditReports audit = getAudit(id);

        if (audit.getAuditStatus()
                != AuditStatus.IN_PROGRESS) {

            throw new RuntimeException(
                    "Audit must be in progress before submission"
            );
        }

        audit.setAuditStatus(
                AuditStatus.SUBMITTED
        );

        audit.setApprovalStatus(
                AuditApprovalStatus.PENDING
        );

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports approveAudit(
            Long id,
            String approverId,
            String remarks) {

        SalesAuditReports audit = getAudit(id);

        Employee approver =
                getEmployee(approverId);

        audit.setApprovalStatus(
                AuditApprovalStatus.APPROVED
        );

        audit.setApprovedBy(approver);
        audit.setApprovedAt(
                LocalDateTime.now()
        );
        audit.setApprovalRemarks(remarks);

        audit.setAuditStatus(
                AuditStatus.COMPLETED
        );

        audit.setOverdue(false);

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports rejectAudit(
            Long id,
            String approverId,
            String remarks) {

        SalesAuditReports audit = getAudit(id);

        Employee approver =
                getEmployee(approverId);

        audit.setApprovalStatus(
                AuditApprovalStatus.REJECTED
        );

        audit.setApprovedBy(approver);
        audit.setApprovedAt(
                LocalDateTime.now()
        );
        audit.setApprovalRemarks(remarks);

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports returnAudit(
            Long id,
            String remarks) {

        SalesAuditReports audit = getAudit(id);

        audit.setApprovalStatus(
                AuditApprovalStatus.RETURNED
        );

        audit.setApprovalRemarks(remarks);

        audit.setAuditStatus(
                AuditStatus.IN_PROGRESS
        );

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports cancelAudit(Long id) {

        SalesAuditReports audit = getAudit(id);

        audit.setAuditStatus(
                AuditStatus.CANCELLED
        );

        return auditRepository.save(audit);
    }

    @Transactional
    public SalesAuditReports completeAudit(Long id) {

        SalesAuditReports audit = getAudit(id);

        audit.setAuditStatus(
                AuditStatus.COMPLETED
        );

        return auditRepository.save(audit);
    }


    // =====================================================
    // APPROVAL
    // =====================================================

    public List<SalesAuditReports> getAuditsByApprovalStatus(
            AuditApprovalStatus status) {

        return auditRepository
                .findByApprovalStatus(status);
    }


    // =====================================================
    // SCHEDULE
    // =====================================================

    @Transactional
    public AuditSchedule createSchedule(
            AuditSchedule schedule) {

        if (schedule.getFrequencyDays() == null ||
                schedule.getFrequencyDays() <= 0) {

            throw new RuntimeException(
                    "Frequency days must be greater than zero"
            );
        }

        if (schedule.getNextAuditDate() == null) {

            schedule.setNextAuditDate(
                    LocalDate.now()
                            .plusDays(
                                    schedule.getFrequencyDays()
                            )
            );
        }

        if (schedule.getScheduleType() == null) {
            schedule.setScheduleType("MANUAL");
        }

        schedule.setActive(true);

        return scheduleRepository.save(schedule);
    }

    public List<AuditSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public AuditSchedule getSchedule(Long id) {

        return scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Schedule not found: " + id
                        )
                );
    }

    @Transactional
    public AuditSchedule updateSchedule(
            Long id,
            AuditSchedule data) {

        AuditSchedule schedule = getSchedule(id);

        schedule.setFrequencyDays(
                data.getFrequencyDays()
        );

        schedule.setNextAuditDate(
                data.getNextAuditDate()
        );

        schedule.setScheduleType(
                data.getScheduleType()
        );

        schedule.setActive(
                data.getActive()
        );

        schedule.setRemarks(
                data.getRemarks()
        );

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {

        scheduleRepository.delete(
                getSchedule(id)
        );
    }

    public List<AuditSchedule> getEmployeeSchedules(
            String employeeId) {

        return scheduleRepository
                .findByEmployee_EmployeeId(employeeId);
    }

    public List<AuditSchedule> getActiveEmployeeSchedules(
            String employeeId) {

        return scheduleRepository
                .findByEmployee_EmployeeIdAndActive(
                        employeeId,
                        true
                );
    }

    public List<AuditSchedule> getInactiveEmployeeSchedules(
            String employeeId) {

        return scheduleRepository
                .findByEmployee_EmployeeIdAndActive(
                        employeeId,
                        false
                );
    }

    public List<AuditSchedule> getUpcomingEmployeeSchedules(
            String employeeId) {

        return scheduleRepository
                .findByEmployee_EmployeeIdAndNextAuditDateGreaterThanEqual(
                        employeeId,
                        LocalDate.now()
                );
    }

    public List<AuditSchedule> getRestaurantSchedules(
            Long restaurantId) {

        return scheduleRepository
                .findByRestaurant_Id(restaurantId);
    }

    public List<AuditSchedule> getBusSchedules(
            Long busId) {

        return scheduleRepository
                .findByBusService_Id(busId);
    }

    public List<AuditSchedule> getSchedulesByTargetType(
            AuditType type) {

        return scheduleRepository
                .findByTargetType(type);
    }

    public List<AuditSchedule> getSchedulesByType(
            String type) {

        return scheduleRepository
                .findByScheduleType(type);
    }

    public List<AuditSchedule> getSchedulesByFrequency(
            Integer days) {

        return scheduleRepository
                .findByFrequencyDays(days);
    }

    public List<AuditSchedule> getUpcomingSchedules() {

        return scheduleRepository
                .findByNextAuditDateGreaterThanEqual(
                        LocalDate.now()
                );
    }

    public List<AuditSchedule> getTodaySchedules() {

        return scheduleRepository
                .findByNextAuditDate(LocalDate.now());
    }

    public List<AuditSchedule> getTomorrowSchedules() {

        return scheduleRepository
                .findByNextAuditDate(
                        LocalDate.now().plusDays(1)
                );
    }

    public List<AuditSchedule> getOverdueSchedules() {

        return scheduleRepository
                .findByNextAuditDateBeforeAndActive(
                        LocalDate.now(),
                        true
                );
    }


    // =====================================================
    // CHECKLIST
    // =====================================================

    @Transactional
    public AuditChecklistItem createChecklist(
            Long auditId,
            AuditChecklistItem data) {

        SalesAuditReports audit = getAudit(auditId);

        data.setAuditReport(audit);

        return checklistRepository.save(data);
    }

    public List<AuditChecklistItem> getChecklist(
            Long auditId) {

        return checklistRepository
                .findByAuditReport_Id(auditId);
    }

    public AuditChecklistItem getChecklistById(
            Long id) {

        return checklistRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Checklist not found"
                        )
                );
    }

    @Transactional
    public AuditChecklistItem updateChecklist(
            Long id,
            AuditChecklistItem data) {

        AuditChecklistItem item =
                getChecklistById(id);

        item.setChecklistName(
                data.getChecklistName()
        );

        item.setStatus(data.getStatus());
        item.setRemarks(data.getRemarks());
        item.setPhotoUrl(data.getPhotoUrl());

        return checklistRepository.save(item);
    }

    @Transactional
    public void deleteChecklist(Long id) {

        checklistRepository.delete(
                getChecklistById(id)
        );
    }

    @Transactional
    public AuditChecklistItem updateChecklistStatus(
            Long id,
            String status) {

        AuditChecklistItem item =
                getChecklistById(id);

        item.setStatus(status);

        return checklistRepository.save(item);
    }

    @Transactional
    public AuditChecklistItem updateChecklistPhoto(
            Long id,
            String photoUrl) {

        AuditChecklistItem item =
                getChecklistById(id);

        item.setPhotoUrl(photoUrl);

        return checklistRepository.save(item);
    }


    // =====================================================
    // ISSUES
    // =====================================================

    @Transactional
    public AuditIssue createIssue(
            Long auditId,
            AuditIssue data) {

        SalesAuditReports audit =
                getAudit(auditId);

        data.setAuditReport(audit);

        if (data.getStatus() == null) {
            data.setStatus("OPEN");
        }

        return issueRepository.save(data);
    }

    public List<AuditIssue> getAuditIssues(
            Long auditId) {

        return issueRepository
                .findByAuditReport_Id(auditId);
    }

    public AuditIssue getIssue(Long id) {

        return issueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Issue not found"
                        )
                );
    }

    @Transactional
    public AuditIssue updateIssue(
            Long id,
            AuditIssue data) {

        AuditIssue issue = getIssue(id);

        issue.setIssueTitle(data.getIssueTitle());
        issue.setDescription(data.getDescription());
        issue.setSeverity(data.getSeverity());
        issue.setDueDate(data.getDueDate());
        issue.setEvidenceUrl(data.getEvidenceUrl());

        return issueRepository.save(issue);
    }

    @Transactional
    public void deleteIssue(Long id) {

        issueRepository.delete(
                getIssue(id)
        );
    }

    @Transactional
    public AuditIssue resolveIssue(
            Long id,
            String remarks) {

        AuditIssue issue = getIssue(id);

        issue.setStatus("RESOLVED");
        issue.setResolutionRemarks(remarks);
        issue.setResolvedDate(LocalDate.now());

        return issueRepository.save(issue);
    }

    @Transactional
    public AuditIssue reopenIssue(Long id) {

        AuditIssue issue = getIssue(id);

        issue.setStatus("OPEN");
        issue.setResolvedDate(null);

        return issueRepository.save(issue);
    }

    public List<AuditIssue> getIssuesByStatus(
            String status) {

        return issueRepository
                .findByStatus(status);
    }

    public List<AuditIssue> getIssuesBySeverity(
            String severity) {

        return issueRepository
                .findBySeverity(severity);
    }

    public List<AuditIssue> getOpenIssues() {

        return issueRepository
                .findByStatus("OPEN");
    }

    public List<AuditIssue> getResolvedIssues() {

        return issueRepository
                .findByStatus("RESOLVED");
    }

    public List<AuditIssue> getOverdueIssues() {

        return issueRepository
                .findByDueDateBeforeAndStatusNot(
                        LocalDate.now(),
                        "RESOLVED"
                );
    }

    public List<AuditIssue> getEmployeeIssues(
            String employeeId) {

        return issueRepository
                .findByAuditReport_Employee_EmployeeId(
                        employeeId
                );
    }

    public List<AuditIssue> getRestaurantIssues(
            Long restaurantId) {

        return issueRepository
                .findByAuditReport_Restaurant_Id(
                        restaurantId
                );
    }

    public List<AuditIssue> getBusIssues(
            Long busId) {

        return issueRepository
                .findByAuditReport_BusService_Id(
                        busId
                );
    }


    // =====================================================
    // HELPERS
    // =====================================================

    private Employee getEmployee(
            String employeeId) {

        return employeeRepository
                .findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found: "
                                        + employeeId
                        )
                );
    }


    // =====================================================
    // DASHBOARD / NOTIFICATION PLACEHOLDERS
    // =====================================================

    public Object getRestaurantsForAudit() {
        return restaurantRepository.findAll();
    }

    public Object getLatestRestaurantAudit(
            Long restaurantId) {
        return auditRepository
                .findTopByRestaurant_IdOrderByAuditDateDesc(
                        restaurantId
                );
    }

    public Object getRestaurantAuditHistory(
            Long restaurantId) {
        return auditRepository
                .findByRestaurant_IdOrderByAuditDateDesc(
                        restaurantId
                );
    }

    public Object getRestaurantOverdueAudits(
            Long restaurantId) {
        return auditRepository
                .findByRestaurant_IdAndOverdue(
                        restaurantId,
                        true
                );
    }

    public Object getBusesForAudit() {
        return busServiceRepository.findAll();
    }

    public Object getLatestBusAudit(Long busId) {
        return auditRepository
                .findTopByBusService_IdOrderByAuditDateDesc(
                        busId
                );
    }

    public Object getBusAuditHistory(Long busId) {
        return auditRepository
                .findByBusService_IdOrderByAuditDateDesc(
                        busId
                );
    }

    public Object getBusOverdueAudits(Long busId) {
        return auditRepository
                .findByBusService_IdAndOverdue(
                        busId,
                        true
                );
    }

    public Object employeeDashboard(
            String employeeId) {

        return getEmployeeAudits(employeeId);
    }

    public Object managementDashboard() {

        return getAllAudits();
    }

    public String sendAuditNotification(Long auditId) {

        SalesAuditReports audit = getAudit(auditId);

        return "Audit notification sent for audit "
                + audit.getId();
    }

    public Object getEmployeeNotifications(
            String employeeId) {

        return getEmployeeAudits(employeeId);
    }

    public Object getUpcomingNotifications(
            String employeeId) {

        return getEmployeeUpcomingAudits(employeeId);
    }

    public Object getOverdueNotifications(
            String employeeId) {

        return getEmployeeOverdueAudits(employeeId);
    }

    private List<SalesAuditReports> getEmployeeUpcomingAudits(
            String employeeId) {

        return auditRepository
                .findByEmployee_EmployeeIdAndAuditDateGreaterThanEqual(
                        employeeId,
                        LocalDate.now()
                );
    }
}