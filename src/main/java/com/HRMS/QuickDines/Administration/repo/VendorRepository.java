package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor,Long> {
}
