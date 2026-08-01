package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.model.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {
}
