package com.HRMS.QuickDines.Attendance.model;



import com.HRMS.QuickDines.Employee.model.Employee;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_regularization")
@Data
public class AttendanceRegularization {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="attendance_id")
    private Attendance attendance;

    private LocalDateTime requestedLoginTime;

    private LocalDateTime requestedLogoutTime;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private String status;

    @ManyToOne
    @JoinColumn(name="approved_by")
    private Employee approvedBy;

    private LocalDateTime approvedDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
