package com.HRMS.QuickDines.Organization.repo;

import com.HRMS.QuickDines.Organization.model.OrganizationHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationHierarchyRepository extends JpaRepository<OrganizationHierarchy, Long> {
}
