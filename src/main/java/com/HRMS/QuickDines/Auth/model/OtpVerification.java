package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


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
    @CreationTimestamp
    private LocalDateTime createdAt;

}
