package com.HRMS.QuickDines.Performance.Controller;

import com.HRMS.QuickDines.Performance.Services.PerformanceService;
import com.HRMS.QuickDines.Performance.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService service;


///=================================
// PERFORMANCE REPORTS
//=================================

@PostMapping("/report/{employeeId}")
public ResponseEntity<?> createPerformanceReport(
        @PathVariable String employeeId,
        @RequestBody PerformanceReports report){

    return ResponseEntity.ok(service.createPerformanceReport(employeeId, report));
}


    @GetMapping("/reports")
    public ResponseEntity<?> getPerformanceReports(){

        return ResponseEntity.ok(service.getPerformanceReports());
    }


    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getPerformanceReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getPerformanceReport(employeeId));
    }


    @PutMapping("/report/{employeeId}")
    public ResponseEntity<?> updatePerformanceReport(
            @PathVariable String employeeId,
            @RequestBody PerformanceReports report){

        return ResponseEntity.ok(service.updatePerformanceReport(employeeId, report));
    }


    @DeleteMapping("/report/{employeeId}")
    public ResponseEntity<?> deletePerformanceReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deletePerformanceReport(employeeId));
    }

//=================================
// AUDIT REPORTS
//=================================

    @PostMapping("/audit/{employeeId}")
    public ResponseEntity<?> createAuditReport(
            @PathVariable String employeeId,
            @RequestBody AuditReports auditReport){

        return ResponseEntity.ok(service.createAuditReport(employeeId, auditReport));
    }


    @GetMapping("/audits")
    public ResponseEntity<?> getAuditReports(){

        return ResponseEntity.ok(service.getAuditReports());
    }


    @GetMapping("/audit/{employeeId}")
    public ResponseEntity<?> getAuditReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getAuditReport(employeeId));
    }


    @PutMapping("/audit/{employeeId}")
    public ResponseEntity<?> updateAuditReport(
            @PathVariable String employeeId,
            @RequestBody AuditReports auditReport){

        return ResponseEntity.ok(service.updateAuditReport(employeeId, auditReport));
    }


    @DeleteMapping("/audit/{employeeId}")
    public ResponseEntity<?> deleteAuditReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteAuditReport(employeeId));
    }

//=================================
// EMPLOYEE RANKINGS
//=================================

    @PostMapping("/ranking/{employeeId}")
    public ResponseEntity<?> createRanking(
            @PathVariable String employeeId,
            @RequestBody EmployeeRankings employeeRanking){

        return ResponseEntity.ok(service.createRanking(employeeId, employeeRanking));
    }


    @GetMapping("/rankings")
    public ResponseEntity<?> getRankings(){

        return ResponseEntity.ok(service.getRankings());
    }


    @GetMapping("/ranking/{employeeId}")
    public ResponseEntity<?> getRanking(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getRanking(employeeId));
    }


    @PutMapping("/ranking/{employeeId}")
    public ResponseEntity<?> updateRanking(
            @PathVariable String employeeId,
            @RequestBody EmployeeRankings employeeRanking){

        return ResponseEntity.ok(service.updateRanking(employeeId, employeeRanking));
    }


    @DeleteMapping("/ranking/{employeeId}")
    public ResponseEntity<?> deleteRanking(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteRanking(employeeId));
    }


//=================================
// REPORTS
//=================================

    @GetMapping("/top-performers")
    public ResponseEntity<?> topPerformers() {

        return ResponseEntity.ok(
                service.topPerformers());
    }


    @GetMapping("/low-performers")
    public ResponseEntity<?> lowPerformers() {

        return ResponseEntity.ok(
                service.lowPerformers());
    }


    @GetMapping("/completed-audits")
    public ResponseEntity<?> completedAudits() {

        return ResponseEntity.ok(
                service.completedAudits());
    }


    @GetMapping("/pending-audits")
    public ResponseEntity<?> pendingAudits() {

        return ResponseEntity.ok(
                service.pendingAudits());
    }


//=================================
// DASHBOARD COUNTS
//=================================

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }

    //=========================================================
    // GOALS
    //=========================================================

    @PostMapping("/goal/{employeeId}")
    public ResponseEntity<?> createGoal(
            @PathVariable String employeeId,
            @RequestBody Goal goal) {

        return ResponseEntity.ok(
                service.createGoal(employeeId, goal));
    }

    @GetMapping("/goals")
    public ResponseEntity<?> getGoals() {

        return ResponseEntity.ok(
                service.getGoals());
    }

    @GetMapping("/goal/{id}")
    public ResponseEntity<?> getGoal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getGoal(id));
    }

    @PutMapping("/goal/{id}")
    public ResponseEntity<?> updateGoal(
            @PathVariable Long id,
            @RequestBody Goal goal) {

        return ResponseEntity.ok(
                service.updateGoal(id, goal));
    }

    @DeleteMapping("/goal/{id}")
    public ResponseEntity<?> deleteGoal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteGoal(id));
    }


    //=========================================================
    // KPIs
    //=========================================================

    @PostMapping("/kpi/{goalId}/{employeeId}")
    public ResponseEntity<?> createKpi(
            @PathVariable Long goalId,
            @PathVariable String employeeId,
            @RequestBody Kpi kpi) {

        return ResponseEntity.ok(
                service.createKpi(goalId, employeeId, kpi));
    }

    @GetMapping("/kpis")
    public ResponseEntity<?> getKpis() {

        return ResponseEntity.ok(
                service.getKpis());
    }

    @GetMapping("/kpi/{id}")
    public ResponseEntity<?> getKpi(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getKpi(id));
    }

    @PutMapping("/kpi/{id}")
    public ResponseEntity<?> updateKpi(
            @PathVariable Long id,
            @RequestBody Kpi kpi) {

        return ResponseEntity.ok(
                service.updateKpi(id, kpi));
    }

    @DeleteMapping("/kpi/{id}")
    public ResponseEntity<?> deleteKpi(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteKpi(id));
    }


    //=========================================================
    // APPRAISALS
    //=========================================================

    @PostMapping("/appraisal/{employeeId}")
    public ResponseEntity<?> createAppraisal(
            @PathVariable String employeeId,
            @RequestBody Appraisal appraisal) {

        return ResponseEntity.ok(
                service.createAppraisal(employeeId, appraisal));
    }

    @GetMapping("/appraisals")
    public ResponseEntity<?> getAppraisals() {

        return ResponseEntity.ok(
                service.getAppraisals());
    }

    @GetMapping("/appraisal/{id}")
    public ResponseEntity<?> getAppraisal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAppraisal(id));
    }

    @PutMapping("/appraisal/{id}")
    public ResponseEntity<?> updateAppraisal(
            @PathVariable Long id,
            @RequestBody Appraisal appraisal) {

        return ResponseEntity.ok(
                service.updateAppraisal(id, appraisal));
    }

    @DeleteMapping("/appraisal/{id}")
    public ResponseEntity<?> deleteAppraisal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAppraisal(id));
    }


    //=========================================================
    // SELF REVIEWS
    //=========================================================

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

    @GetMapping("/self-reviews")
    public ResponseEntity<?> getSelfReviews() {

        return ResponseEntity.ok(
                service.getSelfReviews());
    }

    @GetMapping("/self-review/{id}")
    public ResponseEntity<?> getSelfReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSelfReview(id));
    }

    @PutMapping("/self-review/{id}")
    public ResponseEntity<?> updateSelfReview(
            @PathVariable Long id,
            @RequestBody SelfReview selfReview) {

        return ResponseEntity.ok(
                service.updateSelfReview(id, selfReview));
    }

    @DeleteMapping("/self-review/{id}")
    public ResponseEntity<?> deleteSelfReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSelfReview(id));
    }


    //=========================================================
    // MANAGER REVIEWS
    //=========================================================

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

    @GetMapping("/manager-reviews")
    public ResponseEntity<?> getManagerReviews() {

        return ResponseEntity.ok(
                service.getManagerReviews());
    }

    @GetMapping("/manager-review/{id}")
    public ResponseEntity<?> getManagerReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getManagerReview(id));
    }

    @PutMapping("/manager-review/{id}")
    public ResponseEntity<?> updateManagerReview(
            @PathVariable Long id,
            @RequestBody ManagerReview managerReview) {

        return ResponseEntity.ok(
                service.updateManagerReview(id, managerReview));
    }

    @DeleteMapping("/manager-review/{id}")
    public ResponseEntity<?> deleteManagerReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteManagerReview(id));
    }


    //=========================================================
    // PROMOTION RECOMMENDATIONS
    //=========================================================

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

    @GetMapping("/promotions")
    public ResponseEntity<?> getPromotionRecommendations() {

        return ResponseEntity.ok(
                service.getPromotionRecommendations());
    }

    @GetMapping("/promotion/{id}")
    public ResponseEntity<?> getPromotionRecommendation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPromotionRecommendation(id));
    }

    @PutMapping("/promotion/{id}")
    public ResponseEntity<?> updatePromotionRecommendation(
            @PathVariable Long id,
            @RequestBody PromotionRecommendation recommendation) {

        return ResponseEntity.ok(
                service.updatePromotionRecommendation(
                        id,
                        recommendation));
    }

    @DeleteMapping("/promotion/{id}")
    public ResponseEntity<?> deletePromotionRecommendation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePromotionRecommendation(id));
    }

}
