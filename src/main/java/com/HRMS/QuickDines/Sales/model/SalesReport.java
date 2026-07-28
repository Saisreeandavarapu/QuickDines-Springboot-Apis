package com.HRMS.QuickDines.Sales.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class SalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    private Integer totalVisits;

    private Integer completedDeals;

    private Double incentives;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
