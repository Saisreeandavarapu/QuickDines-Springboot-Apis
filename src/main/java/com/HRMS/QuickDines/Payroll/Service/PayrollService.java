package com.HRMS.QuickDines.Payroll.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Payroll.model.*;
import com.HRMS.QuickDines.Payroll.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CompanyRepository companyRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }

//=================================
// SALARIES
//=================================

    public String createSalary(String employeeId, Salaries salary) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        salary.setEmployee(employee);

        BigDecimal netSalary = salary.getBasicSalary()
                .add(salary.getHra())
                .add(salary.getAllowances())
                .add(salary.getBonus())
                .add(salary.getIncentives())
                .subtract(salary.getDeductions());

        salary.setNetSalary(netSalary);

        salariesRepository.save(salary);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PAYROLL",
                String.valueOf(salary.getId()),
                performedBy,
                salary.getId().toString(),
                "Salary created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_SALARY",
                "PAYROLL",
                "Salary created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "SalaryService",
                "Salary created successfully"
        );

        return "Salary Created Successfully";
    }


    public Object getAllSalaries() {

        return salariesRepository.findAll();
    }


    public Object getSalary(String employeeId) {

        return salariesRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public String updateSalary(Long id, Salaries salary) {

        Salaries existingSalary = salariesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Salary Not Found"));

        // Store old value BEFORE updating
        String oldValue =
                convertToJson(existingSalary);

        existingSalary.setBasicSalary(salary.getBasicSalary());
        existingSalary.setHra(salary.getHra());
        existingSalary.setAllowances(salary.getAllowances());
        existingSalary.setBonus(salary.getBonus());
        existingSalary.setIncentives(salary.getIncentives());
        existingSalary.setDeductions(salary.getDeductions());

        BigDecimal netSalary = salary.getBasicSalary()
                .add(salary.getHra())
                .add(salary.getAllowances())
                .add(salary.getBonus())
                .add(salary.getIncentives())
                .subtract(salary.getDeductions());

        existingSalary.setNetSalary(netSalary);

        salariesRepository.save(existingSalary);

        String newValue =
                convertToJson(existingSalary);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "PAYROLL",
                String.valueOf(id),
                performedBy,
                existingSalary.getEmployee().getEmployeeId(),
                "Salary updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_SALARY",
                "PAYROLL",
                "Salary updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "SalaryService",
                "Salary updated successfully"
        );

        return "Salary Updated Successfully";
    }


    public String deleteSalary(Long id) {

        Salaries salary = salariesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Salary Not Found"));

        // Store deleted value BEFORE deleting
        String deletedValue =
                convertToJson(salary);

        String performedBy =
                getLoggedInEmployeeId();

        salariesRepository.delete(salary);

        auditLogsService.createAuditLog(
                "PAYROLL",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                salary.getEmployee().getEmployeeId(),
                "Salary deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_SALARY",
                "PAYROLL",
                "Salary deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "SalaryService",
                "Salary deleted successfully"
        );

        return "Salary Deleted Successfully";
    }



//=================================
// SALARY SLIPS
//=================================

    public String generateSalarySlip(String employeeId) {

        // 1. Find employee
        Employee employee = employeeRepository
                .findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        // 2. Find employee salary
        Salaries salary = salariesRepository
                .findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Salary Not Found"));

        // 3. Create salary slip
        SalarySlips salarySlip = new SalarySlips();

        salarySlip.setEmployee(employee);
        salarySlip.setSalaries(salary);

        YearMonth currentMonth = YearMonth.now();

        salarySlip.setSalaryMonth(
                String.valueOf(currentMonth.getMonthValue())
        );

        salarySlip.setSalaryYear(
                currentMonth.getYear()
        );

        // 4. Save salary slip first
        salarySlip = salarySlipsRepository.save(salarySlip);

        // 5. Create payroll history
        PayrollHistory history = new PayrollHistory();

        history.setEmployee(employee);
        history.setSalary(salary);
        history.setSalarySlip(salarySlip);

        history.setPayrollMonth(
                String.valueOf(currentMonth.getMonthValue())
        );

        history.setPayrollYear(
                currentMonth.getYear()
        );

        // Gross salary
        history.setGrossSalary(salary.getNetSalary());

        // Net salary
        history.setNetSalary(salary.getNetSalary());

        history.setProcessedAt(LocalDateTime.now());

        // 6. Save payroll history
        payrollHistoryRepository.save(history);


        // 7. Audit
        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PAYROLL",
                String.valueOf(salarySlip.getId()),
                performedBy,
                employee.getEmployeeId(),
                "Salary slip generated successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "GENERATE_SALARY_SLIP",
                "PAYROLL",
                "Salary slip generated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "SalaryService",
                "Salary slip generated successfully"
        );

        return "Salary Slip Generated and Payroll History Saved Successfully";
    }


    public Object getSalarySlip(
            String employeeId) {

        return salarySlipsRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public String deleteSalarySlip(Long id) {

        SalarySlips salarySlip =
                salarySlipsRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Slip Not Found"));

        // Store deleted value BEFORE deleting
        String deletedValue =
                convertToJson(salarySlip);

        String performedBy =
                getLoggedInEmployeeId();

        salarySlipsRepository.delete(salarySlip);

        auditLogsService.createAuditLog(
                "PAYROLL",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                salarySlip.getEmployee().getEmployeeId(),
                "Salary slip deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_SALARY_SLIP",
                "PAYROLL",
                "Salary slip deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "SalaryService",
                "Salary slip deleted successfully"
        );

        return "Salary Slip Deleted Successfully";
    }




//=================================
// PF DETAILS
//=================================

    public String createPfDetails(String employeeId, PfDetails pfDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        if (pfDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException(
                    "PF Details Already Exists");
        }

        pfDetails.setEmployee(employee);

        BigDecimal totalPf = pfDetails.getEmployeePf()
                .add(pfDetails.getEmployerPf());

        pfDetails.setTotalPf(totalPf);

        pfDetailsRepository.save(pfDetails);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PAYROLL",
                String.valueOf(pfDetails.getId()),
                performedBy,
                employeeId,
                "PF Details created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_PF_DETAILS",
                "PAYROLL",
                "PF Details created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "PF Details created successfully"
        );

        return "PF Details Created Successfully";
    }


    public Object getPfDetails(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return pfDetailsRepository.findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("PF Details Not Found"));
    }


    public String updatePfDetails(
            String employeeId,
            PfDetails pfDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        PfDetails existingPf = pfDetailsRepository
                .findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("PF Details Not Found"));

        // Capture old value BEFORE update
        String oldValue = convertToJson(existingPf);

        existingPf.setUanNumber(
                pfDetails.getUanNumber());

        existingPf.setPfNumber(
                pfDetails.getPfNumber());

        existingPf.setEmployeePf(
                pfDetails.getEmployeePf());

        existingPf.setEmployerPf(
                pfDetails.getEmployerPf());

        BigDecimal totalPf = pfDetails.getEmployeePf()
                .add(pfDetails.getEmployerPf());

        existingPf.setTotalPf(totalPf);

        pfDetailsRepository.save(existingPf);

        // Capture new value AFTER update
        String newValue = convertToJson(existingPf);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "PAYROLL",
                String.valueOf(existingPf.getId()),
                performedBy,
                employeeId,
                "PF Details updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_PF_DETAILS",
                "PAYROLL",
                "PF Details updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "PF Details updated successfully"
        );

        return "PF Details Updated Successfully";
    }


//=================================
// ESI DETAILS
//=================================

    public String createEsiDetails(
            String employeeId,
            EsiDetails esiDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        if (esiDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException(
                    "ESI Details Already Exists");
        }

        esiDetails.setEmployee(employee);

        esiDetailsRepository.save(esiDetails);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PAYROLL",
                String.valueOf(esiDetails.getId()),
                performedBy,
                employeeId,
                "ESI Details created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ESI_DETAILS",
                "PAYROLL",
                "ESI Details created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "ESI Details created successfully"
        );

        return "ESI Details Created Successfully";
    }


    public Object getEsiDetails(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return esiDetailsRepository.findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("ESI Details Not Found"));
    }


    public String updateEsiDetails(
            String employeeId,
            EsiDetails esiDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        EsiDetails existingEsi = esiDetailsRepository
                .findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("ESI Details Not Found"));

        // Capture old value BEFORE update
        String oldValue = convertToJson(existingEsi);

        existingEsi.setEsiNumber(
                esiDetails.getEsiNumber());

        existingEsi.setEmployeeContribution(
                esiDetails.getEmployeeContribution());

        existingEsi.setEmployerContribution(
                esiDetails.getEmployerContribution());

        esiDetailsRepository.save(existingEsi);

        // Capture new value AFTER update
        String newValue = convertToJson(existingEsi);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "PAYROLL",
                String.valueOf(existingEsi.getId()),
                performedBy,
                employeeId,
                "ESI Details updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_ESI_DETAILS",
                "PAYROLL",
                "ESI Details updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "ESI Details updated successfully"
        );

        return "ESI Details Updated Successfully";
    }



//=================================
// TDS DETAILS
//=================================

    public String createTdsDetails(String employeeId, TdsDetails tdsDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        if (tdsDetailsRepository.existsByEmployee(employee)) {
            throw new RuntimeException("TDS Details Already Exists");
        }

        tdsDetails.setEmployee(employee);

        tdsDetailsRepository.save(tdsDetails);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "TDS",
                String.valueOf(tdsDetails.getId()),
                performedBy,
                employeeId,
                "TDS Details created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_TDS_DETAILS",
                "PAYROLL",
                "TDS Details created successfully for employee: " + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "TDS Details created successfully for employee: " + employeeId
        );

        return "TDS Details Created Successfully";
    }


    public Object getTdsDetails(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return tdsDetailsRepository.findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("TDS Details Not Found"));
    }


    public String updateTdsDetails(
            String employeeId,
            TdsDetails tdsDetails) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        TdsDetails existingTds =
                tdsDetailsRepository.findByEmployee(employee)
                        .orElseThrow(() ->
                                new RuntimeException("TDS Details Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existingTds);

        existingTds.setFinancialYear(
                tdsDetails.getFinancialYear());

        existingTds.setTotalTax(
                tdsDetails.getTotalTax());

        existingTds.setMonthlyTax(
                tdsDetails.getMonthlyTax());

        tdsDetailsRepository.save(existingTds);

        // Capture NEW value after update
        String newValue = convertToJson(existingTds);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TDS",
                String.valueOf(existingTds.getId()),
                performedBy,
                employeeId,
                "TDS Details updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TDS_DETAILS",
                "PAYROLL",
                "TDS Details updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "TDS Details updated successfully for employee: "
                        + employeeId
        );

        return "TDS Details Updated Successfully";
    }



//=================================
// INCREMENTS
//=================================

    public String createIncrement(
            String employeeId,
            Increments increment) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        Salaries salary = salariesRepository
                .findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("Salary Not Found"));

        BigDecimal previousSalary = salary.getNetSalary();

        BigDecimal incrementPercentage =
                increment.getIncrementPercentage() != null
                        ? increment.getIncrementPercentage()
                        : BigDecimal.ZERO;

        BigDecimal incrementAmount =
                previousSalary
                        .multiply(incrementPercentage)
                        .divide(BigDecimal.valueOf(100));

        BigDecimal currentSalary =
                previousSalary.add(incrementAmount);

        // Save Increment History
        increment.setEmployee(employee);
        increment.setPreviousSalary(previousSalary);
        increment.setCurrentSalary(currentSalary);

        incrementRepository.save(increment);

        // Update Salary Table
        salary.setNetSalary(currentSalary);

        salariesRepository.save(salary);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "INCREMENT",
                String.valueOf(increment.getId()),
                performedBy,
                employeeId,
                "Salary increment created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_INCREMENT",
                "PAYROLL",
                "Salary increment created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Salary increment created successfully for employee: "
                        + employeeId
        );

        return "Increment Added Successfully";
    }


    public Object getIncrement(String employeeId) {

        return incrementRepository
                .findByEmployeeEmployeeId(employeeId);
    }



//=================================
// BONUS MANAGEMENT
//=================================

    public String createBonus(
            String employeeId,
            BonusManagement bonusManagement) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        bonusManagement.setEmployee(employee);

        bonusManagementRepository.save(bonusManagement);

        // Find Salary
        Salaries salary = salariesRepository
                .findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("Salary Not Found"));

        BigDecimal bonusAmount =
                bonusManagement.getBonusAmount() != null
                        ? bonusManagement.getBonusAmount()
                        : BigDecimal.ZERO;

        BigDecimal festivalBonus =
                bonusManagement.getFestivalBonus() != null
                        ? bonusManagement.getFestivalBonus()
                        : BigDecimal.ZERO;

        BigDecimal incentiveAmount =
                bonusManagement.getIncentiveAmount() != null
                        ? bonusManagement.getIncentiveAmount()
                        : BigDecimal.ZERO;

        // Calculate total bonus
        BigDecimal totalBonus =
                bonusAmount.add(festivalBonus);

        // Set salary bonus and incentive
        salary.setBonus(totalBonus);
        salary.setIncentives(incentiveAmount);

        // Calculate Net Salary
        BigDecimal netSalary =
                salary.getBasicSalary()
                        .add(salary.getHra())
                        .add(salary.getAllowances())
                        .add(salary.getBonus())
                        .add(salary.getIncentives())
                        .subtract(salary.getDeductions());

        salary.setNetSalary(netSalary);

        salariesRepository.save(salary);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "BONUS",
                String.valueOf(bonusManagement.getId()),
                performedBy,
                employeeId,
                "Bonus added successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_BONUS",
                "PAYROLL",
                "Bonus added successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Bonus added successfully for employee: "
                        + employeeId
        );

        return "Bonus Added Successfully";
    }


    public Object getBonus(String employeeId) {

        return bonusManagementRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public String updateBonus(
            Long id,
            BonusManagement bonusManagement) {

        BonusManagement existingBonus =
                bonusManagementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bonus Details Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existingBonus);

        existingBonus.setBonusAmount(
                bonusManagement.getBonusAmount());

        existingBonus.setFestivalBonus(
                bonusManagement.getFestivalBonus());

        existingBonus.setIncentiveAmount(
                bonusManagement.getIncentiveAmount());

        existingBonus.setRemarks(
                bonusManagement.getRemarks());

        bonusManagementRepository.save(existingBonus);

        // Capture NEW value after update
        String newValue = convertToJson(existingBonus);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existingBonus.getEmployee()
                        .getEmployeeId();

        auditLogsService.logUpdate(
                "BONUS",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Bonus details updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_BONUS",
                "PAYROLL",
                "Bonus details updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Bonus details updated successfully. ID: "
                        + id
        );

        return "Bonus Details Updated Successfully";
    }




    //=================================
    // REPORTS
    //=================================

    //=================================
// MONTHLY PAYROLL REPORT
//=================================

    //=================================
// MONTHLY PAYROLL REPORT
//=================================

    public Object getMonthlyPayrollReport() {

        List<Salaries> salaries = salariesRepository.findAll();
        List<PfDetails> pfDetails = pfDetailsRepository.findAll();
        List<TdsDetails> tdsDetails = tdsDetailsRepository.findAll();

        BigDecimal totalBasicSalary = BigDecimal.ZERO;
        BigDecimal totalHra = BigDecimal.ZERO;
        BigDecimal totalAllowances = BigDecimal.ZERO;
        BigDecimal totalBonus = BigDecimal.ZERO;
        BigDecimal totalIncentives = BigDecimal.ZERO;
        BigDecimal totalDeductions = BigDecimal.ZERO;
        BigDecimal totalNetSalary = BigDecimal.ZERO;
        BigDecimal totalPfAmount = BigDecimal.ZERO;
        BigDecimal totalTdsAmount = BigDecimal.ZERO;

        //=================================
        // SALARY CALCULATIONS
        //=================================

        for (Salaries salary : salaries) {

            totalBasicSalary = totalBasicSalary.add(
                    salary.getBasicSalary() != null
                            ? salary.getBasicSalary()
                            : BigDecimal.ZERO
            );

            totalHra = totalHra.add(
                    salary.getHra() != null
                            ? salary.getHra()
                            : BigDecimal.ZERO
            );

            totalAllowances = totalAllowances.add(
                    salary.getAllowances() != null
                            ? salary.getAllowances()
                            : BigDecimal.ZERO
            );

            totalBonus = totalBonus.add(
                    salary.getBonus() != null
                            ? salary.getBonus()
                            : BigDecimal.ZERO
            );

            totalIncentives = totalIncentives.add(
                    salary.getIncentives() != null
                            ? salary.getIncentives()
                            : BigDecimal.ZERO
            );

            totalDeductions = totalDeductions.add(
                    salary.getDeductions() != null
                            ? salary.getDeductions()
                            : BigDecimal.ZERO
            );

            totalNetSalary = totalNetSalary.add(
                    salary.getNetSalary() != null
                            ? salary.getNetSalary()
                            : BigDecimal.ZERO
            );
        }

        //=================================
        // PF CALCULATIONS
        //=================================

        for (PfDetails pf : pfDetails) {

            totalPfAmount = totalPfAmount.add(
                    pf.getTotalPf() != null
                            ? pf.getTotalPf()
                            : BigDecimal.ZERO
            );
        }

        //=================================
        // TDS CALCULATIONS
        //=================================

        for (TdsDetails tds : tdsDetails) {

            totalTdsAmount = totalTdsAmount.add(
                    tds.getTotalTax() != null
                            ? tds.getTotalTax()
                            : BigDecimal.ZERO
            );
        }

        //=================================
        // REPORT
        //=================================

        Map<String, Object> report = new HashMap<>();

        report.put(
                "Payroll Month",
                Month.values()[java.time.LocalDate.now()
                        .getMonthValue() - 1]
                        .toString()
        );

        report.put(
                "Payroll Year",
                Year.now().getValue()
        );

        report.put(
                "Total Employees Paid",
                salaries.size()
        );

        report.put(
                "Total Basic Salary",
                totalBasicSalary
        );

        report.put(
                "Total HRA",
                totalHra
        );

        report.put(
                "Total Allowances",
                totalAllowances
        );

        report.put(
                "Total Bonus Amount",
                totalBonus
        );

        report.put(
                "Total Incentives",
                totalIncentives
        );

        report.put(
                "Total Deductions",
                totalDeductions
        );

        report.put(
                "Total PF Amount",
                totalPfAmount
        );

        report.put(
                "Total TDS Amount",
                totalTdsAmount
        );

        report.put(
                "Total Net Salary Paid",
                totalNetSalary
        );

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
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        salaryComponent.setCompany(company);

        salaryComponentRepository.save(salaryComponent);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "SALARY_COMPONENT",
                String.valueOf(salaryComponent.getId()),
                performedBy,
                null,
                "Salary Component created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_SALARY_COMPONENT",
                "PAYROLL",
                "Salary Component created successfully: "
                        + salaryComponent.getComponentName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Salary Component created successfully: "
                        + salaryComponent.getComponentName()
        );

        return "Salary Component Created Successfully";
    }


    public List<SalaryComponent> getSalaryComponents() {

        return salaryComponentRepository.findAll();
    }


    public SalaryComponent getSalaryComponent(Long id) {

        return salaryComponentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salary Component Not Found"));
    }


    public String updateSalaryComponent(
            Long id,
            SalaryComponent salaryComponent) {

        SalaryComponent existing =
                salaryComponentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Component Not Found"));

        // Capture old value BEFORE modification
        String oldValue = convertToJson(existing);

        existing.setComponentName(
                salaryComponent.getComponentName());

        existing.setComponentCode(
                salaryComponent.getComponentCode());

        existing.setComponentType(
                salaryComponent.getComponentType());

        existing.setTaxable(
                salaryComponent.getTaxable());

        existing.setCalculationType(
                salaryComponent.getCalculationType());

        existing.setDefaultValue(
                salaryComponent.getDefaultValue());

        existing.setStatus(
                salaryComponent.getStatus());

        salaryComponentRepository.save(existing);

        // Capture new value AFTER modification
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "SALARY_COMPONENT",
                String.valueOf(id),
                performedBy,
                null,
                "Salary Component updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_SALARY_COMPONENT",
                "PAYROLL",
                "Salary Component updated successfully: "
                        + existing.getComponentName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Salary Component updated successfully: "
                        + existing.getComponentName()
        );

        return "Salary Component Updated Successfully";
    }


    public String deleteSalaryComponent(Long id) {

        SalaryComponent existing =
                salaryComponentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Component Not Found"));

        // Capture deleted value BEFORE deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        salaryComponentRepository.delete(existing);

        auditLogsService.createAuditLog(
                "SALARY_COMPONENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                null,
                "Salary Component deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_SALARY_COMPONENT",
                "PAYROLL",
                "Salary Component deleted successfully: "
                        + existing.getComponentName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Salary Component deleted successfully: "
                        + existing.getComponentName()
        );

        return "Salary Component Deleted Successfully";
    }



//=================================
// ALLOWANCES
//=================================

    public String createAllowance(
            String employeeId,
            Allowance allowance) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        SalaryComponent component =
                salaryComponentRepository.findById(
                                allowance.getSalaryComponent().getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Component Not Found"));

        allowance.setEmployee(employee);
        allowance.setSalaryComponent(component);

        allowanceRepository.save(allowance);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "ALLOWANCE",
                String.valueOf(allowance.getId()),
                performedBy,
                employeeId,
                "Allowance created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ALLOWANCE",
                "PAYROLL",
                "Allowance created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Allowance created successfully for employee: "
                        + employeeId
        );

        return "Allowance Created Successfully";
    }


    public List<Allowance> getAllowances() {

        return allowanceRepository.findAll();
    }


    public Allowance getAllowance(Long id) {

        return allowanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Allowance Not Found"));
    }


    public String updateAllowance(
            Long id,
            Allowance allowance) {

        Allowance existing =
                allowanceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Allowance Not Found"));

        // Capture old value BEFORE modification
        String oldValue = convertToJson(existing);

        existing.setAllowanceType(
                allowance.getAllowanceType());

        existing.setAmount(
                allowance.getAmount());

        existing.setEffectiveFrom(
                allowance.getEffectiveFrom());

        existing.setEffectiveTo(
                allowance.getEffectiveTo());

        existing.setRemarks(
                allowance.getRemarks());

        existing.setStatus(
                allowance.getStatus());

        if (allowance.getSalaryComponent() != null) {

            SalaryComponent component =
                    salaryComponentRepository.findById(
                                    allowance.getSalaryComponent().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Salary Component Not Found"));

            existing.setSalaryComponent(component);
        }

        allowanceRepository.save(existing);

        // Capture new value AFTER modification
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee()
                        .getEmployeeId();

        auditLogsService.logUpdate(
                "ALLOWANCE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Allowance updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_ALLOWANCE",
                "PAYROLL",
                "Allowance updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Allowance updated successfully for employee: "
                        + employeeId
        );

        return "Allowance Updated Successfully";
    }


    public String deleteAllowance(Long id) {

        Allowance existing =
                allowanceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Allowance Not Found"));

        // Capture deleted value BEFORE deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        allowanceRepository.delete(existing);

        auditLogsService.createAuditLog(
                "ALLOWANCE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Allowance deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_ALLOWANCE",
                "PAYROLL",
                "Allowance deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Allowance deleted successfully for employee: "
                        + employeeId
        );

        return "Allowance Deleted Successfully";
    }


//=================================
// DEDUCTIONS
//=================================

    public String createDeduction(
            String employeeId,
            Deduction deduction) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        SalaryComponent component =
                salaryComponentRepository.findById(
                                deduction.getSalaryComponent().getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary Component Not Found"));

        deduction.setEmployee(employee);
        deduction.setSalaryComponent(component);

        deductionRepository.save(deduction);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "DEDUCTION",
                String.valueOf(deduction.getId()),
                performedBy,
                employeeId,
                "Deduction created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_DEDUCTION",
                "PAYROLL",
                "Deduction created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Deduction created successfully for employee: "
                        + employeeId
        );

        return "Deduction Created Successfully";
    }


    public List<Deduction> getDeductions() {

        return deductionRepository.findAll();
    }


    public Deduction getDeduction(Long id) {

        return deductionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Deduction Not Found"));
    }


    public String updateDeduction(
            Long id,
            Deduction deduction) {

        Deduction existing =
                deductionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Deduction Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existing);

        existing.setDeductionType(
                deduction.getDeductionType());

        existing.setAmount(
                deduction.getAmount());

        existing.setDeductionDate(
                deduction.getDeductionDate());

        existing.setRemarks(
                deduction.getRemarks());

        existing.setStatus(
                deduction.getStatus());

        if (deduction.getSalaryComponent() != null) {

            SalaryComponent component =
                    salaryComponentRepository.findById(
                                    deduction.getSalaryComponent().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Salary Component Not Found"));

            existing.setSalaryComponent(component);
        }

        deductionRepository.save(existing);

        // Capture NEW value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "DEDUCTION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Deduction updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_DEDUCTION",
                "PAYROLL",
                "Deduction updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Deduction updated successfully for employee: "
                        + employeeId
        );

        return "Deduction Updated Successfully";
    }


    public String deleteDeduction(Long id) {

        Deduction existing =
                deductionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Deduction Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        deductionRepository.delete(existing);

        auditLogsService.createAuditLog(
                "DEDUCTION",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Deduction deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_DEDUCTION",
                "PAYROLL",
                "Deduction deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Deduction deleted successfully for employee: "
                        + employeeId
        );

        return "Deduction Deleted Successfully";
    }



//=================================
// REIMBURSEMENTS
//=================================

    public String createReimbursement(
            String employeeId,
            Reimbursement reimbursement) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        reimbursement.setEmployee(employee);

        if (reimbursement.getStatus() == null) {
            reimbursement.setStatus("PENDING");
        }

        reimbursementRepository.save(reimbursement);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "REIMBURSEMENT",
                String.valueOf(reimbursement.getId()),
                performedBy,
                employeeId,
                "Reimbursement created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_REIMBURSEMENT",
                "PAYROLL",
                "Reimbursement created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Reimbursement created successfully for employee: "
                        + employeeId
        );

        return "Reimbursement Created Successfully";
    }


    public List<Reimbursement> getReimbursements() {

        return reimbursementRepository.findAll();
    }


    public Reimbursement getReimbursement(Long id) {

        return reimbursementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reimbursement Not Found"));
    }


    public String updateReimbursement(
            Long id,
            Reimbursement reimbursement) {

        Reimbursement existing =
                reimbursementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reimbursement Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existing);

        existing.setReimbursementType(
                reimbursement.getReimbursementType());

        existing.setExpenseDate(
                reimbursement.getExpenseDate());

        existing.setClaimedAmount(
                reimbursement.getClaimedAmount());

        existing.setApprovedAmount(
                reimbursement.getApprovedAmount());

        existing.setStatus(
                reimbursement.getStatus());

        existing.setRemarks(
                reimbursement.getRemarks());

        reimbursementRepository.save(existing);

        // Capture NEW value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "REIMBURSEMENT",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Reimbursement updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_REIMBURSEMENT",
                "PAYROLL",
                "Reimbursement updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Reimbursement updated successfully for employee: "
                        + employeeId
        );

        return "Reimbursement Updated Successfully";
    }


    public String deleteReimbursement(Long id) {

        Reimbursement existing =
                reimbursementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reimbursement Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        reimbursementRepository.delete(existing);

        auditLogsService.createAuditLog(
                "REIMBURSEMENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Reimbursement deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_REIMBURSEMENT",
                "PAYROLL",
                "Reimbursement deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Reimbursement deleted successfully for employee: "
                        + employeeId
        );

        return "Reimbursement Deleted Successfully";
    }



//=================================
// EMPLOYEE LOANS
//=================================

    public String createEmployeeLoan(
            String employeeId,
            EmployeeLoan employeeLoan) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        employeeLoan.setEmployee(employee);

        if (employeeLoan.getStatus() == null) {
            employeeLoan.setStatus("PENDING");
        }

        employeeLoanRepository.save(employeeLoan);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_LOAN",
                String.valueOf(employeeLoan.getId()),
                performedBy,
                employeeId,
                "Employee loan created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_EMPLOYEE_LOAN",
                "PAYROLL",
                "Employee loan created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Employee loan created successfully for employee: "
                        + employeeId
        );

        return "Employee Loan Created Successfully";
    }


    public List<EmployeeLoan> getEmployeeLoans() {

        return employeeLoanRepository.findAll();
    }


    public EmployeeLoan getEmployeeLoan(Long id) {

        return employeeLoanRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Loan Not Found"));
    }


    public String updateEmployeeLoan(
            Long id,
            EmployeeLoan employeeLoan) {

        EmployeeLoan existing =
                employeeLoanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Loan Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existing);

        existing.setLoanType(
                employeeLoan.getLoanType());

        existing.setLoanAmount(
                employeeLoan.getLoanAmount());

        existing.setInterestRate(
                employeeLoan.getInterestRate());

        existing.setInstallmentAmount(
                employeeLoan.getInstallmentAmount());

        existing.setTotalInstallments(
                employeeLoan.getTotalInstallments());

        existing.setApprovedBy(
                employeeLoan.getApprovedBy());

        existing.setStatus(
                employeeLoan.getStatus());

        employeeLoanRepository.save(existing);

        // Capture NEW value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "EMPLOYEE_LOAN",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee loan updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_EMPLOYEE_LOAN",
                "PAYROLL",
                "Employee loan updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Employee loan updated successfully for employee: "
                        + employeeId
        );

        return "Employee Loan Updated Successfully";
    }


    public String deleteEmployeeLoan(Long id) {

        EmployeeLoan existing =
                employeeLoanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Loan Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        employeeLoanRepository.delete(existing);

        auditLogsService.createAuditLog(
                "EMPLOYEE_LOAN",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Employee loan deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_EMPLOYEE_LOAN",
                "PAYROLL",
                "Employee loan deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Employee loan deleted successfully for employee: "
                        + employeeId
        );

        return "Employee Loan Deleted Successfully";
    }


//=================================
// LOAN INSTALLMENTS
//=================================

    public String createLoanInstallment(
            Long loanId,
            LoanInstallment loanInstallment) {

        EmployeeLoan loan =
                employeeLoanRepository.findById(loanId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Loan Not Found"));

        loanInstallment.setLoan(loan);

        if (loanInstallment.getPaymentStatus() == null) {
            loanInstallment.setPaymentStatus("PENDING");
        }

        loanInstallmentRepository.save(loanInstallment);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                loan.getEmployee() != null
                        ? loan.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logCreate(
                "LOAN_INSTALLMENT",
                String.valueOf(loanInstallment.getId()),
                performedBy,
                employeeId,
                "Loan installment created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_LOAN_INSTALLMENT",
                "PAYROLL",
                "Loan installment created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Loan installment created successfully for employee: "
                        + employeeId
        );

        return "Loan Installment Created Successfully";
    }


    public List<LoanInstallment> getLoanInstallments() {

        return loanInstallmentRepository.findAll();
    }


    public LoanInstallment getLoanInstallment(Long id) {

        return loanInstallmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Loan Installment Not Found"));
    }


    public String updateLoanInstallment(
            Long id,
            LoanInstallment loanInstallment) {

        LoanInstallment existing =
                loanInstallmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan Installment Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existing);

        existing.setInstallmentNo(
                loanInstallment.getInstallmentNo());

        existing.setDueDate(
                loanInstallment.getDueDate());

        existing.setAmount(
                loanInstallment.getAmount());

        existing.setPaidAmount(
                loanInstallment.getPaidAmount());

        existing.setPaymentStatus(
                loanInstallment.getPaymentStatus());

        if (loanInstallment.getSalary() != null) {
            existing.setSalary(
                    loanInstallment.getSalary());
        }

        loanInstallmentRepository.save(existing);

        // Capture NEW value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = null;

        if (existing.getLoan() != null &&
                existing.getLoan().getEmployee() != null) {

            employeeId =
                    existing.getLoan()
                            .getEmployee()
                            .getEmployeeId();
        }

        auditLogsService.logUpdate(
                "LOAN_INSTALLMENT",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Loan installment updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_LOAN_INSTALLMENT",
                "PAYROLL",
                "Loan installment updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Loan installment updated successfully for employee: "
                        + employeeId
        );

        return "Loan Installment Updated Successfully";
    }


    public String deleteLoanInstallment(Long id) {

        LoanInstallment existing =
                loanInstallmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan Installment Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = null;

        if (existing.getLoan() != null &&
                existing.getLoan().getEmployee() != null) {

            employeeId =
                    existing.getLoan()
                            .getEmployee()
                            .getEmployeeId();
        }

        loanInstallmentRepository.delete(existing);

        auditLogsService.createAuditLog(
                "LOAN_INSTALLMENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Loan installment deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LOAN_INSTALLMENT",
                "PAYROLL",
                "Loan installment deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Loan installment deleted successfully for employee: "
                        + employeeId
        );

        return "Loan Installment Deleted Successfully";
    }



//=================================
// PAYROLL HISTORY
//=================================

    public String createPayrollHistory(
            String employeeId,
            PayrollHistory payrollHistory) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        payrollHistory.setEmployee(employee);

        if (payrollHistory.getSalary() != null &&
                payrollHistory.getSalary().getId() != null) {

            Salaries salary =
                    salariesRepository.findById(
                                    payrollHistory
                                            .getSalary()
                                            .getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Salary Not Found"));

            payrollHistory.setSalary(salary);
        }

        if (payrollHistory.getSalarySlip() != null &&
                payrollHistory.getSalarySlip().getId() != null) {

            SalarySlips salarySlip =
                    salarySlipsRepository.findById(
                                    payrollHistory
                                            .getSalarySlip()
                                            .getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Salary Slip Not Found"));

            payrollHistory.setSalarySlip(salarySlip);
        }

        payrollHistoryRepository.save(payrollHistory);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "PAYROLL_HISTORY",
                String.valueOf(payrollHistory.getId()),
                performedBy,
                employeeId,
                "Payroll history created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_PAYROLL_HISTORY",
                "PAYROLL",
                "Payroll history created successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Payroll history created successfully for employee: "
                        + employeeId
        );

        return "Payroll History Created Successfully";
    }


    public List<PayrollHistory> getPayrollHistories() {

        return payrollHistoryRepository.findAll();
    }


    public PayrollHistory getPayrollHistory(Long id) {

        return payrollHistoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payroll History Not Found"));
    }


    public String updatePayrollHistory(
            Long id,
            PayrollHistory payrollHistory) {

        PayrollHistory existing =
                payrollHistoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll History Not Found"));

        // Capture OLD value before update
        String oldValue = convertToJson(existing);

        existing.setPayrollMonth(
                payrollHistory.getPayrollMonth());

        existing.setPayrollYear(
                payrollHistory.getPayrollYear());

        existing.setGrossSalary(
                payrollHistory.getGrossSalary());

        existing.setNetSalary(
                payrollHistory.getNetSalary());

        existing.setProcessedAt(
                payrollHistory.getProcessedAt());

        existing.setProcessedBy(
                payrollHistory.getProcessedBy());

        payrollHistoryRepository.save(existing);

        // Capture NEW value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "PAYROLL_HISTORY",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Payroll history updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_PAYROLL_HISTORY",
                "PAYROLL",
                "Payroll history updated successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Payroll history updated successfully for employee: "
                        + employeeId
        );

        return "Payroll History Updated Successfully";
    }


    public String deletePayrollHistory(Long id) {

        PayrollHistory existing =
                payrollHistoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll History Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        String employeeId =
                existing.getEmployee() != null
                        ? existing.getEmployee().getEmployeeId()
                        : null;

        payrollHistoryRepository.delete(existing);

        auditLogsService.createAuditLog(
                "PAYROLL_HISTORY",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Payroll history deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_PAYROLL_HISTORY",
                "PAYROLL",
                "Payroll history deleted successfully for employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "PAYROLL",
                "PayrollService",
                "Payroll history deleted successfully for employee: "
                        + employeeId
        );

        return "Payroll History Deleted Successfully";
    }



}
