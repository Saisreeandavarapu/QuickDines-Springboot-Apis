package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.model.SalesTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesTargetsRepository extends JpaRepository<SalesTarget,Long> {

    Optional<SalesTarget> findByEmployeeEmployeeId(String employeeId);

    List<SalesTarget> findByTargetStatus(String achieved);

    Long countByTargetStatus(String achieved);
}
