package com.HRMS.QuickDines.Payroll.Controller;

import com.HRMS.QuickDines.Payroll.Service.PayrollService;
import com.HRMS.QuickDines.Payroll.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService service;


    //=================================
// SALARIES
//=================================
//hr can update the salary.
    @PostMapping("/salary/{employeeId}")
    public ResponseEntity<?> createSalary(
            @PathVariable String employeeId,
            @RequestBody Salaries salary){

        return ResponseEntity.ok(service.createSalary(employeeId, salary));
    }


    @GetMapping("/salaries")
    public ResponseEntity<?> getAllSalaries(){

        return ResponseEntity.ok(service.getAllSalaries());
    }


    @GetMapping("/salary/{employeeId}")
    public ResponseEntity<?> getSalary(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getSalary(employeeId));
    }


    @PutMapping("/salary/{id}")
    public ResponseEntity<?> updateSalary(
            @PathVariable Long id,
            @RequestBody Salaries salary){

        return ResponseEntity.ok(service.updateSalary(id, salary));
    }


    @DeleteMapping("/salary/{id}")
    public ResponseEntity<?> deleteSalary(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteSalary(id));
    }



    //=================================
// SALARY SLIPS
//=================================

    @PostMapping("/salary-slip/{employeeId}")
    public ResponseEntity<?> generateSalarySlip(
            @PathVariable String employeeId){

        return ResponseEntity.ok(
                service.generateSalarySlip(employeeId));
    }


    @GetMapping("/salary-slip/{employeeId}")
    public ResponseEntity<?> getSalarySlip(
            @PathVariable String employeeId){

        return ResponseEntity.ok(
                service.getSalarySlip(employeeId));
    }


    @DeleteMapping("/salary-slip/{id}")
    public ResponseEntity<?> deleteSalarySlip(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deleteSalarySlip(id));
    }



    //=================================
// PF DETAILS
//=================================

    @PostMapping("/pf/{employeeId}")
    public ResponseEntity<?> createPfDetails(
            @PathVariable String employeeId,
            @RequestBody PfDetails pfDetails){

        return ResponseEntity.ok(service.createPfDetails(employeeId, pfDetails));
    }


    @GetMapping("/pf/{employeeId}")
    public ResponseEntity<?> getPfDetails(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getPfDetails(employeeId));
    }


    @PutMapping("/pf/{employeeId}")
    public ResponseEntity<?> updatePfDetails(
            @PathVariable String employeeId,
            @RequestBody PfDetails pfDetails){

        return ResponseEntity.ok(service.updatePfDetails(employeeId, pfDetails));
    }



    //=================================
// ESI DETAILS
//=================================

    @PostMapping("/esi/{employeeId}")
    public ResponseEntity<?> createEsiDetails(
            @PathVariable String employeeId,
            @RequestBody EsiDetails esiDetails){

        return ResponseEntity.ok(service.createEsiDetails(employeeId, esiDetails));
    }


    @GetMapping("/esi/{employeeId}")
    public ResponseEntity<?> getEsiDetails(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getEsiDetails(employeeId));
    }


    @PutMapping("/esi/{employeeId}")
    public ResponseEntity<?> updateEsiDetails(
            @PathVariable String employeeId,
            @RequestBody EsiDetails esiDetails){

        return ResponseEntity.ok(service.updateEsiDetails(employeeId, esiDetails));
    }


    //=================================
// TDS DETAILS
//=================================

    @PostMapping("/tds/{employeeId}")
    public ResponseEntity<?> createTdsDetails(
            @PathVariable String employeeId,
            @RequestBody TdsDetails tdsDetails){

        return ResponseEntity.ok(service.createTdsDetails(employeeId, tdsDetails));
    }


    @GetMapping("/tds/{employeeId}")
    public ResponseEntity<?> getTdsDetails(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getTdsDetails(employeeId));
    }


    @PutMapping("/tds/{employeeId}")
    public ResponseEntity<?> updateTdsDetails(
            @PathVariable String employeeId,
            @RequestBody TdsDetails tdsDetails){

        return ResponseEntity.ok(service.updateTdsDetails(employeeId, tdsDetails));
    }



    //=================================
// INCREMENTS
//=================================

    @PostMapping("/increment/{employeeId}")
    public ResponseEntity<?> createIncrement(
            @PathVariable String employeeId,
            @RequestBody Increments increment){

        return ResponseEntity.ok(service.createIncrement(employeeId, increment));
    }


    @GetMapping("/increment/{employeeId}")
    public ResponseEntity<?> getIncrement(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getIncrement(employeeId));
    }



    //=================================
// BONUS MANAGEMENT
//=================================

    @PostMapping("/bonus/{employeeId}")
    public ResponseEntity<?> createBonus(
            @PathVariable String employeeId,
            @RequestBody BonusManagement bonusManagement){

        return ResponseEntity.ok(service.createBonus(employeeId, bonusManagement));
    }


    @GetMapping("/bonus/{employeeId}")
    public ResponseEntity<?> getBonus(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getBonus(employeeId));
    }


    @PutMapping("/bonus/{id}")
    public ResponseEntity<?> updateBonus(
            @PathVariable Long id,
            @RequestBody BonusManagement bonusManagement){

        return ResponseEntity.ok(service.updateBonus(id, bonusManagement));
    }



    //=================================
// REPORTS
//=================================

    @GetMapping("/monthly-report")
    public ResponseEntity<?> getMonthlyPayrollReport() {

        return ResponseEntity.ok(service.getMonthlyPayrollReport());
    }


    @GetMapping("/salary-report/{employeeId}")
    public ResponseEntity<?> getSalaryReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getSalaryReport(employeeId));
    }


    @GetMapping("/pf-report/{employeeId}")
    public ResponseEntity<?> getPfReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getPfReport(employeeId));
    }


    @GetMapping("/tds-report/{employeeId}")
    public ResponseEntity<?> getTdsReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getTdsReport(employeeId));
    }

}
