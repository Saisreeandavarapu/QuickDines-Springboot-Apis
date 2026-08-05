package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiRepository extends JpaRepository<Kpi, Long> {
}
