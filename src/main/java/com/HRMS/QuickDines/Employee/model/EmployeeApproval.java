package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_approvals")
public class EmployeeApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // EMPLOYEE
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;


    // =====================================================
    // HR APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "hr_status", nullable = false)
    private ApprovalStatus hrStatus = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_approved_by")
    private Employee hrApprovedBy;

    private LocalDateTime hrApprovedAt;

    @Column(length = 1000)
    private String hrRemarks;


    // =====================================================
    // ADMIN APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_status", nullable = false)
    private ApprovalStatus adminStatus = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_approved_by")
    private Employee adminApprovedBy;

    private LocalDateTime adminApprovedAt;

    @Column(length = 1000)
    private String adminRemarks;


    // =====================================================
    // DEPARTMENT HEAD APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "department_head_status", nullable = false)
    private ApprovalStatus departmentHeadStatus = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_head_approved_by")
    private Employee departmentHeadApprovedBy;

    private LocalDateTime departmentHeadApprovedAt;

    @Column(length = 1000)
    private String departmentHeadRemarks;


    // =====================================================
    // FINAL APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "final_status", nullable = false)
    private ApprovalStatus finalStatus = ApprovalStatus.PENDING;

    private LocalDateTime finalApprovedAt;


    // =====================================================
    // ACCOUNT CREATION
    // =====================================================

    private Boolean accountCreated = false;

    private LocalDateTime accountCreatedAt;

    private Boolean welcomeMailSent = false;

    private LocalDateTime welcomeMailSentAt;


    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}