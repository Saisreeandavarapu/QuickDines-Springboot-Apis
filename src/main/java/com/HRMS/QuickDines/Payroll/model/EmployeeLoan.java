package com.HRMS.QuickDines.Payroll.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_loans")
public class EmployeeLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 100)
    private String loanType;

    @Column(precision = 12, scale = 2)
    private BigDecimal loanAmount;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(precision = 12, scale = 2)
    private BigDecimal installmentAmount;

    private Integer totalInstallments;

    /*
     * approved_by → employee_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}