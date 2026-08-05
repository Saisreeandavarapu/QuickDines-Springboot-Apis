package com.HRMS.QuickDines.Payroll.model;

//import com.HRMS.QuickDines.Organization.model.Company;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "salary_components")
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
   // private Company company;

    @Column(nullable = false, length = 100)
    private String componentName;

    @Column(nullable = false, length = 30)
    private String componentCode;

    // EARNING / DEDUCTION
    private String componentType;

    private Boolean taxable;

    // FIXED / PERCENTAGE
    private String calculationType;

    @Column(precision = 12, scale = 2)
    private BigDecimal defaultValue;

    // ACTIVE / INACTIVE
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
