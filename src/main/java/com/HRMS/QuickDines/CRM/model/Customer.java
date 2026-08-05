package com.HRMS.QuickDines.CRM.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String customerCode;

    private String customerName;

    private String companyName;

    // INDIVIDUAL / COMPANY
    private String customerType;

    private String email;

    private String phone;

    private String alternatePhone;

    private String gstNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    // assigned_sales_employee → employees.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_sales_employee")
    private Employee assignedSalesEmployee;

    // ACTIVE / INACTIVE
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
