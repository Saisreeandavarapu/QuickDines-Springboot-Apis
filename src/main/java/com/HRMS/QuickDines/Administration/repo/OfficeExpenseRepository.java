package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.OfficeExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeExpenseRepository extends JpaRepository<OfficeExpense,Long> {
}
