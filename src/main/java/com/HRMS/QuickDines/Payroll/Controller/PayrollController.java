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
    //=================================
// SALARY COMPONENTS
//=================================

    @PostMapping("/component/{companyId}")
    public ResponseEntity<?> createSalaryComponent(
            @PathVariable Long companyId,
            @RequestBody SalaryComponent salaryComponent) {

        return ResponseEntity.ok(
                service.createSalaryComponent(companyId, salaryComponent));
    }

    @GetMapping("/components")
    public ResponseEntity<?> getSalaryComponents() {

        return ResponseEntity.ok(
                service.getSalaryComponents());
    }

    @GetMapping("/component/{id}")
    public ResponseEntity<?> getSalaryComponent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSalaryComponent(id));
    }

    @PutMapping("/component/{id}")
    public ResponseEntity<?> updateSalaryComponent(
            @PathVariable Long id,
            @RequestBody SalaryComponent salaryComponent) {

        return ResponseEntity.ok(
                service.updateSalaryComponent(id, salaryComponent));
    }

    @DeleteMapping("/component/{id}")
    public ResponseEntity<?> deleteSalaryComponent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSalaryComponent(id));
    }
    //=================================
// ALLOWANCES
//=================================

    @PostMapping("/allowance/{employeeId}")
    public ResponseEntity<?> createAllowance(
            @PathVariable String employeeId,
            @RequestBody Allowance allowance) {

        return ResponseEntity.ok(
                service.createAllowance(employeeId, allowance));
    }

    @GetMapping("/allowances")
    public ResponseEntity<?> getAllowances() {

        return ResponseEntity.ok(
                service.getAllowances());
    }

    @GetMapping("/allowance/{id}")
    public ResponseEntity<?> getAllowance(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAllowance(id));
    }

    @PutMapping("/allowance/{id}")
    public ResponseEntity<?> updateAllowance(
            @PathVariable Long id,
            @RequestBody Allowance allowance) {

        return ResponseEntity.ok(
                service.updateAllowance(id, allowance));
    }

    @DeleteMapping("/allowance/{id}")
    public ResponseEntity<?> deleteAllowance(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAllowance(id));
    }
    //=================================
// DEDUCTIONS
//=================================

    @PostMapping("/deduction/{employeeId}")
    public ResponseEntity<?> createDeduction(
            @PathVariable String employeeId,
            @RequestBody Deduction deduction) {

        return ResponseEntity.ok(
                service.createDeduction(employeeId, deduction));
    }

    @GetMapping("/deductions")
    public ResponseEntity<?> getDeductions() {

        return ResponseEntity.ok(
                service.getDeductions());
    }

    @GetMapping("/deduction/{id}")
    public ResponseEntity<?> getDeduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getDeduction(id));
    }

    @PutMapping("/deduction/{id}")
    public ResponseEntity<?> updateDeduction(
            @PathVariable Long id,
            @RequestBody Deduction deduction) {

        return ResponseEntity.ok(
                service.updateDeduction(id, deduction));
    }

    @DeleteMapping("/deduction/{id}")
    public ResponseEntity<?> deleteDeduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDeduction(id));
    }
    //=================================
// REIMBURSEMENTS
//=================================

    @PostMapping("/reimbursement/{employeeId}")
    public ResponseEntity<?> createReimbursement(
            @PathVariable String employeeId,
            @RequestBody Reimbursement reimbursement) {

        return ResponseEntity.ok(
                service.createReimbursement(employeeId, reimbursement));
    }

    @GetMapping("/reimbursements")
    public ResponseEntity<?> getReimbursements() {

        return ResponseEntity.ok(
                service.getReimbursements());
    }

    @GetMapping("/reimbursement/{id}")
    public ResponseEntity<?> getReimbursement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getReimbursement(id));
    }

    @PutMapping("/reimbursement/{id}")
    public ResponseEntity<?> updateReimbursement(
            @PathVariable Long id,
            @RequestBody Reimbursement reimbursement) {

        return ResponseEntity.ok(
                service.updateReimbursement(id, reimbursement));
    }

    @DeleteMapping("/reimbursement/{id}")
    public ResponseEntity<?> deleteReimbursement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteReimbursement(id));
    }
    //=================================
// EMPLOYEE LOANS
//=================================

    @PostMapping("/loan/{employeeId}")
    public ResponseEntity<?> createEmployeeLoan(
            @PathVariable String employeeId,
            @RequestBody EmployeeLoan employeeLoan) {

        return ResponseEntity.ok(
                service.createEmployeeLoan(employeeId, employeeLoan));
    }

    @GetMapping("/loans")
    public ResponseEntity<?> getEmployeeLoans() {

        return ResponseEntity.ok(
                service.getEmployeeLoans());
    }

    @GetMapping("/loan/{id}")
    public ResponseEntity<?> getEmployeeLoan(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEmployeeLoan(id));
    }

    @PutMapping("/loan/{id}")
    public ResponseEntity<?> updateEmployeeLoan(
            @PathVariable Long id,
            @RequestBody EmployeeLoan employeeLoan) {

        return ResponseEntity.ok(
                service.updateEmployeeLoan(id, employeeLoan));
    }

    @DeleteMapping("/loan/{id}")
    public ResponseEntity<?> deleteEmployeeLoan(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteEmployeeLoan(id));
    }
    //=================================
// LOAN INSTALLMENTS
//=================================

    @PostMapping("/loan-installment/{loanId}")
    public ResponseEntity<?> createLoanInstallment(
            @PathVariable Long loanId,
            @RequestBody LoanInstallment loanInstallment) {

        return ResponseEntity.ok(
                service.createLoanInstallment(loanId, loanInstallment));
    }

    @GetMapping("/loan-installments")
    public ResponseEntity<?> getLoanInstallments() {

        return ResponseEntity.ok(
                service.getLoanInstallments());
    }

    @GetMapping("/loan-installment/{id}")
    public ResponseEntity<?> getLoanInstallment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLoanInstallment(id));
    }

    @PutMapping("/loan-installment/{id}")
    public ResponseEntity<?> updateLoanInstallment(
            @PathVariable Long id,
            @RequestBody LoanInstallment loanInstallment) {

        return ResponseEntity.ok(
                service.updateLoanInstallment(id, loanInstallment));
    }

    @DeleteMapping("/loan-installment/{id}")
    public ResponseEntity<?> deleteLoanInstallment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLoanInstallment(id));
    }
    //=================================
    // PAYROLL HISTORY
    //=================================

    @PostMapping("/history/{employeeId}")
    public ResponseEntity<?> createPayrollHistory(
            @PathVariable String employeeId,
            @RequestBody PayrollHistory payrollHistory) {

        return ResponseEntity.ok(
                service.createPayrollHistory(employeeId, payrollHistory));
    }

    @GetMapping("/histories")
    public ResponseEntity<?> getPayrollHistories() {

        return ResponseEntity.ok(
                service.getPayrollHistories());
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getPayrollHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPayrollHistory(id));
    }

    @PutMapping("/history/{id}")
    public ResponseEntity<?> updatePayrollHistory(
            @PathVariable Long id,
            @RequestBody PayrollHistory payrollHistory) {

        return ResponseEntity.ok(
                service.updatePayrollHistory(id, payrollHistory));
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deletePayrollHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePayrollHistory(id));
    }

}
