package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.Entity.LoginStatus;
import com.HRMS.QuickDines.Auth.model.LoginHistory;
import com.HRMS.QuickDines.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {



    List<LoginHistory> findByLoginDateBetween(
            LocalDate fromDate,
            LocalDate toDate);

    List<LoginHistory> findByEmployee(String employeeId);


    @Query("""
    SELECT l
    FROM LoginHistory l
    WHERE l.employee.employeeId = :employeeId
    AND l.loginStatus = :status
""")
    List<LoginHistory> findByEmployee_EmployeeIdAndLoginStatus(
            @Param("employeeId") String employeeId,
            @Param("status") LoginStatus status
    );

    Optional<LoginHistory> findTopByEmployeeAndLogoutTimeIsNullOrderByIdDesc(Employee employee);
}
