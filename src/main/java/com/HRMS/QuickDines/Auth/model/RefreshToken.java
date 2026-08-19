package com.HRMS.QuickDines.Auth.model;

import com.HRMS.QuickDines.Employee.model.Employee;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Employee_id")
    private Employee employee;


    private String token;

    private LocalDateTime expiryDate;

    private boolean revoked;

    private String status;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
