package com.HRMS.QuickDines.CRM.repo;

import com.HRMS.QuickDines.CRM.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead,Long> {
}
