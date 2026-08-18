    package com.HRMS.QuickDines.Notification.model;

    import com.HRMS.QuickDines.Employee.model.Employee;
    import jakarta.persistence.*;
    import lombok.Data;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Data
    @Table(name = "notifications")
    public class Notification {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // employee_id -> employees.id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

        @Column(length = 200)
        private String title;

        @Column(columnDefinition = "TEXT")
        private String message;

        // INFO, WARNING, SUCCESS, ERROR
        @Column(name = "notification_type")
        private String notificationType;

        // LOW, MEDIUM, HIGH
        private String priority;

        @Column(name = "is_read")
        private Boolean isRead = false;

        // ACTIVE, ARCHIVED
        private String status;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        // One notification can have email records
        @OneToMany(mappedBy = "notification")
        private List<EmailNotification> emailNotifications;

        // One notification can have push records
        @OneToMany(mappedBy = "notification")
        private List<PushNotification> pushNotifications;

        // One notification can have SMS records
        @OneToMany(mappedBy = "notification")
        private List<SmsNotification> smsNotifications;

        // One notification can have WhatsApp records
        @OneToMany(mappedBy = "notification")
        private List<WhatsappNotification> whatsappNotifications;

        // One notification can have multiple logs
        @OneToMany(mappedBy = "notification")
        private List<NotificationLog> notificationLogs;
    }