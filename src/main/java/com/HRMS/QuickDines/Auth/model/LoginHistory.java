package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class LoginHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;


    private LocalDate loginDate;

    private LocalTime loginTime;

    private LocalTime logoutTime;

    private String ipAddress;

    private String browserName;

    private String operatingSystem;

    private String loginStatus;

    private String remarks;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
