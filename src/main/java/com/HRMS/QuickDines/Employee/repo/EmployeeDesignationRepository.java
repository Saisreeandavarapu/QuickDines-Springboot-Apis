package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.model.EmployeeDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDesignationRepository extends JpaRepository<EmployeeDesignation, Long> {
    List<EmployeeDesignation> findByEmployeeId(String employeeId);

    @Query(" SELECT e FROM Employee e WHERE LOWER(e.designation.designationName) = LOWER(:designationName) ")
    List<Employee> findEmployeesByDesignationName(@Param("designationName") String designationName);
}
