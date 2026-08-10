package com.HRMS.QuickDines.Payroll.Controller;

import com.HRMS.QuickDines.Payroll.Service.PayrollService;
import com.HRMS.QuickDines.Payroll.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService service;


    // =========================================================
    // SALARIES
    // =========================================================

    @PreAuthorize("hasAuthority('SALARY_CREATE')")
    @PostMapping("/salary/{employeeId}")
    public ResponseEntity<?> createSalary(
            @PathVariable String employeeId,
            @RequestBody Salaries salary) {

        return ResponseEntity.ok(
                service.createSalary(employeeId, salary));
    }


    @PreAuthorize("hasAuthority('SALARY_READ')")
    @GetMapping("/salaries")
    public ResponseEntity<?> getAllSalaries() {

        return ResponseEntity.ok(
                service.getAllSalaries());
    }


    @PreAuthorize("hasAuthority('SALARY_READ')")
    @GetMapping("/salary/{employeeId}")
    public ResponseEntity<?> getSalary(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getSalary(employeeId));
    }


    @PreAuthorize("hasAuthority('SALARY_UPDATE')")
    @PutMapping("/salary/{id}")
    public ResponseEntity<?> updateSalary(
            @PathVariable Long id,
            @RequestBody Salaries salary) {

        return ResponseEntity.ok(
                service.updateSalary(id, salary));
    }


    @PreAuthorize("hasAuthority('SALARY_DELETE')")
    @DeleteMapping("/salary/{id}")
    public ResponseEntity<?> deleteSalary(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSalary(id));
    }


    // =========================================================
    // SALARY SLIPS
    // =========================================================

    @PreAuthorize("hasAuthority('SALARY_SLIP_CREATE')")
    @PostMapping("/salary-slip/{employeeId}")
    public ResponseEntity<?> generateSalarySlip(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.generateSalarySlip(employeeId));
    }


    @PreAuthorize("hasAuthority('SALARY_SLIP_READ')")
    @GetMapping("/salary-slip/{employeeId}")
    public ResponseEntity<?> getSalarySlip(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getSalarySlip(employeeId));
    }


    @PreAuthorize("hasAuthority('SALARY_SLIP_DELETE')")
    @DeleteMapping("/salary-slip/{id}")
    public ResponseEntity<?> deleteSalarySlip(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSalarySlip(id));
    }


    // =========================================================
    // PF DETAILS
    // =========================================================

    @PreAuthorize("hasAuthority('PF_CREATE')")
    @PostMapping("/pf/{employeeId}")
    public ResponseEntity<?> createPfDetails(
            @PathVariable String employeeId,
            @RequestBody PfDetails pfDetails) {

        return ResponseEntity.ok(
                service.createPfDetails(employeeId, pfDetails));
    }


    @PreAuthorize("hasAuthority('PF_READ')")
    @GetMapping("/pf/{employeeId}")
    public ResponseEntity<?> getPfDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getPfDetails(employeeId));
    }


    @PreAuthorize("hasAuthority('PF_UPDATE')")
    @PutMapping("/pf/{employeeId}")
    public ResponseEntity<?> updatePfDetails(
            @PathVariable String employeeId,
            @RequestBody PfDetails pfDetails) {

        return ResponseEntity.ok(
                service.updatePfDetails(employeeId, pfDetails));
    }


    // =========================================================
    // ESI DETAILS
    // =========================================================

    @PreAuthorize("hasAuthority('ESI_CREATE')")
    @PostMapping("/esi/{employeeId}")
    public ResponseEntity<?> createEsiDetails(
            @PathVariable String employeeId,
            @RequestBody EsiDetails esiDetails) {

        return ResponseEntity.ok(
                service.createEsiDetails(employeeId, esiDetails));
    }


    @PreAuthorize("hasAuthority('ESI_READ')")
    @GetMapping("/esi/{employeeId}")
    public ResponseEntity<?> getEsiDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEsiDetails(employeeId));
    }


    @PreAuthorize("hasAuthority('ESI_UPDATE')")
    @PutMapping("/esi/{employeeId}")
    public ResponseEntity<?> updateEsiDetails(
            @PathVariable String employeeId,
            @RequestBody EsiDetails esiDetails) {

        return ResponseEntity.ok(
                service.updateEsiDetails(employeeId, esiDetails));
    }


    // =========================================================
    // TDS DETAILS
    // =========================================================

    @PreAuthorize("hasAuthority('TDS_CREATE')")
    @PostMapping("/tds/{employeeId}")
    public ResponseEntity<?> createTdsDetails(
            @PathVariable String employeeId,
            @RequestBody TdsDetails tdsDetails) {

        return ResponseEntity.ok(
                service.createTdsDetails(employeeId, tdsDetails));
    }


    @PreAuthorize("hasAuthority('TDS_READ')")
    @GetMapping("/tds/{employeeId}")
    public ResponseEntity<?> getTdsDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getTdsDetails(employeeId));
    }


    @PreAuthorize("hasAuthority('TDS_UPDATE')")
    @PutMapping("/tds/{employeeId}")
    public ResponseEntity<?> updateTdsDetails(
            @PathVariable String employeeId,
            @RequestBody TdsDetails tdsDetails) {

        return ResponseEntity.ok(
                service.updateTdsDetails(employeeId, tdsDetails));
    }


    // =========================================================
    // INCREMENTS
    // =========================================================

    @PreAuthorize("hasAuthority('INCREMENT_CREATE')")
    @PostMapping("/increment/{employeeId}")
    public ResponseEntity<?> createIncrement(
            @PathVariable String employeeId,
            @RequestBody Increments increment) {

        return ResponseEntity.ok(
                service.createIncrement(employeeId, increment));
    }


    @PreAuthorize("hasAuthority('INCREMENT_READ')")
    @GetMapping("/increment/{employeeId}")
    public ResponseEntity<?> getIncrement(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getIncrement(employeeId));
    }


    // =========================================================
    // BONUS MANAGEMENT
    // =========================================================

    @PreAuthorize("hasAuthority('BONUS_CREATE')")
    @PostMapping("/bonus/{employeeId}")
    public ResponseEntity<?> createBonus(
            @PathVariable String employeeId,
            @RequestBody BonusManagement bonusManagement) {

        return ResponseEntity.ok(
                service.createBonus(employeeId, bonusManagement));
    }


    @PreAuthorize("hasAuthority('BONUS_READ')")
    @GetMapping("/bonus/{employeeId}")
    public ResponseEntity<?> getBonus(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getBonus(employeeId));
    }


    @PreAuthorize("hasAuthority('BONUS_UPDATE')")
    @PutMapping("/bonus/{id}")
    public ResponseEntity<?> updateBonus(
            @PathVariable Long id,
            @RequestBody BonusManagement bonusManagement) {

        return ResponseEntity.ok(
                service.updateBonus(id, bonusManagement));
    }


    // =========================================================
    // PAYROLL REPORTS
    // =========================================================

    @PreAuthorize("hasAuthority('PAYROLL_REPORT_READ')")
    @GetMapping("/monthly-report")
    public ResponseEntity<?> getMonthlyPayrollReport() {

        return ResponseEntity.ok(
                service.getMonthlyPayrollReport());
    }


    @PreAuthorize("hasAuthority('PAYROLL_REPORT_READ')")
    @GetMapping("/salary-report/{employeeId}")
    public ResponseEntity<?> getSalaryReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getSalaryReport(employeeId));
    }


    @PreAuthorize("hasAuthority('PAYROLL_REPORT_READ')")
    @GetMapping("/pf-report/{employeeId}")
    public ResponseEntity<?> getPfReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getPfReport(employeeId));
    }


    @PreAuthorize("hasAuthority('PAYROLL_REPORT_READ')")
    @GetMapping("/tds-report/{employeeId}")
    public ResponseEntity<?> getTdsReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getTdsReport(employeeId));
    }


    // =========================================================
    // SALARY COMPONENTS
    // =========================================================

    @PreAuthorize("hasAuthority('SALARY_COMPONENT_CREATE')")
    @PostMapping("/component/{companyId}")
    public ResponseEntity<?> createSalaryComponent(
            @PathVariable Long companyId,
            @RequestBody SalaryComponent salaryComponent) {

        return ResponseEntity.ok(
                service.createSalaryComponent(
                        companyId,
                        salaryComponent));
    }


    @PreAuthorize("hasAuthority('SALARY_COMPONENT_READ')")
    @GetMapping("/components")
    public ResponseEntity<?> getSalaryComponents() {

        return ResponseEntity.ok(
                service.getSalaryComponents());
    }


    @PreAuthorize("hasAuthority('SALARY_COMPONENT_READ')")
    @GetMapping("/component/{id}")
    public ResponseEntity<?> getSalaryComponent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSalaryComponent(id));
    }


    @PreAuthorize("hasAuthority('SALARY_COMPONENT_UPDATE')")
    @PutMapping("/component/{id}")
    public ResponseEntity<?> updateSalaryComponent(
            @PathVariable Long id,
            @RequestBody SalaryComponent salaryComponent) {

        return ResponseEntity.ok(
                service.updateSalaryComponent(
                        id,
                        salaryComponent));
    }


    @PreAuthorize("hasAuthority('SALARY_COMPONENT_DELETE')")
    @DeleteMapping("/component/{id}")
    public ResponseEntity<?> deleteSalaryComponent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSalaryComponent(id));
    }


    // =========================================================
    // ALLOWANCES
    // =========================================================

    @PreAuthorize("hasAuthority('ALLOWANCE_CREATE')")
    @PostMapping("/allowance/{employeeId}")
    public ResponseEntity<?> createAllowance(
            @PathVariable String employeeId,
            @RequestBody Allowance allowance) {

        return ResponseEntity.ok(
                service.createAllowance(
                        employeeId,
                        allowance));
    }


    @PreAuthorize("hasAuthority('ALLOWANCE_READ')")
    @GetMapping("/allowances")
    public ResponseEntity<?> getAllowances() {

        return ResponseEntity.ok(
                service.getAllowances());
    }


    @PreAuthorize("hasAuthority('ALLOWANCE_READ')")
    @GetMapping("/allowance/{id}")
    public ResponseEntity<?> getAllowance(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAllowance(id));
    }


    @PreAuthorize("hasAuthority('ALLOWANCE_UPDATE')")
    @PutMapping("/allowance/{id}")
    public ResponseEntity<?> updateAllowance(
            @PathVariable Long id,
            @RequestBody Allowance allowance) {

        return ResponseEntity.ok(
                service.updateAllowance(
                        id,
                        allowance));
    }


    @PreAuthorize("hasAuthority('ALLOWANCE_DELETE')")
    @DeleteMapping("/allowance/{id}")
    public ResponseEntity<?> deleteAllowance(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAllowance(id));
    }


    // =========================================================
    // DEDUCTIONS
    // =========================================================

    @PreAuthorize("hasAuthority('DEDUCTION_CREATE')")
    @PostMapping("/deduction/{employeeId}")
    public ResponseEntity<?> createDeduction(
            @PathVariable String employeeId,
            @RequestBody Deduction deduction) {

        return ResponseEntity.ok(
                service.createDeduction(
                        employeeId,
                        deduction));
    }


    @PreAuthorize("hasAuthority('DEDUCTION_READ')")
    @GetMapping("/deductions")
    public ResponseEntity<?> getDeductions() {

        return ResponseEntity.ok(
                service.getDeductions());
    }


    @PreAuthorize("hasAuthority('DEDUCTION_READ')")
    @GetMapping("/deduction/{id}")
    public ResponseEntity<?> getDeduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getDeduction(id));
    }


    @PreAuthorize("hasAuthority('DEDUCTION_UPDATE')")
    @PutMapping("/deduction/{id}")
    public ResponseEntity<?> updateDeduction(
            @PathVariable Long id,
            @RequestBody Deduction deduction) {

        return ResponseEntity.ok(
                service.updateDeduction(
                        id,
                        deduction));
    }


    @PreAuthorize("hasAuthority('DEDUCTION_DELETE')")
    @DeleteMapping("/deduction/{id}")
    public ResponseEntity<?> deleteDeduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDeduction(id));
    }


    // =========================================================
    // REIMBURSEMENTS
    // =========================================================

    @PreAuthorize("hasAuthority('REIMBURSEMENT_CREATE')")
    @PostMapping("/reimbursement/{employeeId}")
    public ResponseEntity<?> createReimbursement(
            @PathVariable String employeeId,
            @RequestBody Reimbursement reimbursement) {

        return ResponseEntity.ok(
                service.createReimbursement(
                        employeeId,
                        reimbursement));
    }


    @PreAuthorize("hasAuthority('REIMBURSEMENT_READ')")
    @GetMapping("/reimbursements")
    public ResponseEntity<?> getReimbursements() {

        return ResponseEntity.ok(
                service.getReimbursements());
    }


    @PreAuthorize("hasAuthority('REIMBURSEMENT_READ')")
    @GetMapping("/reimbursement/{id}")
    public ResponseEntity<?> getReimbursement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getReimbursement(id));
    }


    @PreAuthorize("hasAuthority('REIMBURSEMENT_UPDATE')")
    @PutMapping("/reimbursement/{id}")
    public ResponseEntity<?> updateReimbursement(
            @PathVariable Long id,
            @RequestBody Reimbursement reimbursement) {

        return ResponseEntity.ok(
                service.updateReimbursement(
                        id,
                        reimbursement));
    }


    @PreAuthorize("hasAuthority('REIMBURSEMENT_DELETE')")
    @DeleteMapping("/reimbursement/{id}")
    public ResponseEntity<?> deleteReimbursement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteReimbursement(id));
    }


    // =========================================================
    // EMPLOYEE LOANS
    // =========================================================

    @PreAuthorize("hasAuthority('LOAN_CREATE')")
    @PostMapping("/loan/{employeeId}")
    public ResponseEntity<?> createEmployeeLoan(
            @PathVariable String employeeId,
            @RequestBody EmployeeLoan employeeLoan) {

        return ResponseEntity.ok(
                service.createEmployeeLoan(
                        employeeId,
                        employeeLoan));
    }


    @PreAuthorize("hasAuthority('LOAN_READ')")
    @GetMapping("/loans")
    public ResponseEntity<?> getEmployeeLoans() {

        return ResponseEntity.ok(
                service.getEmployeeLoans());
    }


    @PreAuthorize("hasAuthority('LOAN_READ')")
    @GetMapping("/loan/{id}")
    public ResponseEntity<?> getEmployeeLoan(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getEmployeeLoan(id));
    }


    @PreAuthorize("hasAuthority('LOAN_UPDATE')")
    @PutMapping("/loan/{id}")
    public ResponseEntity<?> updateEmployeeLoan(
            @PathVariable Long id,
            @RequestBody EmployeeLoan employeeLoan) {

        return ResponseEntity.ok(
                service.updateEmployeeLoan(
                        id,
                        employeeLoan));
    }


    @PreAuthorize("hasAuthority('LOAN_DELETE')")
    @DeleteMapping("/loan/{id}")
    public ResponseEntity<?> deleteEmployeeLoan(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteEmployeeLoan(id));
    }


    // =========================================================
    // LOAN INSTALLMENTS
    // =========================================================

    @PreAuthorize("hasAuthority('LOAN_INSTALLMENT_CREATE')")
    @PostMapping("/loan-installment/{loanId}")
    public ResponseEntity<?> createLoanInstallment(
            @PathVariable Long loanId,
            @RequestBody LoanInstallment loanInstallment) {

        return ResponseEntity.ok(
                service.createLoanInstallment(
                        loanId,
                        loanInstallment));
    }


    @PreAuthorize("hasAuthority('LOAN_INSTALLMENT_READ')")
    @GetMapping("/loan-installments")
    public ResponseEntity<?> getLoanInstallments() {

        return ResponseEntity.ok(
                service.getLoanInstallments());
    }


    @PreAuthorize("hasAuthority('LOAN_INSTALLMENT_READ')")
    @GetMapping("/loan-installment/{id}")
    public ResponseEntity<?> getLoanInstallment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLoanInstallment(id));
    }


    @PreAuthorize("hasAuthority('LOAN_INSTALLMENT_UPDATE')")
    @PutMapping("/loan-installment/{id}")
    public ResponseEntity<?> updateLoanInstallment(
            @PathVariable Long id,
            @RequestBody LoanInstallment loanInstallment) {

        return ResponseEntity.ok(
                service.updateLoanInstallment(
                        id,
                        loanInstallment));
    }


    @PreAuthorize("hasAuthority('LOAN_INSTALLMENT_DELETE')")
    @DeleteMapping("/loan-installment/{id}")
    public ResponseEntity<?> deleteLoanInstallment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLoanInstallment(id));
    }


    // =========================================================
    // PAYROLL HISTORY
    // =========================================================

    @PreAuthorize("hasAuthority('PAYROLL_HISTORY_CREATE')")
    @PostMapping("/history/{employeeId}")
    public ResponseEntity<?> createPayrollHistory(
            @PathVariable String employeeId,
            @RequestBody PayrollHistory payrollHistory) {

        return ResponseEntity.ok(
                service.createPayrollHistory(
                        employeeId,
                        payrollHistory));
    }


    @PreAuthorize("hasAuthority('PAYROLL_HISTORY_READ')")
    @GetMapping("/histories")
    public ResponseEntity<?> getPayrollHistories() {

        return ResponseEntity.ok(
                service.getPayrollHistories());
    }


    @PreAuthorize("hasAuthority('PAYROLL_HISTORY_READ')")
    @GetMapping("/history/{id}")
    public ResponseEntity<?> getPayrollHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPayrollHistory(id));
    }


    @PreAuthorize("hasAuthority('PAYROLL_HISTORY_UPDATE')")
    @PutMapping("/history/{id}")
    public ResponseEntity<?> updatePayrollHistory(
            @PathVariable Long id,
            @RequestBody PayrollHistory payrollHistory) {

        return ResponseEntity.ok(
                service.updatePayrollHistory(
                        id,
                        payrollHistory));
    }


    @PreAuthorize("hasAuthority('PAYROLL_HISTORY_DELETE')")
    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deletePayrollHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePayrollHistory(id));
    }
}