package com.HRMS.QuickDines.Wallet.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class WalletReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 12, scale = 2)
    private BigDecimal monthlySalary;
    @Column(precision = 12, scale = 2)
    private BigDecimal creditedAmount;
    @Column(precision = 12, scale = 2)
    private BigDecimal debitedAmount;
    @Column(precision = 12, scale = 2)
    private BigDecimal availableBalance;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
