package com.HRMS.QuickDines.Wallet.Controller;

import com.HRMS.QuickDines.Wallet.Service.WalletService;
import com.HRMS.QuickDines.Wallet.model.EmployeeWallet;
import com.HRMS.QuickDines.Wallet.model.WalletTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService service;


    //=========================================================
    // EMPLOYEE WALLET
    //=========================================================

    @PreAuthorize("hasAuthority('WALLET_CREATE')")
    @PostMapping("/create/{employeeId}")
    public ResponseEntity<?> createWallet(
            @PathVariable String employeeId,
            @RequestBody EmployeeWallet employeeWallet) {

        return ResponseEntity.ok(
                service.createWallet(employeeId, employeeWallet));
    }


    @PreAuthorize("hasAuthority('WALLET_READ')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getWallet(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getWallet(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_UPDATE')")
    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateWallet(
            @PathVariable String employeeId,
            @RequestBody EmployeeWallet employeeWallet) {

        return ResponseEntity.ok(
                service.updateWallet(employeeId, employeeWallet));
    }


    @PreAuthorize("hasAuthority('WALLET_DELETE')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteWallet(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteWallet(employeeId));
    }


    //=========================================================
    // TRANSACTIONS
    //=========================================================

    @PreAuthorize("hasAuthority('WALLET_TRANSACTION_CREATE')")
    @PostMapping("/transaction/{employeeId}")
    public ResponseEntity<?> createTransaction(
            @PathVariable String employeeId,
            @RequestBody WalletTransactions walletTransactions) {

        return ResponseEntity.ok(
                service.createTransaction(employeeId, walletTransactions));
    }


    @PreAuthorize("hasAuthority('WALLET_TRANSACTION_READ')")
    @GetMapping("/transactions/{employeeId}")
    public ResponseEntity<?> getTransactions(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getTransactions(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_TRANSACTION_READ')")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTransaction(id));
    }


    @PreAuthorize("hasAuthority('WALLET_TRANSACTION_DELETE')")
    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTransaction(id));
    }


    //=========================================================
    // REPORTS
    //=========================================================

    @PreAuthorize("hasAuthority('WALLET_REPORT_CREATE')")
    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> generateReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.generateReport(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_REPORT_READ')")
    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getReport(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_REPORT_READ')")
    @GetMapping("/monthly-report")
    public ResponseEntity<?> getMonthlyWalletReport() {

        return ResponseEntity.ok(
                service.getMonthlyWalletReport());
    }


    //=========================================================
    // DASHBOARD
    //=========================================================

    @PreAuthorize("hasAuthority('WALLET_BALANCE_READ')")
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> getBalance(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getBalance(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_HISTORY_READ')")
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<?> getWalletHistory(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getWalletHistory(employeeId));
    }


    @PreAuthorize("hasAuthority('WALLET_READ_ALL')")
    @GetMapping("/all-wallets")
    public ResponseEntity<?> getAllWallets() {

        return ResponseEntity.ok(
                service.getAllWallets());
    }
}