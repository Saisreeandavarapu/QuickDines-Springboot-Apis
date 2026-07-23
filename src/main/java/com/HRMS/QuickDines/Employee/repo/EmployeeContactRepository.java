package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.EmployeeContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContacts, Long> {
    List<EmployeeContacts> findByEmployeeId(String employeeId);

    Optional<Object> findById(String id);
}
