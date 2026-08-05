package com.HRMS.QuickDines.Company.repo;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.BranchLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchLocationRepository extends JpaRepository<BranchLocation, Long> {
    List<BranchLocation> findByBranch(Branch branch);
}
