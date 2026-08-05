package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "balance_sheet")
public class BalanceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String financialYear;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAssets;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalLiabilities;

    @Column(precision = 15, scale = 2)
    private BigDecimal shareholderEquity;

    @Column(precision = 15, scale = 2)
    private BigDecimal cashBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal closingBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private Employee generatedBy;

    private LocalDateTime generatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}