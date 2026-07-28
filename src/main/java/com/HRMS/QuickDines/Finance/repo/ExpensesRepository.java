package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findByStatus(String approved);

    Long countByStatus(String approved);
}
