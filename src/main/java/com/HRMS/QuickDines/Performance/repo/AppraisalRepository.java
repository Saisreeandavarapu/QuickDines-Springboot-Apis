package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.Appraisal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppraisalRepository extends JpaRepository<Appraisal, Long> {
}
