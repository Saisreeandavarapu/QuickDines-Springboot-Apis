package com.HRMS.QuickDines.Task.model;
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
@Table(name = "timesheet_tasks")
public class TimesheetTask {

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
    // TASK INFORMATION
    // =====================================================

    private String projectName;

    private String taskName;

    @Column(length = 3000)
    private String workDescription;


    // =====================================================
    // TIME
    // =====================================================

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer breakMinutes;

    private BigDecimal hours;


    // =====================================================
    // TASK STATUS
    // =====================================================

    private String taskStatus;


    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}