package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Employee.Entity.ApprovalType;
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
    @JoinColumn(
            name = "employee_id",
            nullable = false,
            unique = true
    )
    private Employee employee;
    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;



    // =====================================================
    // HR APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "hr_status", nullable = false)
    private ApprovalStatus hrStatus = ApprovalStatus.NOT_REQUIRED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_approved_by")
    private Employee hrApprovedBy;

    private LocalDateTime hrApprovedAt;

    @Column(length = 1000)
    private String hrRemarks;


    // =====================================================
    // SALES MANAGER APPROVAL
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_manager_id")
    private Employee salesManager;

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_manager_status", nullable = false)
    private ApprovalStatus salesManagerStatus =
            ApprovalStatus.NOT_REQUIRED;

    private LocalDateTime salesManagerApprovedAt;

    @Column(length = 1000)
    private String salesManagerRemarks;


    // =====================================================
    // SUPER ADMIN APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "super_admin_status", nullable = false)
    private ApprovalStatus superAdminStatus =
            ApprovalStatus.NOT_REQUIRED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_admin_approved_by")
    private Employee superAdminApprovedBy;

    private LocalDateTime superAdminApprovedAt;

    @Column(length = 1000)
    private String superAdminRemarks;


    // =====================================================
    // FINAL STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "final_status", nullable = false)
    private ApprovalStatus finalStatus =
            ApprovalStatus.PENDING;

    private LocalDateTime finalApprovedAt;


    // =====================================================
    // ACCOUNT
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