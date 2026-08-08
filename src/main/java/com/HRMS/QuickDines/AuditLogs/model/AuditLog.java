package com.HRMS.QuickDines.AuditLogs.model;

import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // MODULE
    // =====================================================

    @Column(
            name = "module_name",
            nullable = false,
            length = 100
    )
    private String moduleName;


    // =====================================================
    // BUSINESS RECORD ID
    // =====================================================

    @Column(name = "reference_id")
    private Long referenceId;


    // =====================================================
    // ACTION
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "action_type",
            nullable = false,
            length = 30
    )
    private AuditActionType actionType;


    // =====================================================
    // EMPLOYEE WHO PERFORMED THE ACTION
    // performed_by -> employees.id
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "performed_by",
            nullable = false
    )
    private Employee performedBy;


    // =====================================================
    // EMPLOYEE WHOSE RECORD WAS AFFECTED
    // employee_id -> employees.id
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id"
    )
    private Employee employee;


    // =====================================================
    // DESCRIPTION
    // =====================================================

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;


    // =====================================================
    // OLD VALUE
    // =====================================================

    @Column(
            name = "old_value",
            columnDefinition = "JSON"
    )
    private String oldValue;


    // =====================================================
    // NEW VALUE
    // =====================================================

    @Column(
            name = "new_value",
            columnDefinition = "JSON"
    )
    private String newValue;


    // =====================================================
    // IP ADDRESS
    // =====================================================

    @Column(
            name = "ip_address",
            length = 50
    )
    private String ipAddress;


    // =====================================================
    // DEVICE INFORMATION
    // =====================================================

    @Column(
            name = "device_info",
            length = 255
    )
    private String deviceInfo;


    // =====================================================
    // CREATED DATE
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

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
