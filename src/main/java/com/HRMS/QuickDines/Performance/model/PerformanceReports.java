package com.HRMS.QuickDines.Performance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PerformanceReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 10, scale = 2)
    private BigDecimal attendancePercentage;
    @Column(precision = 10, scale = 2)
    private BigDecimal taskCompletion;
    @Column(precision = 10, scale = 2)
    private BigDecimal targetAchievement;
    @Column(precision = 10, scale = 2)
    private BigDecimal performanceScore;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
