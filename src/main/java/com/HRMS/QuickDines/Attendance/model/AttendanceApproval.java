package com.HRMS.QuickDines.Attendance.model;

import com.HRMS.QuickDines.Attendance.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "attendance_approvals")
public class AttendanceApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // ATTENDANCE
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_id", nullable = false, unique = true)
    private Attendance attendance;


    // =====================================================
    // EMPLOYEE WHO CHECKED IN LATE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    // =====================================================
    // REPORTING MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;


    // =====================================================
    // APPROVAL STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus approvalStatus;


    // =====================================================
    // APPROVAL DETAILS
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String approvalReason;

    private LocalDateTime approvedAt;


    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}