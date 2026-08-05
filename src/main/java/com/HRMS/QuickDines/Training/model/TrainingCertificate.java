package com.HRMS.QuickDines.Training.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "training_certificates")
public class TrainingCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * training_certificates.completion_id
     * → course_completion.id
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "completion_id",
            nullable = false,
            unique = true
    )
    private CourseCompletion completion;


    /*
     * training_certificates.employee_id
     * → employees.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;


    @Column(
            length = 100,
            unique = true
    )
    private String certificateNumber;


    @Column(length = 255)
    private String certificateUrl;


    private LocalDate issueDate;

    private LocalDate expiryDate;


    @Column(length = 150)
    private String issuedBy;


    @Column(
            length = 100,
            unique = true
    )
    private String verificationCode;


    /*
     * ACTIVE
     * EXPIRED
     */
    @Column(length = 20)
    private String status;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}