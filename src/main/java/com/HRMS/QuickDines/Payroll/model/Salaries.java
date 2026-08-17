package com.HRMS.QuickDines.Payroll.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
