package com.HRMS.QuickDines.Finance.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Finance.model.Expenses;
import com.HRMS.QuickDines.Finance.model.Settlements;
import com.HRMS.QuickDines.Finance.model.TaxReports;
import com.HRMS.QuickDines.Finance.model.Transactions;
import com.HRMS.QuickDines.Finance.repo.ExpensesRepository;
import com.HRMS.QuickDines.Finance.repo.SettlementsRepository;
import com.HRMS.QuickDines.Finance.repo.TaxReportsRepository;
import com.HRMS.QuickDines.Finance.repo.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final EmployeeRepository employeeRepository;
    private final ExpensesRepository expensesRepository;
    private final SettlementsRepository settlementsRepository;
    private final TaxReportsRepository taxReportsRepository;
    private final TransactionsRepository transactionsRepository;


    //=================================
// EXPENSES
//=================================

    public String createExpense(String employeeId, Expenses expense){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        expense.setEmployee(employee);

        expensesRepository.save(expense);

        return "Expense Created Successfully";
    }



    public Object getExpenses(){

        return expensesRepository.findAll();
    }



    public Object getExpense(Long id){

        return expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));
    }



    public String updateExpense(Long id, Expenses expense){

        Expenses existingExpense = expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));

        existingExpense.setExpenseTitle(expense.getExpenseTitle());

        existingExpense.setExpenseCategory(expense.getExpenseCategory());

        existingExpense.setAmount(expense.getAmount());

        existingExpense.setDescription(expense.getDescription());

        existingExpense.setExpenseDate(expense.getExpenseDate());

        existingExpense.setApprovedBy(expense.getApprovedBy());

        existingExpense.setStatus(expense.getStatus());

        expensesRepository.save(existingExpense);

        return "Expense Updated Successfully";
    }



    public String deleteExpense(Long id){

        Expenses expense = expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));

        expensesRepository.delete(expense);

        return "Expense Deleted Successfully";
    }

    //=================================
// TRANSACTIONS
//=================================

    public String createTransaction(String employeeId, Transactions transaction){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        transaction.setEmployee(employee);

        transactionsRepository.save(transaction);

        return "Transaction Created Successfully";
    }



    public Object getTransactions(){

        return transactionsRepository.findAll();
    }



    public Object getTransaction(Long id){

        return transactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
    }



    public String updateTransaction(Long id, Transactions transaction){

        Transactions existingTransaction = transactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));

        existingTransaction.setTransactionId(transaction.getTransactionId());

        existingTransaction.setTransactionType(transaction.getTransactionType());

        existingTransaction.setAmount(transaction.getAmount());

        existingTransaction.setPaymentMethod(transaction.getPaymentMethod());

        existingTransaction.setTransactionStatus(transaction.getTransactionStatus());

        existingTransaction.setTransactionDate(transaction.getTransactionDate());

        existingTransaction.setRemarks(transaction.getRemarks());

        transactionsRepository.save(existingTransaction);

        return "Transaction Updated Successfully";
    }



    public String deleteTransaction(Long id){

        Transactions transaction = transactionsRepository.findById(id).orElseThrow(() ->
                                new RuntimeException("Transaction Not Found"));
        transactionsRepository.delete(transaction);

        return "Transaction Deleted Successfully";
    }

    //=================================
// SETTLEMENTS
//=================================

    public String createSettlement(String employeeId, Settlements settlement){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        settlement.setEmployee(employee);

        settlementsRepository.save(settlement);

        return "Settlement Created Successfully";
    }



    public Object getSettlements(){

        return settlementsRepository.findAll();
    }



    public Object getSettlement(
            Long id){

        return settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));
    }



    public String updateSettlement(Long id, Settlements settlement){

        Settlements existingSettlement = settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));

        existingSettlement.setSettlementAmount(settlement.getSettlementAmount());

        existingSettlement.setSettlementType(settlement.getSettlementType());

        existingSettlement.setSettlementStatus(settlement.getSettlementStatus());

        existingSettlement.setApprovedBy(settlement.getApprovedBy());

        existingSettlement.setSettlementDate(settlement.getSettlementDate());

        existingSettlement.setRemarks(settlement.getRemarks());

        settlementsRepository.save(existingSettlement);

        return "Settlement Updated Successfully";
    }



    public String deleteSettlement(Long id){

        Settlements settlement = settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));

        settlementsRepository.delete(settlement);

        return "Settlement Deleted Successfully";
    }
    //=================================
// TAX REPORTS
//=================================

    public String createTaxReport(String employeeId, TaxReports taxReport){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        taxReport.setEmployee(employee);

        taxReportsRepository.save(taxReport);

        return "Tax Report Created Successfully";
    }



    public Object getTaxReports(){

        return taxReportsRepository.findAll();
    }



    public List<TaxReports> getTaxReport(String employeeId){

        return Collections.singletonList(taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found")));
    }



    public String updateTaxReport(String employeeId, TaxReports taxReport){

        TaxReports existingTaxReport = taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found"));

        existingTaxReport.setFinancialYear(taxReport.getFinancialYear());

        existingTaxReport.setTotalSalary(taxReport.getTotalSalary());

        existingTaxReport.setTotalTds(taxReport.getTotalTds());

        existingTaxReport.setTotalPf(taxReport.getTotalPf());

        existingTaxReport.setTotalEsi(taxReport.getTotalEsi());

        existingTaxReport.setNetIncome(taxReport.getNetIncome());

        taxReportsRepository.save(existingTaxReport);

        return "Tax Report Updated Successfully";
    }



    public String deleteTaxReport(String employeeId){

        TaxReports taxReport = taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found"));

        taxReportsRepository.delete(taxReport);

        return "Tax Report Deleted Successfully";
    }

    //=================================
// REPORTS
//=================================

    public List<Expenses> approvedExpenses(){

        return expensesRepository.findByStatus("APPROVED");
    }



    public List<Expenses> pendingExpenses(){

        return expensesRepository.findByStatus("PENDING");
    }



    public List<Expenses> rejectedExpenses(){

        return expensesRepository.findByStatus("REJECTED");
    }



    public List<Transactions> successfulTransactions(){

        return transactionsRepository.findByTransactionStatus("SUCCESS");
    }



    public List<Transactions> failedTransactions(){

        return transactionsRepository.findByTransactionStatus("FAILED");
    }



    public List<Settlements> completedSettlements(){

        return settlementsRepository.findBySettlementStatus("COMPLETED");
    }



    public Object pendingSettlements(){

        return settlementsRepository
                .findBySettlementStatus("PENDING");
    }


    //=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts() {

        Map<String, Object> counts = new HashMap<>();

        // Expenses Counts
        counts.put("totalExpenses", expensesRepository.count());

        counts.put("approvedExpenses", expensesRepository.countByStatus("APPROVED"));

        counts.put("pendingExpenses", expensesRepository.countByStatus("PENDING"));

        counts.put("rejectedExpenses", expensesRepository.countByStatus("REJECTED"));


        // Transactions Counts
        counts.put("totalTransactions", transactionsRepository.count());

        counts.put("successfulTransactions", transactionsRepository.countByTransactionStatus("SUCCESS"));

        counts.put("failedTransactions", transactionsRepository.countByTransactionStatus("FAILED"));


        // Settlements Counts
        counts.put("totalSettlements", settlementsRepository.count());

        counts.put("completedSettlements", settlementsRepository.countBySettlementStatus("COMPLETED"));

        counts.put("pendingSettlements", settlementsRepository.countBySettlementStatus("PENDING"));


        // Tax Reports Count
        counts.put("totalTaxReports", taxReportsRepository.count());

        return counts;
    }
}
