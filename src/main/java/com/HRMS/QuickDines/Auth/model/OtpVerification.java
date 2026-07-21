package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class OtpVerification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;


    private String email;

    private String mobileNumber;

    private String otp;

    private String otpType;

    private LocalDateTime expiryTime;

    private String verificationStatus;

    private LocalDateTime createdAt;

}
