package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Sales.model.SalesIncentive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesIncentiveRepository extends JpaRepository<SalesIncentive, Long> {
    SalesIncentive findByEmployee(Employee employee);

    List<SalesIncentive> findAllByEmployee(Employee employee);

}
