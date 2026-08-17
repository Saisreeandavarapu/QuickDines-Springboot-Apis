package com.HRMS.QuickDines.Employee.model;

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
    @JoinColumn(name = "department_id")
    private Department department;

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_manager_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    //----------------- Relationships -----------------//


    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private EmployeeProfile profile;


    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private EmployeeDocuments documents;


    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private EmployeeBankDetails bankDetails;


    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private List<EmployeeContacts> contacts;


    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private List<EmployeeDesignation> designations;


    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
    private EmployeeExitManagement exitManagement;

}
