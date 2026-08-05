package com.HRMS.QuickDines.Performance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Designation;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "promotion_recommendations")
public class PromotionRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // employees.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // appraisals.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appraisal_id", nullable = false)
    private Appraisal appraisal;

    // designations.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_designation_id")
    private Designation currentDesignation;

    // designations.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_designation_id")
    private Designation recommendedDesignation;

    @Column(precision = 12, scale = 2)
    private BigDecimal recommendedSalary;

    @Column(columnDefinition = "TEXT")
    private String recommendationReason;

    // users.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}