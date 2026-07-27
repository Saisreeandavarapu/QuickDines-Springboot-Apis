package com.HRMS.QuickDines.Wallet.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class WalletReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlySalary;

    private Double creditedAmount;

    private Double debitedAmount;

    private Double availableBalance;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
