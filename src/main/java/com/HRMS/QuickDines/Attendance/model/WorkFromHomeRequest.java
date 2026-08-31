package com.HRMS.QuickDines.Attendance.model;

import com.HRMS.QuickDines.Attendance.Entity.WorkFromHomeStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_from_home_requests")
@Data
public class WorkFromHomeRequest {

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
    // MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // =====================================================
    // WORK FROM HOME DATE
    // =====================================================

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    // =====================================================
    // REASON
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String reason;

    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkFromHomeStatus status;

    // =====================================================
    // MANAGER COMMENTS
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String managerRemarks;

    // =====================================================
    // APPROVED / REJECTED BY
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private Employee processedBy;

    private LocalDateTime processedAt;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}