package com.HRMS.QuickDines.Documents.repo;

import com.HRMS.QuickDines.Documents.model.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
    Optional<DocumentVerification> findByEmployeeEmployeeId(String employeeId);
}
