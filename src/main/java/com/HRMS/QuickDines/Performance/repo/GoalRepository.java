package com.HRMS.QuickDines.Performance.repo;

import com.HRMS.QuickDines.Performance.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
