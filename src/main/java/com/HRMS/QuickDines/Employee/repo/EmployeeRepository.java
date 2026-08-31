package com.HRMS.QuickDines.Employee.repo;

import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Company.model.Company;
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
           LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.aadhaarNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.panNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Employee> searchEmployees(
            @Param("keyword") String keyword);
    List<Employee> findByDepartmentId(Long departmentId);

    boolean existsByRole_RoleNameIgnoreCase(String superAdmin);

    //Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployee(Employee employee);
    void deleteByEmployee(Employee employee);

    List<Employee> findByEmployeeIdIn(List<String> employeeIds);



    Employee findByEmail(String email);
    Optional<Employee> findFirstByCompanyAndRole(
            Company company,
            String role
    );
    @Query("""
    SELECT e
    FROM Employee e
    WHERE e.company.id = :companyId
      AND e.role.roleName = :roleName
""")
    Optional<Employee> findByCompanyIdAndRoleName(
            @Param("companyId") Long companyId,
            @Param("roleName") String roleName
    );


}
