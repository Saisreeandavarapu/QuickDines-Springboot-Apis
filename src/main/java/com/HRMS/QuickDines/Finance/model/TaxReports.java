package com.HRMS.QuickDines.Finance.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    private Double totalSalary;

    private Double totalTds;

    private Double totalPf;

    private Double totalEsi;

    private Double netIncome;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
