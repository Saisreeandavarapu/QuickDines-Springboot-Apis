package com.HRMS.QuickDines.Documents.repo;

import com.HRMS.QuickDines.Documents.Entity.VerificationStatus;
import com.HRMS.QuickDines.Documents.model.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
    Optional<DocumentVerification> findByEmployeeEmployeeId(String employeeId);
    // Status-wise filter
    List<DocumentVerification> findByVerificationStatus(
            VerificationStatus status);

    // Employee ID-wise filter
//    Optional<DocumentVerification> findByEmployeeEmployeeId(
//            String employeeId);
}
