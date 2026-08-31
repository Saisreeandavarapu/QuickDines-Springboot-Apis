package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Recruitment.Entity.OnboardingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_onboarding")
@Data
public class EmployeeOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // EMPLOYEE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // =====================================================
    // MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // =====================================================
    // ONBOARDING DATE
    // =====================================================

    private LocalDate joiningDate;

    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus status;

    // =====================================================
    // MANAGER PARTICIPATION
    // =====================================================

    private boolean managerParticipated;

    private LocalDateTime managerCompletedAt;

    // =====================================================
    // HR APPROVAL / COMPLETION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_completed_by")
    private Employee hrCompletedBy;

    private LocalDateTime hrCompletedAt;

    @Column(columnDefinition = "TEXT")
    private String hrRemarks;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}