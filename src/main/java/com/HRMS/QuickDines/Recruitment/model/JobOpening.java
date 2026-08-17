package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Recruitment.Entity.JobOpeningStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    private String experienceRequired;
    @Column(precision = 10, scale = 2)
    private BigDecimal salaryPackage;

    private Integer openings;

    @Enumerated(EnumType.STRING)
    private JobOpeningStatus status;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

}