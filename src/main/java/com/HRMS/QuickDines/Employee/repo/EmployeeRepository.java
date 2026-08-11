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
                LEFT JOIN employee_documents d
                WHERE
                   LOWER(e.employee_id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(e.first_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                             LOWER(e.last_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
                                                         LOWER(e.mobile_number) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
                                                                     LOWER(d.aadhaar_number) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                                                                  LOWER(d.pan_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Employee> searchEmployees(
            @Param("keyword") String keyword);

    List<Employee> findByDepartmentId(Long departmentId);

}
