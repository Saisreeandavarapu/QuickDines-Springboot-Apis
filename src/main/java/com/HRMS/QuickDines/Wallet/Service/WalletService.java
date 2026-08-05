package com.HRMS.QuickDines.Wallet.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Wallet.model.EmployeeWallet;
import com.HRMS.QuickDines.Wallet.model.WalletReports;
import com.HRMS.QuickDines.Wallet.model.WalletTransactions;
import com.HRMS.QuickDines.Wallet.repo.EmployeeWalletRepository;
import com.HRMS.QuickDines.Wallet.repo.WalletReportsRepository;
import com.HRMS.QuickDines.Wallet.repo.WalletTransactionsRepository;
import lombok.RequiredArgsConstructor;
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


    // =========================================================
    // EMPLOYEE WALLET
    // =========================================================

    public String createWallet(String employeeId, EmployeeWallet employeeWallet) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        if (employeeWalletRepository.existsByEmployee(employee)) {
            throw new RuntimeException("Wallet Already Exists");
        }

        employeeWallet.setEmployee(employee);

        // Money fields should use BigDecimal
        employeeWallet.setWalletBalance(BigDecimal.ZERO);
        employeeWallet.setSalaryAmount(BigDecimal.ZERO);
        employeeWallet.setBonusAmount(BigDecimal.ZERO);

        employeeWallet.setLeaveCredits(0);
        employeeWallet.setStatus("ACTIVE");

        employeeWalletRepository.save(employeeWallet);

        return "Wallet Created Successfully";
    }


    public EmployeeWallet getWallet(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return employeeWalletRepository.findByEmployee(employee)
                .orElseThrow(() -> new RuntimeException("Wallet Not Found"));
    }


    public String updateWallet(
            String employeeId,
            EmployeeWallet employeeWallet) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmployeeWallet existingWallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() -> new RuntimeException("Wallet Not Found"));

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

        return "Wallet Updated Successfully";
    }


    public String deleteWallet(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() -> new RuntimeException("Wallet Not Found"));

        employeeWalletRepository.delete(wallet);

        return "Wallet Deleted Successfully";
    }


    // =========================================================
    // TRANSACTIONS
    // =========================================================

    public String createTransaction(
            String employeeId,
            WalletTransactions walletTransactions) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmployeeWallet wallet =
                employeeWalletRepository.findByEmployee(employee)
                        .orElseThrow(() -> new RuntimeException("Wallet Not Found"));


        // Prevent NullPointerException
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

            walletBalance = walletBalance.add(transactionAmount);
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

            walletBalance = walletBalance.subtract(transactionAmount);
        }


        // Invalid transaction type
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

        walletTransactionsRepository.delete(transaction);

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


        return wallet.getWalletBalance() != null
                ? wallet.getWalletBalance()
                : BigDecimal.ZERO;
    }


    public List<WalletTransactions> getWalletHistory(
            String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
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
}

