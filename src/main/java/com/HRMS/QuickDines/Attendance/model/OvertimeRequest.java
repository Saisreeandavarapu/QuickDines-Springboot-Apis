    package com.HRMS.QuickDines.Attendance.model;


    import com.HRMS.QuickDines.Attendance.Entity.OvertimeStatus;
    import com.HRMS.QuickDines.Employee.model.Employee;
    import jakarta.persistence.*;
    import lombok.Data;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "overtime_requests")
    @Data
    public class OvertimeRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name="employee_id")
        private Employee employee;

        @ManyToOne
        @JoinColumn(name="attendance_id")
        private Attendance attendance;

        private LocalDate requestDate;

        @Column(precision = 4, scale = 2)
        private BigDecimal requestedHours;

        @Column(precision = 4, scale = 2)
        private BigDecimal approvedHours;

        @Column(columnDefinition = "TEXT")
        private String reason;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OvertimeStatus status;

        @ManyToOne
        @JoinColumn(name="approved_by")
        private Employee approvedBy;

        private LocalDateTime approvedAt;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
    }