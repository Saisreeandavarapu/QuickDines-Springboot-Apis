package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.ProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfitLossRepository extends JpaRepository<ProfitLoss, Long> {
    Optional<ProfitLoss> findByFinancialYear(String financialYear);
}
