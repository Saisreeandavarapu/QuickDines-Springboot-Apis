package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.AssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment,Long> {
}
