package com.HRMS.QuickDines.Performance.Controller;

import com.HRMS.QuickDines.Performance.Services.PerformanceService;
import com.HRMS.QuickDines.Performance.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService service;

    // =========================================================
    // PERFORMANCE REPORTS
    // =========================================================

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_CREATE')")
    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> createPerformanceReport(
            @PathVariable String employeeId,
            @RequestBody PerformanceReports report) {

        return ResponseEntity.ok(
                service.createPerformanceReport(employeeId, report));
    }

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_READ')")
    @GetMapping("/reports")
    public ResponseEntity<?> getPerformanceReports() {

        return ResponseEntity.ok(
                service.getPerformanceReports());
    }

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_READ')")
    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getPerformanceReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getPerformanceReport(employeeId));
    }

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_UPDATE')")
    @PutMapping("/report/{employeeId}")
    public ResponseEntity<?> updatePerformanceReport(
            @PathVariable String employeeId,
            @RequestBody PerformanceReports report) {

        return ResponseEntity.ok(
                service.updatePerformanceReport(employeeId, report));
    }

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_DELETE')")
    @DeleteMapping("/report/{employeeId}")
    public ResponseEntity<?> deletePerformanceReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deletePerformanceReport(employeeId));
    }


    // =========================================================
    // AUDIT REPORTS
    // =========================================================

    @PreAuthorize("hasAuthority('AUDIT_REPORT_CREATE')")
    @PostMapping("/audit/{employeeId}")
    public ResponseEntity<?> createAuditReport(
            @PathVariable String employeeId,
            @RequestBody AuditReports auditReport) {

        return ResponseEntity.ok(
                service.createAuditReport(employeeId, auditReport));
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_READ')")
    @GetMapping("/audits")
    public ResponseEntity<?> getAuditReports() {

        return ResponseEntity.ok(
                service.getAuditReports());
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_READ')")
    @GetMapping("/audit/{employeeId}")
    public ResponseEntity<?> getAuditReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getAuditReport(employeeId));
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_UPDATE')")
    @PutMapping("/audit/{employeeId}")
    public ResponseEntity<?> updateAuditReport(
            @PathVariable String employeeId,
            @RequestBody AuditReports auditReport) {

        return ResponseEntity.ok(
                service.updateAuditReport(employeeId, auditReport));
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_DELETE')")
    @DeleteMapping("/audit/{employeeId}")
    public ResponseEntity<?> deleteAuditReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteAuditReport(employeeId));
    }


    // =========================================================
    // EMPLOYEE RANKINGS
    // =========================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_RANKING_CREATE')")
    @PostMapping("/ranking/{employeeId}")
    public ResponseEntity<?> createRanking(
            @PathVariable String employeeId,
            @RequestBody EmployeeRankings employeeRanking) {

        return ResponseEntity.ok(
                service.createRanking(employeeId, employeeRanking));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_RANKING_READ')")
    @GetMapping("/rankings")
    public ResponseEntity<?> getRankings() {

        return ResponseEntity.ok(
                service.getRankings());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_RANKING_READ')")
    @GetMapping("/ranking/{employeeId}")
    public ResponseEntity<?> getRanking(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getRanking(employeeId));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_RANKING_UPDATE')")
    @PutMapping("/ranking/{employeeId}")
    public ResponseEntity<?> updateRanking(
            @PathVariable String employeeId,
            @RequestBody EmployeeRankings employeeRanking) {

        return ResponseEntity.ok(
                service.updateRanking(employeeId, employeeRanking));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_RANKING_DELETE')")
    @DeleteMapping("/ranking/{employeeId}")
    public ResponseEntity<?> deleteRanking(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteRanking(employeeId));
    }


    // =========================================================
    // PERFORMANCE REPORTS / FILTERS
    // =========================================================

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_READ')")
    @GetMapping("/top-performers")
    public ResponseEntity<?> topPerformers() {

        return ResponseEntity.ok(
                service.topPerformers());
    }

    @PreAuthorize("hasAuthority('PERFORMANCE_REPORT_READ')")
    @GetMapping("/low-performers")
    public ResponseEntity<?> lowPerformers() {

        return ResponseEntity.ok(
                service.lowPerformers());
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_READ')")
    @GetMapping("/completed-audits")
    public ResponseEntity<?> completedAudits() {

        return ResponseEntity.ok(
                service.completedAudits());
    }

    @PreAuthorize("hasAuthority('AUDIT_REPORT_READ')")
    @GetMapping("/pending-audits")
    public ResponseEntity<?> pendingAudits() {

        return ResponseEntity.ok(
                service.pendingAudits());
    }


    // =========================================================
    // DASHBOARD COUNTS
    // =========================================================

    @PreAuthorize("hasAuthority('PERFORMANCE_DASHBOARD_READ')")
    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(
                service.getCounts());
    }


    // =========================================================
    // GOALS
    // =========================================================

    @PreAuthorize("hasAuthority('GOAL_CREATE')")
    @PostMapping("/goal/{employeeId}")
    public ResponseEntity<?> createGoal(
            @PathVariable String employeeId,
            @RequestBody Goal goal) {

        return ResponseEntity.ok(
                service.createGoal(employeeId, goal));
    }

    @PreAuthorize("hasAuthority('GOAL_READ')")
    @GetMapping("/goals")
    public ResponseEntity<?> getGoals() {

        return ResponseEntity.ok(
                service.getGoals());
    }

    @PreAuthorize("hasAuthority('GOAL_READ')")
    @GetMapping("/goal/{id}")
    public ResponseEntity<?> getGoal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getGoal(id));
    }

    @PreAuthorize("hasAuthority('GOAL_UPDATE')")
    @PutMapping("/goal/{id}")
    public ResponseEntity<?> updateGoal(
            @PathVariable Long id,
            @RequestBody Goal goal) {

        return ResponseEntity.ok(
                service.updateGoal(id, goal));
    }

    @PreAuthorize("hasAuthority('GOAL_DELETE')")
    @DeleteMapping("/goal/{id}")
    public ResponseEntity<?> deleteGoal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteGoal(id));
    }


    // =========================================================
    // KPIs
    // =========================================================

    @PreAuthorize("hasAuthority('KPI_CREATE')")
    @PostMapping("/kpi/{goalId}/{employeeId}")
    public ResponseEntity<?> createKpi(
            @PathVariable Long goalId,
            @PathVariable String employeeId,
            @RequestBody Kpi kpi) {

        return ResponseEntity.ok(
                service.createKpi(goalId, employeeId, kpi));
    }

    @PreAuthorize("hasAuthority('KPI_READ')")
    @GetMapping("/kpis")
    public ResponseEntity<?> getKpis() {

        return ResponseEntity.ok(
                service.getKpis());
    }

    @PreAuthorize("hasAuthority('KPI_READ')")
    @GetMapping("/kpi/{id}")
    public ResponseEntity<?> getKpi(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getKpi(id));
    }

    @PreAuthorize("hasAuthority('KPI_UPDATE')")
    @PutMapping("/kpi/{id}")
    public ResponseEntity<?> updateKpi(
            @PathVariable Long id,
            @RequestBody Kpi kpi) {

        return ResponseEntity.ok(
                service.updateKpi(id, kpi));
    }

    @PreAuthorize("hasAuthority('KPI_DELETE')")
    @DeleteMapping("/kpi/{id}")
    public ResponseEntity<?> deleteKpi(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteKpi(id));
    }


    // =========================================================
    // APPRAISALS
    // =========================================================

    @PreAuthorize("hasAuthority('APPRAISAL_CREATE')")
    @PostMapping("/appraisal/{employeeId}")
    public ResponseEntity<?> createAppraisal(
            @PathVariable String employeeId,
            @RequestBody Appraisal appraisal) {

        return ResponseEntity.ok(
                service.createAppraisal(employeeId, appraisal));
    }

    @PreAuthorize("hasAuthority('APPRAISAL_READ')")
    @GetMapping("/appraisals")
    public ResponseEntity<?> getAppraisals() {

        return ResponseEntity.ok(
                service.getAppraisals());
    }

    @PreAuthorize("hasAuthority('APPRAISAL_READ')")
    @GetMapping("/appraisal/{id}")
    public ResponseEntity<?> getAppraisal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAppraisal(id));
    }

    @PreAuthorize("hasAuthority('APPRAISAL_UPDATE')")
    @PutMapping("/appraisal/{id}")
    public ResponseEntity<?> updateAppraisal(
            @PathVariable Long id,
            @RequestBody Appraisal appraisal) {

        return ResponseEntity.ok(
                service.updateAppraisal(id, appraisal));
    }

    @PreAuthorize("hasAuthority('APPRAISAL_DELETE')")
    @DeleteMapping("/appraisal/{id}")
    public ResponseEntity<?> deleteAppraisal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAppraisal(id));
    }


    // =========================================================
    // SELF REVIEWS
    // =========================================================

    @PreAuthorize("hasAuthority('SELF_REVIEW_CREATE')")
    @PostMapping("/self-review/{appraisalId}/{employeeId}")
    public ResponseEntity<?> createSelfReview(
            @PathVariable Long appraisalId,
            @PathVariable String employeeId,
            @RequestBody SelfReview selfReview) {

        return ResponseEntity.ok(
                service.createSelfReview(
                        appraisalId,
                        employeeId,
                        selfReview));
    }

    @PreAuthorize("hasAuthority('SELF_REVIEW_READ')")
    @GetMapping("/self-reviews")
    public ResponseEntity<?> getSelfReviews() {

        return ResponseEntity.ok(
                service.getSelfReviews());
    }

    @PreAuthorize("hasAuthority('SELF_REVIEW_READ')")
    @GetMapping("/self-review/{id}")
    public ResponseEntity<?> getSelfReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSelfReview(id));
    }

    @PreAuthorize("hasAuthority('SELF_REVIEW_UPDATE')")
    @PutMapping("/self-review/{id}")
    public ResponseEntity<?> updateSelfReview(
            @PathVariable Long id,
            @RequestBody SelfReview selfReview) {

        return ResponseEntity.ok(
                service.updateSelfReview(id, selfReview));
    }

    @PreAuthorize("hasAuthority('SELF_REVIEW_DELETE')")
    @DeleteMapping("/self-review/{id}")
    public ResponseEntity<?> deleteSelfReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSelfReview(id));
    }


    // =========================================================
    // MANAGER REVIEWS
    // =========================================================

    @PreAuthorize("hasAuthority('MANAGER_REVIEW_CREATE')")
    @PostMapping("/manager-review/{appraisalId}/{managerId}/{employeeId}")
    public ResponseEntity<?> createManagerReview(
            @PathVariable Long appraisalId,
            @PathVariable String managerId,
            @PathVariable String employeeId,
            @RequestBody ManagerReview managerReview) {

        return ResponseEntity.ok(
                service.createManagerReview(
                        appraisalId,
                        managerId,
                        employeeId,
                        managerReview));
    }

    @PreAuthorize("hasAuthority('MANAGER_REVIEW_READ')")
    @GetMapping("/manager-reviews")
    public ResponseEntity<?> getManagerReviews() {

        return ResponseEntity.ok(
                service.getManagerReviews());
    }

    @PreAuthorize("hasAuthority('MANAGER_REVIEW_READ')")
    @GetMapping("/manager-review/{id}")
    public ResponseEntity<?> getManagerReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getManagerReview(id));
    }

    @PreAuthorize("hasAuthority('MANAGER_REVIEW_UPDATE')")
    @PutMapping("/manager-review/{id}")
    public ResponseEntity<?> updateManagerReview(
            @PathVariable Long id,
            @RequestBody ManagerReview managerReview) {

        return ResponseEntity.ok(
                service.updateManagerReview(id, managerReview));
    }

    @PreAuthorize("hasAuthority('MANAGER_REVIEW_DELETE')")
    @DeleteMapping("/manager-review/{id}")
    public ResponseEntity<?> deleteManagerReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteManagerReview(id));
    }


    // =========================================================
    // PROMOTION RECOMMENDATIONS
    // =========================================================

    @PreAuthorize("hasAuthority('PROMOTION_RECOMMENDATION_CREATE')")
    @PostMapping("/promotion/{employeeId}/{appraisalId}")
    public ResponseEntity<?> createPromotionRecommendation(
            @PathVariable String employeeId,
            @PathVariable Long appraisalId,
            @RequestBody PromotionRecommendation recommendation) {

        return ResponseEntity.ok(
                service.createPromotionRecommendation(
                        employeeId,
                        appraisalId,
                        recommendation));
    }

    @PreAuthorize("hasAuthority('PROMOTION_RECOMMENDATION_READ')")
    @GetMapping("/promotions")
    public ResponseEntity<?> getPromotionRecommendations() {

        return ResponseEntity.ok(
                service.getPromotionRecommendations());
    }

    @PreAuthorize("hasAuthority('PROMOTION_RECOMMENDATION_READ')")
    @GetMapping("/promotion/{id}")
    public ResponseEntity<?> getPromotionRecommendation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPromotionRecommendation(id));
    }

    @PreAuthorize("hasAuthority('PROMOTION_RECOMMENDATION_UPDATE')")
    @PutMapping("/promotion/{id}")
    public ResponseEntity<?> updatePromotionRecommendation(
            @PathVariable Long id,
            @RequestBody PromotionRecommendation recommendation) {

        return ResponseEntity.ok(
                service.updatePromotionRecommendation(
                        id,
                        recommendation));
    }

    @PreAuthorize("hasAuthority('PROMOTION_RECOMMENDATION_DELETE')")
    @DeleteMapping("/promotion/{id}")
    public ResponseEntity<?> deletePromotionRecommendation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePromotionRecommendation(id));
    }
}