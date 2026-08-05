package com.HRMS.QuickDines.Employee.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_experience")
public class EmployeeExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String companyName;

    private String designation;

    private String employmentType;

    private LocalDate startDate;

    private LocalDate endDate;
    @Column(precision = 10, scale = 2)
    private BigDecimal totalExperience;

    private Boolean currentCompany;
    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(length = 1000)
    private String reasonForLeaving;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
