package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {
    Optional<OtpVerification> findByEmailAndOtp(String email, String otp);
    Optional<OtpVerification>
    findTopByEmailAndOtpTypeAndVerificationStatusOrderByCreatedAtDesc(
            String email,
            String otpType,
            String verificationStatus
    );
    List<OtpVerification>
    findByEmailAndOtpTypeAndVerificationStatus(
            String email,
            String otpType,
            String verificationStatus
    );

}
