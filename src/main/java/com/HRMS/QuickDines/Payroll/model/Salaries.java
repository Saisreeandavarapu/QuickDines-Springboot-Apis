package com.HRMS.QuickDines.Payroll.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Salaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 12, scale = 2)
    private BigDecimal basicSalary;
    @Column(precision = 12, scale = 2)
    private BigDecimal hra;
    @Column(precision = 12, scale = 2)
    private BigDecimal allowances;
    @Column(precision = 12, scale = 2)
    private BigDecimal bonus;
    @Column(precision = 12, scale = 2)
    private BigDecimal incentives;
    @Column(precision = 12, scale = 2)
    private BigDecimal deductions;
    @Column(precision = 12, scale = 2)
    private BigDecimal netSalary;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
