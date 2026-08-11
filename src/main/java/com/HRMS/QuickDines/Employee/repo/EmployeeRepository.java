package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(String employeeId);

    Optional<Employee> findByEmployeeId(String employeeId);
    @Query("""
        SELECT DISTINCT e
        FROM Employee e
        LEFT JOIN e.documents d
        WHERE
            e.employeeId LIKE :keyword
            OR e.firstName LIKE :keyword
            OR e.lastName LIKE :keyword
            OR e.mobileNumber LIKE :keyword
            OR d.aadhaarNumber LIKE :keyword
            OR d.panNumber LIKE :keyword
    """)
    List<Employee> searchEmployees(
            @Param("keyword") String keyword);

    List<Employee> findByDepartmentId(Long departmentId);

}
