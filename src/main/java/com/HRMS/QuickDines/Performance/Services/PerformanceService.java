package com.HRMS.QuickDines.Performance.Services;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Performance.model.AuditReports;
import com.HRMS.QuickDines.Performance.model.EmployeeRankings;
import com.HRMS.QuickDines.Performance.model.PerformanceReports;
import com.HRMS.QuickDines.Performance.repo.AuditReportsRepository;
import com.HRMS.QuickDines.Performance.repo.EmployeeRankingsRepository;
import com.HRMS.QuickDines.Performance.repo.PerformanceReportsRepository;
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


}
