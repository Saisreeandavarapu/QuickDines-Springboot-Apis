package com.HRMS.QuickDines.Task.model;

import com.HRMS.QuickDines.Employee.model.Employee;
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
@Table(
        name = "employee_timesheets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_work_date",
                        columnNames = {"employee_id", "work_date"}
                )
        }
)
public class EmployeeTimesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer breakMinutes;

    private BigDecimal totalHours;

    private String projectName;

    @Column(length = 2000)
    private String workDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimesheetStatus status = TimesheetStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private LocalDateTime approvedAt;

    @Column(length = 1000)
    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
