package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction,Long> {
}
