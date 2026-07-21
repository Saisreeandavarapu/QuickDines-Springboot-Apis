package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String mobileNumber;

    private String password;

    private String role;

    private String profileImage;

    private boolean isActive;

    private boolean isVerified;

    private boolean accountLocked;

    private boolean accountExpired;

    private boolean credentialsExpired;

    private Integer loginAttempts;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //USER ROLES

    @OneToMany(mappedBy = "users")
    private List<UserRole> roles;


    //OTP

    @OneToMany(mappedBy = "users")
    private List<OtpVerification> otpVerifications;


    //TOKENS

    @OneToMany(mappedBy = "users")
    private List<RefreshToken> refreshTokens;


    //LOGIN HISTORY

    @OneToMany(mappedBy = "users")
    private List<LoginHistory> loginHistories;


    //DEVICES

    @OneToMany(mappedBy = "users")
    private List<UserDevice> devices;

}
