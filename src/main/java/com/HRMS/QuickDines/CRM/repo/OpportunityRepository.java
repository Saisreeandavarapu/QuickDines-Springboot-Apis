package com.HRMS.QuickDines.CRM.repo;

import com.HRMS.QuickDines.CRM.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity,Long> {
}
