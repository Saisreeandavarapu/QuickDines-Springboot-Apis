package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.GeneralLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long> {
    List<GeneralLedger> findByAccountCode(String accountCode);
}
