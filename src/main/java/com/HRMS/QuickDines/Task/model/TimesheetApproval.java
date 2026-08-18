package com.HRMS.QuickDines.Task.model;
import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Data
@Table(name = "timesheet_approvals")
public class TimesheetApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // TIMESHEET
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheet_id", nullable = false)
    private EmployeeTimesheet timesheet;


    // =====================================================
    // APPROVER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private Employee approver;


    // =====================================================
    // APPROVER ROLE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role approverRole;


    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimesheetStatus status;


    // =====================================================
    // COMMENTS
    // =====================================================

    @Column(length = 2000)
    private String remarks;


    private LocalDateTime actionAt;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
