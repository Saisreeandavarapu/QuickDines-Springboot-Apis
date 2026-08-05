package com.HRMS.QuickDines.Performance.Services;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Performance.model.*;
import com.HRMS.QuickDines.Performance.repo.*;
import lombok.RequiredArgsConstructor;
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



//=================================
// PERFORMANCE REPORTS
//=================================


// CREATE

    public String createPerformanceReport(String employeeId, PerformanceReports report){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        report.setEmployee(employee);

        performanceReportsRepository.save(report);

        return "Performance Report Created Successfully";
    }


// GET ALL

    public List<PerformanceReports> getPerformanceReports(){

        return performanceReportsRepository.findAll();
    }


// GET BY EMPLOYEE ID

    public List<PerformanceReports> getPerformanceReport(String employeeId){

        return performanceReportsRepository.findByEmployeeEmployeeId(employeeId);
    }


// UPDATE

    public String updatePerformanceReport(String employeeId, PerformanceReports report){

        PerformanceReports existingReport = (PerformanceReports) performanceReportsRepository.findByEmployeeEmployeeId(employeeId);

        if(existingReport == null){
            return "Performance Report Not Found";
        }

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

        return "Performance Report Updated Successfully";
    }


// DELETE

    public String deletePerformanceReport(String employeeId){

        PerformanceReports report = (PerformanceReports) performanceReportsRepository.findByEmployeeEmployeeId(employeeId);

        if(report == null){
            return "Performance Report Not Found";
        }

        performanceReportsRepository.delete(report);

        return "Performance Report Deleted Successfully";
    }


//=================================
// AUDIT REPORTS
//=================================
// CREATE

    public String createAuditReport(String employeeId, AuditReports auditReport){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        auditReport.setEmployee(employee);

        auditReportsRepository.save(auditReport);

        return "Audit Report Created Successfully";
    }


// GET ALL

    public List<AuditReports> getAuditReports(){

        return auditReportsRepository.findAll();
    }


// GET BY EMPLOYEE ID

    public List<AuditReports> getAuditReport(String employeeId){

        return auditReportsRepository.findByEmployeeEmployeeId(employeeId);
    }


// UPDATE

    public String updateAuditReport(String employeeId, AuditReports auditReport){

        AuditReports existingAudit = (AuditReports) auditReportsRepository.findByEmployeeEmployeeId(employeeId);

        if(existingAudit == null){
            return "Audit Report Not Found";
        }

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

        return "Audit Report Updated Successfully";
    }


// DELETE

    public String deleteAuditReport(String employeeId){

        AuditReports auditReport = (AuditReports) auditReportsRepository.findByEmployeeEmployeeId(employeeId);

        if(auditReport == null){
            return "Audit Report Not Found";
        }

        auditReportsRepository.delete(auditReport);

        return "Audit Report Deleted Successfully";
    }


//=================================
// EMPLOYEE RANKINGS
//=================================

// CREATE

    public String createRanking(String employeeId, EmployeeRankings employeeRanking){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeRanking.setEmployee(employee);

        employeeRankingsRepository.save(employeeRanking);

        return "Employee Ranking Created Successfully";
    }


// GET ALL

    public List<EmployeeRankings> getRankings(){

        return employeeRankingsRepository.findAll();
    }


// GET BY EMPLOYEE ID

    public EmployeeRankings getRanking(String employeeId){

        return employeeRankingsRepository.findByEmployeeEmployeeId(employeeId);
    }


// UPDATE

    public String updateRanking(String employeeId, EmployeeRankings employeeRanking){

        EmployeeRankings existingRanking = employeeRankingsRepository.findByEmployeeEmployeeId(employeeId);

        if(existingRanking == null){
            return "Employee Ranking Not Found";
        }

        existingRanking.setRankPosition(
                employeeRanking.getRankPosition());

        existingRanking.setDepartmentRank(
                employeeRanking.getDepartmentRank());

        existingRanking.setPerformancePercentage(
                employeeRanking.getPerformancePercentage());

        existingRanking.setRewardsPoints(
                employeeRanking.getRewardsPoints());

        employeeRankingsRepository.save(existingRanking);

        return "Employee Ranking Updated Successfully";
    }


// DELETE

    public String deleteRanking(String employeeId){

        EmployeeRankings employeeRanking = employeeRankingsRepository.findByEmployeeEmployeeId(employeeId);

        if(employeeRanking == null){
            return "Employee Ranking Not Found";
        }

        employeeRankingsRepository.delete(employeeRanking);

        return "Employee Ranking Deleted Successfully";
    }


//=================================
// REPORTS
//=================================

    public Object topPerformers() {

        return performanceReportsRepository.findByPerformanceScoreGreaterThanEqual(90.0);
    }


    public Object lowPerformers() {

        return performanceReportsRepository.findByPerformanceScoreLessThan(50.0);
    }


    public Object completedAudits() {

        return auditReportsRepository.findByAuditStatus("COMPLETED");
    }


    public Object pendingAudits() {

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

        existing.setGoalTitle(goal.getGoalTitle());
        existing.setGoalDescription(goal.getGoalDescription());
        existing.setGoalCategory(goal.getGoalCategory());
        existing.setStartDate(goal.getStartDate());
        existing.setEndDate(goal.getEndDate());
        existing.setTargetValue(goal.getTargetValue());
        existing.setAchievedValue(goal.getAchievedValue());
        existing.setStatus(goal.getStatus());

        goalRepository.save(existing);

        return "Goal Updated Successfully";
    }


    public String deleteGoal(Long id) {

        Goal existing = goalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));

        goalRepository.delete(existing);

        return "Goal Deleted Successfully";
    }


    //=========================================================
    // KPIs
    //=========================================================

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

        return "KPI Created Successfully";
    }


    public List<Kpi> getKpis() {

        return kpiRepository.findAll();
    }


    public Kpi getKpi(Long id) {

        return kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));
    }


    public String updateKpi(
            Long id,
            Kpi kpi) {

        Kpi existing = kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));

        existing.setKpiName(kpi.getKpiName());
        existing.setTargetScore(kpi.getTargetScore());
        existing.setAchievedScore(kpi.getAchievedScore());
        existing.setWeightage(kpi.getWeightage());
        existing.setEvaluationPeriod(kpi.getEvaluationPeriod());
        existing.setStatus(kpi.getStatus());

        kpiRepository.save(existing);

        return "KPI Updated Successfully";
    }


    public String deleteKpi(Long id) {

        Kpi existing = kpiRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("KPI Not Found"));

        kpiRepository.delete(existing);

        return "KPI Deleted Successfully";
    }


    //=========================================================
    // APPRAISALS
    //=========================================================

    public String createAppraisal(
            String employeeId,
            Appraisal appraisal) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        appraisal.setEmployee(employee);

        appraisalRepository.save(appraisal);

        return "Appraisal Created Successfully";
    }


    public List<Appraisal> getAppraisals() {

        return appraisalRepository.findAll();
    }


    public Appraisal getAppraisal(Long id) {

        return appraisalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appraisal Not Found"));
    }


    public String updateAppraisal(Long id, Appraisal appraisal) {

        Appraisal existing = appraisalRepository.findById(id).orElseThrow(() -> new RuntimeException("Appraisal Not Found"));

        existing.setAppraisalPeriod(appraisal.getAppraisalPeriod());

        existing.setAppraisalYear(appraisal.getAppraisalYear());

        existing.setOverallScore(appraisal.getOverallScore());

        existing.setRating(appraisal.getRating());

        existing.setAppraisalStatus(appraisal.getAppraisalStatus());

        existing.setAppraisedBy(appraisal.getAppraisedBy());

        existing.setRemarks(appraisal.getRemarks());

        appraisalRepository.save(existing);

        return "Appraisal Updated Successfully";
    }


    public String deleteAppraisal(Long id) {

        Appraisal existing = appraisalRepository.findById(id).orElseThrow(() -> new RuntimeException("Appraisal Not Found"));

        appraisalRepository.delete(existing);

        return "Appraisal Deleted Successfully";
    }


    //=========================================================
    // SELF REVIEWS
    //=========================================================

    public String createSelfReview(Long appraisalId, String employeeId, SelfReview selfReview) {

        Appraisal appraisal = appraisalRepository.findById(appraisalId).orElseThrow(() -> new RuntimeException("Appraisal Not Found"));

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        selfReview.setAppraisal(appraisal);
        selfReview.setEmployee(employee);

        selfReviewRepository.save(selfReview);

        return "Self Review Created Successfully";
    }


    public List<SelfReview> getSelfReviews() {

        return selfReviewRepository.findAll();
    }


    public SelfReview getSelfReview(Long id) {

        return selfReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Self Review Not Found"));
    }


    public String updateSelfReview(Long id, SelfReview selfReview) {

        SelfReview existing = selfReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Self Review Not Found"));

        existing.setStrengths(selfReview.getStrengths());

        existing.setImprovements(selfReview.getImprovements());

        existing.setAchievements(selfReview.getAchievements());

        existing.setOverallRating(selfReview.getOverallRating());

        existing.setSubmittedAt(selfReview.getSubmittedAt());

        selfReviewRepository.save(existing);

        return "Self Review Updated Successfully";
    }


    public String deleteSelfReview(Long id) {

        SelfReview existing = selfReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Self Review Not Found"));

        selfReviewRepository.delete(existing);

        return "Self Review Deleted Successfully";
    }


    //=========================================================
    // MANAGER REVIEWS
    //=========================================================

    public String createManagerReview(Long appraisalId, String managerId, String employeeId, ManagerReview managerReview) {

        Appraisal appraisal = appraisalRepository.findById(appraisalId).orElseThrow(() -> new RuntimeException("Appraisal Not Found"));
        Employee manager = employeeRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager Not Found"));

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        managerReview.setAppraisal(appraisal);
        managerReview.setManager(manager);
        managerReview.setEmployee(employee);

        managerReviewRepository.save(managerReview);

        return "Manager Review Created Successfully";
    }


    public List<ManagerReview> getManagerReviews() {

        return managerReviewRepository.findAll();
    }


    public ManagerReview getManagerReview(Long id) {

        return managerReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Manager Review Not Found"));
    }


    public String updateManagerReview(Long id, ManagerReview managerReview) {

        ManagerReview existing = managerReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Manager Review Not Found"));

        existing.setPerformanceRating(managerReview.getPerformanceRating());

        existing.setStrengths(managerReview.getStrengths());

        existing.setImprovementPlan(managerReview.getImprovementPlan());

        existing.setRecommendations(managerReview.getRecommendations());

        existing.setReviewDate(managerReview.getReviewDate());

        managerReviewRepository.save(existing);

        return "Manager Review Updated Successfully";
    }


    public String deleteManagerReview(Long id) {

        ManagerReview existing = managerReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Manager Review Not Found"));

        managerReviewRepository.delete(existing);

        return "Manager Review Deleted Successfully";
    }


    //=========================================================
    // PROMOTION RECOMMENDATIONS
    //=========================================================

    public String createPromotionRecommendation(String employeeId, Long appraisalId, PromotionRecommendation recommendation) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Appraisal appraisal = appraisalRepository.findById(appraisalId).orElseThrow(() -> new RuntimeException("Appraisal Not Found"));

        recommendation.setEmployee(employee);
        recommendation.setAppraisal(appraisal);

        promotionRecommendationRepository.save(recommendation);

        return "Promotion Recommendation Created Successfully";
    }


    public List<PromotionRecommendation> getPromotionRecommendations() {

        return promotionRecommendationRepository.findAll();
    }


    public PromotionRecommendation getPromotionRecommendation(Long id) {

        return promotionRecommendationRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotion Recommendation Not Found"));
    }


    public String updatePromotionRecommendation(Long id, PromotionRecommendation recommendation) {

        PromotionRecommendation existing = promotionRecommendationRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotion Recommendation Not Found"));

        existing.setCurrentDesignation(recommendation.getCurrentDesignation());

        existing.setRecommendedDesignation(recommendation.getRecommendedDesignation());

        existing.setRecommendedSalary(recommendation.getRecommendedSalary());

        existing.setRecommendationReason(recommendation.getRecommendationReason());

        existing.setApprovedBy(recommendation.getApprovedBy());

        existing.setStatus(recommendation.getStatus());

        promotionRecommendationRepository.save(existing);

        return "Promotion Recommendation Updated Successfully";
    }


    public String deletePromotionRecommendation(Long id) {

        PromotionRecommendation existing = promotionRecommendationRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotion Recommendation Not Found"));

        promotionRecommendationRepository.delete(existing);

        return "Promotion Recommendation Deleted Successfully";
    }


}
