package com.HRMS.QuickDines.Sales.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

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

    private Integer monthlyTarget;

    private Integer achievedTarget;

    private String targetStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
