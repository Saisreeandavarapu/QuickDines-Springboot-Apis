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

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final EmployeeWalletRepository employeeWalletRepository;
    private final EmployeeRepository employeeRepository;
    private final WalletTransactionsRepository  walletTransactionsRepository ;
    private final WalletReportsRepository walletReportsRepository;
    //=================================
    // EMPLOYEE WALLET
    //=================================


    public String createWallet(String employeeId, EmployeeWallet employeeWallet){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        if (employeeWalletRepository.existsByEmployee(employee)) {

            throw new RuntimeException("Wallet Already Exists");
        }
        employeeWallet.setEmployee(employee);

        employeeWallet.setWalletBalance(0.0);
        employeeWallet.setSalaryAmount(0.0);
        employeeWallet.setBonusAmount(0.0);
        employeeWallet.setLeaveCredits(0);
        employeeWallet.setStatus("ACTIVE");

        employeeWalletRepository.save(employeeWallet);

        return "Wallet Created Successfully";
    }

    public Object getWallet(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));
    }

    public String updateWallet(String employeeId,EmployeeWallet employeeWallet){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmployeeWallet existingWallet = employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));

        existingWallet.setWalletBalance(employeeWallet.getWalletBalance());

        existingWallet.setSalaryAmount(employeeWallet.getSalaryAmount());

        existingWallet.setBonusAmount(employeeWallet.getBonusAmount());

        existingWallet.setLeaveCredits(employeeWallet.getLeaveCredits());

        existingWallet.setStatus(employeeWallet.getStatus());

        employeeWalletRepository.save(existingWallet);

        return "Wallet Updated Successfully";
    }

    public String deleteWallet(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        EmployeeWallet wallet = employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));
        employeeWalletRepository.delete(wallet);
        return "Wallet Deleted Successfully";
    }



    //=================================
// TRANSACTIONS
//=================================

    public String createTransaction(String employeeId, WalletTransactions walletTransactions){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        EmployeeWallet wallet = employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));
        Double walletBalance = wallet.getWalletBalance();

        // CREDIT

        if (walletTransactions.getTransactionType().equalsIgnoreCase("CREDIT")) {

            walletBalance += walletTransactions.getAmount();
        }

        // DEBIT

        else if (walletTransactions.getTransactionType().equalsIgnoreCase("DEBIT")) {

            if (walletBalance < walletTransactions.getAmount()) {
                throw new RuntimeException("Insufficient Wallet Balance");
            }
            walletBalance -= walletTransactions.getAmount();
        }
        wallet.setWalletBalance(walletBalance);

        employeeWalletRepository.save(wallet);
        walletTransactions.setEmployee(employee);
        walletTransactions.setTransactionStatus("SUCCESS");
        walletTransactions.setTransactionDate(
                LocalDate.now());

        walletTransactionsRepository.save(walletTransactions);
        return "Transaction Successful";
    }

    public List<EmployeeWallet> getTransactions(String employeeId){

        return walletTransactionsRepository.findByEmployeeEmployeeId(employeeId);
    }

    public Object getTransaction(Long id){
        return walletTransactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
    }

    public String deleteTransaction(Long id){

        WalletTransactions transaction = walletTransactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
        walletTransactionsRepository.delete(transaction);
        return "Transaction Deleted Successfully";
    }



    //=================================
// REPORTS
//=================================

    public String generateReport(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        EmployeeWallet wallet = employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));


        List<WalletTransactions> transactions = walletTransactionsRepository.findByEmployeeEmployeeId(employeeId);

        Double creditedAmount = 0.0;
        Double debitedAmount = 0.0;


        for (WalletTransactions transaction : transactions) {

            if ("CREDIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                creditedAmount += transaction.getAmount();
            }

            else if ("DEBIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                debitedAmount += transaction.getAmount();
            }
        }


        WalletReports report = new WalletReports();

        report.setEmployee(employee);
        report.setMonthlySalary(wallet.getSalaryAmount());
        report.setCreditedAmount(creditedAmount);
        report.setDebitedAmount(debitedAmount);
        report.setAvailableBalance(
                wallet.getWalletBalance());

        walletReportsRepository.save(report);


        return "Wallet Report Generated Successfully";
    }

    public List<WalletReports> getReport(String employeeId){

        return walletReportsRepository.findByEmployeeEmployeeId(employeeId);
    }

    public Object getMonthlyWalletReport(){

        List<EmployeeWallet> wallets = employeeWalletRepository.findAll();
        Double totalSalary = 0.0;
        Double totalWalletBalance = 0.0;
        Double totalBonus = 0.0;

        for (EmployeeWallet wallet : wallets) {

            totalSalary += wallet.getSalaryAmount();

            totalWalletBalance += wallet.getWalletBalance();

            totalBonus += wallet.getBonusAmount();
        }
        Map<String, Object> report = new HashMap<>();

        report.put("Total Employees", wallets.size());

        report.put("Total Salary Credited", totalSalary);

        report.put("Total Bonus Credited", totalBonus);

        report.put("Total Wallet Balance", totalWalletBalance);

        report.put("Month", Month.values().toString());

        report.put("Year", Year.now().getValue());
        return report;
    }



    //=================================
    // DASHBOARD
    //=================================


    public Object getBalance(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        EmployeeWallet wallet = employeeWalletRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Wallet Not Found"));

        return wallet.getWalletBalance();
    }

    public List<WalletTransactions> getWalletHistory(String employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return walletTransactionsRepository.findByEmployee(employee);
    }

    public Object getAllWallets(){
        return employeeWalletRepository.findAll();
    }

}
