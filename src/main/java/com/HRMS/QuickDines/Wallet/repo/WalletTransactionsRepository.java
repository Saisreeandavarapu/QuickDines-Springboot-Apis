package com.HRMS.QuickDines.Wallet.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Wallet.model.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionsRepository extends JpaRepository<WalletTransactions, Long> {
   List<WalletTransactions> findByEmployeeEmployeeId(String employeeId);

    List<WalletTransactions> findByEmployee(Employee employee);
}
