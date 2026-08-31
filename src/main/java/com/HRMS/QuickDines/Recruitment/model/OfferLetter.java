package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Recruitment.Entity.OfferLetterStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class OfferLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // APPLICATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    // =====================================================
    // OFFER DETAILS
    // =====================================================

    private String designation;

    @Column(precision = 10, scale = 2)
    private BigDecimal offeredSalary;

    private LocalDate joiningDate;

    // =====================================================
    // WORKFLOW STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferLetterStatus offerStatus;

    // =====================================================
    // AUDIT TIMESTAMPS
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}