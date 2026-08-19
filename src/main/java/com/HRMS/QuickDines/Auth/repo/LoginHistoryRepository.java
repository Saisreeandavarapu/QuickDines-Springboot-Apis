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

    List<LoginHistory> findByLoginStatus(LoginStatus loginStatus);

    List<LoginHistory> findByUsersId(Long userId);

    List<LoginHistory> findByUsersIdAndLoginStatus(
            Long userId,
            LoginStatus loginStatus);

    List<LoginHistory> findByLoginDateBetween(
            LocalDate fromDate,
            LocalDate toDate);
    @Query("""
    SELECT l
    FROM LoginHistory l
    LEFT JOIN l.users u
    WHERE
        LOWER(u.employeeId) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(u.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(l.ipAddress) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(l.browserName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(l.operatingSystem) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(l.remarks) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(CAST(l.loginStatus AS string))
            LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    List<LoginHistory> searchLoginHistory(
            @Param("search") String search
    );

    List<LoginHistory> findByEmployee(String employeeId);

   // List<LoginHistory> findFailedLogins(String employeeId);

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
