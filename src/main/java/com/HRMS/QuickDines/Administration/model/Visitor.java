package com.HRMS.QuickDines.Administration.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String visitorName;

    @Column(length = 150)
    private String companyName;

    @Column(length = 20)
    private String mobileNumber;

    @Column(length = 150)
    private String email;

    @Column(length = 255)
    private String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private String visitorStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}