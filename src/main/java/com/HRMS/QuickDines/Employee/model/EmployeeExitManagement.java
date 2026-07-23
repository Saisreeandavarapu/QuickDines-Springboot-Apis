package com.HRMS.QuickDines.Employee.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeExitManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate resignationDate;
    private LocalDate lastWorkingDay;
    private String reason;
    private String exitStatus;
    private String relievingLetter;
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
