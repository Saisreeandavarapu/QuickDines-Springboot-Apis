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
@Table(name = "audit_schedules")
public class AuditSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    private BusService busService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    private LocalDate lastAuditDate;

    private LocalDate nextAuditDate;

    // 7, 15, 30, etc.
    private Integer frequencyDays;

    // AUTOMATIC / MANUAL
    private String scheduleType;

    private Boolean active = true;

    @Column(length = 1000)
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
