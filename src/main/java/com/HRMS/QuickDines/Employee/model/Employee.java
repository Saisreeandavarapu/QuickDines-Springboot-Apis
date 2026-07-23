package com.HRMS.QuickDines.Employee.model;

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
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;

    private Long departmentId;

    private Long managerId;

    private String employmentType;

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
