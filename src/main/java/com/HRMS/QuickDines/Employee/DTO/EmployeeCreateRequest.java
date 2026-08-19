package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeCreateRequest {

    // ================= EMPLOYEE =================

    private Long companyId;
    private Long branchId;
    private Long departmentId;
    private Long roleId;
    private Long reportingManagerId;
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String password;


    // ================= PROFILE =================

    private EmployeeProfileRequest profile;


    // ================= ADDRESS =================

    private List<EmployeeAddressRequest> addresses;


    // ================= CONTACTS =================

    private List<EmployeeContactRequest> contacts;


    // ================= BANK =================

    private EmployeeBankDetailsRequest bankDetails;


    // ================= DOCUMENTS =================

    private EmployeeDocumentsRequest documents;


    // ================= CERTIFICATIONS =================

    private List<EmployeeCertificationRequest> certifications;


    // ================= EXPERIENCE =================

    private List<EmployeeExperienceRequest> experiences;


    // ================= FAMILY =================

    private List<EmployeeFamilyMemberRequest> familyMembers;


    // ================= LANGUAGES =================

    private List<EmployeeLanguageRequest> languages;


    // ================= SKILLS =================

    private List<EmployeeSkillRequest> skills;


    // ================= DESIGNATIONS =================

    private List<EmployeeDesignationRequest> designations;


    // ================= PROMOTIONS =================

    private List<EmployeePromotionRequest> promotions;


    // ================= TRANSFERS =================

    private List<EmployeeTransferRequest> transfers;


    // ================= APPROVAL =================

    private EmployeeApprovalRequest approval;


    // ================= EXIT =================

    private EmployeeExitRequest exitManagement;
}
