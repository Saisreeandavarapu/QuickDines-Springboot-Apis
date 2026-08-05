package com.HRMS.QuickDines.Employee.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_family_members")
public class EmployeeFamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String memberName;

    private String relationship;

    private LocalDate dateOfBirth;

    private String occupation;

    private String mobileNumber;

    private Boolean dependent;

    private Boolean nominee;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
