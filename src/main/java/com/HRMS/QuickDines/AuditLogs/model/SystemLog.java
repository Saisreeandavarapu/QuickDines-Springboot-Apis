package com.HRMS.QuickDines.AuditLogs.model;

import com.HRMS.QuickDines.AuditLogs.Entity.SystemLogLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // LOG LEVEL
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "log_level",
            nullable = false,
            length = 20
    )
    private SystemLogLevel logLevel;


    // =====================================================
    // MODULE
    // =====================================================

    @Column(
            name = "module_name",
            length = 100
    )
    private String moduleName;


    // =====================================================
    // SERVICE
    // =====================================================

    @Column(
            name = "service_name",
            length = 100
    )
    private String serviceName;


    // =====================================================
    // API ENDPOINT
    // =====================================================

    @Column(
            name = "api_endpoint",
            length = 255
    )
    private String apiEndpoint;


    // =====================================================
    // REQUEST METHOD
    // =====================================================

    @Column(
            name = "request_method",
            length = 20
    )
    private String requestMethod;


    // =====================================================
    // RESPONSE CODE
    // =====================================================

    @Column(name = "response_code")
    private Integer responseCode;


    // =====================================================
    // ERROR MESSAGE
    // =====================================================

    @Column(
            name = "error_message",
            columnDefinition = "TEXT"
    )
    private String errorMessage;


    // =====================================================
    // STACK TRACE
    // =====================================================

    @Column(
            name = "stack_trace",
            columnDefinition = "LONGTEXT"
    )
    private String stackTrace;


    // =====================================================
    // SERVER NAME
    // =====================================================

    @Column(
            name = "server_name",
            length = 100
    )
    private String serverName;


    // =====================================================
    // LOGGED AT
    // =====================================================

    @Column(
            name = "logged_at",
            nullable = false
    )
    private LocalDateTime loggedAt;


    // =====================================================
    // CREATED AT
    // =====================================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (loggedAt == null) {
            loggedAt = now;
        }

        if (createdAt == null) {
            createdAt = now;
        }
    }
}