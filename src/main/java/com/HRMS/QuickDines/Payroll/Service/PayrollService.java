package com.HRMS.QuickDines.Payroll.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Payroll.model.*;
import com.HRMS.QuickDines.Payroll.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
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

    public String generateSalarySlip(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));


        Salaries salary = (Salaries) salariesRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Salary Not Found"));

        SalarySlips salarySlip = new SalarySlips();

        salarySlip.setEmployee(employee);

        salarySlip.setSalaries(salary);

        salarySlip.setSalaryMonth(Month.values().toString());

        salarySlip.setSalaryYear(Year.now().getValue());

        // Cloudinary URL or PDF URL
        salarySlip.setSalarySlipUrl("Salary Slip URL");

        salarySlipsRepository.save(salarySlip);
        return "Salary Slip Generated Successfully";
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

}
