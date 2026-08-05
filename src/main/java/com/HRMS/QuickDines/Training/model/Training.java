package com.HRMS.QuickDines.Training.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String trainingCode;

    @Column(length = 200, nullable = false)
    private String trainingTitle;

    @Column(length = 100)
    private String trainingCategory;

    @Column(length = 150)
    private String trainerName;

    /*
     * ONLINE
     * OFFLINE
     * HYBRID
     */
    @Column(length = 20)
    private String trainingMode;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(precision = 5, scale = 2)
    private BigDecimal durationHours;

    @Column(length = 255)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    /*
     * PLANNED
     * ONGOING
     * COMPLETED
     * CANCELLED
     */
    @Column(length = 20)
    private String status;


    /*
     * trainings.created_by → users.id
     *
     * Replace User with your actual User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;


    /*
     * Training → Training Assignments
     */
    @OneToMany(
            mappedBy = "training",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TrainingAssignment> assignments =
            new ArrayList<>();


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}