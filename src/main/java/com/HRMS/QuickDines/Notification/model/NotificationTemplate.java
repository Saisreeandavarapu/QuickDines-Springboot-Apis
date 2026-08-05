package com.HRMS.QuickDines.Notification.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notification_templates")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", length = 150)
    private String templateName;

    @Column(name = "template_code", length = 50, unique = true)
    private String templateCode;

    // EMAIL, SMS, PUSH, WHATSAPP
    @Column(name = "notification_channel")
    private String notificationChannel;

    @Column(length = 255)
    private String subject;

    @Column(name = "message_template", columnDefinition = "TEXT")
    private String messageTemplate;

    @Column(columnDefinition = "TEXT")
    private String variables;

    // ACTIVE, INACTIVE
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}