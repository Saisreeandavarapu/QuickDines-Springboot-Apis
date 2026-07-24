package com.HRMS.QuickDines.Payroll.repo;

import com.HRMS.QuickDines.Payroll.model.Increments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncrementRepository extends JpaRepository<Increments,Long> {
    List<Increments> findByEmployeeEmployeeId(String employeeId);
}
