package com.HRMS.QuickDines.Finance.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String financialYear;
    @Column(precision = 12, scale = 2)
    private BigDecimal totalSalary;
    @Column(precision = 12, scale = 2)
    private BigDecimal totalTds;
    @Column(precision = 12, scale = 2)
    private BigDecimal totalPf;
    @Column(precision = 12, scale = 2)
    private BigDecimal totalEsi;
    @Column(precision = 12, scale = 2)
    private BigDecimal netIncome;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
