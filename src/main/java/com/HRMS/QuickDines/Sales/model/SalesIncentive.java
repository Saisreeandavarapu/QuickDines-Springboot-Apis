package com.HRMS.QuickDines.Sales.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
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
    @Column(precision = 10, scale = 2)
    private BigDecimal incentiveAmount;
    @Column(precision = 10, scale = 2)
    private BigDecimal targetBonus;

    private LocalDate creditedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
