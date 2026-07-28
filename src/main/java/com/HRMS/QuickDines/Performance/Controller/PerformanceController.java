package com.HRMS.QuickDines.Performance.Controller;

import com.HRMS.QuickDines.Performance.Services.PerformanceService;
import com.HRMS.QuickDines.Performance.model.AuditReports;
import com.HRMS.QuickDines.Performance.model.EmployeeRankings;
import com.HRMS.QuickDines.Performance.model.PerformanceReports;
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

}
