package com.HRMS.QuickDines.AuditLogs.model;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // EMPLOYEE
    // employee_id -> employees.id
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;


    // =====================================================
    // ACTIVITY NAME
    // =====================================================

    @Column(
            name = "activity_name",
            nullable = false,
            length = 150
    )
    private String activityName;


    // =====================================================
    // MODULE
    // =====================================================

    @Column(
            name = "activity_module",
            nullable = false,
            length = 100
    )
    private String activityModule;


    // =====================================================
    // DESCRIPTION
    // =====================================================

    @Column(
            name = "activity_description",
            columnDefinition = "TEXT"
    )
    private String activityDescription;


    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "activity_status",
            nullable = false,
            length = 20
    )
    private ActivityStatus activityStatus;


    // =====================================================
    // IP
    // =====================================================

    @Column(
            name = "login_ip",
            length = 50
    )
    private String loginIp;


    // =====================================================
    // BROWSER
    // =====================================================

    @Column(
            name = "browser",
            length = 100
    )
    private String browser;


    // =====================================================
    // OPERATING SYSTEM
    // =====================================================

    @Column(
            name = "operating_system",
            length = 100
    )
    private String operatingSystem;


    // =====================================================
    // ACTIVITY TIME
    // =====================================================

    @Column(
            name = "activity_time",
            nullable = false
    )
    private LocalDateTime activityTime;


    // =====================================================
    // CREATED AT
    // =====================================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (activityTime == null) {
            activityTime = now;
        }

        if (createdAt == null) {
            createdAt = now;
        }
    }
}
