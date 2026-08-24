package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Organization.model.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "department_name")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String password;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_manager_id", nullable = true)
    @JsonIgnore
    private Employee employee;



    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // ================= RELATIONSHIPS =================

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private EmployeeProfile profile;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private EmployeeDocuments documents;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private EmployeeBankDetails bankDetails;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private EmployeeExitManagement exitManagement;

    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private EmployeeApproval approval;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeContacts> contacts;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeDesignation> designations;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeAddress> addresses;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeCertification> certifications;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeExperience> experiences;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeFamilyMember> familyMembers;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeLanguage> languages;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeePromotion> promotions;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeSkill> skills;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeTransfer> transfers;


}
