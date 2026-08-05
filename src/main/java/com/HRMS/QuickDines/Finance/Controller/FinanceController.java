package com.HRMS.QuickDines.Finance.Controller;

import com.HRMS.QuickDines.Finance.Service.FinanceService;
import com.HRMS.QuickDines.Finance.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private FinanceService service;


    //=================================
// EXPENSES
//=================================

    @PostMapping("/expense/{employeeId}")
    public ResponseEntity<?> createExpense(
            @PathVariable String employeeId,
            @RequestBody Expenses expense){

        return ResponseEntity.ok(service.createExpense(employeeId, expense));
    }


    @GetMapping("/expenses")
    public ResponseEntity<?> getExpenses(){

        return ResponseEntity.ok(service.getExpenses());
    }


    @GetMapping("/expense/{id}")
    public ResponseEntity<?> getExpense(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getExpense(id));
    }


    @PutMapping("/expense/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @RequestBody Expenses expense){

        return ResponseEntity.ok(service.updateExpense(id, expense));
    }


    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteExpense(id));
    }

    //=================================
// TRANSACTIONS
//=================================

    @PostMapping("/transaction/{employeeId}")
    public ResponseEntity<?> createTransaction(
            @PathVariable String employeeId,
            @RequestBody Transactions transaction){

        return ResponseEntity.ok(service.createTransaction(employeeId, transaction));
    }


    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(){

        return ResponseEntity.ok(service.getTransactions());
    }


    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getTransaction(id));
    }


    @PutMapping("/transaction/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transactions transaction){

        return ResponseEntity.ok(service.updateTransaction(id, transaction));
    }


    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteTransaction(id));
    }
    //=================================
// SETTLEMENTS
//=================================

    @PostMapping("/settlement/{employeeId}")
    public ResponseEntity<?> createSettlement(
            @PathVariable String employeeId,
            @RequestBody Settlements settlement){

        return ResponseEntity.ok(service.createSettlement(employeeId, settlement));
    }


    @GetMapping("/settlements")
    public ResponseEntity<?> getSettlements(){

        return ResponseEntity.ok(service.getSettlements());
    }


    @GetMapping("/settlement/{id}")
    public ResponseEntity<?> getSettlement(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getSettlement(id));
    }


    @PutMapping("/settlement/{id}")
    public ResponseEntity<?> updateSettlement(
            @PathVariable Long id,
            @RequestBody Settlements settlement){

        return ResponseEntity.ok(service.updateSettlement(id, settlement));
    }


    @DeleteMapping("/settlement/{id}")
    public ResponseEntity<?> deleteSettlement(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteSettlement(id));
    }

    //=================================
// TAX REPORTS
//=================================

    @PostMapping("/tax-report/{employeeId}")
    public ResponseEntity<?> createTaxReport(
            @PathVariable String employeeId,
            @RequestBody TaxReports taxReport){

        return ResponseEntity.ok(service.createTaxReport(employeeId, taxReport));
    }


    @GetMapping("/tax-reports")
    public ResponseEntity<?> getTaxReports(){

        return ResponseEntity.ok(service.getTaxReports());
    }


    @GetMapping("/tax-report/{employeeId}")
    public ResponseEntity<?> getTaxReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getTaxReport(employeeId));
    }


    @PutMapping("/tax-report/{employeeId}")
    public ResponseEntity<?> updateTaxReport(
            @PathVariable String employeeId,
            @RequestBody TaxReports taxReport){

        return ResponseEntity.ok(service.updateTaxReport(employeeId, taxReport));
    }


    @DeleteMapping("/tax-report/{employeeId}")
    public ResponseEntity<?> deleteTaxReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteTaxReport(employeeId));
    }

    //=================================
// REPORTS
//=================================

    @GetMapping("/expenses/approved")
    public ResponseEntity<?> approvedExpenses(){

        return ResponseEntity.ok(service.approvedExpenses());
    }


    @GetMapping("/expenses/pending")
    public ResponseEntity<?> pendingExpenses(){

        return ResponseEntity.ok(service.pendingExpenses());
    }


    @GetMapping("/expenses/rejected")
    public ResponseEntity<?> rejectedExpenses(){

        return ResponseEntity.ok(service.rejectedExpenses());
    }


    @GetMapping("/transactions/success")
    public ResponseEntity<?> successfulTransactions(){

        return ResponseEntity.ok(service.successfulTransactions());
    }


    @GetMapping("/transactions/failed")
    public ResponseEntity<?> failedTransactions(){

        return ResponseEntity.ok(service.failedTransactions());
    }


    @GetMapping("/settlements/completed")
    public ResponseEntity<?> completedSettlements(){

        return ResponseEntity.ok(service.completedSettlements());
    }


    @GetMapping("/settlements/pending")
    public ResponseEntity<?> pendingSettlements(){

        return ResponseEntity.ok(service.pendingSettlements());
    }
    //=================================
// DASHBOARD COUNTS
//=================================

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }
    //=========================================================
    // INVOICE MANAGEMENT
    //=========================================================

    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(
            @RequestBody InvoiceManagement invoice) {

        return ResponseEntity.ok(
                service.createInvoice(invoice));
    }

    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoices() {

        return ResponseEntity.ok(
                service.getInvoices());
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<?> getInvoice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInvoice(id));
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<?> updateInvoice(
            @PathVariable Long id,
            @RequestBody InvoiceManagement invoice) {

        return ResponseEntity.ok(
                service.updateInvoice(id, invoice));
    }

    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<?> deleteInvoice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInvoice(id));
    }


    //=========================================================
    // ACCOUNTS PAYABLE
    //=========================================================

    @PostMapping("/payable")
    public ResponseEntity<?> createPayable(
            @RequestBody AccountsPayable payable) {

        return ResponseEntity.ok(
                service.createPayable(payable));
    }

    @GetMapping("/payables")
    public ResponseEntity<?> getPayables() {

        return ResponseEntity.ok(
                service.getPayables());
    }

    @GetMapping("/payable/{id}")
    public ResponseEntity<?> getPayable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPayable(id));
    }

    @PutMapping("/payable/{id}")
    public ResponseEntity<?> updatePayable(
            @PathVariable Long id,
            @RequestBody AccountsPayable payable) {

        return ResponseEntity.ok(
                service.updatePayable(id, payable));
    }

    @DeleteMapping("/payable/{id}")
    public ResponseEntity<?> deletePayable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePayable(id));
    }


    //=========================================================
    // ACCOUNTS RECEIVABLE
    //=========================================================

    @PostMapping("/receivable")
    public ResponseEntity<?> createReceivable(
            @RequestBody AccountsReceivable receivable) {

        return ResponseEntity.ok(
                service.createReceivable(receivable));
    }

    @GetMapping("/receivables")
    public ResponseEntity<?> getReceivables() {

        return ResponseEntity.ok(
                service.getReceivables());
    }

    @GetMapping("/receivable/{id}")
    public ResponseEntity<?> getReceivable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getReceivable(id));
    }

    @PutMapping("/receivable/{id}")
    public ResponseEntity<?> updateReceivable(
            @PathVariable Long id,
            @RequestBody AccountsReceivable receivable) {

        return ResponseEntity.ok(
                service.updateReceivable(id, receivable));
    }

    @DeleteMapping("/receivable/{id}")
    public ResponseEntity<?> deleteReceivable(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteReceivable(id));
    }


    //=========================================================
    // GENERAL LEDGER
    //=========================================================

    @PostMapping("/ledger")
    public ResponseEntity<?> createLedger(
            @RequestBody GeneralLedger ledger) {

        return ResponseEntity.ok(
                service.createLedger(ledger));
    }

    @GetMapping("/ledgers")
    public ResponseEntity<?> getLedgers() {

        return ResponseEntity.ok(
                service.getLedgers());
    }

    @GetMapping("/ledger/{id}")
    public ResponseEntity<?> getLedger(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLedger(id));
    }

    @PutMapping("/ledger/{id}")
    public ResponseEntity<?> updateLedger(
            @PathVariable Long id,
            @RequestBody GeneralLedger ledger) {

        return ResponseEntity.ok(
                service.updateLedger(id, ledger));
    }

    @DeleteMapping("/ledger/{id}")
    public ResponseEntity<?> deleteLedger(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLedger(id));
    }


    //=========================================================
    // BUDGET MANAGEMENT
    //=========================================================

    @PostMapping("/budget")
    public ResponseEntity<?> createBudget(
            @RequestBody Budget budget) {

        return ResponseEntity.ok(
                service.createBudget(budget));
    }

    @GetMapping("/budgets")
    public ResponseEntity<?> getBudgets() {

        return ResponseEntity.ok(
                service.getBudgets());
    }

    @GetMapping("/budget/{id}")
    public ResponseEntity<?> getBudget(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBudget(id));
    }

    @PutMapping("/budget/{id}")
    public ResponseEntity<?> updateBudget(
            @PathVariable Long id,
            @RequestBody Budget budget) {

        return ResponseEntity.ok(
                service.updateBudget(id, budget));
    }

    @DeleteMapping("/budget/{id}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBudget(id));
    }


    //=========================================================
    // PROFIT & LOSS
    //=========================================================

    @PostMapping("/profit-loss")
    public ResponseEntity<?> createProfitLoss(
            @RequestBody ProfitLoss profitLoss) {

        return ResponseEntity.ok(
                service.createProfitLoss(profitLoss));
    }

    @GetMapping("/profit-losses")
    public ResponseEntity<?> getProfitLosses() {

        return ResponseEntity.ok(
                service.getProfitLosses());
    }

    @GetMapping("/profit-loss/{id}")
    public ResponseEntity<?> getProfitLoss(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getProfitLoss(id));
    }

    @PutMapping("/profit-loss/{id}")
    public ResponseEntity<?> updateProfitLoss(
            @PathVariable Long id,
            @RequestBody ProfitLoss profitLoss) {

        return ResponseEntity.ok(
                service.updateProfitLoss(id, profitLoss));
    }

    @DeleteMapping("/profit-loss/{id}")
    public ResponseEntity<?> deleteProfitLoss(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteProfitLoss(id));
    }


    //=========================================================
    // BALANCE SHEET
    //=========================================================

    @PostMapping("/balance-sheet")
    public ResponseEntity<?> createBalanceSheet(
            @RequestBody BalanceSheet balanceSheet) {

        return ResponseEntity.ok(
                service.createBalanceSheet(balanceSheet));
    }

    @GetMapping("/balance-sheets")
    public ResponseEntity<?> getBalanceSheets() {

        return ResponseEntity.ok(
                service.getBalanceSheets());
    }

    @GetMapping("/balance-sheet/{id}")
    public ResponseEntity<?> getBalanceSheet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getBalanceSheet(id));
    }

    @PutMapping("/balance-sheet/{id}")
    public ResponseEntity<?> updateBalanceSheet(
            @PathVariable Long id,
            @RequestBody BalanceSheet balanceSheet) {

        return ResponseEntity.ok(
                service.updateBalanceSheet(id, balanceSheet));
    }

    @DeleteMapping("/balance-sheet/{id}")
    public ResponseEntity<?> deleteBalanceSheet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteBalanceSheet(id));
    }

    //=========================================================
    // ACCOUNTS PAYABLE
    //=========================================================

    @GetMapping("/accounts-payable/invoice/{invoiceNumber}")
    public ResponseEntity<?> getAccountsPayableByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getAccountsPayableByInvoiceNumber(invoiceNumber));
    }


    //=========================================================
    // ACCOUNTS RECEIVABLE
    //=========================================================

    @GetMapping("/accounts-receivable/invoice/{invoiceNumber}")
    public ResponseEntity<?> getAccountsReceivableByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getAccountsReceivableByInvoiceNumber(invoiceNumber));
    }


    //=========================================================
    // GENERAL LEDGER
    //=========================================================

    @GetMapping("/general-ledger/account/{accountCode}")
    public ResponseEntity<?> getGeneralLedgerByAccountCode(
            @PathVariable String accountCode) {

        return ResponseEntity.ok(
                service.getGeneralLedgerByAccountCode(accountCode));
    }


    //=========================================================
    // BUDGETS
    //=========================================================

    @GetMapping("/budgets/department/{departmentId}")
    public ResponseEntity<?> getBudgetsByDepartment(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                service.getBudgetsByDepartment(departmentId));
    }


    //=========================================================
    // PROFIT & LOSS
    //=========================================================

    @GetMapping("/profit-loss/year/{financialYear}")
    public ResponseEntity<?> getProfitLossByFinancialYear(
            @PathVariable String financialYear) {

        return ResponseEntity.ok(
                service.getProfitLossByFinancialYear(financialYear));
    }


    //=========================================================
    // BALANCE SHEET
    //=========================================================

    @GetMapping("/balance-sheet/year/{financialYear}")
    public ResponseEntity<?> getBalanceSheetByFinancialYear(
            @PathVariable String financialYear) {

        return ResponseEntity.ok(
                service.getBalanceSheetByFinancialYear(financialYear));
    }


    //=========================================================
    // INVOICE MANAGEMENT - INVOICE NUMBER
    //=========================================================

    @GetMapping("/invoice/invoice-number/{invoiceNumber}")
    public ResponseEntity<?> getInvoiceByInvoiceNumber(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                service.getInvoiceByInvoiceNumber(invoiceNumber));
    }


    //=========================================================
    // INVOICE MANAGEMENT - CUSTOMER
    //=========================================================

    @GetMapping("/invoice/customer/{customerId}")
    public ResponseEntity<?> getInvoicesByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                service.getInvoicesByCustomer(customerId));
    }


    //=========================================================
    // INVOICE MANAGEMENT - VENDOR
    //=========================================================

    @GetMapping("/invoice/vendor/{vendorId}")
    public ResponseEntity<?> getInvoicesByVendor(
            @PathVariable Long vendorId) {

        return ResponseEntity.ok(
                service.getInvoicesByVendor(vendorId));
    }

}
