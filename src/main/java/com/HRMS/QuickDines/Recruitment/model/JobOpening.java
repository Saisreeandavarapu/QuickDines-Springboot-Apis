package com.HRMS.QuickDines.Recruitment.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Recruitment.Entity.EmploymentType;
import com.HRMS.QuickDines.Recruitment.Entity.JobOpeningStatus;
import com.HRMS.QuickDines.Recruitment.Entity.WorkMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Branch branch;

    private Integer minimumExperience;

    private Integer maximumExperience;

    @Column(precision = 12, scale = 2)
    private BigDecimal minimumSalary;

    @Column(precision = 12, scale = 2)
    private BigDecimal maximumSalary;

    private Integer openings;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private JobOpeningStatus status;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private String experienceRequired;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryPackage;

    private LocalDate applicationDeadline;

    private LocalDateTime postedAt;

    private LocalDateTime closedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Employee who created the job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee createdBy;

    // Employee who last updated the job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee updatedBy;

    @OneToMany(
            mappedBy = "jobOpening",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Application> applications;
    @OneToMany(mappedBy = "jobOpening")
    @JsonIgnore
    private List<RecruitmentApproval> recruitmentApprovals;
}