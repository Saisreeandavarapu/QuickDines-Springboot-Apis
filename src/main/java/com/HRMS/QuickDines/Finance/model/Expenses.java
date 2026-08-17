package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    private String expenseTitle;

    private String expenseCategory;
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    private String description;

    private LocalDate expenseDate;

    private String approvedBy;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
