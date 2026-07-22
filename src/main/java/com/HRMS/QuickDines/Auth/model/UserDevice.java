package com.HRMS.QuickDines.Auth.model;


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

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;


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