package com.HRMS.QuickDines.Finance.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Finance.model.*;
import com.HRMS.QuickDines.Finance.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


    // =========================================================
    // LOGGED-IN EMPLOYEE
    // =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }


    // =========================================================
    // CLIENT INFORMATION
    // =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }

    //=================================
// EXPENSES
//=================================

    public String createExpense(String employeeId, Expenses expense) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        expense.setEmployee(employee);

        expensesRepository.save(expense);
        String performedBy = getLoggedInEmployeeId();
        // AUDIT LOG
        auditLogsService.logCreate("FINANCE", String.valueOf(expense.getId()), employeeId, employeeId, "Expense created successfully");

        // ACTIVITY LOG
        auditLogsService.logActivity(employeeId, "CREATE_EXPENSE", "FINANCE", "Employee created a new expense", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        // SYSTEM LOG
        auditLogsService.logInfo("FINANCE", "FinanceService", "Expense created successfully");

        return "Expense Created Successfully";
    }


    public Object getExpenses() {

        return expensesRepository.findAll();
    }


    public Object getExpense(Long id) {

        return expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));
    }


    public String updateExpense(Long id, Expenses expense) {

        // =========================================================
        // FIND EXISTING EXPENSE
        // =========================================================

        Expenses existingExpense = expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));

        // =========================================================
        // CAPTURE OLD VALUE BEFORE UPDATE
        // =========================================================

        String oldValue = convertToJson(existingExpense);

        // =========================================================
        // UPDATE EXPENSE
        // =========================================================

        existingExpense.setExpenseTitle(expense.getExpenseTitle());

        existingExpense.setExpenseCategory(expense.getExpenseCategory());

        existingExpense.setAmount(expense.getAmount());

        existingExpense.setDescription(expense.getDescription());

        existingExpense.setExpenseDate(expense.getExpenseDate());

        existingExpense.setApprovedBy(expense.getApprovedBy());

        existingExpense.setStatus(expense.getStatus());

        // =========================================================
        // SAVE UPDATED EXPENSE
        // =========================================================

        Expenses updatedExpense = expensesRepository.save(existingExpense);

        // =========================================================
        // CAPTURE NEW VALUE AFTER UPDATE
        // =========================================================

        String newValue = convertToJson(updatedExpense);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = existingExpense.getEmployee().getEmployeeId();

        // =========================================================
        // AUDIT LOG
        // =========================================================

        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, employeeId, "Expense updated successfully", oldValue, newValue);

        // =========================================================
        // ACTIVITY LOG
        // =========================================================

        auditLogsService.logActivity(performedBy, "UPDATE_EXPENSE", "FINANCE", "Expense updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        // =========================================================
        // SYSTEM LOG
        // =========================================================

        auditLogsService.logInfo("FINANCE", "FinanceService", "Expense updated successfully");

        return "Expense Updated Successfully";
    }


    public String deleteExpense(Long id) {

        // =========================
        // FIND EXISTING EXPENSE
        // =========================
        Expenses expense = expensesRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));

        // =========================
        // CAPTURE DELETED DATA
        // =========================
        String deletedValue = convertToJson(expense);

        // =========================
        // GET EMPLOYEE ID
        // =========================
        String employeeId = null;

        if (expense.getEmployee() != null) {
            employeeId = expense.getEmployee().getEmployeeId();
        }

        // =========================
        // GET LOGGED-IN USER
        // =========================
        String performedBy = getLoggedInEmployeeId();

        // =========================
        // DELETE RECORD
        // =========================
        expensesRepository.delete(expense);

        // =========================
        // AUDIT LOG
        // =========================
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Expense deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        // =========================
        // ACTIVITY LOG
        // =========================
        if (employeeId != null) {

            auditLogsService.logActivity(employeeId, "DELETE_EXPENSE", "FINANCE", "Expense deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        }

        // =========================
        // SYSTEM LOG
        // =========================
        auditLogsService.logInfo("FINANCE", "FinanceService", "Expense deleted successfully");

        return "Expense Deleted Successfully";
    }


    //=================================
// TRANSACTIONS
//=================================

    public String createTransaction(String employeeId, Transactions transaction) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        transaction.setEmployee(employee);

        transactionsRepository.save(transaction);
        String performedBy = getLoggedInEmployeeId();
        // AUDIT
        auditLogsService.logCreate("FINANCE", String.valueOf(transaction.getId()), performedBy, employeeId, "Transaction created successfully");
        // ACTIVITY
        auditLogsService.logActivity(employeeId, "CREATE_TRANSACTION", "FINANCE", "Transaction created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        // SYSTEM
        auditLogsService.logInfo("FINANCE", "FinanceService", "Transaction created successfully");
        return "Transaction Created Successfully";
    }


    public Object getTransactions() {

        return transactionsRepository.findAll();
    }


    public Object getTransaction(Long id) {

        return transactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
    }


    public String updateTransaction(Long id, Transactions transaction) {
        Transactions existingTransaction = transactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
        String oldValue = convertToJson(existingTransaction);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = null;
        if (existingTransaction.getEmployee() != null) {
            employeeId = existingTransaction.getEmployee().getEmployeeId();
        }
        existingTransaction.setTransactionId(transaction.getTransactionId());
        existingTransaction.setTransactionType(transaction.getTransactionType());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setPaymentMethod(transaction.getPaymentMethod());
        existingTransaction.setTransactionStatus(transaction.getTransactionStatus());
        existingTransaction.setTransactionDate(transaction.getTransactionDate());
        existingTransaction.setRemarks(transaction.getRemarks());
        Transactions updatedTransaction = transactionsRepository.save(existingTransaction);
        String newValue = convertToJson(updatedTransaction);
        // AUDIT
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, employeeId, "Transaction updated successfully", oldValue, newValue);
        // ACTIVITY
        if (employeeId != null) {
            auditLogsService.logActivity(employeeId, "UPDATE_TRANSACTION", "FINANCE", "Transaction updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        }
        // SYSTEM
        auditLogsService.logInfo("FINANCE", "FinanceService", "Transaction updated successfully");

        return "Transaction Updated Successfully";
    }


    public String deleteTransaction(Long id) {

        Transactions transaction = transactionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
        String deletedValue = convertToJson(transaction);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = null;
        if (transaction.getEmployee() != null) {
            employeeId = transaction.getEmployee().getEmployeeId();
        }
        transactionsRepository.delete(transaction);
        // AUDIT
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Transaction deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());
        // ACTIVITY
        if (employeeId != null) {
            auditLogsService.logActivity(employeeId, "DELETE_TRANSACTION", "FINANCE", "Transaction deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        }
        // SYSTEM
        auditLogsService.logInfo("FINANCE", "FinanceService", "Transaction deleted successfully");
        return "Transaction Deleted Successfully";
    }

    //=================================
// SETTLEMENTS
//=================================

    public String createSettlement(String employeeId, Settlements settlement) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        settlement.setEmployee(employee);

        settlementsRepository.save(settlement);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(settlement.getId()), performedBy, employeeId, "Settlement created successfully");
        auditLogsService.logActivity(employeeId, "CREATE_SETTLEMENT", "FINANCE", "Settlement created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Settlement created successfully");
        return "Settlement Created Successfully";
    }


    public Object getSettlements() {

        return settlementsRepository.findAll();
    }


    public Object getSettlement(Long id) {

        return settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));
    }


    public String updateSettlement(Long id, Settlements settlement) {

        Settlements existingSettlement = settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));

        existingSettlement.setSettlementAmount(settlement.getSettlementAmount());

        existingSettlement.setSettlementType(settlement.getSettlementType());

        existingSettlement.setSettlementStatus(settlement.getSettlementStatus());

        existingSettlement.setApprovedBy(settlement.getApprovedBy());

        existingSettlement.setSettlementDate(settlement.getSettlementDate());

        existingSettlement.setRemarks(settlement.getRemarks());

        settlementsRepository.save(existingSettlement);
        String oldValue = convertToJson(existingSettlement);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = null;
        if (existingSettlement.getEmployee() != null) {
            employeeId = existingSettlement.getEmployee().getEmployeeId();
        }
        // existing setters remain unchanged
        Settlements updatedSettlement = settlementsRepository.save(existingSettlement);
        String newValue = convertToJson(updatedSettlement);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, employeeId, "Settlement updated successfully", oldValue, newValue);
        auditLogsService.logActivity(employeeId, "UPDATE_SETTLEMENT", "FINANCE", "Settlement updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Settlement updated successfully");

        return "Settlement Updated Successfully";
    }


    public String deleteSettlement(Long id) {

        Settlements settlement = settlementsRepository.findById(id).orElseThrow(() -> new RuntimeException("Settlement Not Found"));

        settlementsRepository.delete(settlement);
        String deletedValue = convertToJson(settlement);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = null;
        if (settlement.getEmployee() != null) {
            employeeId = settlement.getEmployee().getEmployeeId();
        }
        settlementsRepository.delete(settlement);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, employeeId, "Settlement deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());
        if (employeeId != null) {
            auditLogsService.logActivity(employeeId, "DELETE_SETTLEMENT", "FINANCE", "Settlement deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        }
        auditLogsService.logInfo("FINANCE", "FinanceService", "Settlement deleted successfully");
        return "Settlement Deleted Successfully";
    }
    //=================================
// TAX REPORTS
//=================================

    public String createTaxReport(String employeeId, TaxReports taxReport) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        taxReport.setEmployee(employee);
        String performedBy = getLoggedInEmployeeId();
        taxReportsRepository.save(taxReport);
        auditLogsService.logCreate("FINANCE", employeeId, performedBy, employeeId, "Tax report created successfully");
        auditLogsService.logActivity(employeeId, "CREATE_TAX_REPORT", "FINANCE", "Tax report created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Tax report created successfully");

        return "Tax Report Created Successfully";
    }


    public Object getTaxReports() {

        return taxReportsRepository.findAll();
    }


    public List<TaxReports> getTaxReport(String employeeId) {

        return Collections.singletonList(taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found")));
    }


    public String updateTaxReport(String employeeId, TaxReports taxReport) {

        TaxReports existingTaxReport = taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found"));

        existingTaxReport.setFinancialYear(taxReport.getFinancialYear());

        existingTaxReport.setTotalSalary(taxReport.getTotalSalary());

        existingTaxReport.setTotalTds(taxReport.getTotalTds());

        existingTaxReport.setTotalPf(taxReport.getTotalPf());

        existingTaxReport.setTotalEsi(taxReport.getTotalEsi());

        existingTaxReport.setNetIncome(taxReport.getNetIncome());

        taxReportsRepository.save(existingTaxReport);
        String oldValue =
                convertToJson(existingTaxReport);

        String performedBy =
                getLoggedInEmployeeId();

        //After save

        String newValue =
                convertToJson(existingTaxReport);

        auditLogsService.logUpdate(
                "FINANCE",
                employeeId,
                performedBy,
                employeeId,
                "Tax report updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_TAX_REPORT",
                "FINANCE",
                "Tax report updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "FINANCE",
                "FinanceService",
                "Tax report updated successfully"
        );
        return "Tax Report Updated Successfully";
    }


    public String deleteTaxReport(String employeeId) {

        TaxReports taxReport = taxReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Tax Report Not Found"));

        taxReportsRepository.delete(taxReport);
        String deletedValue =
                convertToJson(taxReport);

        String performedBy =
                getLoggedInEmployeeId();

        //Then:

        taxReportsRepository.delete(taxReport);

        auditLogsService.createAuditLog(
                "FINANCE",
                employeeId,
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                employeeId,
                "Tax report deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_TAX_REPORT",
                "FINANCE",
                "Tax report deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "FINANCE",
                "FinanceService",
                "Tax report deleted successfully"
        );
        return "Tax Report Deleted Successfully";
    }

    //=================================
// REPORTS
//=================================

    public List<Expenses> approvedExpenses() {

        return expensesRepository.findByStatus("APPROVED");
    }


    public List<Expenses> pendingExpenses() {

        return expensesRepository.findByStatus("PENDING");
    }


    public List<Expenses> rejectedExpenses() {

        return expensesRepository.findByStatus("REJECTED");
    }


    public List<Transactions> successfulTransactions() {

        return transactionsRepository.findByTransactionStatus("SUCCESS");
    }


    public List<Transactions> failedTransactions() {

        return transactionsRepository.findByTransactionStatus("FAILED");
    }


    public List<Settlements> completedSettlements() {

        return settlementsRepository.findBySettlementStatus("COMPLETED");
    }


    public Object pendingSettlements() {

        return settlementsRepository.findBySettlementStatus("PENDING");
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

    public String createInvoice(InvoiceManagement invoice) {

        invoiceManagementRepository.save(invoice);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(invoice.getId()), performedBy, null, "Invoice created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_INVOICE", "FINANCE", "Invoice created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Invoice created successfully");

        return "Invoice Created Successfully";
    }


    public List<InvoiceManagement> getInvoices() {

        return invoiceManagementRepository.findAll();
    }


    public InvoiceManagement getInvoice(Long id) {

        return invoiceManagementRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice Not Found"));
    }


    public String updateInvoice(Long id, InvoiceManagement invoice) {

        InvoiceManagement existing = invoiceManagementRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice Not Found"));

        existing.setInvoiceNumber(invoice.getInvoiceNumber());

        existing.setCustomer(invoice.getCustomer());

        existing.setVendor(invoice.getVendor());

        existing.setInvoiceType(invoice.getInvoiceType());

        existing.setInvoiceDate(invoice.getInvoiceDate());

        existing.setDueDate(invoice.getDueDate());

        existing.setSubtotal(invoice.getSubtotal());

        existing.setTaxAmount(invoice.getTaxAmount());

        existing.setDiscount(invoice.getDiscount());

        existing.setTotalAmount(invoice.getTotalAmount());

        existing.setPaymentStatus(invoice.getPaymentStatus());

        invoiceManagementRepository.save(existing);
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // existing setters
        InvoiceManagement updated = invoiceManagementRepository.save(existing);
        String newValue = convertToJson(updated);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, null, "Invoice updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_INVOICE", "FINANCE", "Invoice updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Invoice updated successfully");

        return "Invoice Updated Successfully";
    }


    public String deleteInvoice(Long id) {

        InvoiceManagement existing = invoiceManagementRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice Not Found"));

        invoiceManagementRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        invoiceManagementRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "Invoice deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());
        auditLogsService.logActivity(performedBy, "DELETE_INVOICE", "FINANCE", "Invoice deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Invoice deleted successfully");
        return "Invoice Deleted Successfully";
    }


    //=========================================================
    // ACCOUNTS PAYABLE
    //=========================================================

    public String createPayable(AccountsPayable payable) {

        accountsPayableRepository.save(payable);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(payable.getId()), performedBy, null, "payable created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_PAYABLE", "FINANCE", "payable created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "payable created successfully");

        return "Accounts Payable Created Successfully";
    }


    public List<AccountsPayable> getPayables() {

        return accountsPayableRepository.findAll();
    }


    public AccountsPayable getPayable(Long id) {

        return accountsPayableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Payable Not Found"));
    }


    public String updatePayable(Long id, AccountsPayable payable) {

        AccountsPayable existing = accountsPayableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Payable Not Found"));

        existing.setVendor(payable.getVendor());

        existing.setInvoice(payable.getInvoice());

        existing.setInvoiceNumber(payable.getInvoiceNumber());

        existing.setInvoiceDate(payable.getInvoiceDate());

        existing.setDueDate(payable.getDueDate());

        existing.setTotalAmount(payable.getTotalAmount());

        existing.setPaidAmount(payable.getPaidAmount());

        existing.setBalanceAmount(payable.getBalanceAmount());

        existing.setPaymentStatus(payable.getPaymentStatus());

        accountsPayableRepository.save(existing);
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // your existing setters
        AccountsPayable updated = accountsPayableRepository.save(existing);
        String newValue = convertToJson(updated);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, null, "Accounts payable updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_PAYABLE", "FINANCE", "Accounts payable updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Accounts payable updated successfully");
        return "Accounts Payable Updated Successfully";
    }


    public String deletePayable(Long id) {

        AccountsPayable existing = accountsPayableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Payable Not Found"));

        accountsPayableRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        accountsPayableRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "Accounts payable deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        return "Accounts Payable Deleted Successfully";
    }


    //=========================================================
    // ACCOUNTS RECEIVABLE
    //=========================================================

    public String createReceivable(AccountsReceivable receivable) {

        accountsReceivableRepository.save(receivable);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(receivable.getId()), performedBy, null, "receivable created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_RECEIVABLE", "FINANCE", "receivable created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "receivable created successfully");

        return "Accounts Receivable Created Successfully";
    }


    public List<AccountsReceivable> getReceivables() {

        return accountsReceivableRepository.findAll();
    }


    public AccountsReceivable getReceivable(Long id) {

        return accountsReceivableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Receivable Not Found"));
    }


    public String updateReceivable(Long id, AccountsReceivable receivable) {

        AccountsReceivable existing = accountsReceivableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Receivable Not Found"));

        existing.setCustomer(receivable.getCustomer());

        existing.setInvoice(receivable.getInvoice());

        existing.setInvoiceNumber(receivable.getInvoiceNumber());

        existing.setInvoiceDate(receivable.getInvoiceDate());

        existing.setDueDate(receivable.getDueDate());

        existing.setInvoiceAmount(receivable.getInvoiceAmount());

        existing.setReceivedAmount(receivable.getReceivedAmount());

        existing.setBalanceAmount(receivable.getBalanceAmount());

        existing.setPaymentStatus(receivable.getPaymentStatus());

        accountsReceivableRepository.save(existing);
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // existing setters
        AccountsReceivable updated = accountsReceivableRepository.save(existing);
        String newValue = convertToJson(updated);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, null, "Accounts receivable updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_RECEIVABLE", "FINANCE", "Accounts receivable updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Accounts receivable updated successfully");

        return "Accounts Receivable Updated Successfully";
    }


    public String deleteReceivable(Long id) {

        AccountsReceivable existing = accountsReceivableRepository.findById(id).orElseThrow(() -> new RuntimeException("Accounts Receivable Not Found"));

        accountsReceivableRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // accountsPayableRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "Account receivable deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        return "Accounts Receivable Deleted Successfully";
    }


    //=========================================================
    // GENERAL LEDGER
    //=========================================================

    public String createLedger(GeneralLedger ledger) {

        generalLedgerRepository.save(ledger);

        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(ledger.getId()), performedBy, null, "ledger created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_LEDGER", "FINANCE", "receivable created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "ledger created successfully");
        return "General Ledger Created Successfully";
    }


    public List<GeneralLedger> getLedgers() {

        return generalLedgerRepository.findAll();
    }


    public GeneralLedger getLedger(Long id) {

        return generalLedgerRepository.findById(id).orElseThrow(() -> new RuntimeException("General Ledger Not Found"));
    }


    public String updateLedger(Long id, GeneralLedger ledger) {

        GeneralLedger existing = generalLedgerRepository.findById(id).orElseThrow(() -> new RuntimeException("General Ledger Not Found"));

        existing.setAccountCode(ledger.getAccountCode());

        existing.setAccountName(ledger.getAccountName());

        existing.setTransaction(ledger.getTransaction());

        existing.setDebitAmount(ledger.getDebitAmount());

        existing.setCreditAmount(ledger.getCreditAmount());

        existing.setBalance(ledger.getBalance());

        existing.setTransactionDate(ledger.getTransactionDate());

        existing.setRemarks(ledger.getRemarks());

        generalLedgerRepository.save(existing);
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // keep all your existing setters
        GeneralLedger updated = generalLedgerRepository.save(existing);
        String newValue = convertToJson(updated);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, null, "General ledger updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_LEDGER", "FINANCE", "General ledger updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "General ledger updated successfully");
        return "General Ledger Updated Successfully";
    }


    public String deleteLedger(Long id) {

        GeneralLedger existing = generalLedgerRepository.findById(id).orElseThrow(() -> new RuntimeException("General Ledger Not Found"));

        generalLedgerRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        generalLedgerRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "General ledger deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());
        return "General Ledger Deleted Successfully";
    }


    //=========================================================
    // BUDGET MANAGEMENT
    //=========================================================

    public String createBudget(Budget budget) {

        calculateRemainingBudget(budget);

        budgetRepository.save(budget);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(budget.getId()), performedBy, null, "Budget created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_BUDGET", "FINANCE", "Budget created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Budget created successfully");

        return "Budget Created Successfully";
    }


    public List<Budget> getBudgets() {

        return budgetRepository.findAll();
    }


    public Budget getBudget(Long id) {

        return budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget Not Found"));
    }


    public String updateBudget(Long id, Budget budget) {

        Budget existing = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget Not Found"));

        existing.setDepartment(budget.getDepartment());

        existing.setBudgetName(budget.getBudgetName());

        existing.setFinancialYear(budget.getFinancialYear());

        existing.setAllocatedAmount(budget.getAllocatedAmount());

        existing.setUtilizedAmount(budget.getUtilizedAmount());

        calculateRemainingBudget(existing);

        existing.setApprovedBy(budget.getApprovedBy());

        existing.setStatus(budget.getStatus());

        budgetRepository.save(existing);
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();

        // Then after your existing setters and save:

        String newValue = convertToJson(existing);

        auditLogsService.logUpdate(
                "FINANCE",
                String.valueOf(id),
                performedBy,
                existing.getId().toString(),
                "Budget updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_BUDGET",
                "FINANCE",
                "Budget updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "FINANCE",
                "FinanceService",
                "Budget updated successfully"
        );

        return "Budget Updated Successfully";
    }


    public String deleteBudget(Long id) {

        Budget existing = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget Not Found"));

        budgetRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        budgetRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existing.getId().toString(), "Budget deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        return "Budget Deleted Successfully";
    }


    private void calculateRemainingBudget(Budget budget) {

        if (budget.getAllocatedAmount() != null && budget.getUtilizedAmount() != null) {

            budget.setRemainingAmount(budget.getAllocatedAmount().subtract(budget.getUtilizedAmount()));
        }
    }


    //=========================================================
    // PROFIT & LOSS
    //=========================================================

    public String createProfitLoss(ProfitLoss profitLoss) {

        calculateProfitLoss(profitLoss);

        profitLossRepository.save(profitLoss);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(profitLoss.getId()), performedBy, null, "profit created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_PROFIT_LOSS", "FINANCE", "profit created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "profit created successfully");


        return "Profit & Loss Created Successfully";
    }


    public List<ProfitLoss> getProfitLosses() {

        return profitLossRepository.findAll();
    }


    public ProfitLoss getProfitLoss(Long id) {

        return profitLossRepository.findById(id).orElseThrow(() -> new RuntimeException("Profit & Loss Not Found"));
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
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // keep your existing setters
        profitLossRepository.save(existing);
        String newValue = convertToJson(existing);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, existing.getId().toString(), "Profit & Loss updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_PROFIT_LOSS", "FINANCE", "Profit & Loss updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Profit & Loss updated successfully");

        return "Profit & Loss Updated Successfully";
    }


    public String deleteProfitLoss(Long id) {

        ProfitLoss existing = profitLossRepository.findById(id).orElseThrow(() -> new RuntimeException("Profit & Loss Not Found"));

        profitLossRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        profitLossRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "Profit & Loss deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

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
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("FINANCE", String.valueOf(balanceSheet.getId()), performedBy, null, "balance sheet created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_BALANCE_SHEET", "FINANCE", "balance sheet created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "balance sheet created successfully");

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
        String oldValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        // your existing setters
        balanceSheetRepository.save(existing);
        String newValue = convertToJson(existing);
        auditLogsService.logUpdate("FINANCE", String.valueOf(id), performedBy, existing.getId().toString(), "Balance sheet updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_BALANCE_SHEET", "FINANCE", "Balance sheet updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("FINANCE", "FinanceService", "Balance sheet updated successfully");

        return "Balance Sheet Updated Successfully";
    }


    public String deleteBalanceSheet(Long id) {

        BalanceSheet existing = balanceSheetRepository.findById(id).orElseThrow(() -> new RuntimeException("Balance Sheet Not Found"));

        balanceSheetRepository.delete(existing);
        String deletedValue = convertToJson(existing);
        String performedBy = getLoggedInEmployeeId();
        balanceSheetRepository.delete(existing);
        auditLogsService.createAuditLog("FINANCE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, null, "Balance sheet deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

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

