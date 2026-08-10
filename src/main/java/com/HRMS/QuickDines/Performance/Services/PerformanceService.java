package com.HRMS.QuickDines.Performance.Services;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Performance.model.*;
import com.HRMS.QuickDines.Performance.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerformanceService {


    private final EmployeeRepository employeeRepository;
    private final PerformanceReportsRepository performanceReportsRepository;
    private final AuditReportsRepository auditReportsRepository;
    private final EmployeeRankingsRepository employeeRankingsRepository;
    private final GoalRepository goalRepository;
    private final KpiRepository kpiRepository;
    private final AppraisalRepository appraisalRepository;
    private final SelfReviewRepository selfReviewRepository;
    private final ManagerReviewRepository managerReviewRepository;
    private final PromotionRecommendationRepository promotionRecommendationRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }




//=================================
// PERFORMANCE REPORTS
//=================================

// CREATE

    public String createPerformanceReport(
            String employeeId,
            PerformanceReports report) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        report.setEmployee(employee);

        performanceReportsRepository.save(report);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PERFORMANCE_REPORT",
                String.valueOf(report.getId()),
                performedBy,
                employeeId,
                "Performance report created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_PERFORMANCE_REPORT",
                "PERFORMANCE",
                "Performance report created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Performance report created successfully for employee: "
                        + employeeId
        );

        return "Performance Report Created Successfully";
    }



//=================================
// GET ALL
//=================================

    public List<PerformanceReports> getPerformanceReports() {

        return performanceReportsRepository.findAll();
    }



//=================================
// GET BY EMPLOYEE ID
//=================================

    public List<PerformanceReports> getPerformanceReport(
            String employeeId) {

        return performanceReportsRepository
                .findByEmployeeEmployeeId(employeeId);
    }



//=================================
// UPDATE
//=================================

    public String updatePerformanceReport(
            String employeeId,
            PerformanceReports report) {

        List<PerformanceReports> reports =
                performanceReportsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (reports == null || reports.isEmpty()) {
            return "Performance Report Not Found";
        }

        PerformanceReports existingReport =
                reports.get(0);

        // Capture OLD value before update
        String oldValue =
                convertToJson(existingReport);

        existingReport.setAttendancePercentage(
                report.getAttendancePercentage());

        existingReport.setTaskCompletion(
                report.getTaskCompletion());

        existingReport.setTargetAchievement(
                report.getTargetAchievement());

        existingReport.setPerformanceScore(
                report.getPerformanceScore());

        existingReport.setRemarks(
                report.getRemarks());

        performanceReportsRepository.save(existingReport);

        // Capture NEW value after update
        String newValue =
                convertToJson(existingReport);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "PERFORMANCE_REPORT",
                String.valueOf(existingReport.getId()),
                performedBy,
                employeeId,
                "Performance report updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_PERFORMANCE_REPORT",
                "PERFORMANCE",
                "Performance report updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Performance report updated successfully for employee: "
                        + employeeId
        );

        return "Performance Report Updated Successfully";
    }



//=================================
// DELETE
//=================================

    public String deletePerformanceReport(
            String employeeId) {

        List<PerformanceReports> reports =
                performanceReportsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (reports == null || reports.isEmpty()) {
            return "Performance Report Not Found";
        }

        PerformanceReports report =
                reports.get(0);

        // Capture deleted value before deletion
        String deletedValue =
                convertToJson(report);

        String performedBy =
                getLoggedInEmployeeId();

        performanceReportsRepository.delete(report);

        auditLogsService.createAuditLog(
                "PERFORMANCE_REPORT",
                String.valueOf(report.getId()),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Performance report deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_PERFORMANCE_REPORT",
                "PERFORMANCE",
                "Performance report deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Performance report deleted successfully for employee: "
                        + employeeId
        );

        return "Performance Report Deleted Successfully";
    }




//=================================
// AUDIT REPORTS
//=================================

// CREATE

    public String createAuditReport(
            String employeeId,
            AuditReports auditReport) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        auditReport.setEmployee(employee);

        auditReportsRepository.save(auditReport);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "AUDIT_REPORT",
                String.valueOf(auditReport.getId()),
                performedBy,
                employeeId,
                "Audit report created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_AUDIT_REPORT",
                "AUDIT",
                "Audit report created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "AUDIT",
                "AuditService",
                "Audit report created successfully for employee: "
                        + employeeId
        );

        return "Audit Report Created Successfully";
    }



//=================================
// GET ALL
//=================================

    public List<AuditReports> getAuditReports() {

        return auditReportsRepository.findAll();
    }



//=================================
// GET BY EMPLOYEE ID
//=================================

    public List<AuditReports> getAuditReport(
            String employeeId) {

        return auditReportsRepository
                .findByEmployeeEmployeeId(employeeId);
    }



//=================================
// UPDATE
//=================================

    public String updateAuditReport(
            String employeeId,
            AuditReports auditReport) {

        List<AuditReports> reports =
                auditReportsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (reports == null || reports.isEmpty()) {
            return "Audit Report Not Found";
        }

        AuditReports existingAudit =
                reports.get(0);

        // Capture OLD value before update
        String oldValue =
                convertToJson(existingAudit);

        existingAudit.setAuditType(
                auditReport.getAuditType());

        existingAudit.setAuditStatus(
                auditReport.getAuditStatus());

        existingAudit.setWarningCount(
                auditReport.getWarningCount());

        existingAudit.setRemarks(
                auditReport.getRemarks());

        existingAudit.setAuditDate(
                auditReport.getAuditDate());

        auditReportsRepository.save(existingAudit);

        // Capture NEW value after update
        String newValue =
                convertToJson(existingAudit);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "AUDIT_REPORT",
                String.valueOf(existingAudit.getId()),
                performedBy,
                employeeId,
                "Audit report updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_AUDIT_REPORT",
                "AUDIT",
                "Audit report updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "AUDIT",
                "AuditService",
                "Audit report updated successfully for employee: "
                        + employeeId
        );

        return "Audit Report Updated Successfully";
    }



//=================================
// DELETE
//=================================

    public String deleteAuditReport(
            String employeeId) {

        List<AuditReports> reports =
                auditReportsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (reports == null || reports.isEmpty()) {
            return "Audit Report Not Found";
        }

        AuditReports auditReport =
                reports.get(0);

        // Capture deleted value before deletion
        String deletedValue =
                convertToJson(auditReport);

        String performedBy =
                getLoggedInEmployeeId();

        auditReportsRepository.delete(auditReport);

        auditLogsService.createAuditLog(
                "AUDIT_REPORT",
                String.valueOf(auditReport.getId()),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Audit report deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_AUDIT_REPORT",
                "AUDIT",
                "Audit report deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "AUDIT",
                "AuditService",
                "Audit report deleted successfully for employee: "
                        + employeeId
        );

        return "Audit Report Deleted Successfully";
    }



//=================================
// EMPLOYEE RANKINGS
//=================================

// CREATE

    public String createRanking(
            String employeeId,
            EmployeeRankings employeeRanking) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        employeeRanking.setEmployee(employee);

        employeeRankingsRepository.save(employeeRanking);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_RANKING",
                String.valueOf(employeeRanking.getId()),
                performedBy,
                employeeId,
                "Employee ranking created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_EMPLOYEE_RANKING",
                "PERFORMANCE",
                "Employee ranking created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Employee ranking created successfully for employee: "
                        + employeeId
        );

        return "Employee Ranking Created Successfully";
    }



//=================================
// GET ALL
//=================================

    public List<EmployeeRankings> getRankings() {

        return employeeRankingsRepository.findAll();
    }



//=================================
// GET BY EMPLOYEE ID
//=================================

    public EmployeeRankings getRanking(
            String employeeId) {

        return employeeRankingsRepository
                .findByEmployeeEmployeeId(employeeId);
    }



//=================================
// UPDATE
//=================================

    public String updateRanking(
            String employeeId,
            EmployeeRankings employeeRanking) {

        EmployeeRankings existingRanking =
                employeeRankingsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (existingRanking == null) {
            return "Employee Ranking Not Found";
        }

        // Capture OLD value before update
        String oldValue =
                convertToJson(existingRanking);

        existingRanking.setRankPosition(
                employeeRanking.getRankPosition());

        existingRanking.setDepartmentRank(
                employeeRanking.getDepartmentRank());

        existingRanking.setPerformancePercentage(
                employeeRanking.getPerformancePercentage());

        existingRanking.setRewardsPoints(
                employeeRanking.getRewardsPoints());

        employeeRankingsRepository.save(existingRanking);

        // Capture NEW value after update
        String newValue =
                convertToJson(existingRanking);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "EMPLOYEE_RANKING",
                String.valueOf(existingRanking.getId()),
                performedBy,
                employeeId,
                "Employee ranking updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_EMPLOYEE_RANKING",
                "PERFORMANCE",
                "Employee ranking updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Employee ranking updated successfully for employee: "
                        + employeeId
        );

        return "Employee Ranking Updated Successfully";
    }



//=================================
// DELETE
//=================================

    public String deleteRanking(
            String employeeId) {

        EmployeeRankings employeeRanking =
                employeeRankingsRepository
                        .findByEmployeeEmployeeId(employeeId);

        if (employeeRanking == null) {
            return "Employee Ranking Not Found";
        }

        // Capture deleted value before deletion
        String deletedValue =
                convertToJson(employeeRanking);

        String performedBy =
                getLoggedInEmployeeId();

        employeeRankingsRepository.delete(employeeRanking);

        auditLogsService.createAuditLog(
                "EMPLOYEE_RANKING",
                String.valueOf(employeeRanking.getId()),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Employee ranking deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_EMPLOYEE_RANKING",
                "PERFORMANCE",
                "Employee ranking deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Employee ranking deleted successfully for employee: "
                        + employeeId
        );

        return "Employee Ranking Deleted Successfully";
    }




//=================================
// REPORTS
//=================================

    public List<PerformanceReports> topPerformers() {

        return performanceReportsRepository.findByPerformanceScoreGreaterThanEqual(90.0);
    }


    public List<PerformanceReports>  lowPerformers() {

        return performanceReportsRepository.findByPerformanceScoreLessThan(50.0);
    }


    public List<AuditReports> completedAudits() {

        return auditReportsRepository.findByAuditStatus("COMPLETED");
    }


    public List<AuditReports> pendingAudits() {

        return auditReportsRepository.findByAuditStatus("PENDING");
    }


//=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts() {

        Map<String, Object> counts = new HashMap<>();

        counts.put("totalPerformanceReports", performanceReportsRepository.count());

        counts.put("totalAuditReports", auditReportsRepository.count());

        counts.put("totalEmployeeRankings", employeeRankingsRepository.count());

        counts.put("topPerformers", performanceReportsRepository.findByPerformanceScoreGreaterThanEqual(90.0).size());

        counts.put("lowPerformers", performanceReportsRepository.findByPerformanceScoreLessThan(50.0).size());

        counts.put("completedAudits", auditReportsRepository.findByAuditStatus("COMPLETED").size());

        counts.put("pendingAudits", auditReportsRepository.findByAuditStatus("PENDING").size());

        return counts;
    }
    //=========================================================
// GOALS
//=========================================================

    public String createGoal(
            String employeeId,
            Goal goal) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        goal.setEmployee(employee);

        goalRepository.save(goal);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "GOAL",
                String.valueOf(goal.getId()),
                performedBy,
                employeeId,
                "Goal created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_GOAL",
                "PERFORMANCE",
                "Goal created successfully for employee: " + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Goal created successfully for employee: " + employeeId
        );

        return "Goal Created Successfully";
    }


    public List<Goal> getGoals() {

        return goalRepository.findAll();
    }


    public Goal getGoal(Long id) {

        return goalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));
    }


    public String updateGoal(
            Long id,
            Goal goal) {

        Goal existing = goalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));

        String oldValue = convertToJson(existing);

        existing.setGoalTitle(goal.getGoalTitle());
        existing.setGoalDescription(goal.getGoalDescription());
        existing.setGoalCategory(goal.getGoalCategory());
        existing.setStartDate(goal.getStartDate());
        existing.setEndDate(goal.getEndDate());
        existing.setTargetValue(goal.getTargetValue());
        existing.setAchievedValue(goal.getAchievedValue());
        existing.setStatus(goal.getStatus());

        goalRepository.save(existing);

        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = existing.getEmployee() != null
                ? existing.getEmployee().getId().toString()
                : null;

        auditLogsService.logUpdate(
                "GOAL",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Goal updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_GOAL",
                "PERFORMANCE",
                "Goal updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Goal updated successfully"
        );

        return "Goal Updated Successfully";
    }


    public String deleteGoal(Long id) {

        Goal existing = goalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));

        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = existing.getEmployee() != null
                ? existing.getEmployee().getId().toString()
                : null;

        goalRepository.delete(existing);

        auditLogsService.createAuditLog(
                "GOAL",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Goal deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_GOAL",
                "PERFORMANCE",
                "Goal deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Goal deleted successfully"
        );

        return "Goal Deleted Successfully";
    }


    //=========================================================
// KPIs
//=========================================================

    // CREATE
    public String createKpi(
            Long goalId,
            String employeeId,
            Kpi kpi) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        kpi.setGoal(goal);
        kpi.setEmployee(employee);

        kpiRepository.save(kpi);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "KPI",
                String.valueOf(kpi.getId()),
                performedBy,
                employeeId,
                "KPI created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_KPI",
                "PERFORMANCE",
                "KPI created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "KPI created successfully for employee: "
                        + employeeId
        );

        return "KPI Created Successfully";
    }


    // GET ALL
    public List<Kpi> getKpis() {

        return kpiRepository.findAll();
    }


    // GET BY ID
    public Kpi getKpi(Long id) {

        return kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));
    }


    // UPDATE
    public String updateKpi(
            Long id,
            Kpi kpi) {

        Kpi existing = kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));

        String oldValue =
                convertToJson(existing);

        existing.setKpiName(kpi.getKpiName());
        existing.setTargetScore(kpi.getTargetScore());
        existing.setAchievedScore(kpi.getAchievedScore());
        existing.setWeightage(kpi.getWeightage());
        existing.setEvaluationPeriod(kpi.getEvaluationPeriod());
        existing.setStatus(kpi.getStatus());

        kpiRepository.save(existing);

        String newValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee().getEmployeeId();

        auditLogsService.logUpdate(
                "KPI",
                String.valueOf(id),
                performedBy,
                employeeId,
                "KPI updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_KPI",
                "PERFORMANCE",
                "KPI updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "KPI updated successfully for employee: "
                        + employeeId
        );

        return "KPI Updated Successfully";
    }


    // DELETE
    public String deleteKpi(Long id) {

        Kpi existing = kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee().getEmployeeId();

        kpiRepository.delete(existing);

        auditLogsService.createAuditLog(
                "KPI",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                "KPI deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_KPI",
                "PERFORMANCE",
                "KPI deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "KPI deleted successfully for employee: "
                        + employeeId
        );

        return "KPI Deleted Successfully";
    }


//=========================================================
// APPRAISALS
//=========================================================

// CREATE

    public String createAppraisal(
            String employeeId,
            Appraisal appraisal) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        appraisal.setEmployee(employee);

        appraisalRepository.save(appraisal);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "APPRAISAL",
                String.valueOf(appraisal.getId()),
                performedBy,
                employeeId,
                "Appraisal created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_APPRAISAL",
                "PERFORMANCE",
                "Appraisal created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Appraisal created successfully for employee: "
                        + employeeId
        );

        return "Appraisal Created Successfully";
    }


// GET ALL

    public List<Appraisal> getAppraisals() {

        return appraisalRepository.findAll();
    }


// GET BY ID

    public Appraisal getAppraisal(Long id) {

        return appraisalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appraisal Not Found"));
    }


// UPDATE

    public String updateAppraisal(
            Long id,
            Appraisal appraisal) {

        Appraisal existing =
                appraisalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appraisal Not Found"));

        String oldValue =
                convertToJson(existing);

        existing.setAppraisalPeriod(
                appraisal.getAppraisalPeriod());

        existing.setAppraisalYear(
                appraisal.getAppraisalYear());

        existing.setOverallScore(
                appraisal.getOverallScore());

        existing.setRating(
                appraisal.getRating());

        existing.setAppraisalStatus(
                appraisal.getAppraisalStatus());

        existing.setAppraisedBy(
                appraisal.getAppraisedBy());

        existing.setRemarks(
                appraisal.getRemarks());

        appraisalRepository.save(existing);

        String newValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "APPRAISAL",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Appraisal updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_APPRAISAL",
                "PERFORMANCE",
                "Appraisal updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Appraisal updated successfully"
        );

        return "Appraisal Updated Successfully";
    }


// DELETE

    public String deleteAppraisal(Long id) {

        Appraisal existing =
                appraisalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appraisal Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        appraisalRepository.delete(existing);

        auditLogsService.createAuditLog(
                "APPRAISAL",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Appraisal deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_APPRAISAL",
                "PERFORMANCE",
                "Appraisal deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Appraisal deleted successfully"
        );

        return "Appraisal Deleted Successfully";
    }



//=========================================================
// SELF REVIEWS
//=========================================================

// CREATE

    public String createSelfReview(
            Long appraisalId,
            String employeeId,
            SelfReview selfReview) {

        Appraisal appraisal =
                appraisalRepository.findById(appraisalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appraisal Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        selfReview.setAppraisal(appraisal);
        selfReview.setEmployee(employee);

        selfReviewRepository.save(selfReview);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "SELF_REVIEW",
                String.valueOf(selfReview.getId()),
                performedBy,
                employeeId,
                "Self review created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_SELF_REVIEW",
                "PERFORMANCE",
                "Self review created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Self review created successfully for employee: "
                        + employeeId
        );

        return "Self Review Created Successfully";
    }


// GET ALL

    public List<SelfReview> getSelfReviews() {

        return selfReviewRepository.findAll();
    }


// GET BY ID

    public SelfReview getSelfReview(Long id) {

        return selfReviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Self Review Not Found"));
    }


// UPDATE

    public String updateSelfReview(
            Long id,
            SelfReview selfReview) {

        SelfReview existing =
                selfReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Self Review Not Found"));

        String oldValue =
                convertToJson(existing);

        existing.setStrengths(
                selfReview.getStrengths());

        existing.setImprovements(
                selfReview.getImprovements());

        existing.setAchievements(
                selfReview.getAchievements());

        existing.setOverallRating(
                selfReview.getOverallRating());

        existing.setSubmittedAt(
                selfReview.getSubmittedAt());

        selfReviewRepository.save(existing);

        String newValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "SELF_REVIEW",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Self review updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_SELF_REVIEW",
                "PERFORMANCE",
                "Self review updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Self review updated successfully"
        );

        return "Self Review Updated Successfully";
    }


// DELETE

    public String deleteSelfReview(Long id) {

        SelfReview existing =
                selfReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Self Review Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        selfReviewRepository.delete(existing);

        auditLogsService.createAuditLog(
                "SELF_REVIEW",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Self review deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_SELF_REVIEW",
                "PERFORMANCE",
                "Self review deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Self review deleted successfully"
        );

        return "Self Review Deleted Successfully";
    }




//=========================================================
// MANAGER REVIEWS
//=========================================================

// CREATE

    public String createManagerReview(
            Long appraisalId,
            String managerId,
            String employeeId,
            ManagerReview managerReview) {

        Appraisal appraisal =
                appraisalRepository.findById(appraisalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appraisal Not Found"));

        Employee manager =
                employeeRepository.findById(managerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Manager Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        managerReview.setAppraisal(appraisal);
        managerReview.setManager(manager);
        managerReview.setEmployee(employee);

        managerReviewRepository.save(managerReview);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "MANAGER_REVIEW",
                String.valueOf(managerReview.getId()),
                performedBy,
                employeeId,
                "Manager review created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_MANAGER_REVIEW",
                "PERFORMANCE",
                "Manager review created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Manager review created successfully for employee: "
                        + employeeId
        );

        return "Manager Review Created Successfully";
    }


// GET ALL

    public List<ManagerReview> getManagerReviews() {

        return managerReviewRepository.findAll();
    }


// GET BY ID

    public ManagerReview getManagerReview(Long id) {

        return managerReviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Manager Review Not Found"));
    }


// UPDATE

    public String updateManagerReview(
            Long id,
            ManagerReview managerReview) {

        ManagerReview existing =
                managerReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Manager Review Not Found"));

        String oldValue =
                convertToJson(existing);

        existing.setPerformanceRating(
                managerReview.getPerformanceRating());

        existing.setStrengths(
                managerReview.getStrengths());

        existing.setImprovementPlan(
                managerReview.getImprovementPlan());

        existing.setRecommendations(
                managerReview.getRecommendations());

        existing.setReviewDate(
                managerReview.getReviewDate());

        managerReviewRepository.save(existing);

        String newValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "MANAGER_REVIEW",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Manager review updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_MANAGER_REVIEW",
                "PERFORMANCE",
                "Manager review updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Manager review updated successfully"
        );

        return "Manager Review Updated Successfully";
    }


// DELETE

    public String deleteManagerReview(Long id) {

        ManagerReview existing =
                managerReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Manager Review Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        managerReviewRepository.delete(existing);

        auditLogsService.createAuditLog(
                "MANAGER_REVIEW",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Manager review deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_MANAGER_REVIEW",
                "PERFORMANCE",
                "Manager review deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Manager review deleted successfully"
        );

        return "Manager Review Deleted Successfully";
    }



//=========================================================
// PROMOTION RECOMMENDATIONS
//=========================================================

// CREATE

    public String createPromotionRecommendation(
            String employeeId,
            Long appraisalId,
            PromotionRecommendation recommendation) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        Appraisal appraisal =
                appraisalRepository.findById(appraisalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appraisal Not Found"));

        recommendation.setEmployee(employee);
        recommendation.setAppraisal(appraisal);

        promotionRecommendationRepository.save(
                recommendation);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PROMOTION_RECOMMENDATION",
                String.valueOf(recommendation.getId()),
                performedBy,
                employeeId,
                "Promotion recommendation created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_PROMOTION_RECOMMENDATION",
                "PERFORMANCE",
                "Promotion recommendation created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Promotion recommendation created successfully for employee: "
                        + employeeId
        );

        return "Promotion Recommendation Created Successfully";
    }


// GET ALL

    public List<PromotionRecommendation>
    getPromotionRecommendations() {

        return promotionRecommendationRepository.findAll();
    }


// GET BY ID

    public PromotionRecommendation
    getPromotionRecommendation(Long id) {

        return promotionRecommendationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Promotion Recommendation Not Found"));
    }


// UPDATE

    public String updatePromotionRecommendation(
            Long id,
            PromotionRecommendation recommendation) {

        PromotionRecommendation existing =
                promotionRecommendationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Promotion Recommendation Not Found"));

        String oldValue =
                convertToJson(existing);

        existing.setCurrentDesignation(
                recommendation.getCurrentDesignation());

        existing.setRecommendedDesignation(
                recommendation.getRecommendedDesignation());

        existing.setRecommendedSalary(
                recommendation.getRecommendedSalary());

        existing.setRecommendationReason(
                recommendation.getRecommendationReason());

        existing.setApprovedBy(
                recommendation.getApprovedBy());

        existing.setStatus(
                recommendation.getStatus());

        promotionRecommendationRepository.save(existing);

        String newValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "PROMOTION_RECOMMENDATION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Promotion recommendation updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_PROMOTION_RECOMMENDATION",
                "PERFORMANCE",
                "Promotion recommendation updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Promotion recommendation updated successfully"
        );

        return "Promotion Recommendation Updated Successfully";
    }


// DELETE

    public String deletePromotionRecommendation(Long id) {

        PromotionRecommendation existing =
                promotionRecommendationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Promotion Recommendation Not Found"));

        String deletedValue =
                convertToJson(existing);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        promotionRecommendationRepository.delete(existing);

        auditLogsService.createAuditLog(
                "PROMOTION_RECOMMENDATION",
                String.valueOf(id),
                AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Promotion recommendation deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_PROMOTION_RECOMMENDATION",
                "PERFORMANCE",
                "Promotion recommendation deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PERFORMANCE",
                "PerformanceService",
                "Promotion recommendation deleted successfully"
        );

        return "Promotion Recommendation Deleted Successfully";
    }



}
