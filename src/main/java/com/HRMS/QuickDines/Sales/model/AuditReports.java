package com.HRMS.QuickDines.Sales.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Sales.Entity.AuditApprovalStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditType;
import com.HRMS.QuickDines.Sales.model.Restaurant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_reports")
public class AuditReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // SALES EMPLOYEE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // =====================================================
    // RESTAURANT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // =====================================================
    // SCHEDULE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private AuditSchedule schedule;

    // =====================================================
    // AUDIT INFORMATION
    // =====================================================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditType auditType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditStatus auditStatus;

    private Integer warningCount;

    private LocalDate auditDate;

    @Column(length = 2000)
    private String remarks;

    // =====================================================
    // MANAGEMENT HEAD APPROVAL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditApprovalStatus approvalStatus =
            AuditApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private LocalDateTime approvedAt;

    @Column(length = 2000)
    private String approvalRemarks;

    // =====================================================
    // OVERDUE
    // =====================================================

    private Boolean overdue = false;

    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}