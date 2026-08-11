package com.HRMS.QuickDines.Training.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Training.Entity.AssignmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "training_assignments")
public class TrainingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * training_assignments.training_id
     * → trainings.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "training_id",
            nullable = false
    )
    private Training training;


    /*
     * training_assignments.employee_id
     * → employees.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;


    /*
     * training_assignments.assigned_by
     * → users.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private Employee assignedBy;


    private LocalDate assignedDate;

    private LocalDate dueDate;


    /*
     * PENDING
     * IN_PROGRESS
     * COMPLETED
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AssignmentStatus assignmentStatus;


    @Column(columnDefinition = "TEXT")
    private String remarks;


    /*
     * Assignment → Course Completion
     */
    @OneToOne(
            mappedBy = "assignment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private CourseCompletion courseCompletion;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}