package com.HRMS.QuickDines.Employee.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aadhaarNumber;
    private String panNumber;
    private String passportNumber;

    private String resumeUrl;

    private String aadhaarDocument;
    private String panDocument;
    private String degreeCertificate;
    private String pgCertificate;
    private String offerLetter;
    private String joiningLetter;
    private String salarySlips;
    private String experienceLetter;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}