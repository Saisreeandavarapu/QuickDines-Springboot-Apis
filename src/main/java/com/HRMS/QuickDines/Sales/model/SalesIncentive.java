package com.HRMS.QuickDines.Sales.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class SalesIncentive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    private Double incentiveAmount;

    private Double targetBonus;

    private LocalDate creditedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
