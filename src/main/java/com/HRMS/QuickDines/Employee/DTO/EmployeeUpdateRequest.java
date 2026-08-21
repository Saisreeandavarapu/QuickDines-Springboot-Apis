package com.HRMS.QuickDines.Employee.DTO;

import com.HRMS.QuickDines.Employee.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeUpdateRequest {

    // Employee
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String status;

    // Profile
    private EmployeeProfile profile;

    // Documents
    private EmployeeDocuments documents;

    // Bank
    private EmployeeBankDetails bankDetails;

    // Contacts
    private List<EmployeeContacts> contacts;

    // Addresses
    private List<EmployeeAddress> addresses;

    // Certifications
    private List<EmployeeCertification> certifications;

    // Experience
    private List<EmployeeExperience> experiences;

    // Family
    private List<EmployeeFamilyMember> familyMembers;

    // Languages
    private List<EmployeeLanguage> languages;

    // Skills
    private List<EmployeeSkill> skills;

    // Designations
    private List<EmployeeDesignation> designations;

    // Promotions
    private List<EmployeePromotion> promotions;

    // Transfers
    private List<EmployeeTransfer> transfers;

    // Exit
    private EmployeeExitManagement exitManagement;

    // Approval
    private EmployeeApproval approval;
}
