package com.HRMS.QuickDines.Auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Data
public class UserRole{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;


    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;


    private String assignedBy;

    private LocalDateTime assignedDate;

    private String status;

    private LocalDateTime createdAt;

}
