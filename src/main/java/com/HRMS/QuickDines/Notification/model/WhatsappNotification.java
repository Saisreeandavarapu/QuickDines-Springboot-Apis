package com.HRMS.QuickDines.Notification.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "whatsapp_notifications")
public class WhatsappNotification {

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

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "template_name", length = 100)
    private String templateName;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    // PENDING, SENT, DELIVERED, READ, FAILED
    @Column(name = "delivery_status")
    private String deliveryStatus;

    private LocalDateTime sentAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}