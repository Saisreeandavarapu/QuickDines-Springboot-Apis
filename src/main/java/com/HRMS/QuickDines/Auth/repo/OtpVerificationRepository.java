package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {
}
