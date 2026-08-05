package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.BalanceSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceSheetRepository extends JpaRepository<BalanceSheet, Long> {
    Optional<BalanceSheet> findByFinancialYear(String financialYear);
}
