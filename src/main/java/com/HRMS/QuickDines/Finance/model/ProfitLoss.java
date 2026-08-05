package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "profit_loss")
public class ProfitLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String financialYear;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalIncome;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalExpenses;

    @Column(precision = 15, scale = 2)
    private BigDecimal grossProfit;

    @Column(precision = 15, scale = 2)
    private BigDecimal operatingExpenses;

    @Column(precision = 15, scale = 2)
    private BigDecimal netProfit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private Employee generatedBy;

    private LocalDateTime generatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}