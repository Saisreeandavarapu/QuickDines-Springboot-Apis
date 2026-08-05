package com.HRMS.QuickDines.Notification.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "push_notifications")
public class PushNotification {

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

    @Column(name = "device_token", length = 255)
    private String deviceToken;

    // ANDROID, IOS, WEB
    private String platform;

    // PENDING, SENT, FAILED
    @Column(name = "push_status")
    private String pushStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private LocalDateTime createdAt;
}