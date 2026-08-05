package com.HRMS.QuickDines.CRM.repo;

import com.HRMS.QuickDines.CRM.model.Followup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowupRepository extends JpaRepository<Followup,Long> {
}
