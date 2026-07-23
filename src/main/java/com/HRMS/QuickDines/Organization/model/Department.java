package com.HRMS.QuickDines.Organization.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;

    private String departmentCode;

    private String managerName;

    private String description;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "department")
    private List<Designation> designations;


    @OneToMany(mappedBy = "department")
    private List<Team> teams;


    @OneToMany(mappedBy = "department")
    private List<OrganizationHierarchy> hierarchies;

}
