package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
