package com.HRMS.QuickDines.Wallet.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Wallet.Entity.WalletStatus;
import com.HRMS.QuickDines.Wallet.model.EmployeeWallet;
import com.HRMS.QuickDines.Wallet.model.WalletReports;
import com.HRMS.QuickDines.Wallet.model.WalletTransactions;
import com.HRMS.QuickDines.Wallet.repo.EmployeeWalletRepository;
import com.HRMS.QuickDines.Wallet.repo.WalletReportsRepository;
import com.HRMS.QuickDines.Wallet.repo.WalletTransactionsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final EmployeeWalletRepository employeeWalletRepository;
    private final EmployeeRepository employeeRepository;
    private final WalletTransactionsRepository walletTransactionsRepository;
    private final WalletReportsRepository walletReportsRepository;
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



// =========================================================
// EMPLOYEE WALLET
// =========================================================

    public String createWallet(
            String employeeId,
            EmployeeWallet employeeWallet) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        if (employeeWalletRepository.existsByEmployee(employee)) {
            throw new RuntimeException("Wallet Already Exists");
        }

        employeeWallet.setEmployee(employee);

        employeeWallet.setWalletBalance(BigDecimal.ZERO);
        employeeWallet.setSalaryAmount(BigDecimal.ZERO);
        employeeWallet.setBonusAmount(BigDecimal.ZERO);

        employeeWallet.setLeaveCredits(0);
        employeeWallet.setStatus(WalletStatus.valueOf("ACTIVE"));

        employeeWalletRepository.save(employeeWallet);


        // =====================================================
        // AUDIT - CREATE WALLET
        // =====================================================

        String performedBy = getLoggedInEmployeeId();

        String newValue = convertToJson(employeeWallet);

        auditLogsService.logCreate(
                "WALLET",
                String.valueOf(employeeWallet.getId()),
                performedBy,
                employeeWallet.getId().toString(),
                "Employee Wallet created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_WALLET",
                "WALLET",
                "Employee Wallet created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WALLET",
                "WalletService",
                "Employee Wallet created successfully"
        );


        return "Wallet Created Successfully";
    }


    public EmployeeWallet getWallet(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return employeeWalletRepository.findByEmployee(employee)
                .orElseThrow(() ->
                        new RuntimeException("Wallet Not Found"));
    }


    public String updateWallet(
            String employeeId,
            EmployeeWallet employeeWallet) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        EmployeeWallet existingWallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() ->
                                new RuntimeException("Wallet Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = convertToJson(existingWallet);


        existingWallet.setWalletBalance(
                employeeWallet.getWalletBalance()
        );

        existingWallet.setSalaryAmount(
                employeeWallet.getSalaryAmount()
        );

        existingWallet.setBonusAmount(
                employeeWallet.getBonusAmount()
        );

        existingWallet.setLeaveCredits(
                employeeWallet.getLeaveCredits()
        );

        existingWallet.setStatus(
                employeeWallet.getStatus()
        );

        employeeWalletRepository.save(existingWallet);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = convertToJson(existingWallet);

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT - UPDATE WALLET
        // =====================================================

        auditLogsService.logUpdate(
                "WALLET",
                String.valueOf(existingWallet.getId()),
                performedBy,
                null,
                "Employee Wallet updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_WALLET",
                "WALLET",
                "Employee Wallet updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WALLET",
                "WalletService",
                "Employee Wallet updated successfully"
        );


        return "Wallet Updated Successfully";
    }


    public String deleteWallet(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() ->
                                new RuntimeException("Wallet Not Found"));


        // =====================================================
        // OLD VALUE BEFORE DELETE
        // =====================================================

        String deletedValue = convertToJson(wallet);

        String performedBy = getLoggedInEmployeeId();


        employeeWalletRepository.delete(wallet);


        // =====================================================
        // AUDIT - DELETE WALLET
        // =====================================================

        auditLogsService.createAuditLog(
                "WALLET",
                String.valueOf(wallet.getId()),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                wallet.getId().toString(),
                "Employee Wallet deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_WALLET",
                "WALLET",
                "Employee Wallet deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WALLET",
                "WalletService",
                "Employee Wallet deleted successfully"
        );


        return "Wallet Deleted Successfully";
    }


// =========================================================
// TRANSACTIONS
// =========================================================

    public String createTransaction(
            String employeeId,
            WalletTransactions walletTransactions) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() ->
                                new RuntimeException("Wallet Not Found"));


        // =====================================================
        // WALLET BALANCE
        // =====================================================

        BigDecimal walletBalance =
                wallet.getWalletBalance() != null
                        ? wallet.getWalletBalance()
                        : BigDecimal.ZERO;

        BigDecimal transactionAmount =
                walletTransactions.getAmount() != null
                        ? walletTransactions.getAmount()
                        : BigDecimal.ZERO;


        // =====================================================
        // CREDIT
        // =====================================================

        if ("CREDIT".equalsIgnoreCase(
                walletTransactions.getTransactionType())) {

            walletBalance =
                    walletBalance.add(transactionAmount);
        }


        // =====================================================
        // DEBIT
        // =====================================================

        else if ("DEBIT".equalsIgnoreCase(
                walletTransactions.getTransactionType())) {

            if (walletBalance.compareTo(transactionAmount) < 0) {

                throw new RuntimeException(
                        "Insufficient Wallet Balance"
                );
            }

            walletBalance =
                    walletBalance.subtract(transactionAmount);
        }


        // =====================================================
        // INVALID TRANSACTION
        // =====================================================

        else {

            throw new RuntimeException(
                    "Invalid Transaction Type. Use CREDIT or DEBIT"
            );
        }


        wallet.setWalletBalance(walletBalance);

        employeeWalletRepository.save(wallet);


        walletTransactions.setEmployee(employee);

        walletTransactions.setTransactionStatus("SUCCESS");

        walletTransactions.setTransactionDate(
                LocalDate.now()
        );

        walletTransactionsRepository.save(walletTransactions);


        // =====================================================
        // AUDIT - CREATE TRANSACTION
        // =====================================================

        String performedBy = getLoggedInEmployeeId();

        String newValue =
                convertToJson(walletTransactions);

        auditLogsService.logCreate(
                "WALLET_TRANSACTION",
                String.valueOf(walletTransactions.getId()),
                performedBy,
                walletTransactions.getId().toString(),
                "Wallet transaction created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_WALLET_TRANSACTION",
                "WALLET_TRANSACTION",
                "Wallet transaction created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WALLET_TRANSACTION",
                "WalletService",
                "Wallet transaction created successfully"
        );


        return "Transaction Successful";
    }


    public List<WalletTransactions> getTransactions(
            String employeeId) {

        return walletTransactionsRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    public WalletTransactions getTransaction(Long id) {

        return walletTransactionsRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Transaction Not Found"
                        )
                );
    }


    public String deleteTransaction(Long id) {

        WalletTransactions transaction =
                walletTransactionsRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction Not Found"
                                )
                        );


        // =====================================================
        // OLD VALUE BEFORE DELETE
        // =====================================================

        String deletedValue =
                convertToJson(transaction);

        String performedBy =
                getLoggedInEmployeeId();


        walletTransactionsRepository.delete(transaction);


        // =====================================================
        // AUDIT - DELETE TRANSACTION
        // =====================================================

        auditLogsService.createAuditLog(
                "WALLET_TRANSACTION",
                String.valueOf(transaction.getId()),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                transaction.getId().toString(),
                "Wallet transaction deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_WALLET_TRANSACTION",
                "WALLET_TRANSACTION",
                "Wallet transaction deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WALLET_TRANSACTION",
                "WalletService",
                "Wallet transaction deleted successfully"
        );


        return "Transaction Deleted Successfully";
    }

// =========================================================
// REPORTS
// =========================================================

    public String generateReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Employee Not Found"
                        )
                );

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Wallet Not Found"
                                )
                        );


        List<WalletTransactions> transactions =
                walletTransactionsRepository
                        .findByEmployeeEmployeeId(employeeId);


        BigDecimal creditedAmount = BigDecimal.ZERO;
        BigDecimal debitedAmount = BigDecimal.ZERO;


        for (WalletTransactions transaction : transactions) {

            BigDecimal amount =
                    transaction.getAmount() != null
                            ? transaction.getAmount()
                            : BigDecimal.ZERO;


            if ("CREDIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                creditedAmount =
                        creditedAmount.add(amount);
            }

            else if ("DEBIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                debitedAmount =
                        debitedAmount.add(amount);
            }
        }


        WalletReports report = new WalletReports();

        report.setEmployee(employee);

        report.setMonthlySalary(
                wallet.getSalaryAmount()
        );

        report.setCreditedAmount(
                creditedAmount
        );

        report.setDebitedAmount(
                debitedAmount
        );

        report.setAvailableBalance(
                wallet.getWalletBalance()
        );


        walletReportsRepository.save(report);


        // =====================================================
        // AUDIT - CREATE WALLET REPORT
        // =====================================================

        String performedBy =
                getLoggedInEmployeeId();

        String newValue =
                convertToJson(report);


        auditLogsService.logCreate(
                "WALLET_REPORT",
                String.valueOf(report.getId()),
                performedBy,
                report.getId().toString(),
                "Wallet Report generated successfully"
        );


        auditLogsService.logActivity(
                performedBy,
                "GENERATE_WALLET_REPORT",
                "WALLET_REPORT",
                "Wallet Report generated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );


        auditLogsService.logInfo(
                "WALLET_REPORT",
                "WalletService",
                "Wallet Report generated successfully"
        );


        return "Wallet Report Generated Successfully";
    }


    public List<WalletReports> getReport(
            String employeeId) {

        return walletReportsRepository
                .findByEmployeeEmployeeId(employeeId);
    }


// =========================================================
// MONTHLY WALLET REPORT
// =========================================================

    public Object getMonthlyWalletReport() {

        List<EmployeeWallet> wallets =
                employeeWalletRepository.findAll();


        BigDecimal totalSalary = BigDecimal.ZERO;
        BigDecimal totalWalletBalance = BigDecimal.ZERO;
        BigDecimal totalBonus = BigDecimal.ZERO;


        for (EmployeeWallet wallet : wallets) {

            BigDecimal salary =
                    wallet.getSalaryAmount() != null
                            ? wallet.getSalaryAmount()
                            : BigDecimal.ZERO;

            BigDecimal balance =
                    wallet.getWalletBalance() != null
                            ? wallet.getWalletBalance()
                            : BigDecimal.ZERO;

            BigDecimal bonus =
                    wallet.getBonusAmount() != null
                            ? wallet.getBonusAmount()
                            : BigDecimal.ZERO;


            totalSalary =
                    totalSalary.add(salary);

            totalWalletBalance =
                    totalWalletBalance.add(balance);

            totalBonus =
                    totalBonus.add(bonus);
        }


        Map<String, Object> report =
                new HashMap<>();


        report.put(
                "Total Employees",
                wallets.size()
        );

        report.put(
                "Total Salary Credited",
                totalSalary
        );

        report.put(
                "Total Bonus Credited",
                totalBonus
        );

        report.put(
                "Total Wallet Balance",
                totalWalletBalance
        );

        report.put(
                "Month",
                LocalDate.now()
                        .getMonth()
                        .toString()
        );

        report.put(
                "Year",
                LocalDate.now()
                        .getYear()
        );


        return report;
    }


// =========================================================
// DASHBOARD
// =========================================================

    public BigDecimal getBalance(String employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Wallet Not Found"
                                )
                        );


        return wallet.getWalletBalance() != null
                ? wallet.getWalletBalance()
                : BigDecimal.ZERO;
    }


    public List<WalletTransactions> getWalletHistory(
            String employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        return walletTransactionsRepository
                .findByEmployee(employee);
    }


    public List<EmployeeWallet> getAllWallets() {

        return employeeWalletRepository.findAll();
    }

    // =========================================================
// FILTER EMPLOYEE WALLETS BY STATUS
// =========================================================

    public List<EmployeeWallet> getWalletsByStatus(
            WalletStatus status) {

        if (status == null) {
            throw new RuntimeException(
                    "Wallet status is required");
        }

        return employeeWalletRepository
                .findByStatus(status);
    }

}

