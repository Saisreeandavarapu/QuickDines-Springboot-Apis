package com.HRMS.QuickDines.Auth.model;


import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity
@Data
public class UserDevice{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Employee_id")
    private Employee employee;


    private String deviceName;

    private String deviceId;

    private String browserName;

    private String operatingSystem;

    private String ipAddress;

    private LocalDateTime lastLogin;

    private String deviceStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;

}