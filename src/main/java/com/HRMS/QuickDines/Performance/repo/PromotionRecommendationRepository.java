package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.PromotionRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRecommendationRepository extends JpaRepository<PromotionRecommendation, Long> {
}
