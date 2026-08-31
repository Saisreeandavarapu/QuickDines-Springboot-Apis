package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalAction;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalModule;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalStatus;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_approvals")
@Data
public class RecruitmentApproval {

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
    // WORKFLOW MODULE
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApprovalModule module;

    // =====================================================
    // EMPLOYEE
    // =====================================================

    /*
     * Employee related to this workflow.
     *
     * Example:
     * - Employee onboarding
     * - Probation confirmation
     * - Salary structure
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // =====================================================
    // APPLICATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    // =====================================================
    // JOB OPENING
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    // =====================================================
    // REQUESTED BY
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private Employee requestedBy;

    // =====================================================
    // CURRENT APPROVER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Employee approver;

    // =====================================================
    // APPROVAL LEVEL
    // =====================================================

    @Column(nullable = false)
    private Integer approvalLevel;

    // =====================================================
    // ACTION
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApprovalAction action;

    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApprovalStatus status;

    // =====================================================
    // REASON / COMMENTS
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String reason;

    // =====================================================
    // PROCESSED BY
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private Employee processedBy;

    // =====================================================
    // PROCESSED TIME
    // =====================================================

    private LocalDateTime processedAt;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}