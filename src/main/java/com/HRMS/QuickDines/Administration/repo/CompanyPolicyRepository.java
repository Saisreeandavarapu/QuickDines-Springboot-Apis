package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.CompanyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPolicyRepository extends JpaRepository<CompanyPolicy,Long> {
}
