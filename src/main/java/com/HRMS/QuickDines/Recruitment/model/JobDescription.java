package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Recruitment.Entity.JobDescriptionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
@Data
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // COMPANY
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // =====================================================
    // JOB OPENING
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id", nullable = false, unique = true)
    private JobOpening jobOpening;

    // =====================================================
    // JOB DETAILS
    // =====================================================

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @Column(columnDefinition = "TEXT")
    private String skills;

    // =====================================================
    // DEFINED BY MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defined_by")
    private Employee definedBy;

    // =====================================================
    // REVIEWED BY HR
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;

    private LocalDateTime reviewedAt;

    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobDescriptionStatus status;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}