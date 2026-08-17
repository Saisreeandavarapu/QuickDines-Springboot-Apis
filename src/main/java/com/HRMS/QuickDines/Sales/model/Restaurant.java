package com.HRMS.QuickDines.Sales.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Sales.Entity.RestaurantStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee sales_executive_id;


    private String restaurantName;

    private String ownerName;

    @Column(unique = true)
    private String email;

    private String mobile;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
