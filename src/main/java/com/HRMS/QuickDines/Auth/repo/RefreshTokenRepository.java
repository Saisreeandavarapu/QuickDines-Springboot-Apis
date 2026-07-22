package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.RefreshToken;
import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<Object> findByUsers(Users user);

    Optional<RefreshToken> findByToken(String refreshToken);
}
