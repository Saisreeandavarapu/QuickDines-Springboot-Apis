package com.HRMS.QuickDines.Notification.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "email_notifications")
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // notification_id -> notifications.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    // employee_id -> employees.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "email_address", length = 150)
    private String emailAddress;

    @Column(length = 255)
    private String subject;

    // PENDING, SENT, FAILED
    @Column(name = "email_status")
    private String emailStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private LocalDateTime createdAt;
}