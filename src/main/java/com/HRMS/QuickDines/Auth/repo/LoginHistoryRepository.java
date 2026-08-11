package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.Entity.LoginStatus;
import com.HRMS.QuickDines.Auth.model.LoginHistory;
import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
    Optional<Object> findTopByUsersOrderByIdDesc(Users user);
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
            LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.ipAddress) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.browserName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.operatingSystem) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.remarks) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(CAST(l.loginStatus AS string))
                LIKE LOWER(CONCAT('%', :search, '%'))
        """)
    List<LoginHistory> searchLoginHistory(
            @Param("search") String search);
}
