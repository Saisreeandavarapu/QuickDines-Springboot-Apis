package com.HRMS.QuickDines.Finance.Controller;

import com.HRMS.QuickDines.Finance.Service.FinanceService;
import com.HRMS.QuickDines.Finance.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService service;

    public FinanceController(FinanceService service) {
        this.service = service;
    }

    // =========================================================
    // EXPENSES
    // =========================================================

    @PostMapping("/expense/{employeeId}")
    @PreAuthorize("hasAuthority('EXPENSE_CREATE')")
    public ResponseEntity<?> createExpense(
            @PathVariable String employeeId,
            @RequestBody Expenses expense) {

        return ResponseEntity.ok(
                service.createExpense(employeeId, expense));
    }


    @GetMapping("/expenses")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    public ResponseEntity<?> getExpenses() {

        return ResponseEntity.ok(
                service.getExpenses());
    }


    @GetMapping("/expense/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    public ResponseEntity<?> getExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getExpense(id));
    }


    @PutMapping("/expense/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE')")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @RequestBody Expenses expense) {

        return ResponseEntity.ok(
                service.updateExpense(id, expense));
    }


    @DeleteMapping("/expense/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_DELETE')")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteExpense(id));
    }


    // =========================================================
    // TRANSACTIONS
    // =========================================================

    @PostMapping("/transaction/{employeeId}")
    @PreAuthorize("hasAuthority('TRANSACTION_CREATE')")
    public ResponseEntity<?> createTransaction(
            @PathVariable String employeeId,
            @RequestBody Transactions transaction) {

        return ResponseEntity.ok(
                service.createTransaction(employeeId, transaction));
    }


    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('TRANSACTION_VIEW')")
    public ResponseEntity<?> getTransactions() {

        return ResponseEntity.ok(
                service.getTransactions());
    }


    @GetMapping("/transaction/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_VIEW')")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTransaction(id));
    }


    @PutMapping("/transaction/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_UPDATE')")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transactions transaction) {

        return ResponseEntity.ok(
                service.updateTransaction(id, transaction));
    }


    @DeleteMapping("/transaction/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_DELETE')")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTransaction(id));
    }


    // =========================================================
    // SETTLEMENTS
    // =========================================================

    @PostMapping("/settlement/{employeeId}")
    @PreAuthorize("hasAuthority('SETTLEMENT_CREATE')")
    public ResponseEntity<?> createSettlement(
            @PathVariable String employeeId,
            @RequestBody Settlements settlement) {

        return ResponseEntity.ok(
                service.createSettlement(employeeId, settlement));
    }


    @GetMapping("/settlements")
    @PreAuthorize("hasAuthority('SETTLEMENT_VIEW')")
    public ResponseEntity<?> getSettlements() {

        return ResponseEntity.ok(
                service.getSettlements());
    }


    @GetMapping("/settlement/{id}")
    @PreAuthorize("hasAuthority('SETTLEMENT_VIEW')")
    public ResponseEntity<?> getSettlement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getSettlement(id));
    }


    @PutMapping("/settlement/{id}")
    @PreAuthorize("hasAuthority('SETTLEMENT_UPDATE')")
    public ResponseEntity<?> updateSettlement(
            @PathVariable Long id,
            @RequestBody Settlements settlement) {

        return ResponseEntity.ok(
                service.updateSettlement(id, settlement));
    }


    @DeleteMapping("/settlement/{id}")
    @PreAuthorize("hasAuthority('SETTLEMENT_DELETE')")
    public ResponseEntity<?> deleteSettlement(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteSettlement(id));
    }


    // =========================================================
    // TAX REPORTS
    // =========================================================

    @PostMapping("/tax-report/{employeeId}")
    @PreAuthorize("hasAuthority('TAX_REPORT_CREATE')")
    public ResponseEntity<?> createTaxReport(
            @PathVariable String employeeId,
            @RequestBody TaxReports taxReport) {

        return ResponseEntity.ok(
                service.createTaxReport(employeeId, taxReport));
    }


    @GetMapping("/tax-reports")
    @PreAuthorize("hasAuthority('TAX_REPORT_VIEW')")
    public ResponseEntity<?> getTaxReports() {

        return ResponseEntity.ok(
                service.getTaxReports());
    }


    @GetMapping("/tax-report/{employeeId}")
    @PreAuthorize("hasAuthority('TAX_REPORT_VIEW')")
    public ResponseEntity<?> getTaxReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getTaxReport(employeeId));
    }


    @PutMapping("/tax-report/{employeeId}")
    @PreAuthorize("hasAuthority('TAX_REPORT_UPDATE')")
    public ResponseEntity<?> updateTaxReport(
            @PathVariable String employeeId,
            @RequestBody TaxReports taxReport) {

        return ResponseEntity.ok(
                service.updateTaxReport(employeeId, taxReport));
    }


    @DeleteMapping("/tax-report/{employeeId}")
    @PreAuthorize("hasAuthority('TAX_REPORT_DELETE')")
    public ResponseEntity<?> deleteTaxReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteTaxReport(employeeId));
    }


    // =========================================================
    // EXPENSE REPORTS
    // =========================================================

    @GetMapping("/expenses/approved")
    @PreAuthorize("hasAuthority('EXPENSE_APPROVED_VIEW')")
    public ResponseEntity<?> approvedExpenses() {

        return ResponseEntity.ok(
                service.approvedExpenses());
    }


    @GetMapping("/expenses/pending")
    @PreAuthorize("hasAuthority('EXPENSE_PENDING_VIEW')")
    public ResponseEntity<?> pendingExpenses() {

        return ResponseEntity.ok(
                service.pendingExpenses());
    }


    @GetMapping("/expenses/rejected")
    @PreAuthorize("hasAuthority('EXPENSE_REJECTED_VIEW')")
    public ResponseEntity<?> rejectedExpenses() {

        return ResponseEntity.ok(
                service.rejectedExpenses());
    }


    // =========================================================
    // TRANSACTION REPORTS
    // =========================================================

    @GetMapping("/transactions/success")
    @PreAuthorize("hasAuthority('TRANSACTION_SUCCESS_VIEW')")
    public ResponseEntity<?> successfulTransactions() {

        return ResponseEntity.ok(
                service.successfulTransactions());
    }


    @GetMapping("/transactions/failed")
    @PreAuthorize("hasAuthority('TRANSACTION_FAILED_VIEW')")
    public ResponseEntity<?> failedTransactions() {

        return ResponseEntity.ok(
                service.failedTransactions());
    }


    // =========================================================
    // SETTLEMENT REPORTS
    // =========================================================

    @GetMapping("/settlements/completed")
    @PreAuthorize("hasAuthority('SETTLEMENT_COMPLETED_VIEW')")
    public ResponseEntity<?> completedSettlements() {

        return ResponseEntity.ok(
                service.completedSettlements());
    }


    @GetMapping("/settlements/pending")
    @PreAuthorize("hasAuthority('SETTLEMENT_PENDING_VIEW')")
    public ResponseEntity<?> pendingSettlements() {

        return ResponseEntity.ok(
                service.pendingSettlements());
    }


    // =========================================================
    // DASHBOARD COUNTS
    // =========================================================

    @GetMapping("/counts")
    @PreAuthorize("hasAuthority('FINANCE_DASHBOARD_VIEW')")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(
                service.getCounts());
    }


    // =========================================================
    // INVOICE MANAGEMENT
    // =========================================================

    @PostMapping("/invoice")
    @PreAuthorize("hasAuthority('INVOICE_CREATE')")
    public ResponseEntity<?> createInvoice(
            @RequestBody InvoiceManagement invoice) {

        return ResponseEntity.ok(
                service.createInvoice(invoice));
    }


    @GetMapping("/invoices")
    @PreAuthorize("hasAuthority('INVOICE_VIEW')")
    public ResponseEntity<?> getInvoices() {

        return ResponseEntity.ok(
                service.getInvoices());
    }


    @GetMapping("/invoice/{id}")
    @PreAuthorize("hasAuthority('INVOICE_VIEW')")
    public ResponseEntity<?> getInvoice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInvoice(id));
    }


    @PutMapping("/invoice/{id}")
    @PreAuthorize("hasAuthority('INVOICE_UPDATE')")
    public ResponseEntity<?> updateInvoice(
            @PathVariable Long id,
            @RequestBody InvoiceManagement invoice) {

        return ResponseEntity.ok(
                service.updateInvoice(id, invoice));
    }


    @DeleteMapping("/invoice/{id}")
    @PreAuthorize("hasAuthority('INVOICE_DELETE')")
    public ResponseEntity<?> deleteInvoice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInvoice(id));
    }


    // =========================================================
    // ACCOUNTS PAYABLE
    // =========================================================

    @PostMapping("/payable")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_CREATE')")
    public ResponseEntity<?> createPayable(
            @RequestBody AccountsPayable payable) {

        return ResponseEntity.ok(
                service.createPayable(payable));
    }


    @GetMapping("/payables")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_VIEW')")
    public ResponseEntity<?> getPayables() {

        return ResponseEntity.ok(
                service.getPayables());
    }


    @GetMapping("/payable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_VIEW')")
    public ResponseEntity<?> getPayable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPayable(id));
    }


    @PutMapping("/payable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_UPDATE')")
    public ResponseEntity<?> updatePayable(
            @PathVariable Long id,
            @RequestBody AccountsPayable payable) {

        return ResponseEntity.ok(
                service.updatePayable(id, payable));
    }


    @DeleteMapping("/payable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_DELETE')")
    public ResponseEntity<?> deletePayable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePayable(id));
    }


    // =========================================================
    // ACCOUNTS RECEIVABLE
    // =========================================================

    @PostMapping("/receivable")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_CREATE')")
    public ResponseEntity<?> createReceivable(
            @RequestBody AccountsReceivable receivable) {

        return ResponseEntity.ok(
                service.createReceivable(receivable));
    }


    @GetMapping("/receivables")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_VIEW')")
    public ResponseEntity<?> getReceivables() {

        return ResponseEntity.ok(
                service.getReceivables());
    }


    @GetMapping("/receivable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_VIEW')")
    public ResponseEntity<?> getReceivable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getReceivable(id));
    }


    @PutMapping("/receivable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_UPDATE')")
    public ResponseEntity<?> updateReceivable(
            @PathVariable Long id,
            @RequestBody AccountsReceivable receivable) {

        return ResponseEntity.ok(
                service.updateReceivable(id, receivable));
    }


    @DeleteMapping("/receivable/{id}")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_DELETE')")
    public ResponseEntity<?> deleteReceivable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteReceivable(id));
    }


    // =========================================================
    // GENERAL LEDGER
    // =========================================================

    @PostMapping("/ledger")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_CREATE')")
    public ResponseEntity<?> createLedger(
            @RequestBody GeneralLedger ledger) {

        return ResponseEntity.ok(
                service.createLedger(ledger));
    }


    @GetMapping("/ledgers")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_VIEW')")
    public ResponseEntity<?> getLedgers() {

        return ResponseEntity.ok(
                service.getLedgers());
    }


    @GetMapping("/ledger/{id}")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_VIEW')")
    public ResponseEntity<?> getLedger(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLedger(id));
    }


    @PutMapping("/ledger/{id}")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_UPDATE')")
    public ResponseEntity<?> updateLedger(
            @PathVariable Long id,
            @RequestBody GeneralLedger ledger) {

        return ResponseEntity.ok(
                service.updateLedger(id, ledger));
    }


    @DeleteMapping("/ledger/{id}")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_DELETE')")
    public ResponseEntity<?> deleteLedger(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLedger(id));
    }


    // =========================================================
    // BUDGET MANAGEMENT
    // =========================================================

    @PostMapping("/budget")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    public ResponseEntity<?> createBudget(
            @RequestBody Budget budget) {

        return ResponseEntity.ok(
                service.createBudget(budget));
    }


    @GetMapping("/budgets")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    public ResponseEntity<?> getBudgets() {

        return ResponseEntity.ok(
                service.getBudgets());
    }


    @GetMapping("/budget/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    public ResponseEntity<?> getBudget(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBudget(id));
    }


    @PutMapping("/budget/{id}")
    @PreAuthorize("hasAuthority('BUDGET_UPDATE')")
    public ResponseEntity<?> updateBudget(
            @PathVariable Long id,
            @RequestBody Budget budget) {

        return ResponseEntity.ok(
                service.updateBudget(id, budget));
    }


    @DeleteMapping("/budget/{id}")
    @PreAuthorize("hasAuthority('BUDGET_DELETE')")
    public ResponseEntity<?> deleteBudget(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBudget(id));
    }


    // =========================================================
    // PROFIT & LOSS
    // =========================================================

    @PostMapping("/profit-loss")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_CREATE')")
    public ResponseEntity<?> createProfitLoss(
            @RequestBody ProfitLoss profitLoss) {

        return ResponseEntity.ok(
                service.createProfitLoss(profitLoss));
    }


    @GetMapping("/profit-losses")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_VIEW')")
    public ResponseEntity<?> getProfitLosses() {

        return ResponseEntity.ok(
                service.getProfitLosses());
    }


    @GetMapping("/profit-loss/{id}")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_VIEW')")
    public ResponseEntity<?> getProfitLoss(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getProfitLoss(id));
    }


    @PutMapping("/profit-loss/{id}")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_UPDATE')")
    public ResponseEntity<?> updateProfitLoss(
            @PathVariable Long id,
            @RequestBody ProfitLoss profitLoss) {

        return ResponseEntity.ok(
                service.updateProfitLoss(id, profitLoss));
    }


    @DeleteMapping("/profit-loss/{id}")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_DELETE')")
    public ResponseEntity<?> deleteProfitLoss(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteProfitLoss(id));
    }


    // =========================================================
    // BALANCE SHEET
    // =========================================================

    @PostMapping("/balance-sheet")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_CREATE')")
    public ResponseEntity<?> createBalanceSheet(
            @RequestBody BalanceSheet balanceSheet) {

        return ResponseEntity.ok(
                service.createBalanceSheet(balanceSheet));
    }


    @GetMapping("/balance-sheets")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_VIEW')")
    public ResponseEntity<?> getBalanceSheets() {

        return ResponseEntity.ok(
                service.getBalanceSheets());
    }


    @GetMapping("/balance-sheet/{id}")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_VIEW')")
    public ResponseEntity<?> getBalanceSheet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBalanceSheet(id));
    }


    @PutMapping("/balance-sheet/{id}")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_UPDATE')")
    public ResponseEntity<?> updateBalanceSheet(
            @PathVariable Long id,
            @RequestBody BalanceSheet balanceSheet) {

        return ResponseEntity.ok(
                service.updateBalanceSheet(id, balanceSheet));
    }


    @DeleteMapping("/balance-sheet/{id}")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_DELETE')")
    public ResponseEntity<?> deleteBalanceSheet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBalanceSheet(id));
    }


    // =========================================================
    // ACCOUNTS PAYABLE - INVOICE
    // =========================================================

    @GetMapping("/accounts-payable/invoice/{invoiceNumber}")
    @PreAuthorize("hasAuthority('ACCOUNTS_PAYABLE_VIEW')")
    public ResponseEntity<?> getAccountsPayableByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getAccountsPayableByInvoiceNumber(invoiceNumber));
    }


    // =========================================================
    // ACCOUNTS RECEIVABLE - INVOICE
    // =========================================================

    @GetMapping("/accounts-receivable/invoice/{invoiceNumber}")
    @PreAuthorize("hasAuthority('ACCOUNTS_RECEIVABLE_VIEW')")
    public ResponseEntity<?> getAccountsReceivableByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getAccountsReceivableByInvoiceNumber(invoiceNumber));
    }


    // =========================================================
    // GENERAL LEDGER - ACCOUNT
    // =========================================================

    @GetMapping("/general-ledger/account/{accountCode}")
    @PreAuthorize("hasAuthority('GENERAL_LEDGER_VIEW')")
    public ResponseEntity<?> getGeneralLedgerByAccountCode(
            @PathVariable String accountCode) {

        return ResponseEntity.ok(
                service.getGeneralLedgerByAccountCode(accountCode));
    }


    // =========================================================
    // BUDGETS - DEPARTMENT
    // =========================================================

    @GetMapping("/budgets/department/{departmentId}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    public ResponseEntity<?> getBudgetsByDepartment(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                service.getBudgetsByDepartment(departmentId));
    }


    // =========================================================
    // PROFIT & LOSS - FINANCIAL YEAR
    // =========================================================

    @GetMapping("/profit-loss/year/{financialYear}")
    @PreAuthorize("hasAuthority('PROFIT_LOSS_VIEW')")
    public ResponseEntity<?> getProfitLossByFinancialYear(
            @PathVariable String financialYear) {

        return ResponseEntity.ok(
                service.getProfitLossByFinancialYear(financialYear));
    }


    // =========================================================
    // BALANCE SHEET - FINANCIAL YEAR
    // =========================================================

    @GetMapping("/balance-sheet/year/{financialYear}")
    @PreAuthorize("hasAuthority('BALANCE_SHEET_VIEW')")
    public ResponseEntity<?> getBalanceSheetByFinancialYear(
            @PathVariable String financialYear) {

        return ResponseEntity.ok(
                service.getBalanceSheetByFinancialYear(financialYear));
    }


    // =========================================================
    // INVOICE - INVOICE NUMBER
    // =========================================================

    @GetMapping("/invoice/invoice-number/{invoiceNumber}")
    @PreAuthorize("hasAuthority('INVOICE_VIEW')")
    public ResponseEntity<?> getInvoiceByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getInvoiceByInvoiceNumber(invoiceNumber));
    }


    // =========================================================
    // INVOICE - CUSTOMER
    // =========================================================

    @GetMapping("/invoice/customer/{customerId}")
    @PreAuthorize("hasAuthority('INVOICE_VIEW')")
    public ResponseEntity<?> getInvoicesByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                service.getInvoicesByCustomer(customerId));
    }


    // =========================================================
    // INVOICE - VENDOR
    // =========================================================

    @GetMapping("/invoice/vendor/{vendorId}")
    @PreAuthorize("hasAuthority('INVOICE_VIEW')")
    public ResponseEntity<?> getInvoicesByVendor(
            @PathVariable Long vendorId) {

        return ResponseEntity.ok(
                service.getInvoicesByVendor(vendorId));
    }
}
