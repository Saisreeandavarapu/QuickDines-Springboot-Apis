package com.HRMS.QuickDines.Wallet.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double walletBalance;

    private Double salaryAmount;

    private Double bonusAmount;

    private Integer leaveCredits;

    private String status;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
