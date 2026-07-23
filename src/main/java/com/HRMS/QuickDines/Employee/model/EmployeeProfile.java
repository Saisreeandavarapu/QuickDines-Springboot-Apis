package com.HRMS.QuickDines.Employee.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImage;
    private String fatherName;
    private String motherName;
    private String maritalStatus;
    private String bloodGroup;
    private String nationality;
    private String emergencyContact;
    private String alternateMobile;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Integer profileCompletion;

    private String profileStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
