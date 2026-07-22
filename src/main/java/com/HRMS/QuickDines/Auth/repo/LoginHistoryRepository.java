package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.LoginHistory;
import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
    Optional<Object> findTopByUsersOrderByIdDesc(Users user);
}
