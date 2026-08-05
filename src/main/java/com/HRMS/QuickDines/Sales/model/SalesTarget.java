package com.HRMS.QuickDines.Sales.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class SalesTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyTarget;
    @Column(precision = 10, scale = 2)
    private BigDecimal achievedTarget;

    private String targetStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
