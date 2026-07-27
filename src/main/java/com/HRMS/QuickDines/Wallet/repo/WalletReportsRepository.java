package com.HRMS.QuickDines.Wallet.repo;

import com.HRMS.QuickDines.Wallet.model.WalletReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletReportsRepository extends JpaRepository<WalletReports, Long> {
    List<WalletReports> findByEmployeeEmployeeId(String employeeId);
}
