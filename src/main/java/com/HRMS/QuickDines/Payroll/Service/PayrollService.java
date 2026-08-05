package com.HRMS.QuickDines.Payroll.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Payroll.model.*;
import com.HRMS.QuickDines.Payroll.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final SalariesRepository salariesRepository;
    private final EmployeeRepository employeeRepository;
    private final SalarySlipsRepository salarySlipsRepository;
    private final PfDetailsRepository pfDetailsRepository;
    private final EsiDetailsRepository esiDetailsRepository;
    private final TdsDetailsRepository tdsDetailsRepository;
    private final IncrementRepository incrementRepository;
    private final BonusManagementRepository bonusManagementRepository;
    private final SalaryComponentRepository salaryComponentRepository;
    private final AllowanceRepository allowanceRepository;
    private final DeductionRepository deductionRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final EmployeeLoanRepository employeeLoanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final PayrollHistoryRepository payrollHistoryRepository;
    //=================================
    // SALARIES
    //=================================

    public String createSalary(String employeeId, Salaries salary){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        salary.setEmployee(employee);

        Double netSalary = salary.getBasicSalary()
                        + salary.getHra()
                        + salary.getAllowances()
                        + salary.getBonus()
                        + salary.getIncentives()
                        - salary.getDeductions();

        salary.setNetSalary(netSalary);

        salariesRepository.save(salary);

        return "Salary Created Successfully";
    }

    public Object getAllSalaries(){
        return salariesRepository.findAll();
    }
    public Object getSalary(String employeeId){
        return salariesRepository.findByEmployeeEmployeeId(employeeId);
    }

    public String updateSalary(Long id, Salaries salary){

        Salaries existingSalary = salariesRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Not Found"));

        existingSalary.setBasicSalary(salary.getBasicSalary());

        existingSalary.setHra(salary.getHra());

        existingSalary.setAllowances(salary.getAllowances());

        existingSalary.setBonus(salary.getBonus());

        existingSalary.setIncentives(salary.getIncentives());

        existingSalary.setDeductions(salary.getDeductions());

        Double netSalary = salary.getBasicSalary()
                        + salary.getHra()
                        + salary.getAllowances()
                        + salary.getBonus()
                        + salary.getIncentives()
                        - salary.getDeductions();
        existingSalary.setNetSalary(netSalary);

        salariesRepository.save(existingSalary);

        return "Salary Updated Successfully";
    }

    public String deleteSalary(Long id){

        Salaries salary = salariesRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Not Found"));
        salariesRepository.delete(salary);
        return "Salary Deleted Successfully";
    }



    //=================================
    // SALARY SLIPS
    //=================================

    public String generateSalarySlip(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Salaries salary = salariesRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Salary Not Found"));

        SalarySlips salarySlip = new SalarySlips();

        salarySlip.setEmployee(employee);
        salarySlip.setSalaries(salary);

        // Set month/year
        salarySlip.setSalaryMonth(String.valueOf(YearMonth.now().getMonthValue()));

        salarySlip.setSalaryYear(YearMonth.now().getYear());

        salarySlipsRepository.save(salarySlip);

        // Create payroll history
        PayrollHistory history = new PayrollHistory();

        history.setEmployee(employee);
        history.setSalary(salary);
        history.setSalarySlip(salarySlip);

        history.setPayrollMonth(String.valueOf(YearMonth.now().getMonthValue()));

        history.setPayrollYear(YearMonth.now().getYear());

       // history.setGrossSalary(calculateGrossSalary(salary));

        history.setNetSalary(BigDecimal.valueOf(salary.getNetSalary()));

        history.setProcessedAt(LocalDateTime.now());

        payrollHistoryRepository.save(history);

        return "Salary Slip Generated and Payroll History Saved Successfully";
    }

    public Object getSalarySlip(
            String employeeId){

        return salarySlipsRepository.findByEmployeeEmployeeId(employeeId);
    }

    public String deleteSalarySlip(Long id){
        SalarySlips salarySlip = salarySlipsRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Slip Not Found"));
        salarySlipsRepository.delete(salarySlip);
        return "Salary Slip Deleted Successfully";
    }



    //=================================
    // PF DETAILS
    //=================================


    public String createPfDetails(String employeeId, PfDetails pfDetails){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        if (pfDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException(
                    "PF Details Already Exists");
        }
        pfDetails.setEmployee(employee);
        Double totalPf = pfDetails.getEmployeePf() + pfDetails.getEmployerPf();
        pfDetails.setTotalPf(totalPf);
        pfDetailsRepository.save(pfDetails);
        return "PF Details Created Successfully";
    }

    public Object getPfDetails(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return pfDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("PF Details Not Found"));
    }

    public String updatePfDetails(String employeeId, PfDetails pfDetails){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        PfDetails existingPf = pfDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("PF Details Not Found"));

        existingPf.setUanNumber(pfDetails.getUanNumber());

        existingPf.setPfNumber(pfDetails.getPfNumber());

        existingPf.setEmployeePf(pfDetails.getEmployeePf());

        existingPf.setEmployerPf(pfDetails.getEmployerPf());

        Double totalPf = pfDetails.getEmployeePf()
                        + pfDetails.getEmployerPf();

        existingPf.setTotalPf(totalPf);
        pfDetailsRepository.save(existingPf);

        return "PF Details Updated Successfully";
    }



    //=================================
    // ESI DETAILS
    //=================================


    public String createEsiDetails(String  employeeId, EsiDetails esiDetails){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        if (esiDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException(
                    "ESI Details Already Exists");
        }
        esiDetails.setEmployee(employee);

        esiDetailsRepository.save(esiDetails);

        return "ESI Details Created Successfully";
    }

    public Object getEsiDetails(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return esiDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("ESI Details Not Found"));
    }

    public String updateEsiDetails(String employeeId,EsiDetails esiDetails){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        EsiDetails existingEsi = esiDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("ESI Details Not Found"));
        existingEsi.setEsiNumber(esiDetails.getEsiNumber());
        existingEsi.setEmployeeContribution(esiDetails.getEmployeeContribution());

        existingEsi.setEmployerContribution(esiDetails.getEmployerContribution());
        esiDetailsRepository.save(existingEsi);
        return "ESI Details Updated Successfully";
    }



    //=================================
    // TDS DETAILS
    //=================================


    public String createTdsDetails(String employeeId, TdsDetails tdsDetails){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        if (tdsDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException(
                    "TDS Details Already Exists");
        }
        tdsDetails.setEmployee(employee);

        tdsDetailsRepository.save(tdsDetails);

        return "TDS Details Created Successfully";
    }

    public Object getTdsDetails(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return tdsDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("TDS Details Not Found"));
    }

    public String updateTdsDetails(String employeeId, TdsDetails tdsDetails){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        TdsDetails existingTds = tdsDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("TDS Details Not Found"));
        existingTds.setFinancialYear(tdsDetails.getFinancialYear());
        existingTds.setTotalTax(tdsDetails.getTotalTax());
        existingTds.setMonthlyTax(tdsDetails.getMonthlyTax());
        tdsDetailsRepository.save(existingTds);
        return "TDS Details Updated Successfully";
    }



    //=================================
// INCREMENTS
//=================================

    public String createIncrement(String employeeId, Increments increment){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        Salaries salary = (Salaries) salariesRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Salary Not Found"));
        Double previousSalary = salary.getNetSalary();

        Double incrementPercentage = increment.getIncrementPercentage();

        Double incrementAmount = previousSalary * incrementPercentage / 100;

        Double currentSalary = previousSalary + incrementAmount;
        // Save Increment History

        increment.setEmployee(employee);

        increment.setPreviousSalary(previousSalary);

        increment.setCurrentSalary(currentSalary);

        incrementRepository.save(increment);
        // Update Salary Table

        salary.setNetSalary(currentSalary);
        salariesRepository.save(salary);
        return "Increment Added Successfully";
    }
    public Object getIncrement(String employeeId){

        return incrementRepository.findByEmployeeEmployeeId(employeeId);
    }

    //=================================
    // BONUS MANAGEMENT
    //=================================


    public String createBonus(String employeeId, BonusManagement bonusManagement){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        bonusManagement.setEmployee(employee);
        bonusManagementRepository.save(bonusManagement);


        // Optional:
        // Update employee salary bonus amount.

        Salaries salary = (Salaries) salariesRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Salary Not Found"));
        Double totalBonus = bonusManagement.getBonusAmount() + bonusManagement.getFestivalBonus();
        Double totalIncentive = bonusManagement.getIncentiveAmount();
        salary.setBonus(totalBonus);
        salary.setIncentives(totalIncentive);
        Double netSalary =
                salary.getBasicSalary()
                        + salary.getHra()
                        + salary.getAllowances()
                        + salary.getBonus()
                        + salary.getIncentives()
                        - salary.getDeductions();

        salary.setNetSalary(netSalary);

        salariesRepository.save(salary);
        return "Bonus Added Successfully";
    }

    public Object getBonus(String employeeId){

        return bonusManagementRepository.findByEmployeeEmployeeId(employeeId);
    }

    public String updateBonus(Long id, BonusManagement bonusManagement){

        BonusManagement existingBonus = bonusManagementRepository.findById(id).orElseThrow(() -> new RuntimeException("Bonus Details Not Found"));
        existingBonus.setBonusAmount(bonusManagement.getBonusAmount());

        existingBonus.setFestivalBonus(bonusManagement.getFestivalBonus());

        existingBonus.setIncentiveAmount(bonusManagement.getIncentiveAmount());

        existingBonus.setRemarks(bonusManagement.getRemarks());
        bonusManagementRepository.save(existingBonus);
        return "Bonus Details Updated Successfully";
    }



    //=================================
    // REPORTS
    //=================================

    //=================================
// MONTHLY PAYROLL REPORT
//=================================

    public Object getMonthlyPayrollReport() {

        List<Salaries> salaries = salariesRepository.findAll();
        List<PfDetails> pfDetails = pfDetailsRepository.findAll();
        List<TdsDetails> tdsDetails = tdsDetailsRepository.findAll();

        Double totalBasicSalary = 0.0;
        Double totalHra = 0.0;
        Double totalAllowances = 0.0;
        Double totalBonus = 0.0;
        Double totalIncentives = 0.0;
        Double totalDeductions = 0.0;
        Double totalNetSalary = 0.0;
        Double totalPfAmount = 0.0;
        Double totalTdsAmount = 0.0;


        // Salary Calculations

        for (Salaries salary : salaries) {
            totalBasicSalary += salary.getBasicSalary();
            totalHra += salary.getHra();
            totalAllowances += salary.getAllowances();
            totalBonus += salary.getBonus();
            totalIncentives += salary.getIncentives();
            totalDeductions += salary.getDeductions();
            totalNetSalary += salary.getNetSalary();

        }


        // PF Calculations

        for (PfDetails pf : pfDetails) {totalPfAmount += pf.getTotalPf();}

        // TDS Calculations

        for (TdsDetails tds : tdsDetails) {totalTdsAmount += tds.getTotalTax();}
        Map<String, Object> report = new HashMap<>();

        report.put("Payroll Month", Month.values().toString());
        report.put("Payroll Year", Year.now().getValue());

        report.put("Total Employees Paid", salaries.size());

        report.put("Total Basic Salary", totalBasicSalary);
        report.put("Total HRA", totalHra);
        report.put("Total Allowances", totalAllowances);
        report.put("Total Bonus Amount", totalBonus);
        report.put("Total Incentives", totalIncentives);
        report.put("Total Deductions", totalDeductions);
        report.put("Total PF Amount", totalPfAmount);
        report.put("Total TDS Amount", totalTdsAmount);
        report.put("Total Net Salary Paid", totalNetSalary);

        return report;
    }

    public Object getSalaryReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return salariesRepository.findByEmployee(employee).orElseThrow(() ->new RuntimeException("Salary Details Not Found"));
    }


    public Object getPfReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return pfDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("PF Details Not Found"));
    }

    public Object getTdsReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return tdsDetailsRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("TDS Details Not Found"));
    }
    //=================================
// SALARY COMPONENTS
//=================================

    public String createSalaryComponent(
            Long companyId,
            SalaryComponent salaryComponent) {

        // Find company here using your CompanyRepository
        // Company company = companyRepository.findById(companyId)
        //         .orElseThrow(() ->
        //                 new RuntimeException("Company Not Found"));
        // salaryComponent.setCompany(company);


        salaryComponentRepository.save(salaryComponent);

        return "Salary Component Created Successfully";
    }

    public List<SalaryComponent> getSalaryComponents() {

        return salaryComponentRepository.findAll();
    }

    public SalaryComponent getSalaryComponent(Long id) {

        return salaryComponentRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));
    }

    public String updateSalaryComponent(Long id,SalaryComponent salaryComponent) {

        SalaryComponent existing = salaryComponentRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

        existing.setComponentName(salaryComponent.getComponentName());

        existing.setComponentCode(salaryComponent.getComponentCode());

        existing.setComponentType(salaryComponent.getComponentType());

        existing.setTaxable(salaryComponent.getTaxable());

        existing.setCalculationType(salaryComponent.getCalculationType());

        existing.setDefaultValue(salaryComponent.getDefaultValue());

        existing.setStatus(salaryComponent.getStatus());

        salaryComponentRepository.save(existing);

        return "Salary Component Updated Successfully";
    }

    public String deleteSalaryComponent(Long id) {

        SalaryComponent existing = salaryComponentRepository.findById(id).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

        salaryComponentRepository.delete(existing);

        return "Salary Component Deleted Successfully";
    }
    //=================================
// ALLOWANCES
//=================================

    public String createAllowance(String employeeId, Allowance allowance) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalaryComponent component = salaryComponentRepository.findById(allowance.getSalaryComponent().getId()).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

        allowance.setEmployee(employee);
        allowance.setSalaryComponent(component);

        allowanceRepository.save(allowance);

        return "Allowance Created Successfully";
    }

    public List<Allowance> getAllowances() {

        return allowanceRepository.findAll();
    }

    public Allowance getAllowance(Long id) {

        return allowanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Allowance Not Found"));
    }

    public String updateAllowance(Long id, Allowance allowance) {

        Allowance existing = allowanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Allowance Not Found"));

        existing.setAllowanceType(allowance.getAllowanceType());

        existing.setAmount(allowance.getAmount());

        existing.setEffectiveFrom(allowance.getEffectiveFrom());

        existing.setEffectiveTo(allowance.getEffectiveTo());

        existing.setRemarks(allowance.getRemarks());

        existing.setStatus(allowance.getStatus());

        if (allowance.getSalaryComponent() != null) {

            SalaryComponent component = salaryComponentRepository.findById(allowance.getSalaryComponent().getId()).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

            existing.setSalaryComponent(component);
        }

        allowanceRepository.save(existing);

        return "Allowance Updated Successfully";
    }

    public String deleteAllowance(Long id) {

        Allowance existing = allowanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Allowance Not Found"));

        allowanceRepository.delete(existing);

        return "Allowance Deleted Successfully";
    }
    //=================================
// DEDUCTIONS
//=================================

    public String createDeduction(String employeeId, Deduction deduction) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        SalaryComponent component = salaryComponentRepository.findById(deduction.getSalaryComponent().getId()).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

        deduction.setEmployee(employee);
        deduction.setSalaryComponent(component);

        deductionRepository.save(deduction);

        return "Deduction Created Successfully";
    }

    public List<Deduction> getDeductions() {

        return deductionRepository.findAll();
    }

    public Deduction getDeduction(Long id) {

        return deductionRepository.findById(id).orElseThrow(() -> new RuntimeException("Deduction Not Found"));
    }

    public String updateDeduction(Long id, Deduction deduction) {

        Deduction existing = deductionRepository.findById(id).orElseThrow(() -> new RuntimeException("Deduction Not Found"));

        existing.setDeductionType(deduction.getDeductionType());

        existing.setAmount(deduction.getAmount());

        existing.setDeductionDate(deduction.getDeductionDate());

        existing.setRemarks(deduction.getRemarks());

        existing.setStatus(deduction.getStatus());

        if (deduction.getSalaryComponent() != null) {

            SalaryComponent component = salaryComponentRepository.findById(deduction.getSalaryComponent().getId()).orElseThrow(() -> new RuntimeException("Salary Component Not Found"));

            existing.setSalaryComponent(component);
        }

        deductionRepository.save(existing);

        return "Deduction Updated Successfully";
    }

    public String deleteDeduction(Long id) {

        Deduction existing = deductionRepository.findById(id).orElseThrow(() -> new RuntimeException("Deduction Not Found"));

        deductionRepository.delete(existing);

        return "Deduction Deleted Successfully";
    }
    //=================================
// REIMBURSEMENTS
//=================================

    public String createReimbursement(String employeeId, Reimbursement reimbursement) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        reimbursement.setEmployee(employee);

        if (reimbursement.getStatus() == null) {
            reimbursement.setStatus("PENDING");
        }

        reimbursementRepository.save(reimbursement);

        return "Reimbursement Created Successfully";
    }

    public List<Reimbursement> getReimbursements() {

        return reimbursementRepository.findAll();
    }

    public Reimbursement getReimbursement(Long id) {

        return reimbursementRepository.findById(id).orElseThrow(() -> new RuntimeException("Reimbursement Not Found"));
    }

    public String updateReimbursement(Long id, Reimbursement reimbursement) {

        Reimbursement existing = reimbursementRepository.findById(id).orElseThrow(() -> new RuntimeException("Reimbursement Not Found"));

        existing.setReimbursementType(reimbursement.getReimbursementType());

        existing.setExpenseDate(reimbursement.getExpenseDate());

        existing.setClaimedAmount(reimbursement.getClaimedAmount());

        existing.setApprovedAmount(reimbursement.getApprovedAmount());

        existing.setStatus(reimbursement.getStatus());

        existing.setRemarks(reimbursement.getRemarks());

        reimbursementRepository.save(existing);

        return "Reimbursement Updated Successfully";
    }

    public String deleteReimbursement(Long id) {

        Reimbursement existing = reimbursementRepository.findById(id).orElseThrow(() -> new RuntimeException("Reimbursement Not Found"));

        reimbursementRepository.delete(existing);

        return "Reimbursement Deleted Successfully";
    }
    //=================================
// EMPLOYEE LOANS
//=================================

    public String createEmployeeLoan(String employeeId, EmployeeLoan employeeLoan) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeLoan.setEmployee(employee);

        if (employeeLoan.getStatus() == null) {
            employeeLoan.setStatus("PENDING");
        }

        employeeLoanRepository.save(employeeLoan);

        return "Employee Loan Created Successfully";
    }

    public List<EmployeeLoan> getEmployeeLoans() {

        return employeeLoanRepository.findAll();
    }

    public EmployeeLoan getEmployeeLoan(Long id) {

        return employeeLoanRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Loan Not Found"));
    }

    public String updateEmployeeLoan(Long id, EmployeeLoan employeeLoan) {

        EmployeeLoan existing = employeeLoanRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Loan Not Found"));

        existing.setLoanType(employeeLoan.getLoanType());

        existing.setLoanAmount(employeeLoan.getLoanAmount());

        existing.setInterestRate(employeeLoan.getInterestRate());

        existing.setInstallmentAmount(employeeLoan.getInstallmentAmount());

        existing.setTotalInstallments(employeeLoan.getTotalInstallments());

        existing.setApprovedBy(employeeLoan.getApprovedBy());

        existing.setStatus(employeeLoan.getStatus());

        employeeLoanRepository.save(existing);

        return "Employee Loan Updated Successfully";
    }

    public String deleteEmployeeLoan(Long id) {

        EmployeeLoan existing = employeeLoanRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Loan Not Found"));

        employeeLoanRepository.delete(existing);

        return "Employee Loan Deleted Successfully";
    }
    //=================================
// LOAN INSTALLMENTS
//=================================

    public String createLoanInstallment(Long loanId, LoanInstallment loanInstallment) {

        EmployeeLoan loan = employeeLoanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Employee Loan Not Found"));

        loanInstallment.setLoan(loan);

        if (loanInstallment.getPaymentStatus() == null) {
            loanInstallment.setPaymentStatus("PENDING");
        }

        loanInstallmentRepository.save(loanInstallment);

        return "Loan Installment Created Successfully";
    }

    public List<LoanInstallment> getLoanInstallments() {

        return loanInstallmentRepository.findAll();
    }

    public LoanInstallment getLoanInstallment(Long id) {

        return loanInstallmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan Installment Not Found"));
    }

    public String updateLoanInstallment(Long id, LoanInstallment loanInstallment) {

        LoanInstallment existing = loanInstallmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan Installment Not Found"));

        existing.setInstallmentNo(loanInstallment.getInstallmentNo());

        existing.setDueDate(loanInstallment.getDueDate());

        existing.setAmount(loanInstallment.getAmount());

        existing.setPaidAmount(loanInstallment.getPaidAmount());

        existing.setPaymentStatus(loanInstallment.getPaymentStatus());

        if (loanInstallment.getSalary() != null) {
            existing.setSalary(loanInstallment.getSalary());
        }

        loanInstallmentRepository.save(existing);

        return "Loan Installment Updated Successfully";
    }

    public String deleteLoanInstallment(Long id) {

        LoanInstallment existing = loanInstallmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan Installment Not Found"));

        loanInstallmentRepository.delete(existing);

        return "Loan Installment Deleted Successfully";
    }
    //=================================
    // PAYROLL HISTORY
    //=================================

    public String createPayrollHistory(
            String employeeId,
            PayrollHistory payrollHistory) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        payrollHistory.setEmployee(employee);

        if (payrollHistory.getSalary() != null &&
                payrollHistory.getSalary().getId() != null) {

            Salaries salary = salariesRepository.findById(
                            payrollHistory.getSalary().getId()).orElseThrow(() -> new RuntimeException("Salary Not Found"));
            payrollHistory.setSalary(salary);
        }

        if (payrollHistory.getSalarySlip() != null && payrollHistory.getSalarySlip().getId() != null) {

            SalarySlips salarySlip = salarySlipsRepository.findById(payrollHistory.getSalarySlip().getId()).orElseThrow();

            payrollHistory.setSalarySlip(salarySlip);
        }

        payrollHistoryRepository.save(payrollHistory);

        return "Payroll History Created Successfully";
    }
    public List<PayrollHistory> getPayrollHistories() {

        return payrollHistoryRepository.findAll();
    }

    public PayrollHistory getPayrollHistory(Long id) {

        return payrollHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll History Not Found"));
    }

    public String updatePayrollHistory(Long id, PayrollHistory payrollHistory) {

        PayrollHistory existing = payrollHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll History Not Found"));

        existing.setPayrollMonth(payrollHistory.getPayrollMonth());

        existing.setPayrollYear(payrollHistory.getPayrollYear());

        existing.setGrossSalary(payrollHistory.getGrossSalary());

        existing.setNetSalary(payrollHistory.getNetSalary());

        existing.setProcessedAt(payrollHistory.getProcessedAt());

        existing.setProcessedBy(payrollHistory.getProcessedBy());

        payrollHistoryRepository.save(existing);

        return "Payroll History Updated Successfully";
    }

    public String deletePayrollHistory(Long id) {

        PayrollHistory existing = payrollHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll History Not Found"));

        payrollHistoryRepository.delete(existing);

        return "Payroll History Deleted Successfully";
    }

}
