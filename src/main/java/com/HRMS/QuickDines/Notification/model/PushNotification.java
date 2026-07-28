package com.HRMS.QuickDines.Notification.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity

public class PushNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String notificationTitle;

    private String notificationMessage;

    private String sentStatus;

    private LocalDateTime sentDate;

    private LocalDateTime createdAt;
}
