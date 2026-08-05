package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.ManagerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerReviewRepository extends JpaRepository<ManagerReview, Long> {
}
