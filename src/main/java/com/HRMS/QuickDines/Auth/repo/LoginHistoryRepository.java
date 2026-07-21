package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}
