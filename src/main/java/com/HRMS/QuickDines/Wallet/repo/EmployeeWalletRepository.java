package com.HRMS.QuickDines.Wallet.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Wallet.Entity.WalletStatus;
import com.HRMS.QuickDines.Wallet.model.EmployeeWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeWalletRepository extends JpaRepository<EmployeeWallet,Long> {
    boolean existsByEmployee(Employee employee);

    Optional<EmployeeWallet> findByEmployee(Employee employee);
    List<EmployeeWallet> findByStatus(
            WalletStatus status);

}
