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


    // Employee who is exiting/resigning
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    // Employee/HR/Admin who approved the exit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exit_approved_by")
    private Employee exitApprovedBy;

}
