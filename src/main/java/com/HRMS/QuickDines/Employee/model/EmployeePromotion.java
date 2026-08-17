package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Auth.model.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_promotions")
public class EmployeePromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_designation_id")
    private EmployeeDesignation previousDesignation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_designation_id")
    private EmployeeDesignation newDesignation;

    private BigDecimal previousSalary;

    private BigDecimal newSalary;

    private LocalDate promotionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    @Column(length = 1000)
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

}