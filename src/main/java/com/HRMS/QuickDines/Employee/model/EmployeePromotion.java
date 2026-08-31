package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Employee.Entity.EmployeePromotionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_promotions")
public class EmployeePromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // EMPLOYEE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // =====================================================
    // PREVIOUS DESIGNATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_designation_id")
    private EmployeeDesignation previousDesignation;

    // =====================================================
    // NEW DESIGNATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_designation_id")
    private EmployeeDesignation newDesignation;

    // =====================================================
    // SALARY
    // =====================================================

    private BigDecimal previousSalary;

    private BigDecimal newSalary;

    // =====================================================
    // PROMOTION DATE
    // =====================================================

    private LocalDate promotionDate;

    // =====================================================
    // REASON
    // =====================================================

    @Column(length = 1000)
    private String reason;

    // =====================================================
    // APPROVAL WORKFLOW
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeePromotionStatus status;

    // =====================================================
    // MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    private LocalDateTime managerActionAt;

    @Column(length = 1000)
    private String managerRemarks;

    // =====================================================
    // HR
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private Employee hr;

    private LocalDateTime hrActionAt;

    @Column(length = 1000)
    private String hrRemarks;

    // =====================================================
    // ADMIN / MANAGEMENT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Employee admin;

    private LocalDateTime adminActionAt;

    @Column(length = 1000)
    private String adminRemarks;

    // =====================================================
    // FINAL APPROVER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}