package com.HRMS.QuickDines.Payroll.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PfDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uanNumber;
    private String pfNumber;
    @Column(precision = 12, scale = 2)
    private BigDecimal employeePf;
    @Column(precision = 12, scale = 2)
    private BigDecimal employerPf;
    @Column(precision = 12, scale = 2)
    private BigDecimal totalPf;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
