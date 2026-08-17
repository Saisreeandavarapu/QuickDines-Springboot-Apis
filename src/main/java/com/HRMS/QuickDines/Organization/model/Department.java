package com.HRMS.QuickDines.Organization.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

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
