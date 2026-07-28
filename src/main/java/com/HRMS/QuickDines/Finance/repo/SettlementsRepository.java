package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.Settlements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementsRepository extends JpaRepository<Settlements, Long> {
    List<Settlements> findBySettlementStatus(String completed);

    Long countBySettlementStatus(String completed);
}
