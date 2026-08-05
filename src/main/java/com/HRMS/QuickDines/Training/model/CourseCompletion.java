package com.HRMS.QuickDines.Training.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "course_completion")
public class CourseCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * course_completion.assignment_id
     * → training_assignments.id
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assignment_id",
            nullable = false,
            unique = true
    )
    private TrainingAssignment assignment;


    /*
     * course_completion.employee_id
     * → employees.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;


    private LocalDate completionDate;


    @Column(precision = 5, scale = 2)
    private BigDecimal completionPercentage;


    @Column(precision = 5, scale = 2)
    private BigDecimal assessmentScore;


    @Column(length = 10)
    private String grade;


    /*
     * PASS
     * FAIL
     */
    @Column(length = 10)
    private String result;


    @Column(columnDefinition = "TEXT")
    private String remarks;


    /*
     * Course Completion → Certificate
     */
    @OneToOne(
            mappedBy = "completion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private TrainingCertificate certificate;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}