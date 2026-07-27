package com.HRMS.QuickDines.Documents.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aadhaarDocument;

    private String panCardDocument;

    private String resumeDocument;

    private String sscDocument;

    private String intermediateDocument;

    private String degreeDocument;

    private String pgDocument;

    private String offerLetterDocument;

    private String salarySlipDocument;

    private String status;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
