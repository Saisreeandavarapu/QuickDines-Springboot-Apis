package com.HRMS.QuickDines.Finance.Controller;

import com.HRMS.QuickDines.Finance.Service.FinanceService;
import com.HRMS.QuickDines.Finance.model.Expenses;
import com.HRMS.QuickDines.Finance.model.Settlements;
import com.HRMS.QuickDines.Finance.model.TaxReports;
import com.HRMS.QuickDines.Finance.model.Transactions;
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
}
