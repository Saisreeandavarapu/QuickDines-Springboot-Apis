package com.HRMS.QuickDines.Wallet.Controller;

import com.HRMS.QuickDines.Wallet.Service.WalletService;
import com.HRMS.QuickDines.Wallet.model.EmployeeWallet;
import com.HRMS.QuickDines.Wallet.model.WalletTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService service;


    //=================================
// EMPLOYEE WALLET
//=================================

    @PostMapping("/create/{employeeId}")
    public ResponseEntity<?> createWallet(
            @PathVariable String employeeId,
            @RequestBody EmployeeWallet employeeWallet){

        return ResponseEntity.ok(service.createWallet(employeeId, employeeWallet));
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getWallet(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getWallet(employeeId));
    }


    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateWallet(
            @PathVariable String employeeId,
            @RequestBody EmployeeWallet employeeWallet){

        return ResponseEntity.ok(service.updateWallet(employeeId, employeeWallet));
    }


    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteWallet(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.deleteWallet(employeeId));
    }



    //=================================
// TRANSACTIONS
//=================================

    @PostMapping("/transaction/{employeeId}")
    public ResponseEntity<?> createTransaction(
            @PathVariable String employeeId,
            @RequestBody WalletTransactions walletTransactions){

        return ResponseEntity.ok(service.createTransaction(employeeId, walletTransactions));
    }


    @GetMapping("/transactions/{employeeId}")
    public ResponseEntity<?> getTransactions(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getTransactions(employeeId));
    }


    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getTransaction(id));
    }


    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteTransaction(id));
    }


    //=================================
// REPORTS
//=================================

    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> generateReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.generateReport(employeeId));
    }


    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getReport(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getReport(employeeId));
    }


    @GetMapping("/monthly-report")
    public ResponseEntity<?> getMonthlyWalletReport(){

        return ResponseEntity.ok(service.getMonthlyWalletReport());
    }


    //=================================
// DASHBOARD
//=================================

    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> getBalance(@PathVariable String employeeId){

        return ResponseEntity.ok(service.getBalance(employeeId));
    }


    @GetMapping("/history/{employeeId}")
    public ResponseEntity<?> getWalletHistory(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getWalletHistory(employeeId));
    }


    @GetMapping("/all-wallets")
    public ResponseEntity<?> getAllWallets(){

        return ResponseEntity.ok(service.getAllWallets());
    }

}
