package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.UserRole;
import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<Object> findByUsers(Users user);
}
