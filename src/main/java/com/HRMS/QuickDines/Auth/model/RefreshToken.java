package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity
@Data
public class RefreshToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;


    private String token;

    private LocalDateTime expiryDate;

    private boolean revoked;

    private String status;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
