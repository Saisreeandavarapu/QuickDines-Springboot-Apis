package com.HRMS.QuickDines.Wallet.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Wallet.Entity.WalletStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 12, scale = 2)
    private BigDecimal walletBalance;
    @Column(precision = 12, scale = 2)
    private BigDecimal salaryAmount;
    @Column(precision = 12, scale = 2)
    private BigDecimal bonusAmount;

    private Integer leaveCredits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
