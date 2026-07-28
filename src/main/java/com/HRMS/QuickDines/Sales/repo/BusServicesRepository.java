package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.model.BusService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusServicesRepository extends JpaRepository<BusService, Long> {
    List<BusService> findByStatus(String active);

    Long countByStatus(String active);
}
