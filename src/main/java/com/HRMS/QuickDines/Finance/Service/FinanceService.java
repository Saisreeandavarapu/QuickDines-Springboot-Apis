package com.HRMS.QuickDines.Finance.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Finance.model.*;
import com.HRMS.QuickDines.Finance.repo.*;
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
    private final InvoiceManagementRepository invoiceManagementRepository;
    private final AccountsPayableRepository accountsPayableRepository;
    private final AccountsReceivableRepository accountsReceivableRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final BudgetRepository budgetRepository;
    private final ProfitLossRepository profitLossRepository;
    private final BalanceSheetRepository balanceSheetRepository;


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


        //=========================================================
        // INVOICE MANAGEMENT
        //=========================================================

        public String createInvoice(
                InvoiceManagement invoice) {

            invoiceManagementRepository.save(invoice);

            return "Invoice Created Successfully";
        }


        public List<InvoiceManagement> getInvoices() {

            return invoiceManagementRepository.findAll();
        }


        public InvoiceManagement getInvoice(Long id) {

            return invoiceManagementRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Invoice Not Found"));
        }


        public String updateInvoice(
                Long id,
                InvoiceManagement invoice) {

            InvoiceManagement existing =
                    invoiceManagementRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Invoice Not Found"));

            existing.setInvoiceNumber(
                    invoice.getInvoiceNumber());

            existing.setCustomer(
                    invoice.getCustomer());

            existing.setVendor(
                    invoice.getVendor());

            existing.setInvoiceType(
                    invoice.getInvoiceType());

            existing.setInvoiceDate(
                    invoice.getInvoiceDate());

            existing.setDueDate(
                    invoice.getDueDate());

            existing.setSubtotal(
                    invoice.getSubtotal());

            existing.setTaxAmount(
                    invoice.getTaxAmount());

            existing.setDiscount(
                    invoice.getDiscount());

            existing.setTotalAmount(
                    invoice.getTotalAmount());

            existing.setPaymentStatus(
                    invoice.getPaymentStatus());

            invoiceManagementRepository.save(existing);

            return "Invoice Updated Successfully";
        }


        public String deleteInvoice(Long id) {

            InvoiceManagement existing =
                    invoiceManagementRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Invoice Not Found"));

            invoiceManagementRepository.delete(existing);

            return "Invoice Deleted Successfully";
        }


        //=========================================================
        // ACCOUNTS PAYABLE
        //=========================================================

        public String createPayable(
                AccountsPayable payable) {

            accountsPayableRepository.save(payable);

            return "Accounts Payable Created Successfully";
        }


        public List<AccountsPayable> getPayables() {

            return accountsPayableRepository.findAll();
        }


        public AccountsPayable getPayable(Long id) {

            return accountsPayableRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Accounts Payable Not Found"));
        }


        public String updatePayable(
                Long id,
                AccountsPayable payable) {

            AccountsPayable existing =
                    accountsPayableRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Accounts Payable Not Found"));

            existing.setVendor(payable.getVendor());

            existing.setInvoice(payable.getInvoice());

            existing.setInvoiceNumber(
                    payable.getInvoiceNumber());

            existing.setInvoiceDate(
                    payable.getInvoiceDate());

            existing.setDueDate(
                    payable.getDueDate());

            existing.setTotalAmount(
                    payable.getTotalAmount());

            existing.setPaidAmount(
                    payable.getPaidAmount());

            existing.setBalanceAmount(
                    payable.getBalanceAmount());

            existing.setPaymentStatus(
                    payable.getPaymentStatus());

            accountsPayableRepository.save(existing);

            return "Accounts Payable Updated Successfully";
        }


        public String deletePayable(Long id) {

            AccountsPayable existing =
                    accountsPayableRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Accounts Payable Not Found"));

            accountsPayableRepository.delete(existing);

            return "Accounts Payable Deleted Successfully";
        }


        //=========================================================
        // ACCOUNTS RECEIVABLE
        //=========================================================

        public String createReceivable(
                AccountsReceivable receivable) {

            accountsReceivableRepository.save(receivable);

            return "Accounts Receivable Created Successfully";
        }


        public List<AccountsReceivable> getReceivables() {

            return accountsReceivableRepository.findAll();
        }


        public AccountsReceivable getReceivable(Long id) {

            return accountsReceivableRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Accounts Receivable Not Found"));
        }


        public String updateReceivable(
                Long id,
                AccountsReceivable receivable) {

            AccountsReceivable existing =
                    accountsReceivableRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Accounts Receivable Not Found"));

            existing.setCustomer(
                    receivable.getCustomer());

            existing.setInvoice(
                    receivable.getInvoice());

            existing.setInvoiceNumber(
                    receivable.getInvoiceNumber());

            existing.setInvoiceDate(
                    receivable.getInvoiceDate());

            existing.setDueDate(
                    receivable.getDueDate());

            existing.setInvoiceAmount(
                    receivable.getInvoiceAmount());

            existing.setReceivedAmount(
                    receivable.getReceivedAmount());

            existing.setBalanceAmount(
                    receivable.getBalanceAmount());

            existing.setPaymentStatus(
                    receivable.getPaymentStatus());

            accountsReceivableRepository.save(existing);

            return "Accounts Receivable Updated Successfully";
        }


        public String deleteReceivable(Long id) {

            AccountsReceivable existing =
                    accountsReceivableRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Accounts Receivable Not Found"));

            accountsReceivableRepository.delete(existing);

            return "Accounts Receivable Deleted Successfully";
        }


        //=========================================================
        // GENERAL LEDGER
        //=========================================================

        public String createLedger(
                GeneralLedger ledger) {

            generalLedgerRepository.save(ledger);

            return "General Ledger Created Successfully";
        }


        public List<GeneralLedger> getLedgers() {

            return generalLedgerRepository.findAll();
        }


        public GeneralLedger getLedger(Long id) {

            return generalLedgerRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "General Ledger Not Found"));
        }


        public String updateLedger(
                Long id,
                GeneralLedger ledger) {

            GeneralLedger existing =
                    generalLedgerRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "General Ledger Not Found"));

            existing.setAccountCode(
                    ledger.getAccountCode());

            existing.setAccountName(
                    ledger.getAccountName());

            existing.setTransaction(
                    ledger.getTransaction());

            existing.setDebitAmount(
                    ledger.getDebitAmount());

            existing.setCreditAmount(
                    ledger.getCreditAmount());

            existing.setBalance(
                    ledger.getBalance());

            existing.setTransactionDate(
                    ledger.getTransactionDate());

            existing.setRemarks(
                    ledger.getRemarks());

            generalLedgerRepository.save(existing);

            return "General Ledger Updated Successfully";
        }


        public String deleteLedger(Long id) {

            GeneralLedger existing =
                    generalLedgerRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "General Ledger Not Found"));

            generalLedgerRepository.delete(existing);

            return "General Ledger Deleted Successfully";
        }


        //=========================================================
        // BUDGET MANAGEMENT
        //=========================================================

        public String createBudget(
                Budget budget) {

            calculateRemainingBudget(budget);

            budgetRepository.save(budget);

            return "Budget Created Successfully";
        }


        public List<Budget> getBudgets() {

            return budgetRepository.findAll();
        }


        public Budget getBudget(Long id) {

            return budgetRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Budget Not Found"));
        }


        public String updateBudget(
                Long id,
                Budget budget) {

            Budget existing =
                    budgetRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Budget Not Found"));

            existing.setDepartment(
                    budget.getDepartment());

            existing.setBudgetName(
                    budget.getBudgetName());

            existing.setFinancialYear(
                    budget.getFinancialYear());

            existing.setAllocatedAmount(
                    budget.getAllocatedAmount());

            existing.setUtilizedAmount(
                    budget.getUtilizedAmount());

            calculateRemainingBudget(existing);

            existing.setApprovedBy(
                    budget.getApprovedBy());

            existing.setStatus(
                    budget.getStatus());

            budgetRepository.save(existing);

            return "Budget Updated Successfully";
        }


        public String deleteBudget(Long id) {

            Budget existing =
                    budgetRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Budget Not Found"));

            budgetRepository.delete(existing);

            return "Budget Deleted Successfully";
        }


        private void calculateRemainingBudget(
                Budget budget) {

            if (budget.getAllocatedAmount() != null &&
                    budget.getUtilizedAmount() != null) {

                budget.setRemainingAmount(
                        budget.getAllocatedAmount()
                                .subtract(
                                        budget.getUtilizedAmount()));
            }
        }


        //=========================================================
        // PROFIT & LOSS
        //=========================================================

        public String createProfitLoss(
                ProfitLoss profitLoss) {

            calculateProfitLoss(profitLoss);

            profitLossRepository.save(profitLoss);

            return "Profit & Loss Created Successfully";
        }


        public List<ProfitLoss> getProfitLosses() {

            return profitLossRepository.findAll();
        }


        public ProfitLoss getProfitLoss(Long id) {

            return profitLossRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Profit & Loss Not Found"));
        }


        public String updateProfitLoss(Long id, ProfitLoss profitLoss) {

            ProfitLoss existing = profitLossRepository.findById(id).orElseThrow(() -> new RuntimeException("Profit & Loss Not Found"));

            existing.setFinancialYear(profitLoss.getFinancialYear());

            existing.setTotalIncome(profitLoss.getTotalIncome());

            existing.setTotalExpenses(profitLoss.getTotalExpenses());

            existing.setGrossProfit(profitLoss.getGrossProfit());

            existing.setOperatingExpenses(profitLoss.getOperatingExpenses());

            existing.setNetProfit(profitLoss.getNetProfit());

            existing.setGeneratedBy(profitLoss.getGeneratedBy());

            existing.setGeneratedAt(profitLoss.getGeneratedAt());

            profitLossRepository.save(existing);

            return "Profit & Loss Updated Successfully";
        }


        public String deleteProfitLoss(Long id) {

            ProfitLoss existing = profitLossRepository.findById(id).orElseThrow(() -> new RuntimeException("Profit & Loss Not Found"));

            profitLossRepository.delete(existing);

            return "Profit & Loss Deleted Successfully";
        }


        private void calculateProfitLoss(ProfitLoss profitLoss) {

            if (profitLoss.getTotalIncome() != null && profitLoss.getTotalExpenses() != null) {

                profitLoss.setGrossProfit(profitLoss.getTotalIncome().subtract(profitLoss.getTotalExpenses()));
            }

            if (profitLoss.getGrossProfit() != null && profitLoss.getOperatingExpenses() != null) {

                profitLoss.setNetProfit(profitLoss.getGrossProfit().subtract(profitLoss.getOperatingExpenses()));
            }
        }


        //=========================================================
        // BALANCE SHEET
        //=========================================================

        public String createBalanceSheet(BalanceSheet balanceSheet) {

            calculateClosingBalance(balanceSheet);

            balanceSheetRepository.save(balanceSheet);

            return "Balance Sheet Created Successfully";
        }


        public List<BalanceSheet> getBalanceSheets() {

            return balanceSheetRepository.findAll();
        }


        public BalanceSheet getBalanceSheet(Long id) {

            return balanceSheetRepository.findById(id).orElseThrow(() -> new RuntimeException("Balance Sheet Not Found"));
        }


        public String updateBalanceSheet(Long id, BalanceSheet balanceSheet) {

            BalanceSheet existing = balanceSheetRepository.findById(id).orElseThrow(() -> new RuntimeException("Balance Sheet Not Found"));
            existing.setFinancialYear(balanceSheet.getFinancialYear());

            existing.setTotalAssets(balanceSheet.getTotalAssets());

            existing.setTotalLiabilities(balanceSheet.getTotalLiabilities());

            existing.setShareholderEquity(balanceSheet.getShareholderEquity());

            existing.setCashBalance(balanceSheet.getCashBalance());

            calculateClosingBalance(existing);

            existing.setGeneratedBy(balanceSheet.getGeneratedBy());

            existing.setGeneratedAt(balanceSheet.getGeneratedAt());

            balanceSheetRepository.save(existing);

            return "Balance Sheet Updated Successfully";
        }


        public String deleteBalanceSheet(Long id) {

            BalanceSheet existing = balanceSheetRepository.findById(id).orElseThrow(() -> new RuntimeException("Balance Sheet Not Found"));

            balanceSheetRepository.delete(existing);

            return "Balance Sheet Deleted Successfully";
        }


        private void calculateClosingBalance(BalanceSheet balanceSheet) {

            if (balanceSheet.getCashBalance() != null) {

                balanceSheet.setClosingBalance(balanceSheet.getCashBalance());
            }
        }
    //=========================================================
// ACCOUNTS PAYABLE
//=========================================================

    public Object getAccountsPayableByInvoiceNumber(String invoiceNumber) {

        return accountsPayableRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(() -> new RuntimeException("Accounts Payable Not Found"));
    }


//=========================================================
// ACCOUNTS RECEIVABLE
//=========================================================

    public Object getAccountsReceivableByInvoiceNumber(String invoiceNumber) {

        return accountsReceivableRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(() -> new RuntimeException("Accounts Receivable Not Found"));
    }


//=========================================================
// GENERAL LEDGER
//=========================================================

    public Object getGeneralLedgerByAccountCode(String accountCode) {

        return generalLedgerRepository.findByAccountCode(accountCode);
    }


//=========================================================
// BUDGETS
//=========================================================

    public Object getBudgetsByDepartment(Long departmentId) {

        return budgetRepository.findByDepartmentId(departmentId);
    }


//=========================================================
// PROFIT & LOSS
//=========================================================

    public Object getProfitLossByFinancialYear(String financialYear) {

        return profitLossRepository.findByFinancialYear(financialYear).orElseThrow(() -> new RuntimeException("Profit & Loss Not Found"));
    }


//=========================================================
// BALANCE SHEET
//=========================================================

    public Object getBalanceSheetByFinancialYear(String financialYear) {

        return balanceSheetRepository.findByFinancialYear(financialYear).orElseThrow(() -> new RuntimeException("Balance Sheet Not Found"));
    }


//=========================================================
// INVOICE MANAGEMENT
//=========================================================

    public Object getInvoiceByInvoiceNumber(String invoiceNumber) {

        return invoiceManagementRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(() -> new RuntimeException("Invoice Not Found"));
    }


    public Object getInvoicesByCustomer(Long customerId) {

        return invoiceManagementRepository.findByCustomerId(customerId);
    }


    public Object getInvoicesByVendor(Long vendorId) {

        return invoiceManagementRepository.findByVendorId(vendorId);
    }
    }

