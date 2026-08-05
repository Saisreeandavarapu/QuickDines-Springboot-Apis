package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.SelfReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfReviewRepository extends JpaRepository<SelfReview, Long> {
}
