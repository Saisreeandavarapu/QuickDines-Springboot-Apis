package com.HRMS.QuickDines.Organization.model;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_hierarchy")
@Data
public class OrganizationHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;


    private String reportingManager;

    private String departmentHead;

    private Integer hierarchyLevel;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    @CreationTimestamp
    private LocalDateTime createdAt;

}
