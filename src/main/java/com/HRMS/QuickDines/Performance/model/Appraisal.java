package com.HRMS.QuickDines.Performance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "appraisals")
public class Appraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // employees.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 50)
    private String appraisalPeriod;

    private Integer appraisalYear;

    @Column(precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(length = 20)
    private String rating;

    private String appraisalStatus;

    // users.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appraised_by")
    private Employee appraisedBy;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}