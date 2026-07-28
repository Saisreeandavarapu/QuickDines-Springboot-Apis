package com.HRMS.QuickDines.Performance.model;

import java.time.LocalDate;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String auditType;

    private String auditStatus;

    private Integer warningCount;

    private String remarks;

    private LocalDate auditDate;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
