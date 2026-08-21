package com.HRMS.QuickDines.Employee.Service;

import com.HRMS.QuickDines.AdvanceServices.CloudinaryService;
import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Auth.repo.RoleRepository;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.DTO.*;
import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Employee.model.*;
import com.HRMS.QuickDines.Employee.repo.*;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Designation;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.HRMS.QuickDines.Organization.repo.DesignationRepository;
import com.HRMS.QuickDines.Organization.repo.TeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final DepartmentRepository departmentRepository;
    private final CloudinaryService cloudinaryService;
    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeDesignationRepository employeeDesignationRepository;
    private final EmployeeExitRepository employeeExitRepository;
    private final EmployeeBankRepository employeeBankRepository;
    private final EmployeeContactRepository employeeContactRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final EmployeeCertificationRepository employeeCertificationRepository;
    private final EmployeeExperienceRepository employeeExperienceRepository;
    private final EmployeeLanguageRepository employeeLanguageRepository;
    private final EmployeeFamilyMemberRepository employeeFamilyMemberRepository;
    private final EmployeeAddressRepository employeeAddressRepository;
    private final EmployeePromotionRepository employeePromotionRepository;
    private final EmployeeTransferRepository employeeTransferRepository;
    private final DesignationRepository designationRepository;
    private final TeamRepository teamRepository;
    private final BranchRepository branchRepository;
    private final EmployeeApprovalRepository employeeApprovalRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


    // =========================================================
    // LOGGED-IN EMPLOYEE
    // =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }


    // =========================================================
    // CLIENT INFORMATION
    // =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


    @Transactional
    public String createEmployee(EmployeeCreateRequest request) {

        // =====================================================
        // 1. FETCH MASTER DATA
        // =====================================================

        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company not found"));

        Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new RuntimeException("Branch not found"));

        Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));


        // =====================================================
        // 2. CREATE EMPLOYEE
        // =====================================================

        Employee employee = new Employee();

        employee.setCompany(company);
        employee.setBranch(branch);
        employee.setDepartment(department);
        employee.setRole(role);

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setMobileNumber(request.getMobileNumber());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setPassword(request.getPassword());

        employee.setStatus("ACTIVE");


        // =====================================================
        // 3. REPORTING MANAGER
        // =====================================================

        if (request.getReportingManagerId() != null) {

            Employee manager = employeeRepository.findById(request.getReportingManagerId()).orElseThrow(() -> new RuntimeException("Reporting manager not found"));

            employee.setEmployee(manager);
        }


//        // =====================================================
//        // 4. USER ACCOUNT
//        // =====================================================
//
//        if (request.getUserId() != null) {
//
//            Users user = usersRepository.findById(request.getUserId())
//                    .orElseThrow(() ->
//                            new RuntimeException("User not found"));
//
//            employee.setUser(user);
//        }


        // =====================================================
        // 5. GENERATE EMPLOYEE ID
        // =====================================================

        Long count = employeeRepository.count() + 1;

        String departmentCode = department.getDepartmentCode().toUpperCase();

        String employeeCode = "QD-" + departmentCode + "-" + LocalDate.now().getYear() + "-" + String.format("%03d", count);

        employee.setEmployeeId(employeeCode);


        // =====================================================
        // 6. SAVE EMPLOYEE FIRST
        // =====================================================

        Employee savedEmployee = employeeRepository.save(employee);


        // =====================================================
        // 7. PROFILE
        // =====================================================

        if (request.getProfile() != null) {

            EmployeeProfileRequest data = request.getProfile();

            EmployeeProfile profile = new EmployeeProfile();

            profile.setEmployee(savedEmployee);

            profile.setProfileImage(data.getProfileImage());
            profile.setFatherName(data.getFatherName());
            profile.setMotherName(data.getMotherName());
            profile.setMaritalStatus(data.getMaritalStatus());
            profile.setBloodGroup(data.getBloodGroup());
            profile.setNationality(data.getNationality());
            profile.setEmergencyContact(data.getEmergencyContact());
            profile.setAlternateMobile(data.getAlternateMobile());
            profile.setAddress(data.getAddress());
            profile.setCity(data.getCity());
            profile.setState(data.getState());
            profile.setPincode(data.getPincode());
            profile.setCountry(data.getCountry());

            profile.setProfileCompletion(0);
            profile.setProfileStatus("INCOMPLETE");

            employeeProfileRepository.save(profile);
        }


        // =====================================================
        // 8. ADDRESSES
        // =====================================================

        if (request.getAddresses() != null) {

            for (EmployeeAddressRequest data : request.getAddresses()) {

                EmployeeAddress address = new EmployeeAddress();

                address.setEmployee(savedEmployee);

                address.setAddressType(data.getAddressType());
                address.setAddressLine1(data.getAddressLine1());
                address.setAddressLine2(data.getAddressLine2());
                address.setCity(data.getCity());
                address.setDistrict(data.getDistrict());
                address.setState(data.getState());
                address.setCountry(data.getCountry());
                address.setPostalCode(data.getPostalCode());

                employeeAddressRepository.save(address);
            }
        }


        // =====================================================
        // 9. CONTACTS
        // =====================================================

        if (request.getContacts() != null) {

            for (EmployeeContactRequest data : request.getContacts()) {

                EmployeeContacts contact = new EmployeeContacts();

                contact.setEmployee(savedEmployee);

                contact.setEmergencyContactName(data.getEmergencyContactName());

                contact.setRelation(data.getRelation());

                contact.setMobileNumber(data.getMobileNumber());

                contact.setAlternateNumber(data.getAlternateNumber());

                employeeContactRepository.save(contact);
            }
        }


        // =====================================================
        // 10. BANK DETAILS
        // =====================================================

        if (request.getBankDetails() != null) {

            EmployeeBankDetailsRequest data = request.getBankDetails();

            EmployeeBankDetails bank = new EmployeeBankDetails();

            bank.setEmployee(savedEmployee);

            bank.setAccountHolderName(data.getAccountHolderName());

            bank.setBankName(data.getBankName());

            bank.setAccountNumber(data.getAccountNumber());

            bank.setIfscCode(data.getIfscCode());

            bank.setBranchName(data.getBranchName());

            bank.setUpiId(data.getUpiId());

            bank.setAccountStatus(data.getAccountStatus());

            employeeBankRepository.save(bank);
        }


        // =====================================================
        // 11. DOCUMENTS
        // =====================================================

        if (request.getDocuments() != null) {

            EmployeeDocumentsRequest data = request.getDocuments();

            EmployeeDocuments documents = new EmployeeDocuments();

            documents.setEmployee(savedEmployee);

            documents.setAadhaarNumber(data.getAadhaarNumber());

            documents.setPanNumber(data.getPanNumber());

            documents.setPassportNumber(data.getPassportNumber());

            documents.setResumeUrl(data.getResumeUrl());

            documents.setAadhaarDocument(data.getAadhaarDocument());

            documents.setPanDocument(data.getPanDocument());

            documents.setDegreeCertificate(data.getDegreeCertificate());

            documents.setPgCertificate(data.getPgCertificate());

            documents.setOfferLetter(data.getOfferLetter());

            documents.setJoiningLetter(data.getJoiningLetter());

            documents.setSalarySlips(data.getSalarySlips());

            documents.setExperienceLetter(data.getExperienceLetter());

            documents.setStatus(data.getStatus());

            employeeDocumentRepository.save(documents);
        }


        // =====================================================
        // 12. CERTIFICATIONS
        // =====================================================

        if (request.getCertifications() != null) {

            for (EmployeeCertificationRequest data : request.getCertifications()) {

                EmployeeCertification certification = new EmployeeCertification();

                certification.setEmployee(savedEmployee);

                certification.setCertificationName(data.getCertificationName());

                certification.setIssuingOrganization(data.getIssuingOrganization());

                certification.setCertificateNumber(data.getCertificateNumber());

                certification.setIssueDate(data.getIssueDate());

                certification.setExpiryDate(data.getExpiryDate());

                certification.setCredentialUrl(data.getCredentialUrl());

                certification.setAttachmentUrl(data.getAttachmentUrl());

                certification.setStatus(data.getStatus());

                employeeCertificationRepository.save(certification);
            }
        }


        // =====================================================
        // 13. EXPERIENCE
        // =====================================================

        if (request.getExperiences() != null) {

            for (EmployeeExperienceRequest data : request.getExperiences()) {

                EmployeeExperience experience = new EmployeeExperience();

                experience.setEmployee(savedEmployee);

                experience.setCompanyName(data.getCompanyName());

                experience.setDesignation(data.getDesignation());

                experience.setEmploymentType(data.getEmploymentType());

                experience.setStartDate(data.getStartDate());

                experience.setEndDate(data.getEndDate());

                experience.setTotalExperience(data.getTotalExperience());

                experience.setCurrentCompany(data.getCurrentCompany());

                experience.setSalary(data.getSalary());

                experience.setReasonForLeaving(data.getReasonForLeaving());

                employeeExperienceRepository.save(experience);
            }
        }


        // =====================================================
        // 14. FAMILY MEMBERS
        // =====================================================

        if (request.getFamilyMembers() != null) {

            for (EmployeeFamilyMemberRequest data : request.getFamilyMembers()) {

                EmployeeFamilyMember family = new EmployeeFamilyMember();

                family.setEmployee(savedEmployee);

                family.setMemberName(data.getMemberName());

                family.setRelationship(data.getRelationship());

                family.setDateOfBirth(data.getDateOfBirth());

                family.setOccupation(data.getOccupation());

                family.setMobileNumber(data.getMobileNumber());

                family.setDependent(data.getDependent());

                family.setNominee(data.getNominee());

                employeeFamilyMemberRepository.save(family);
            }
        }


        // =====================================================
        // 15. LANGUAGES
        // =====================================================

        if (request.getLanguages() != null) {

            for (EmployeeLanguageRequest data : request.getLanguages()) {

                EmployeeLanguage language = new EmployeeLanguage();

                language.setEmployee(savedEmployee);

                language.setLanguageName(data.getLanguageName());

                language.setReadLevel(data.getReadLevel());

                language.setWriteLevel(data.getWriteLevel());

                language.setSpeakLevel(data.getSpeakLevel());

                language.setNativeLanguage(data.getNativeLanguage());

                employeeLanguageRepository.save(language);
            }
        }


        // =====================================================
        // 16. SKILLS
        // =====================================================

        if (request.getSkills() != null) {

            for (EmployeeSkillRequest data : request.getSkills()) {

                EmployeeSkill skill = new EmployeeSkill();

                skill.setEmployee(savedEmployee);

                skill.setSkillName(data.getSkillName());

                skill.setSkillCategory(data.getSkillCategory());

                skill.setProficiencyLevel(data.getProficiencyLevel());

                skill.setExperienceYears(data.getExperienceYears());

                skill.setLastUsed(data.getLastUsed());

                skill.setCertificationAvailable(data.getCertificationAvailable());

                skill.setRemarks(data.getRemarks());

                employeeSkillRepository.save(skill);
            }
        }


//        // =====================================================
//        // 17. APPROVAL
//        // =====================================================
//
        // =====================================================
// 17. EMPLOYEE APPROVAL
// =====================================================

        EmployeeApproval approval = new EmployeeApproval();

        approval.setEmployee(savedEmployee);


// =====================================================
// CHECK WHETHER SUPER ADMIN ALREADY EXISTS
// =====================================================

        boolean superAdminExists = employeeRepository.existsByRole_RoleNameIgnoreCase("SUPER_ADMIN");


// =====================================================
// SUPER ADMIN LOGIC
// =====================================================

        if ("SUPER_ADMIN".equalsIgnoreCase(role.getRoleName())) {

            if (!superAdminExists) {

                // =================================================
                // FIRST SUPER ADMIN
                // =================================================
                // No Super Admin exists yet.
                // Automatically approve.

                approval.setHrStatus(ApprovalStatus.APPROVED);

                approval.setAdminStatus(ApprovalStatus.APPROVED);

                approval.setDepartmentHeadStatus(ApprovalStatus.APPROVED);

                approval.setFinalStatus(ApprovalStatus.APPROVED);

                approval.setFinalApprovedAt(LocalDateTime.now());

                employee.setStatus("ACTIVE");

            } else {

                // =================================================
                // ANOTHER SUPER ADMIN
                // =================================================
                // Super Admin already exists.
                // Therefore this new Super Admin requires
                // normal approval.

                approval.setHrStatus(ApprovalStatus.PENDING);

                approval.setAdminStatus(ApprovalStatus.PENDING);

                approval.setDepartmentHeadStatus(ApprovalStatus.PENDING);

                approval.setFinalStatus(ApprovalStatus.PENDING);

                employee.setStatus("PENDING_APPROVAL");
            }

        } else {

            // =====================================================
            // NORMAL EMPLOYEE / OTHER ROLE
            // =====================================================
            // Always follows normal approval process.

            approval.setHrStatus(ApprovalStatus.PENDING);

            approval.setAdminStatus(ApprovalStatus.PENDING);

            approval.setDepartmentHeadStatus(ApprovalStatus.PENDING);

            approval.setFinalStatus(ApprovalStatus.PENDING);

            employee.setStatus("PENDING_APPROVAL");
        }


// =====================================================
// ACCOUNT STATUS
// =====================================================

        approval.setAccountCreated(false);
        approval.setWelcomeMailSent(false);


// =====================================================
// SAVE EMPLOYEE STATUS
// =====================================================

        employeeRepository.save(employee);


// =====================================================
// SAVE APPROVAL
// =====================================================

        employeeApprovalRepository.save(approval);


        // =====================================================
        // 18. RETURN
        // =====================================================


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("EMPLOYEE", savedEmployee.getEmployeeId(), performedBy, savedEmployee.getEmployeeId(), "Employee created successfully");


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(savedEmployee.getEmployeeId(), "CREATE_EMPLOYEE", "EMPLOYEE", "New employee created: " + savedEmployee.getEmployeeId(), ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE", "EmployeeService", "Employee created successfully: " + savedEmployee.getEmployeeId());

        return "Employee Created Successfully";

    }


    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }


    public Employee getEmployee(Long id) {

        return employeeRepository.findById(id).get();
    }


    public String updateEmployee(Long id, Employee employee) {

        // =========================================================
        // FETCH EXISTING EMPLOYEE
        // =========================================================

        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));


        // =========================================================
        // CAPTURE OLD VALUES BEFORE UPDATE
        // =========================================================

        String oldValue = convertEmployeeToJson(existingEmployee);


        // =========================================================
        // UPDATE EMPLOYEE
        // =========================================================

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setMobileNumber(employee.getMobileNumber());
        existingEmployee.setStatus(employee.getStatus());
        existingEmployee.setDepartment(employee.getDepartment());


        // =========================================================
        // SAVE UPDATED EMPLOYEE
        // =========================================================

        employeeRepository.save(existingEmployee);


        // =========================================================
        // CAPTURE NEW VALUES AFTER UPDATE
        // =========================================================

        String newValue = convertEmployeeToJson(existingEmployee);


        // =========================================================
        // GET LOGGED-IN EMPLOYEE
        // =========================================================

        String performedBy = getLoggedInEmployeeId();


        // =========================================================
        // AUDIT LOG
        // =========================================================

        auditLogsService.logUpdate("EMPLOYEE", existingEmployee.getEmployeeId(), performedBy, existingEmployee.getEmployeeId(), "Employee information updated", oldValue, newValue);


        // =========================================================
        // ACTIVITY LOG
        // =========================================================

        auditLogsService.logActivity(existingEmployee.getEmployeeId(), "UPDATE_EMPLOYEE", "EMPLOYEE", "Employee information updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        // =========================================================
        // SYSTEM LOG
        // =========================================================

        auditLogsService.logInfo("EMPLOYEE", "EmployeeService", "Employee updated: " + existingEmployee.getEmployeeId());


        return "Employee Updated Successfully";
    }


// =========================================================
// CONVERT EMPLOYEE VALUES TO JSON
// =========================================================

    private String convertEmployeeToJson(Employee employee) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> employeeData = new LinkedHashMap<>();

            employeeData.put("employeeId", employee.getEmployeeId());

            employeeData.put("firstName", employee.getFirstName());

            employeeData.put("lastName", employee.getLastName());

            employeeData.put("email", employee.getEmail());

            employeeData.put("mobileNumber", employee.getMobileNumber());

            employeeData.put("status", employee.getStatus());

            employeeData.put("departmentId", employee.getDepartment().getDepartmentCode()

            );

            return objectMapper.writeValueAsString(employeeData);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to convert employee data to JSON", e);
        }
    }


    public String deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        String employeeCode = employee.getEmployeeId();


        employeeRepository.delete(employee);

        String performedBy = getLoggedInEmployeeId();
        // AUDIT
        auditLogsService.logDelete("EMPLOYEE", employeeCode, performedBy, employeeCode, "Employee deleted");


        // ACTIVITY
        auditLogsService.logActivity(employeeCode, "DELETE_EMPLOYEE", "EMPLOYEE", "Employee deleted: " + employeeCode, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        // SYSTEM
        auditLogsService.logInfo("EMPLOYEE", "EmployeeService", "Employee deleted: " + employeeCode);


        return "Employee Deleted";
    }


    public Employee getEmployeeDetails(Long id) {

        return employeeProfileRepository.findById(id).orElseThrow().getEmployee();
    }


    public String uploadDocument(String employeeId, MultipartFile file, String documentType) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        EmployeeDocuments documents = employeeDocumentRepository.findByEmployeeId(employeeId).orElse(new EmployeeDocuments());
        documents.setEmployee(employee);
        // Upload to Cloudinary
        String fileUrl = cloudinaryService.uploadFile(file);

        switch (documentType.toUpperCase()) {

            case "AADHAAR":
                documents.setAadhaarDocument(fileUrl);
                break;

            case "PAN":
                documents.setPanDocument(fileUrl);
                break;

            case "DEGREE":
                documents.setDegreeCertificate(fileUrl);
                break;

            case "PG":
                documents.setPgCertificate(fileUrl);
                break;

            case "RESUME":
                documents.setResumeUrl(fileUrl);
                break;

            case "OFFER_LETTER":
                documents.setOfferLetter(fileUrl);
                break;

            case "JOINING_LETTER":
                documents.setJoiningLetter(fileUrl);
                break;

            case "SALARY_SLIP":
                documents.setSalarySlips(fileUrl);
                break;

            case "EXPERIENCE":
                documents.setExperienceLetter(fileUrl);
                break;

            default:
                throw new RuntimeException("Invalid Document Type");
        }

        documents.setStatus("UPLOADED");

        employeeDocumentRepository.save(documents);
        String performedBy = getLoggedInEmployeeId();
        employeeDocumentRepository.save(documents);


        auditLogsService.logCreate("EMPLOYEE_DOCUMENT", employee.getEmployeeId(), performedBy, employee.getEmployeeId(), documentType + " document uploaded");

        auditLogsService.logActivity(employee.getEmployeeId(), "UPLOAD_DOCUMENT", "EMPLOYEE_DOCUMENT", documentType + " document uploaded", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_DOCUMENT", "EmployeeService", documentType + " document uploaded for " + employee.getEmployeeId());
        return documentType + " Uploaded Successfully";

    }


    // --------------------------------------------

    public EmployeeDocuments getDocuments(String employeeId) {

        return employeeDocumentRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Documents Not Found"));
    }


    // --------------------------------------------

    public String deleteDocument(Long id) {

        EmployeeDocuments document = employeeDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document Not Found"));
        employeeDocumentRepository.delete(document);
        // AUDIT
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("DOCUMENT", document.getEmployee().getEmployeeId(), performedBy, document.getEmployee().getEmployeeId(), "Employee documents deleted");


        // ACTIVITY
        auditLogsService.logActivity(document.getEmployee().getEmployeeId(), "DELETE_DOCUMENTS", "DOCUMENT", "Employee documents deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        // SYSTEM
        auditLogsService.logInfo("DOCUMENT", "DocumentService", "Documents deleted for employee " + document.getEmployee().getEmployeeId());
        return "Document Deleted Successfully";

    }

    //=========================================
// PROFILE
//=========================================

    public String createProfile(String employeeId, EmployeeProfile profile) {
        Employee employee = employeeRepository.findById(Long.valueOf(employeeId)).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        profile.setEmployee(employee);
        profile.setProfileStatus("ACTIVE");
        profile.setProfileCompletion(0);
        employeeProfileRepository.save(profile);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_PROFILE", employeeId, performedBy, employeeId, "Employee profile created");

        auditLogsService.logActivity(employeeId, "CREATE_PROFILE", "EMPLOYEE_PROFILE", "Employee profile created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_PROFILE", "EmployeeService", "Employee profile created for " + employeeId);
        return "Employee Profile Created Successfully";
    }

    public List<EmployeeProfile> getAllProfiles() {
        return employeeProfileRepository.findAll();
    }

    public EmployeeProfile getProfile(String employeeId) {
        return employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));
    }


    public String updateProfile(String employeeId, EmployeeProfile profile) {

        EmployeeProfile existingProfile = employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));

        existingProfile.setFatherName(profile.getFatherName());

        existingProfile.setMotherName(profile.getMotherName());

        existingProfile.setMaritalStatus(profile.getMaritalStatus());

        existingProfile.setBloodGroup(profile.getBloodGroup());

        existingProfile.setNationality(profile.getNationality());

        existingProfile.setEmergencyContact(profile.getEmergencyContact());

        existingProfile.setAlternateMobile(profile.getAlternateMobile());

        existingProfile.setAddress(profile.getAddress());

        existingProfile.setCity(profile.getCity());

        existingProfile.setState(profile.getState());

        existingProfile.setPincode(profile.getPincode());

        existingProfile.setCountry(profile.getCountry());

        existingProfile.setProfileImage(profile.getProfileImage());

        existingProfile.setProfileCompletion(profile.getProfileCompletion());

        existingProfile.setProfileStatus(profile.getProfileStatus());

        employeeProfileRepository.save(existingProfile);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("DOCUMENT", existingProfile.getEmployee().getEmployeeId(), performedBy, existingProfile.getEmployee().getEmployeeId(), "Employee documents updated", "Previous documents", "Updated documents");

        auditLogsService.logActivity(existingProfile.getEmployee().getEmployeeId(), "UPDATE_DOCUMENTS", "DOCUMENT", "Employee documents updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("DOCUMENT", "DocumentService", "Documents updated for employee " + existingProfile.getEmployee().getEmployeeId());

        return "Employee Profile Updated Successfully";
    }


    public String deleteProfile(String employeeId) {

        EmployeeProfile profile = employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));
        employeeProfileRepository.delete(profile);
        String oldValue = convertToJson(profile);

        employeeProfileRepository.delete(profile);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_PROFILE", employeeId, performedBy, employeeId, "Employee profile deleted");

        auditLogsService.logActivity(employeeId, "DELETE_PROFILE", "EMPLOYEE_PROFILE", "Employee profile deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_PROFILE", "EmployeeService", "Employee profile deleted for " + employeeId);
        return "Employee Profile Deleted Successfully";
    }


    //=========================================
// BANK DETAILS
//=========================================

    public String createBankDetails(String employeeId, EmployeeBankDetails bankDetails) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        bankDetails.setEmployee(employee);
        bankDetails.setAccountStatus("ACTIVE");

        employeeBankRepository.save(bankDetails);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_BANK", employeeId, performedBy, employeeId, "Bank details created");

        auditLogsService.logActivity(employeeId, "CREATE_BANK_DETAILS", "EMPLOYEE_BANK", "Bank details created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_BANK", "EmployeeService", "Bank details created for " + employeeId);
        return "Bank Details Added Successfully";
    }


    public EmployeeBankDetails getBankDetails(String employeeId) {

        return employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Bank Details Not Found"));
    }


    public String updateBankDetails(String employeeId, EmployeeBankDetails bankDetails) {

        EmployeeBankDetails existingDetails = employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Bank Details Not Found"));

        existingDetails.setAccountHolderName(bankDetails.getAccountHolderName());

        existingDetails.setBankName(bankDetails.getBankName());

        existingDetails.setAccountNumber(bankDetails.getAccountNumber());

        existingDetails.setIfscCode(bankDetails.getIfscCode());

        existingDetails.setBranchName(bankDetails.getBranchName());

        existingDetails.setUpiId(bankDetails.getUpiId());

        existingDetails.setAccountStatus(bankDetails.getAccountStatus());

        employeeBankRepository.save(existingDetails);


        String newValue = convertToJson(existingDetails);

        String performedBy = getLoggedInEmployeeId();
        String oldValue = convertToJson(existingDetails);
        auditLogsService.logUpdate("EMPLOYEE_BANK", employeeId, performedBy, employeeId, "Bank details updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_BANK_DETAILS", "EMPLOYEE_BANK", "Bank details updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_BANK", "EmployeeService", "Bank details updated for " + employeeId);

        return "Bank Details Updated Successfully";
    }


    public String deleteBankDetails(String employeeId) {

        EmployeeBankDetails bankDetails = employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Bank Details Not Found"));

        employeeBankRepository.delete(bankDetails);
        String oldValue = convertToJson(bankDetails);

        employeeBankRepository.delete(bankDetails);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_BANK", employeeId, performedBy, employeeId, "Bank details deleted");

        auditLogsService.logActivity(employeeId, "DELETE_BANK_DETAILS", "EMPLOYEE_BANK", "Bank details deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_BANK", "EmployeeService", "Bank details deleted for " + employeeId);
        return "Bank Details Deleted Successfully";
    }


    //=========================================
// CONTACTS
//=========================================

    public String createContacts(String employeeId, EmployeeContacts contacts) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        contacts.setEmployee(employee);
        employeeContactRepository.save(contacts);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_CONTACT", employeeId, performedBy, employeeId, "Emergency contact created");

        auditLogsService.logActivity(employeeId, "CREATE_CONTACT", "EMPLOYEE_CONTACT", "Emergency contact created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_CONTACT", "EmployeeService", "Emergency contact created for " + employeeId);
        return "Emergency Contact Added Successfully";
    }


    public List<EmployeeContacts> getContacts(String employeeId) {
        return employeeContactRepository.findByEmployeeId(employeeId);
    }


    public String updateContacts(String id, EmployeeContacts contacts) {

        EmployeeContacts existingContact = (EmployeeContacts) employeeContactRepository.findByEmployeeId(id);

        existingContact.setEmergencyContactName(contacts.getEmergencyContactName());

        existingContact.setRelation(contacts.getRelation());

        existingContact.setMobileNumber(contacts.getMobileNumber());

        existingContact.setAlternateNumber(contacts.getAlternateNumber());

        employeeContactRepository.save(existingContact);
        String newValue = convertToJson(existingContact);
        String performedBy = getLoggedInEmployeeId();
        String oldValue = convertToJson(existingContact);
        auditLogsService.logUpdate("EMPLOYEE_CONTACT", id, performedBy, id, "Emergency contact updated", oldValue, newValue);

        auditLogsService.logActivity(id, "UPDATE_CONTACT", "EMPLOYEE_CONTACT", "Emergency contact updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_CONTACT", "EmployeeService", "Emergency contact updated for " + id);

        return "Emergency Contact Updated Successfully";
    }


    public String deleteContacts(String id) {

        EmployeeContacts contact = (EmployeeContacts) employeeContactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact Not Found"));
        employeeContactRepository.delete(contact);
        return "Emergency Contact Deleted Successfully";
    }


    //=========================================
// DESIGNATION
//=========================================

    public String createDesignation(String employeeId, EmployeeDesignation designation) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        designation.setEmployee(employee);

        employeeDesignationRepository.save(designation);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_DESIGNATION", employeeId, performedBy, employeeId, "Employee designation created");

        auditLogsService.logActivity(employeeId, "CREATE_DESIGNATION", "EMPLOYEE_DESIGNATION", "Employee designation created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_DESIGNATION", "EmployeeService", "Designation created for " + employeeId);
        return "Employee Designation Added Successfully";
    }


    public List<EmployeeDesignation> getDesignation(String employeeId) {

        return employeeDesignationRepository.findByEmployeeId(employeeId);
    }


    public String updateDesignation(String id, EmployeeDesignation designation) {

        EmployeeDesignation existingDesignation = (EmployeeDesignation) employeeDesignationRepository.findByEmployeeId(id);

        existingDesignation.setDesignation(designation.getDesignation());

        existingDesignation.setPreviousDesignation(designation.getPreviousDesignation());

        existingDesignation.setPromotedDate(designation.getPromotedDate());

        existingDesignation.setSalaryGrade(designation.getSalaryGrade());

        employeeDesignationRepository.save(existingDesignation);
        String newValue = convertToJson(existingDesignation);
        String performedBy = getLoggedInEmployeeId();
        String oldValue = convertToJson(existingDesignation);
        auditLogsService.logUpdate("EMPLOYEE_DESIGNATION", id, performedBy, id, "Employee designation updated", oldValue, newValue);

        auditLogsService.logActivity(id, "UPDATE_DESIGNATION", "EMPLOYEE_DESIGNATION", "Employee designation updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_DESIGNATION", "EmployeeService", "Designation updated for " + id);
        return "Employee Designation Updated Successfully";
    }


    public String deleteDesignation(String id) {

        EmployeeDesignation designation = employeeDesignationRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Designation Not Found"));

        employeeDesignationRepository.delete(designation);

        String oldValue = convertToJson(designation);

        employeeDesignationRepository.delete(designation);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_DESIGNATION", String.valueOf(designation.getId()), performedBy, designation.getEmployee().getEmployeeId(), "Employee designation deleted");

        auditLogsService.logActivity(designation.getEmployee().getEmployeeId(), "DELETE_DESIGNATION", "EMPLOYEE_DESIGNATION", "Employee designation deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_DESIGNATION", "EmployeeService", "Designation deleted");

        return "Employee Designation Deleted Successfully";
    }


    //=========================================
// EXIT MANAGEMENT
//=========================================

    public String createExitManagement(String employeeId, EmployeeExitManagement exitManagement) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        exitManagement.setEmployee(employee);
        if (exitManagement.getExitStatus() == null) {
            exitManagement.setExitStatus("PENDING");
        }
        employeeExitRepository.save(exitManagement);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_EXIT", employeeId, performedBy, employeeId, "Employee exit management created");

        auditLogsService.logActivity(employeeId, "CREATE_EXIT", "EMPLOYEE_EXIT", "Employee exit management created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXIT", "EmployeeService", "Exit management created for " + employeeId);
        return "Employee Exit Management Created Successfully";
    }


    public EmployeeExitManagement getExitManagement(String employeeId) {

        return employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Exit Management Details Not Found"));
    }


    public String updateExitManagement(String employeeId, EmployeeExitManagement exitManagement) {

        EmployeeExitManagement existingExit = employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Exit Management Details Not Found"));

        existingExit.setResignationDate(exitManagement.getResignationDate());

        existingExit.setLastWorkingDay(exitManagement.getLastWorkingDay());

        existingExit.setReason(exitManagement.getReason());

        existingExit.setExitStatus(exitManagement.getExitStatus());

        existingExit.setRelievingLetter(exitManagement.getRelievingLetter());

        existingExit.setRemarks(exitManagement.getRemarks());

        employeeExitRepository.save(existingExit);
        String newValue = convertToJson(existingExit);
        String performedBy = getLoggedInEmployeeId();
        String oldValue = convertToJson(existingExit);
        auditLogsService.logUpdate("EMPLOYEE_EXIT", employeeId, performedBy, employeeId, "Employee exit management updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_EXIT", "EMPLOYEE_EXIT", "Employee exit management updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXIT", "EmployeeService", "Exit management updated for " + employeeId);

        return "Employee Exit Management Updated Successfully";
    }


    public String deleteExitManagement(String employeeId) {

        EmployeeExitManagement exitManagement = employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Exit Management Details Not Found"));

        employeeExitRepository.delete(exitManagement);
        String oldValue = convertToJson(exitManagement);

        employeeExitRepository.delete(exitManagement);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_EXIT", employeeId, performedBy, employeeId, "Employee exit management deleted");

        auditLogsService.logActivity(employeeId, "DELETE_EXIT", "EMPLOYEE_EXIT", "Employee exit management deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXIT", "EmployeeService", "Exit management deleted for " + employeeId);

        return "Employee Exit Management Deleted Successfully";
    }
    //=================================
// EMPLOYEE SKILLS
//=================================

    public String createSkill(String employeeId, EmployeeSkill employeeSkill) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeSkill.setEmployee(employee);

        employeeSkillRepository.save(employeeSkill);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_SKILL", employeeId, performedBy, employeeId, "Employee skill created");

        auditLogsService.logActivity(employeeId, "CREATE_SKILL", "EMPLOYEE_SKILL", "Employee skill created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_SKILL", "EmployeeService", "Skill created for " + employeeId);

        return "Employee Skill Added Successfully";
    }

    public List<EmployeeSkill> getSkills() {

        return employeeSkillRepository.findAll();
    }

    public EmployeeSkill getSkill(Long id) {

        return employeeSkillRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));
    }

    public String updateSkill(Long id, EmployeeSkill employeeSkill) {

        EmployeeSkill existingSkill = employeeSkillRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));

        existingSkill.setSkillName(employeeSkill.getSkillName());
        existingSkill.setSkillCategory(employeeSkill.getSkillCategory());
        existingSkill.setProficiencyLevel(employeeSkill.getProficiencyLevel());
        existingSkill.setExperienceYears(employeeSkill.getExperienceYears());
        existingSkill.setLastUsed(employeeSkill.getLastUsed());
        existingSkill.setCertificationAvailable(employeeSkill.getCertificationAvailable());
        existingSkill.setRemarks(employeeSkill.getRemarks());

        employeeSkillRepository.save(existingSkill);
        String oldValue = convertToJson(existingSkill);

// existing setters

        employeeSkillRepository.save(existingSkill);

        String newValue = convertToJson(existingSkill);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("EMPLOYEE_SKILL", String.valueOf(id), performedBy, existingSkill.getEmployee().getEmployeeId(), "Employee skill updated", oldValue, newValue);

        auditLogsService.logActivity(existingSkill.getEmployee().getEmployeeId(), "UPDATE_SKILL", "EMPLOYEE_SKILL", "Employee skill updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_SKILL", "EmployeeService", "Skill updated: " + id);

        return "Employee Skill Updated Successfully";
    }

    public String deleteSkill(Long id) {

        EmployeeSkill existingSkill = employeeSkillRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));

        employeeSkillRepository.delete(existingSkill);

        String employeeId = existingSkill.getEmployee().getEmployeeId();

        employeeSkillRepository.delete(existingSkill);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_SKILL", String.valueOf(id), performedBy, employeeId, "Employee skill deleted");

        auditLogsService.logActivity(employeeId, "DELETE_SKILL", "EMPLOYEE_SKILL", "Employee skill deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_SKILL", "EmployeeService", "Skill deleted: " + id);

        return "Employee Skill Deleted Successfully";
    }

    //=================================
// EMPLOYEE CERTIFICATIONS
//=================================

    public String createCertification(String employeeId, EmployeeCertification employeeCertification) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeCertification.setEmployee(employee);

        employeeCertificationRepository.save(employeeCertification);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_CERTIFICATION", employeeId, performedBy, employeeId, "Employee certification created");

        auditLogsService.logActivity(employeeId, "CREATE_CERTIFICATION", "EMPLOYEE_CERTIFICATION", "Employee certification created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_CERTIFICATION", "EmployeeService", "Certification created for " + employeeId);

        return "Employee Certification Added Successfully";
    }

    public List<EmployeeCertification> getCertifications() {

        return employeeCertificationRepository.findAll();
    }

    public EmployeeCertification getCertification(Long id) {

        return employeeCertificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));
    }

    public String updateCertification(Long id, EmployeeCertification employeeCertification) {

        EmployeeCertification existingCertification = employeeCertificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));

        existingCertification.setCertificationName(employeeCertification.getCertificationName());

        existingCertification.setIssuingOrganization(employeeCertification.getIssuingOrganization());

        existingCertification.setCertificateNumber(employeeCertification.getCertificateNumber());

        existingCertification.setIssueDate(employeeCertification.getIssueDate());

        existingCertification.setExpiryDate(employeeCertification.getExpiryDate());

        existingCertification.setCredentialUrl(employeeCertification.getCredentialUrl());

        existingCertification.setAttachmentUrl(employeeCertification.getAttachmentUrl());

        existingCertification.setStatus(employeeCertification.getStatus());

        employeeCertificationRepository.save(existingCertification);
        String newValue = convertToJson(existingCertification);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingCertification.getEmployee().getEmployeeId();
        String oldValue = convertToJson(existingCertification);

        auditLogsService.logUpdate("EMPLOYEE_CERTIFICATION", String.valueOf(id), performedBy, employeeId, "Employee certification updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_CERTIFICATION", "EMPLOYEE_CERTIFICATION", "Employee certification updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_CERTIFICATION", "EmployeeService", "Certification updated: " + id);

        return "Employee Certification Updated Successfully";
    }

    public String deleteCertification(Long id) {

        EmployeeCertification existingCertification = employeeCertificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));

        employeeCertificationRepository.delete(existingCertification);
        String employeeId = existingCertification.getEmployee().getEmployeeId();

        employeeCertificationRepository.delete(existingCertification);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_CERTIFICATION", String.valueOf(id), performedBy, employeeId, "Employee certification deleted");

        auditLogsService.logActivity(employeeId, "DELETE_CERTIFICATION", "EMPLOYEE_CERTIFICATION", "Employee certification deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_CERTIFICATION", "EmployeeService", "Certification deleted: " + id);

        return "Employee Certification Deleted Successfully";
    }

    //=================================
// EMPLOYEE EXPERIENCE
//=================================

    public String createExperience(String employeeId, EmployeeExperience employeeExperience) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeExperience.setEmployee(employee);

        employeeExperienceRepository.save(employeeExperience);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_EXPERIENCE", employeeId, performedBy, employeeId, "Employee experience created");

        auditLogsService.logActivity(employeeId, "CREATE_EXPERIENCE", "EMPLOYEE_EXPERIENCE", "Employee experience created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXPERIENCE", "EmployeeService", "Experience created for " + employeeId);

        return "Employee Experience Added Successfully";
    }

    public List<EmployeeExperience> getExperiences() {

        return employeeExperienceRepository.findAll();
    }

    public EmployeeExperience getExperience(Long id) {

        return employeeExperienceRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));
    }

    public String updateExperience(Long id, EmployeeExperience employeeExperience) {

        EmployeeExperience existingExperience = employeeExperienceRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));

        existingExperience.setCompanyName(employeeExperience.getCompanyName());
        existingExperience.setDesignation(employeeExperience.getDesignation());
        existingExperience.setEmploymentType(employeeExperience.getEmploymentType());
        existingExperience.setStartDate(employeeExperience.getStartDate());
        existingExperience.setEndDate(employeeExperience.getEndDate());
        existingExperience.setTotalExperience(employeeExperience.getTotalExperience());
        existingExperience.setCurrentCompany(employeeExperience.getCurrentCompany());
        existingExperience.setSalary(employeeExperience.getSalary());
        existingExperience.setReasonForLeaving(employeeExperience.getReasonForLeaving());

        employeeExperienceRepository.save(existingExperience);

        String oldValue = convertToJson(existingExperience);

// existing setters

        employeeExperienceRepository.save(existingExperience);

        String newValue = convertToJson(existingExperience);

        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingExperience.getEmployee().getEmployeeId();

        auditLogsService.logUpdate("EMPLOYEE_EXPERIENCE", String.valueOf(id), performedBy, employeeId, "Employee experience updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_EXPERIENCE", "EMPLOYEE_EXPERIENCE", "Employee experience updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXPERIENCE", "EmployeeService", "Experience updated: " + id);

        return "Employee Experience Updated Successfully";
    }

    public String deleteExperience(Long id) {

        EmployeeExperience existingExperience = employeeExperienceRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));

        employeeExperienceRepository.delete(existingExperience);
        String employeeId = existingExperience.getEmployee().getEmployeeId();

        employeeExperienceRepository.delete(existingExperience);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_EXPERIENCE", String.valueOf(id), performedBy, employeeId, "Employee experience deleted");

        auditLogsService.logActivity(employeeId, "DELETE_EXPERIENCE", "EMPLOYEE_EXPERIENCE", "Employee experience deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_EXPERIENCE", "EmployeeService", "Experience deleted: " + id);

        return "Employee Experience Deleted Successfully";
    }

    //=================================
// EMPLOYEE LANGUAGES
//=================================

    public String createLanguage(String employeeId, EmployeeLanguage employeeLanguage) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeLanguage.setEmployee(employee);

        employeeLanguageRepository.save(employeeLanguage);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_LANGUAGE", employeeId, performedBy, employeeId, "Employee language created");

        auditLogsService.logActivity(employeeId, "CREATE_LANGUAGE", "EMPLOYEE_LANGUAGE", "Employee language created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_LANGUAGE", "EmployeeService", "Language created for " + employeeId);

        return "Employee Language Added Successfully";
    }

    public List<EmployeeLanguage> getLanguages() {

        return employeeLanguageRepository.findAll();
    }

    public EmployeeLanguage getLanguage(Long id) {

        return employeeLanguageRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Language Not Found"));
    }

    public String updateLanguage(Long id, EmployeeLanguage employeeLanguage) {

        EmployeeLanguage existingLanguage = employeeLanguageRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Language Not Found"));

        existingLanguage.setLanguageName(employeeLanguage.getLanguageName());
        existingLanguage.setReadLevel(employeeLanguage.getReadLevel());
        existingLanguage.setWriteLevel(employeeLanguage.getWriteLevel());
        existingLanguage.setSpeakLevel(employeeLanguage.getSpeakLevel());
        existingLanguage.setNativeLanguage(employeeLanguage.getNativeLanguage());

        employeeLanguageRepository.save(existingLanguage);

        String newValue = convertToJson(existingLanguage);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingLanguage.getEmployee().getEmployeeId();
        String oldValue = convertToJson(existingLanguage);
        auditLogsService.logUpdate("EMPLOYEE_LANGUAGE", String.valueOf(id), performedBy, employeeId, "Employee language updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_LANGUAGE", "EMPLOYEE_LANGUAGE", "Employee language updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_LANGUAGE", "EmployeeService", "Language updated: " + id);

        return "Employee Language Updated Successfully";
    }

    public String deleteLanguage(Long id) {

        EmployeeLanguage existingLanguage = employeeLanguageRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Language Not Found"));

        employeeLanguageRepository.delete(existingLanguage);
        String employeeId = existingLanguage.getEmployee().getEmployeeId();

        employeeLanguageRepository.delete(existingLanguage);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_LANGUAGE", String.valueOf(id), performedBy, employeeId, "Employee language deleted");

        auditLogsService.logActivity(employeeId, "DELETE_LANGUAGE", "EMPLOYEE_LANGUAGE", "Employee language deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_LANGUAGE", "EmployeeService", "Language deleted: " + id);

        return "Employee Language Deleted Successfully";
    }

    //=================================
// EMPLOYEE FAMILY MEMBERS
//=================================

    public String createFamilyMember(String employeeId, EmployeeFamilyMember employeeFamilyMember) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeFamilyMember.setEmployee(employee);

        employeeFamilyMemberRepository.save(employeeFamilyMember);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_FAMILY_MEMBER", employeeId, performedBy, employeeId, "Employee family member created");

        auditLogsService.logActivity(employeeId, "CREATE_FAMILY_MEMBER", "EMPLOYEE_FAMILY_MEMBER", "Employee family member created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_FAMILY_MEMBER", "EmployeeService", "Family member created for " + employeeId);

        return "Employee Family Member Added Successfully";
    }

    public List<EmployeeFamilyMember> getFamilyMembers() {

        return employeeFamilyMemberRepository.findAll();
    }

    public EmployeeFamilyMember getFamilyMember(Long id) {

        return employeeFamilyMemberRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));
    }

    public String updateFamilyMember(Long id, EmployeeFamilyMember employeeFamilyMember) {

        EmployeeFamilyMember existingMember = employeeFamilyMemberRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));

        existingMember.setMemberName(employeeFamilyMember.getMemberName());
        existingMember.setRelationship(employeeFamilyMember.getRelationship());
        existingMember.setDateOfBirth(employeeFamilyMember.getDateOfBirth());
        existingMember.setOccupation(employeeFamilyMember.getOccupation());
        existingMember.setMobileNumber(employeeFamilyMember.getMobileNumber());
        existingMember.setDependent(employeeFamilyMember.getDependent());
        existingMember.setNominee(employeeFamilyMember.getNominee());

        employeeFamilyMemberRepository.save(existingMember);
        String oldValue = convertToJson(existingMember);

// existing setters

        employeeFamilyMemberRepository.save(existingMember);

        String newValue = convertToJson(existingMember);

        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingMember.getEmployee().getEmployeeId();

        auditLogsService.logUpdate("EMPLOYEE_FAMILY_MEMBER", String.valueOf(id), performedBy, employeeId, "Employee family member updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_FAMILY_MEMBER", "EMPLOYEE_FAMILY_MEMBER", "Employee family member updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_FAMILY_MEMBER", "EmployeeService", "Family member updated: " + id);

        return "Employee Family Member Updated Successfully";
    }

    public String deleteFamilyMember(Long id) {

        EmployeeFamilyMember existingMember = employeeFamilyMemberRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));

        employeeFamilyMemberRepository.delete(existingMember);
        String employeeId = existingMember.getEmployee().getEmployeeId();

        employeeFamilyMemberRepository.delete(existingMember);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_FAMILY_MEMBER", String.valueOf(id), performedBy, employeeId, "Employee family member deleted");

        auditLogsService.logActivity(employeeId, "DELETE_FAMILY_MEMBER", "EMPLOYEE_FAMILY_MEMBER", "Employee family member deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_FAMILY_MEMBER", "EmployeeService", "Family member deleted: " + id);

        return "Employee Family Member Deleted Successfully";
    }

    //=================================
// EMPLOYEE ADDRESSES
//=================================

    public String createAddress(String employeeId, EmployeeAddress employeeAddress) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // Save Employee Address
        employeeAddress.setEmployee(employee);
        employeeAddressRepository.save(employeeAddress);

        // Check if Profile already exists
        EmployeeProfile profile = employeeProfileRepository.findByEmployee(employee).orElse(new EmployeeProfile());

        profile.setEmployee(employee);
        profile.setAddress(employeeAddress.getAddressLine1() + ", " + employeeAddress.getAddressLine2());
        profile.setCity(employeeAddress.getCity());
        profile.setState(employeeAddress.getState());
        profile.setPincode(employeeAddress.getPostalCode());
        profile.setCountry(employeeAddress.getCountry());

        profile.setProfileStatus("ACTIVE");

        employeeProfileRepository.save(profile);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_ADDRESS", employeeId, performedBy, employeeId, "Employee address created");

        auditLogsService.logActivity(employeeId, "CREATE_ADDRESS", "EMPLOYEE_ADDRESS", "Employee address created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_ADDRESS", "EmployeeService", "Address created for " + employeeId);

        return "Employee Address Created Successfully";
    }

    public List<EmployeeAddress> getAddresses() {

        return employeeAddressRepository.findAll();
    }

    public EmployeeAddress getAddress(Long id) {

        return employeeAddressRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Address Not Found"));
    }

    public String updateAddress(Long id, EmployeeAddress employeeAddress) {

        EmployeeAddress existingAddress = employeeAddressRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Address Not Found"));

        existingAddress.setAddressType(employeeAddress.getAddressType());
        existingAddress.setAddressLine1(employeeAddress.getAddressLine1());
        existingAddress.setAddressLine2(employeeAddress.getAddressLine2());
        existingAddress.setCity(employeeAddress.getCity());
        existingAddress.setDistrict(employeeAddress.getDistrict());
        existingAddress.setState(employeeAddress.getState());
        existingAddress.setCountry(employeeAddress.getCountry());
        existingAddress.setPostalCode(employeeAddress.getPostalCode());

        employeeAddressRepository.save(existingAddress);

        // Update Employee Profile Automatically
        Employee employee = existingAddress.getEmployee();

        EmployeeProfile profile = employeeProfileRepository.findByEmployee(employee).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));

        profile.setAddress(employeeAddress.getAddressLine1() + ", " + employeeAddress.getAddressLine2());

        profile.setCity(employeeAddress.getCity());
        profile.setState(employeeAddress.getState());
        profile.setCountry(employeeAddress.getCountry());
        profile.setPincode(employeeAddress.getPostalCode());

        employeeProfileRepository.save(profile);
        String newValue = convertToJson(existingAddress);

        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingAddress.getEmployee().getEmployeeId();
        String oldValue = convertToJson(existingAddress);
        auditLogsService.logUpdate("EMPLOYEE_ADDRESS", String.valueOf(id), performedBy, employeeId, "Employee address updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_ADDRESS", "EMPLOYEE_ADDRESS", "Employee address and profile updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_ADDRESS", "EmployeeService", "Address updated: " + id);

        return "Employee Address & Profile Updated Successfully";
    }

    public String deleteAddress(Long id) {

        EmployeeAddress existingAddress = employeeAddressRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Address Not Found"));

        employeeAddressRepository.delete(existingAddress);
        String employeeId = existingAddress.getEmployee().getEmployeeId();

        employeeAddressRepository.delete(existingAddress);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_ADDRESS", String.valueOf(id), performedBy, employeeId, "Employee address deleted");

        auditLogsService.logActivity(employeeId, "DELETE_ADDRESS", "EMPLOYEE_ADDRESS", "Employee address deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_ADDRESS", "EmployeeService", "Address deleted: " + id);

        return "Employee Address Deleted Successfully";
    }

    //=================================
// EMPLOYEE PROMOTIONS
//=================================

    public String createPromotion(String employeeId, EmployeePromotion employeePromotion) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeePromotion.setEmployee(employee);

        employeePromotionRepository.save(employeePromotion);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_PROMOTION", employeeId, performedBy, employeeId, "Employee promotion created");

        auditLogsService.logActivity(employeeId, "CREATE_PROMOTION", "EMPLOYEE_PROMOTION", "Employee promotion created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_PROMOTION", "EmployeeService", "Promotion created for " + employeeId);

        return "Employee Promotion Created Successfully";
    }

    public List<EmployeePromotion> getPromotions() {

        return employeePromotionRepository.findAll();
    }

    public EmployeePromotion getPromotion(Long id) {

        return employeePromotionRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));
    }

    public String updatePromotion(Long id, EmployeePromotion employeePromotion) {

        EmployeePromotion existingPromotion = employeePromotionRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));

        existingPromotion.setPreviousDesignation(employeePromotion.getPreviousDesignation());
        existingPromotion.setNewDesignation(employeePromotion.getNewDesignation());
        existingPromotion.setPreviousSalary(employeePromotion.getPreviousSalary());
        existingPromotion.setNewSalary(employeePromotion.getNewSalary());
        existingPromotion.setPromotionDate(employeePromotion.getPromotionDate());
        existingPromotion.setApprovedBy(employeePromotion.getApprovedBy());
        existingPromotion.setReason(employeePromotion.getReason());

        employeePromotionRepository.save(existingPromotion);
        String oldValue = convertToJson(existingPromotion);
        String newValue = convertToJson(existingPromotion);

        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingPromotion.getEmployee().getEmployeeId();

        auditLogsService.logUpdate("EMPLOYEE_PROMOTION", String.valueOf(id), performedBy, employeeId, "Employee promotion updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_PROMOTION", "EMPLOYEE_PROMOTION", "Employee promotion updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_PROMOTION", "EmployeeService", "Promotion updated: " + id);

        return "Employee Promotion Updated Successfully";
    }

    public String deletePromotion(Long id) {

        EmployeePromotion existingPromotion = employeePromotionRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));

        employeePromotionRepository.delete(existingPromotion);
        String employeeId = existingPromotion.getEmployee().getEmployeeId();

        employeePromotionRepository.delete(existingPromotion);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_PROMOTION", String.valueOf(id), performedBy, employeeId, "Employee promotion deleted");

        auditLogsService.logActivity(employeeId, "DELETE_PROMOTION", "EMPLOYEE_PROMOTION", "Employee promotion deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_PROMOTION", "EmployeeService", "Promotion deleted: " + id);

        return "Employee Promotion Deleted Successfully";
    }
    //=================================
// EMPLOYEE TRANSFERS
//=================================

    public String createTransfer(String employeeId, EmployeeTransfer employeeTransfer) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeTransfer.setEmployee(employee);

        employeeTransferRepository.save(employeeTransfer);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("EMPLOYEE_TRANSFER", employeeId, performedBy, employeeId, "Employee transfer created");

        auditLogsService.logActivity(employeeId, "CREATE_TRANSFER", "EMPLOYEE_TRANSFER", "Employee transfer created", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_TRANSFER", "EmployeeService", "Transfer created for " + employeeId);

        return "Employee Transfer Created Successfully";
    }

    public List<EmployeeTransfer> getTransfers() {

        return employeeTransferRepository.findAll();
    }

    public EmployeeTransfer getTransfer(Long id) {

        return employeeTransferRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));
    }

    public String updateTransfer(Long id, EmployeeTransfer employeeTransfer) {

        EmployeeTransfer existingTransfer = employeeTransferRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));

        existingTransfer.setFromDepartment(employeeTransfer.getFromDepartment());
        existingTransfer.setToDepartment(employeeTransfer.getToDepartment());

        existingTransfer.setFromBranch(employeeTransfer.getFromBranch());
        existingTransfer.setToBranch(employeeTransfer.getToBranch());

        existingTransfer.setFromTeam(employeeTransfer.getFromTeam());
        existingTransfer.setToTeam(employeeTransfer.getToTeam());

        existingTransfer.setTransferDate(employeeTransfer.getTransferDate());
        existingTransfer.setApprovedBy(employeeTransfer.getApprovedBy());
        existingTransfer.setReason(employeeTransfer.getReason());

        employeeTransferRepository.save(existingTransfer);
        String oldValue = convertToJson(existingTransfer);
        String newValue = convertToJson(existingTransfer);

        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingTransfer.getEmployee().getEmployeeId();

        auditLogsService.logUpdate("EMPLOYEE_TRANSFER", String.valueOf(id), performedBy, employeeId, "Employee transfer updated", oldValue, newValue);

        auditLogsService.logActivity(employeeId, "UPDATE_TRANSFER", "EMPLOYEE_TRANSFER", "Employee transfer updated", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_TRANSFER", "EmployeeService", "Transfer updated: " + id);

        return "Employee Transfer Updated Successfully";
    }

    public String deleteTransfer(Long id) {

        EmployeeTransfer existingTransfer = employeeTransferRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));

        employeeTransferRepository.delete(existingTransfer);
        String employeeId = existingTransfer.getEmployee().getEmployeeId();

        employeeTransferRepository.delete(existingTransfer);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete("EMPLOYEE_TRANSFER", String.valueOf(id), performedBy, employeeId, "Employee transfer deleted");

        auditLogsService.logActivity(employeeId, "DELETE_TRANSFER", "EMPLOYEE_TRANSFER", "Employee transfer deleted", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("EMPLOYEE_TRANSFER", "EmployeeService", "Transfer deleted: " + id);

        return "Employee Transfer Deleted Successfully";
    }

    //=================================
// REPORTS
//=================================

    public List<EmployeeSkill> certifiedSkills() {

        return employeeSkillRepository.findAll().stream().filter(skill -> Boolean.TRUE.equals(skill.getCertificationAvailable())).toList();
    }

    public List<EmployeeCertification> activeCertifications() {

        return employeeCertificationRepository.findAll().stream().filter(certification -> "ACTIVE".equalsIgnoreCase(certification.getStatus())).toList();
    }

    public List<EmployeeCertification> expiredCertifications() {

        return employeeCertificationRepository.findAll().stream().filter(certification -> certification.getExpiryDate() != null && certification.getExpiryDate().isBefore(LocalDate.now())).toList();
    }

    public List<EmployeeExperience> currentExperiences() {

        return employeeExperienceRepository.findAll().stream().filter(EmployeeExperience::getCurrentCompany).toList();
    }

    public List<EmployeeExperience> previousExperiences() {

        return employeeExperienceRepository.findAll().stream().filter(experience -> !experience.getCurrentCompany()).toList();
    }

    public List<EmployeePromotion> latestPromotions() {

        return employeePromotionRepository.findAll().stream().sorted(Comparator.comparing(EmployeePromotion::getPromotionDate).reversed()).toList();
    }

    public List<EmployeeTransfer> latestTransfers() {

        return employeeTransferRepository.findAll().stream().sorted(Comparator.comparing(EmployeeTransfer::getTransferDate).reversed()).toList();
    }

    //=================================
// DASHBOARD
//=================================

    public Map<String, Object> getCounts() {

        Map<String, Object> counts = new HashMap<>();

        counts.put("totalEmployees", employeeRepository.count());

        counts.put("totalProfiles", employeeProfileRepository.count());

        counts.put("totalDocuments", employeeDocumentRepository.count());

        counts.put("totalBankDetails", employeeBankRepository.count());

        counts.put("totalEmergencyContacts", employeeContactRepository.count());

        counts.put("totalDesignations", employeeDesignationRepository.count());

        counts.put("totalExitManagement", employeeExitRepository.count());

        counts.put("totalSkills", employeeSkillRepository.count());

        counts.put("totalCertifications", employeeCertificationRepository.count());

        counts.put("totalExperiences", employeeExperienceRepository.count());

        counts.put("totalLanguages", employeeLanguageRepository.count());

        counts.put("totalFamilyMembers", employeeFamilyMemberRepository.count());

        counts.put("totalAddresses", employeeAddressRepository.count());

        counts.put("totalPromotions", employeePromotionRepository.count());

        counts.put("totalTransfers", employeeTransferRepository.count());

        counts.put("activeEmployees", employeeRepository.findAll().stream().filter(employee -> "ACTIVE".equalsIgnoreCase(employee.getStatus())).count());

        counts.put("inactiveEmployees", employeeRepository.findAll().stream().filter(employee -> "INACTIVE".equalsIgnoreCase(employee.getStatus())).count());

        counts.put("certifiedSkills", employeeSkillRepository.findAll().stream().filter(skill -> Boolean.TRUE.equals(skill.getCertificationAvailable())).count());

        counts.put("activeCertifications", employeeCertificationRepository.findAll().stream().filter(c -> "ACTIVE".equalsIgnoreCase(c.getStatus())).count());

        counts.put("expiredCertifications", employeeCertificationRepository.findAll().stream().filter(c -> c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now())).count());

        counts.put("currentExperienceEmployees", employeeExperienceRepository.findAll().stream().filter(EmployeeExperience::getCurrentCompany).count());

        return counts;
    }

    public List<Employee> searchEmployees(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepository.findAll();
        }

        return employeeRepository.searchEmployees(keyword.trim());
    }

    public List<Employee> getEmployeesByDepartmentName(String departmentName) {

        Department department = departmentRepository.findByDepartmentNameIgnoreCase(departmentName).orElseThrow(() -> new RuntimeException("Department Not Found"));

        return employeeRepository.findByDepartmentId(department.getId());
    }

    public List<Employee> findEmployeesByDesignationName(String designationName) {
        return employeeDesignationRepository.findEmployeesByDesignationName(designationName);
    }

    @Transactional
    public Map<String, Object> uploadEmployees(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Excel file is required");
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new RuntimeException("Only .xlsx Excel files are supported");
        }

        int employeeCount = 0;
        int addressCount = 0;
        int bankCount = 0;
        int documentCount = 0;

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            /*
             * =====================================================
             * 1. EMPLOYEES
             * =====================================================
             */

            Sheet employeeSheet = workbook.getSheet("Employees");

            if (employeeSheet == null) {
                throw new RuntimeException("Employees sheet not found");
            }

            Map<String, Employee> employeeMap = new HashMap<>();

            for (int i = 1; i <= employeeSheet.getLastRowNum(); i++) {

                Row row = employeeSheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String employeeId = getCellValue(row.getCell(0));

                if (employeeId == null || employeeId.isBlank()) {
                    continue;
                }

                if (employeeRepository.findByEmployeeId(employeeId).isPresent()) {

                    throw new RuntimeException("Employee already exists: " + employeeId);
                }

                Employee employee = new Employee();

                employee.setEmployeeId(employeeId);

                employee.setFirstName(getCellValue(row.getCell(1)));

                employee.setLastName(getCellValue(row.getCell(2)));

                employee.setEmail(getCellValue(row.getCell(3)));

                employee.setMobileNumber(getCellValue(row.getCell(4)));

                employee.setGender(getCellValue(row.getCell(5)));

                employee.setDateOfBirth(getLocalDate(row.getCell(6)));

                employee.setJoiningDate(getLocalDate(row.getCell(7)));

                employee.setStatus(getCellValue(row.getCell(8)));

                /*
                 * Company / Branch / Department
                 *
                 * Resolve these using your repositories.
                 */

                Employee savedEmployee = employeeRepository.save(employee);

                employeeMap.put(employeeId, savedEmployee);

                employeeCount++;
            }

            /*
             * =====================================================
             * 2. ADDRESSES
             * =====================================================
             */

            Sheet addressSheet = workbook.getSheet("Addresses");

            if (addressSheet != null) {

                for (int i = 1; i <= addressSheet.getLastRowNum(); i++) {

                    Row row = addressSheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeAddress address = new EmployeeAddress();

                    address.setEmployee(employee);

                    address.setAddressType(getCellValue(row.getCell(1)));

                    address.setAddressLine1(getCellValue(row.getCell(2)));

                    address.setAddressLine2(getCellValue(row.getCell(3)));

                    address.setCity(getCellValue(row.getCell(4)));

                    address.setDistrict(getCellValue(row.getCell(5)));

                    address.setState(getCellValue(row.getCell(6)));

                    address.setCountry(getCellValue(row.getCell(7)));

                    address.setPostalCode(getCellValue(row.getCell(8)));

                    employeeAddressRepository.save(address);

                    addressCount++;
                }
            }

            /*
             * =====================================================
             * 3. BANK DETAILS
             * =====================================================
             */

            Sheet bankSheet = workbook.getSheet("BankDetails");

            if (bankSheet != null) {

                for (int i = 1; i <= bankSheet.getLastRowNum(); i++) {

                    Row row = bankSheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeBankDetails bank = new EmployeeBankDetails();

                    bank.setEmployee(employee);

                    bank.setAccountHolderName(getCellValue(row.getCell(1)));

                    bank.setBankName(getCellValue(row.getCell(2)));

                    bank.setAccountNumber(getCellValue(row.getCell(3)));

                    bank.setIfscCode(getCellValue(row.getCell(4)));

                    bank.setBranchName(getCellValue(row.getCell(5)));

                    bank.setUpiId(getCellValue(row.getCell(6)));

                    bank.setAccountStatus(getCellValue(row.getCell(7)));

                    employeeBankRepository.save(bank);

                    bankCount++;
                }
            }

            /*
             * =====================================================
             * 4. DOCUMENTS
             * =====================================================
             */

            Sheet documentSheet = workbook.getSheet("Documents");

            if (documentSheet != null) {

                for (int i = 1; i <= documentSheet.getLastRowNum(); i++) {

                    Row row = documentSheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeDocuments documents = new EmployeeDocuments();

                    documents.setEmployee(employee);

                    documents.setAadhaarNumber(getCellValue(row.getCell(1)));

                    documents.setPanNumber(getCellValue(row.getCell(2)));

                    documents.setPassportNumber(getCellValue(row.getCell(3)));

                    documents.setResumeUrl(getCellValue(row.getCell(4)));

                    documents.setAadhaarDocument(getCellValue(row.getCell(5)));

                    documents.setPanDocument(getCellValue(row.getCell(6)));

                    documents.setDegreeCertificate(getCellValue(row.getCell(7)));

                    documents.setPgCertificate(getCellValue(row.getCell(8)));

                    documents.setOfferLetter(getCellValue(row.getCell(9)));

                    documents.setJoiningLetter(getCellValue(row.getCell(10)));

                    documents.setSalarySlips(getCellValue(row.getCell(11)));

                    documents.setExperienceLetter(getCellValue(row.getCell(12)));

                    documents.setStatus(getCellValue(row.getCell(13)));

                    employeeDocumentRepository.save(documents);

                    documentCount++;
                }
            }

            /*
             * =====================================================
             * OTHER SHEETS
             * =====================================================
             */
            // Contacts
            Sheet contactSheet = workbook.getSheet("Contacts");

            if (contactSheet != null) {

                for (int i = 1; i <= contactSheet.getLastRowNum(); i++) {

                    Row row = contactSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeContacts contact = new EmployeeContacts();

                    contact.setEmployee(employee);

                    contact.setEmergencyContactName(getCellValue(row.getCell(1)));

                    contact.setRelation(getCellValue(row.getCell(2)));

                    contact.setMobileNumber(getCellValue(row.getCell(3)));

                    contact.setAlternateNumber(getCellValue(row.getCell(4)));

                    employeeContactRepository.save(contact);
                }
            }
//             //Certifications
            Sheet certificationSheet = workbook.getSheet("Certifications");

            if (certificationSheet != null) {

                for (int i = 1; i <= certificationSheet.getLastRowNum(); i++) {

                    Row row = certificationSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeCertification certification = new EmployeeCertification();

                    certification.setEmployee(employee);

                    certification.setCertificationName(getCellValue(row.getCell(1)));

                    certification.setIssuingOrganization(getCellValue(row.getCell(2)));

                    certification.setCertificateNumber(getCellValue(row.getCell(3)));

                    certification.setIssueDate(getLocalDate(row.getCell(4)));

                    certification.setExpiryDate(getLocalDate(row.getCell(5)));

                    certification.setCredentialUrl(getCellValue(row.getCell(6)));

                    certification.setAttachmentUrl(getCellValue(row.getCell(7)));

                    certification.setStatus(getCellValue(row.getCell(8)));

                    employeeCertificationRepository.save(certification);
                }
            }
//             * Experience
            Sheet experienceSheet = workbook.getSheet("Experience");

            if (experienceSheet != null) {

                for (int i = 1; i <= experienceSheet.getLastRowNum(); i++) {

                    Row row = experienceSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeExperience experience = new EmployeeExperience();

                    experience.setEmployee(employee);

                    experience.setCompanyName(getCellValue(row.getCell(1)));

                    experience.setDesignation(getCellValue(row.getCell(2)));

                    experience.setEmploymentType(getCellValue(row.getCell(3)));

                    experience.setStartDate(getLocalDate(row.getCell(4)));

                    experience.setEndDate(getLocalDate(row.getCell(5)));

                    String totalExperience = getCellValue(row.getCell(6));

                    if (totalExperience != null && !totalExperience.isBlank()) {

                        experience.setTotalExperience(new java.math.BigDecimal(totalExperience));
                    }

                    String currentCompany = getCellValue(row.getCell(7));

                    if (currentCompany != null && !currentCompany.isBlank()) {

                        experience.setCurrentCompany(Boolean.parseBoolean(currentCompany));
                    }

                    String salary = getCellValue(row.getCell(8));

                    if (salary != null && !salary.isBlank()) {

                        experience.setSalary(new java.math.BigDecimal(salary));
                    }

                    experience.setReasonForLeaving(getCellValue(row.getCell(9)));

                    employeeExperienceRepository.save(experience);
                }
            }
//             * FamilyMembers
            Sheet familySheet = workbook.getSheet("FamilyMembers");

            if (familySheet != null) {

                for (int i = 1; i <= familySheet.getLastRowNum(); i++) {

                    Row row = familySheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeFamilyMember family = new EmployeeFamilyMember();

                    family.setEmployee(employee);

                    family.setMemberName(getCellValue(row.getCell(1)));

                    family.setRelationship(getCellValue(row.getCell(2)));

                    family.setDateOfBirth(getLocalDate(row.getCell(3)));

                    family.setOccupation(getCellValue(row.getCell(4)));

                    family.setMobileNumber(getCellValue(row.getCell(5)));

                    String dependent = getCellValue(row.getCell(6));

                    if (dependent != null && !dependent.isBlank()) {

                        family.setDependent(Boolean.parseBoolean(dependent));
                    }

                    String nominee = getCellValue(row.getCell(7));

                    if (nominee != null && !nominee.isBlank()) {

                        family.setNominee(Boolean.parseBoolean(nominee));
                    }

                    employeeFamilyMemberRepository.save(family);
                }
            }
//             * Languages
            Sheet languageSheet = workbook.getSheet("Languages");

            if (languageSheet != null) {

                for (int i = 1; i <= languageSheet.getLastRowNum(); i++) {

                    Row row = languageSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeLanguage language = new EmployeeLanguage();

                    language.setEmployee(employee);

                    language.setLanguageName(getCellValue(row.getCell(1)));

                    language.setReadLevel(getCellValue(row.getCell(2)));

                    language.setWriteLevel(getCellValue(row.getCell(3)));

                    language.setSpeakLevel(getCellValue(row.getCell(4)));

                    String nativeLanguage = getCellValue(row.getCell(5));

                    if (nativeLanguage != null && !nativeLanguage.isBlank()) {

                        language.setNativeLanguage(Boolean.parseBoolean(nativeLanguage));
                    }

                    employeeLanguageRepository.save(language);
                }
            }
//             * Skills
            Sheet skillSheet = workbook.getSheet("Skills");

            if (skillSheet != null) {

                for (int i = 1; i <= skillSheet.getLastRowNum(); i++) {

                    Row row = skillSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeSkill skill = new EmployeeSkill();

                    skill.setEmployee(employee);

                    skill.setSkillName(getCellValue(row.getCell(1)));

                    skill.setSkillCategory(getCellValue(row.getCell(2)));

                    skill.setProficiencyLevel(getCellValue(row.getCell(3)));

                    String experienceYears = getCellValue(row.getCell(4));

                    if (experienceYears != null && !experienceYears.isBlank()) {

                        skill.setExperienceYears(new java.math.BigDecimal(experienceYears));
                    }

                    skill.setLastUsed(getLocalDate(row.getCell(5)));

                    String certificationAvailable = getCellValue(row.getCell(6));

                    if (certificationAvailable != null && !certificationAvailable.isBlank()) {

                        skill.setCertificationAvailable(Boolean.parseBoolean(certificationAvailable));
                    }

                    skill.setRemarks(getCellValue(row.getCell(7)));

                    employeeSkillRepository.save(skill);
                }
            }
//             * Designations
            Sheet designationSheet = workbook.getSheet("Designations");

            if (designationSheet != null) {

                for (int i = 1; i <= designationSheet.getLastRowNum(); i++) {

                    Row row = designationSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    String designationName = getCellValue(row.getCell(1));

                    Designation designation = designationRepository.findByDesignationName(designationName).orElseThrow(() -> new RuntimeException("Designation not found: " + designationName));

                    EmployeeDesignation employeeDesignation = new EmployeeDesignation();

                    employeeDesignation.setEmployee(employee);

                    employeeDesignation.setDesignation(designation);

                    employeeDesignation.setPromotedDate(getLocalDate(row.getCell(2)));

                    String previousDesignationName = getCellValue(row.getCell(3));

                    if (previousDesignationName != null && !previousDesignationName.isBlank()) {

                        Designation previousDesignation = designationRepository.findByDesignationName(previousDesignationName).orElseThrow(() -> new RuntimeException("Previous designation not found: " + previousDesignationName));

                        employeeDesignation.setPreviousDesignation(previousDesignation);
                    }

                    employeeDesignation.setSalaryGrade(getCellValue(row.getCell(4)));

                    employeeDesignationRepository.save(employeeDesignation);
                }
            }
//             * Promotions
            Sheet promotionSheet = workbook.getSheet("Promotions");

            if (promotionSheet != null) {

                for (int i = 1; i <= promotionSheet.getLastRowNum(); i++) {

                    Row row = promotionSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeePromotion promotion = new EmployeePromotion();

                    promotion.setEmployee(employee);

                    String previousSalary = getCellValue(row.getCell(1));

                    if (previousSalary != null && !previousSalary.isBlank()) {

                        promotion.setPreviousSalary(new java.math.BigDecimal(previousSalary));
                    }

                    String newSalary = getCellValue(row.getCell(2));

                    if (newSalary != null && !newSalary.isBlank()) {

                        promotion.setNewSalary(new java.math.BigDecimal(newSalary));
                    }

                    promotion.setPromotionDate(getLocalDate(row.getCell(3)));

                    promotion.setReason(getCellValue(row.getCell(4)));

                    employeePromotionRepository.save(promotion);
                }
            }
//             * Transfers
            Sheet transferSheet = workbook.getSheet("Transfers");

            if (transferSheet != null) {

                for (int i = 1; i <= transferSheet.getLastRowNum(); i++) {

                    Row row = transferSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeTransfer transfer = new EmployeeTransfer();

                    transfer.setEmployee(employee);

                    String fromDepartment = getCellValue(row.getCell(1));

                    String toDepartment = getCellValue(row.getCell(2));

                    String fromBranch = getCellValue(row.getCell(3));

                    String toBranch = getCellValue(row.getCell(4));

                    String fromTeam = getCellValue(row.getCell(5));

                    String toTeam = getCellValue(row.getCell(6));

                    /*
                     * Resolve Department / Branch / Team
                     * using your repositories.
                     */

                    if (fromDepartment != null && !fromDepartment.isBlank()) {

                        transfer.setFromDepartment(departmentRepository.findByDepartmentName(fromDepartment).orElseThrow(() -> new RuntimeException("From department not found: " + fromDepartment)));
                    }

                    if (toDepartment != null && !toDepartment.isBlank()) {

                        transfer.setToDepartment(departmentRepository.findByDepartmentName(toDepartment).orElseThrow(() -> new RuntimeException("To department not found: " + toDepartment)));
                    }

                    if (fromBranch != null && !fromBranch.isBlank()) {

                        transfer.setFromBranch(branchRepository.findByBranchName(fromBranch).orElseThrow(() -> new RuntimeException("From branch not found: " + fromBranch)));
                    }

                    if (toBranch != null && !toBranch.isBlank()) {

                        transfer.setToBranch(branchRepository.findByBranchName(toBranch).orElseThrow(() -> new RuntimeException("To branch not found: " + toBranch)));
                    }

                    if (fromTeam != null && !fromTeam.isBlank()) {

                        transfer.setFromTeam(teamRepository.findByTeamName(fromTeam).orElseThrow(() -> new RuntimeException("From team not found: " + fromTeam)));
                    }

                    if (toTeam != null && !toTeam.isBlank()) {

                        transfer.setToTeam(teamRepository.findByTeamName(toTeam).orElseThrow(() -> new RuntimeException("To team not found: " + toTeam)));
                    }

                    transfer.setTransferDate(getLocalDate(row.getCell(7)));

                    transfer.setReason(getCellValue(row.getCell(8)));

                    employeeTransferRepository.save(transfer);
                }
            }
//             * ExitManagement
            Sheet exitSheet = workbook.getSheet("ExitManagement");

            if (exitSheet != null) {

                for (int i = 1; i <= exitSheet.getLastRowNum(); i++) {

                    Row row = exitSheet.getRow(i);

                    if (row == null) continue;

                    String employeeId = getCellValue(row.getCell(0));

                    Employee employee = getEmployee(employeeMap, employeeId);

                    EmployeeExitManagement exit = new EmployeeExitManagement();

                    exit.setEmployee(employee);

                    exit.setResignationDate(getLocalDate(row.getCell(1)));

                    exit.setLastWorkingDay(getLocalDate(row.getCell(2)));

                    exit.setReason(getCellValue(row.getCell(3)));

                    exit.setExitStatus(getCellValue(row.getCell(4)));

                    exit.setRelievingLetter(getCellValue(row.getCell(5)));

                    exit.setRemarks(getCellValue(row.getCell(6)));

                    String approvedByEmployeeId = getCellValue(row.getCell(7));

                    if (approvedByEmployeeId != null && !approvedByEmployeeId.isBlank()) {

                        Employee approvedBy = employeeRepository.findByEmployeeId(approvedByEmployeeId).orElseThrow(() -> new RuntimeException("Approving employee not found: " + approvedByEmployeeId));

                        exit.setExitApprovedBy(approvedBy);
                    }

                    employeeExitRepository.save(exit);
                }
            }

            Map<String, Object> response = new LinkedHashMap<>();

            response.put("message", "Employee Excel uploaded successfully");

            response.put("employeesCreated", employeeCount);

            response.put("addressesCreated", addressCount);

            response.put("bankDetailsCreated", bankCount);

            response.put("documentsCreated", documentCount);

            response.put("status", "SUCCESS");

            return response;

        } catch (Exception e) {

            throw new RuntimeException("Excel upload failed: " + e.getMessage(), e);
        }
    }

    private Employee getEmployee(Map<String, Employee> employeeMap, String employeeId) {

        Employee employee = employeeMap.get(employeeId);

        if (employee == null) {
            throw new RuntimeException("Employee ID not found in Employees sheet: " + employeeId);
        }

        return employee;
    }

    private String getCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell).trim();
    }

    private java.time.LocalDate getLocalDate(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {

            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String value = getCellValue(cell);

        if (value == null || value.isBlank()) {
            return null;
        }

        return java.time.LocalDate.parse(value);
    }

    // =====================================================
    // CREATE APPROVAL
    // =====================================================

    @Transactional
    public EmployeeApproval createApproval(String employeeId) {

        Employee employee = getEmployeeByEmployeeId(employeeId);

        if (employeeApprovalRepository.existsByEmployee_Id(employee.getId())) {
            throw new RuntimeException("Approval already exists for employee: " + employeeId);
        }

        EmployeeApproval approval = new EmployeeApproval();

        approval.setEmployee(employee);

        approval.setHrStatus(ApprovalStatus.PENDING);
        approval.setAdminStatus(ApprovalStatus.PENDING);
        approval.setDepartmentHeadStatus(ApprovalStatus.PENDING);
        approval.setFinalStatus(ApprovalStatus.PENDING);

        approval.setAccountCreated(false);
        approval.setWelcomeMailSent(false);

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // GET ALL APPROVALS
    // =====================================================

    public List<EmployeeApproval> getAllApprovals() {

        return employeeApprovalRepository.findAll();
    }


    // =====================================================
    // GET APPROVAL BY EMPLOYEE ID
    // =====================================================

    public EmployeeApproval getApproval(String employeeId) {

        Employee employee = getEmployeeByEmployeeId(employeeId);

        return employeeApprovalRepository.findByEmployee_Id(employee.getId()).orElseThrow(() -> new RuntimeException("Approval record not found for employee: " + employeeId));
    }


    // =====================================================
    // HR APPROVAL
    // =====================================================

    @Transactional
    public EmployeeApproval hrApproval(String employeeId, ApprovalRequestdto request, Long approverId) {

        EmployeeApproval approval = getApproval(employeeId);

        Employee approver = getEmployeeById(approverId);

        approval.setHrStatus(request.getStatus());
        approval.setHrApprovedBy(approver);
        approval.setHrApprovedAt(LocalDateTime.now());
        approval.setHrRemarks(request.getRemarks());

        checkFinalApproval(approval);

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // ADMIN APPROVAL
    // =====================================================

    @Transactional
    public EmployeeApproval adminApproval(String employeeId, ApprovalRequestdto request, Long approverId) {

        EmployeeApproval approval = getApproval(employeeId);

        Employee approver = getEmployeeById(approverId);

        approval.setAdminStatus(request.getStatus());
        approval.setAdminApprovedBy(approver);
        approval.setAdminApprovedAt(LocalDateTime.now());
        approval.setAdminRemarks(request.getRemarks());

        checkFinalApproval(approval);

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // DEPARTMENT HEAD APPROVAL
    // =====================================================

    @Transactional
    public EmployeeApproval departmentHeadApproval(String employeeId, ApprovalRequestdto request, Long approverId) {

        EmployeeApproval approval = getApproval(employeeId);

        Employee approver = getEmployeeById(approverId);

        approval.setDepartmentHeadStatus(request.getStatus());
        approval.setDepartmentHeadApprovedBy(approver);
        approval.setDepartmentHeadApprovedAt(LocalDateTime.now());
        approval.setDepartmentHeadRemarks(request.getRemarks());

        checkFinalApproval(approval);

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // CHECK FINAL APPROVAL
    // =====================================================

    private void checkFinalApproval(EmployeeApproval approval) {

        // If anyone rejects
        if (approval.getHrStatus() == ApprovalStatus.REJECTED || approval.getAdminStatus() == ApprovalStatus.REJECTED || approval.getDepartmentHeadStatus() == ApprovalStatus.REJECTED) {

            approval.setFinalStatus(ApprovalStatus.REJECTED);

            approval.setFinalApprovedAt(null);

            return;
        }


        // All three approved
        if (approval.getHrStatus() == ApprovalStatus.APPROVED && approval.getAdminStatus() == ApprovalStatus.APPROVED && approval.getDepartmentHeadStatus() == ApprovalStatus.APPROVED) {

            approval.setFinalStatus(ApprovalStatus.APPROVED);

            approval.setFinalApprovedAt(LocalDateTime.now());

            return;
        }


        // Otherwise pending
        approval.setFinalStatus(ApprovalStatus.PENDING);

        approval.setFinalApprovedAt(null);
    }


    // =====================================================
    // PENDING HR
    // =====================================================

    public List<EmployeeApproval> getPendingHR() {

        return employeeApprovalRepository.findByHrStatus(ApprovalStatus.PENDING);
    }


    // =====================================================
    // PENDING ADMIN
    // =====================================================

    public List<EmployeeApproval> getPendingAdmin() {

        return employeeApprovalRepository.findByAdminStatus(ApprovalStatus.PENDING);
    }


    // =====================================================
    // PENDING DEPARTMENT HEAD
    // =====================================================

    public List<EmployeeApproval> getPendingDepartmentHead() {

        return employeeApprovalRepository.findByDepartmentHeadStatus(ApprovalStatus.PENDING);
    }


    // =====================================================
    // FINAL APPROVED
    // =====================================================

    public List<EmployeeApproval> getFinalApprovedEmployees() {

        return employeeApprovalRepository.findByFinalStatus(ApprovalStatus.APPROVED);
    }


    // =====================================================
    // FINAL REJECTED
    // =====================================================

    public List<EmployeeApproval> getRejectedEmployees() {

        return employeeApprovalRepository.findByFinalStatus(ApprovalStatus.REJECTED);
    }


    // =====================================================
    // MARK ACCOUNT CREATED
    // =====================================================

    @Transactional
    public EmployeeApproval markAccountCreated(String employeeId) {

        EmployeeApproval approval = getApproval(employeeId);

        if (approval.getFinalStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("Employee is not finally approved");
        }

        approval.setAccountCreated(true);

        approval.setAccountCreatedAt(LocalDateTime.now());

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // MARK WELCOME MAIL SENT
    // =====================================================

    @Transactional
    public EmployeeApproval markWelcomeMailSent(String employeeId) {

        EmployeeApproval approval = getApproval(employeeId);

        if (!Boolean.TRUE.equals(approval.getAccountCreated())) {

            throw new RuntimeException("Employee account has not been created");
        }

        approval.setWelcomeMailSent(true);

        approval.setWelcomeMailSentAt(LocalDateTime.now());

        return employeeApprovalRepository.save(approval);
    }


    // =====================================================
    // GET EMPLOYEE BY BUSINESS EMPLOYEE ID
    // =====================================================

    private Employee getEmployeeByEmployeeId(String employeeId) {

        return employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));
    }


    // =====================================================
    // GET EMPLOYEE BY DATABASE ID
    // =====================================================

    private Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    @Transactional
    public String hrApproval(Long employeeId, EmployeeApprovalRequest request) {

        EmployeeApproval approval = employeeApprovalRepository.findByEmployeeId(employeeId);

        if (approval.getHrStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("HR approval has already been processed");
        }

        Employee approver = employeeRepository.findByEmployeeId(getLoggedInEmployeeId()).orElseThrow(() -> new RuntimeException("Approving employee not found"));

        if (request.getHrStatus() == ApprovalStatus.APPROVED) {

            approval.setHrStatus(ApprovalStatus.APPROVED);

            approval.setHrApprovedBy(approver);

            approval.setHrApprovedAt(LocalDateTime.now());

            approval.setHrRemarks(request.getHrRemarks());

            employeeApprovalRepository.save(approval);

            return "Employee approved by HR";

        } else if (request.getHrStatus() == ApprovalStatus.REJECTED) {

            approval.setHrStatus(ApprovalStatus.REJECTED);

            approval.setHrApprovedBy(approver);

            approval.setHrApprovedAt(LocalDateTime.now());

            approval.setHrRemarks(request.getHrRemarks());

            approval.setFinalStatus(ApprovalStatus.REJECTED);

            employeeApprovalRepository.save(approval);

            return "Employee rejected by HR";
        }

        throw new RuntimeException("Invalid HR approval status");
    }

    @Transactional
    public String adminApproval(Long employeeId, EmployeeApprovalRequest request) {

        EmployeeApproval approval = employeeApprovalRepository.findByEmployeeId(employeeId);

        if (approval.getHrStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("HR approval is required before Admin approval");
        }

        if (approval.getAdminStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Admin approval has already been processed");
        }

        Employee approver = employeeRepository.findByEmployeeId(getLoggedInEmployeeId()).orElseThrow(() -> new RuntimeException("Approving employee not found"));

        if (request.getAdminStatus() == ApprovalStatus.APPROVED) {

            approval.setAdminStatus(ApprovalStatus.APPROVED);

            approval.setAdminApprovedBy(approver);

            approval.setAdminApprovedAt(LocalDateTime.now());

            approval.setAdminRemarks(request.getAdminRemarks());

            employeeApprovalRepository.save(approval);

            return "Employee approved by Admin";

        } else if (request.getAdminStatus() == ApprovalStatus.REJECTED) {

            approval.setAdminStatus(ApprovalStatus.REJECTED);

            approval.setAdminApprovedBy(approver);

            approval.setAdminApprovedAt(LocalDateTime.now());

            approval.setAdminRemarks(request.getAdminRemarks());

            approval.setFinalStatus(ApprovalStatus.REJECTED);

            employeeApprovalRepository.save(approval);

            return "Employee rejected by Admin";
        }

        throw new RuntimeException("Invalid Admin approval status");
    }

    @Transactional
    public String departmentHeadApproval(Long employeeId, EmployeeApprovalRequest request) {

        EmployeeApproval approval = employeeApprovalRepository.findByEmployeeId(employeeId);

        if (approval.getHrStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("HR approval is required");
        }

        if (approval.getAdminStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("Admin approval is required");
        }

        if (approval.getDepartmentHeadStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Department Head approval has already been processed");
        }

        Employee approver = employeeRepository.findByEmployeeId(getLoggedInEmployeeId()).orElseThrow(() -> new RuntimeException("Approving employee not found"));

        if (request.getDepartmentHeadStatus() == ApprovalStatus.APPROVED) {

            approval.setDepartmentHeadStatus(ApprovalStatus.APPROVED);

            approval.setDepartmentHeadApprovedBy(approver);

            approval.setDepartmentHeadApprovedAt(LocalDateTime.now());

            approval.setDepartmentHeadRemarks(request.getDepartmentHeadRemarks());

            employeeApprovalRepository.save(approval);

            return "Employee approved by Department Head";

        } else if (request.getDepartmentHeadStatus() == ApprovalStatus.REJECTED) {

            approval.setDepartmentHeadStatus(ApprovalStatus.REJECTED);

            approval.setDepartmentHeadApprovedBy(approver);

            approval.setDepartmentHeadApprovedAt(LocalDateTime.now());

            approval.setDepartmentHeadRemarks(request.getDepartmentHeadRemarks());

            approval.setFinalStatus(ApprovalStatus.REJECTED);

            employeeApprovalRepository.save(approval);

            return "Employee rejected by Department Head";
        }

        throw new RuntimeException("Invalid Department Head approval status");
    }

    @Transactional
    public String finalApproval(Long employeeId) {

        EmployeeApproval approval = employeeApprovalRepository.findByEmployeeId(employeeId);

        if (approval.getHrStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("HR approval is pending");
        }

        if (approval.getAdminStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("Admin approval is pending");
        }

        if (approval.getDepartmentHeadStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("Department Head approval is pending");
        }

        if (approval.getFinalStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Final approval has already been processed");
        }

        approval.setFinalStatus(ApprovalStatus.APPROVED);

        approval.setFinalApprovedAt(LocalDateTime.now());

        employeeApprovalRepository.save(approval);

        return "Employee finally approved";
    }

    @Transactional
    public String sendWelcomeMail(Long employeeId) {

        // =====================================================
        // 1. FIND APPROVAL
        // =====================================================

        EmployeeApproval approval = employeeApprovalRepository.findByEmployeeId(employeeId);


        // =====================================================
        // 2. CHECK FINAL APPROVAL
        // =====================================================

        if (approval.getFinalStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException("Employee is not finally approved");
        }


        // =====================================================
        // 3. CHECK ACCOUNT CREATED
        // =====================================================

        if (!Boolean.TRUE.equals(approval.getAccountCreated())) {

            throw new RuntimeException("Employee account has not been created");
        }


        // =====================================================
        // 4. CHECK WELCOME MAIL
        // =====================================================

        if (Boolean.TRUE.equals(approval.getWelcomeMailSent())) {

            throw new RuntimeException("Welcome mail already sent");
        }


        // =====================================================
        // 5. GET EMPLOYEE
        // =====================================================

        Employee employee = approval.getEmployee();

        if (employee == null) {

            throw new RuntimeException("Employee not found");
        }

        if (employee.getEmail() == null || employee.getEmail().isBlank()) {

            throw new RuntimeException("Employee email address not available");
        }


        // =====================================================
        // 6. SEND WELCOME MAIL
        // =====================================================

        emailService.sendWelcomeMail(employee.getEmail(), employee.getFirstName());


        // =====================================================
        // 7. UPDATE MAIL STATUS
        // =====================================================

        approval.setWelcomeMailSent(true);

        approval.setWelcomeMailSentAt(LocalDateTime.now());

        employeeApprovalRepository.save(approval);


        // =====================================================
        // 8. RESPONSE
        // =====================================================

        return "Welcome mail sent successfully";
    }

    @Transactional
    public List<Employee> getAllEmployeeDetails() {

        List<Employee> employees = employeeRepository.findAll();

        employees.forEach(employee -> {

            // Basic relationships
            Hibernate.initialize(employee.getCompany());
            Hibernate.initialize(employee.getBranch());
            Hibernate.initialize(employee.getDepartment());
            Hibernate.initialize(employee.getRole());

            // One-to-one relationships
            Hibernate.initialize(employee.getProfile());
            Hibernate.initialize(employee.getDocuments());
            Hibernate.initialize(employee.getBankDetails());
            Hibernate.initialize(employee.getExitManagement());
            Hibernate.initialize(employee.getApproval());

            // One-to-many relationships
            Hibernate.initialize(employee.getContacts());
            Hibernate.initialize(employee.getDesignations());
            Hibernate.initialize(employee.getAddresses());
            Hibernate.initialize(employee.getCertifications());
            Hibernate.initialize(employee.getExperiences());
            Hibernate.initialize(employee.getFamilyMembers());
            Hibernate.initialize(employee.getLanguages());
            Hibernate.initialize(employee.getPromotions());
            Hibernate.initialize(employee.getSkills());
            Hibernate.initialize(employee.getTransfers());
        });

        return employees;
    }

    @Transactional
    public Employee getEmployeeByEmployeeIdDetails(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with employeeId: " + employeeId));

        Hibernate.initialize(employee.getCompany());
        Hibernate.initialize(employee.getBranch());
        Hibernate.initialize(employee.getDepartment());
        Hibernate.initialize(employee.getRole());

        Hibernate.initialize(employee.getProfile());
        Hibernate.initialize(employee.getDocuments());
        Hibernate.initialize(employee.getBankDetails());
        Hibernate.initialize(employee.getExitManagement());
        Hibernate.initialize(employee.getApproval());

        Hibernate.initialize(employee.getContacts());
        Hibernate.initialize(employee.getDesignations());
        Hibernate.initialize(employee.getAddresses());
        Hibernate.initialize(employee.getCertifications());
        Hibernate.initialize(employee.getExperiences());
        Hibernate.initialize(employee.getFamilyMembers());
        Hibernate.initialize(employee.getLanguages());
        Hibernate.initialize(employee.getPromotions());
        Hibernate.initialize(employee.getSkills());
        Hibernate.initialize(employee.getTransfers());

        return employee;
    }

    @Transactional
    public Employee updateEmployee(String employeeId, EmployeeUpdateRequest request) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with employeeId: " + employeeId));

        // =========================
        // EMPLOYEE
        // =========================

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setMobileNumber(request.getMobileNumber());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        // =========================
        // PROFILE
        // =========================

        if (request.getProfile() != null) {

            EmployeeProfile profile = employee.getProfile();

            if (profile == null) {
                profile = new EmployeeProfile();
                profile.setEmployee(employee);
                employee.setProfile(profile);
            }

            EmployeeProfile requestProfile = request.getProfile();

            profile.setProfileImage(requestProfile.getProfileImage());
            profile.setFatherName(requestProfile.getFatherName());
            profile.setMotherName(requestProfile.getMotherName());
            profile.setMaritalStatus(requestProfile.getMaritalStatus());
            profile.setBloodGroup(requestProfile.getBloodGroup());
            profile.setNationality(requestProfile.getNationality());
            profile.setEmergencyContact(requestProfile.getEmergencyContact());
            profile.setAlternateMobile(requestProfile.getAlternateMobile());
            profile.setAddress(requestProfile.getAddress());
            profile.setCity(requestProfile.getCity());
            profile.setState(requestProfile.getState());
            profile.setPincode(requestProfile.getPincode());
            profile.setCountry(requestProfile.getCountry());
            profile.setProfileCompletion(requestProfile.getProfileCompletion());
            profile.setProfileStatus(requestProfile.getProfileStatus());
        }

        // =========================
        // BANK DETAILS
        // =========================

        if (request.getBankDetails() != null) {

            EmployeeBankDetails bank = employee.getBankDetails();

            if (bank == null) {
                bank = new EmployeeBankDetails();
                bank.setEmployee(employee);
                employee.setBankDetails(bank);
            }

            EmployeeBankDetails requestBank = request.getBankDetails();

            bank.setAccountHolderName(requestBank.getAccountHolderName());
            bank.setBankName(requestBank.getBankName());
            bank.setAccountNumber(requestBank.getAccountNumber());
            bank.setIfscCode(requestBank.getIfscCode());
            bank.setBranchName(requestBank.getBranchName());
            bank.setUpiId(requestBank.getUpiId());
            bank.setAccountStatus(requestBank.getAccountStatus());
        }

        // =========================
        // DOCUMENTS
        // =========================

        if (request.getDocuments() != null) {

            EmployeeDocuments documents = employee.getDocuments();

            if (documents == null) {
                documents = new EmployeeDocuments();
                documents.setEmployee(employee);
                employee.setDocuments(documents);
            }

            EmployeeDocuments requestDocuments = request.getDocuments();

            documents.setAadhaarNumber(requestDocuments.getAadhaarNumber());
            documents.setPanNumber(requestDocuments.getPanNumber());
            documents.setPassportNumber(requestDocuments.getPassportNumber());
            documents.setResumeUrl(requestDocuments.getResumeUrl());
            documents.setAadhaarDocument(requestDocuments.getAadhaarDocument());
            documents.setPanDocument(requestDocuments.getPanDocument());
            documents.setDegreeCertificate(requestDocuments.getDegreeCertificate());
            documents.setPgCertificate(requestDocuments.getPgCertificate());
            documents.setOfferLetter(requestDocuments.getOfferLetter());
            documents.setJoiningLetter(requestDocuments.getJoiningLetter());
            documents.setSalarySlips(requestDocuments.getSalarySlips());
            documents.setExperienceLetter(requestDocuments.getExperienceLetter());
            documents.setStatus(requestDocuments.getStatus());
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with employeeId: " + employeeId));

        // Delete child records first

        employeeAddressRepository.deleteByEmployee(employee);

        employeeCertificationRepository.deleteByEmployee(employee);

        employeeExperienceRepository.deleteByEmployee(employee);

        employeeFamilyMemberRepository.deleteByEmployee(employee);

        employeeLanguageRepository.deleteByEmployee(employee);

        employeePromotionRepository.deleteByEmployee(employee);

        employeeSkillRepository.deleteByEmployee(employee);

        employeeTransferRepository.deleteByEmployee(employee);

        employeeContactRepository.deleteByEmployee(employee);

        employeeDesignationRepository.deleteByEmployee(employee);

        // One-to-one records
        if (employee.getProfile() != null) {
            employeeProfileRepository.delete(employee.getProfile());
        }

        if (employee.getDocuments() != null) {
            employeeDocumentRepository.delete(employee.getDocuments());
        }

        if (employee.getBankDetails() != null) {
            employeeBankRepository.delete(employee.getBankDetails());
        }

        if (employee.getApproval() != null) {
            employeeApprovalRepository.delete(employee.getApproval());
        }

        // Exit management requires special care because
        // it contains exitApprovedBy -> Employee
        if (employee.getExitManagement() != null) {
            employeeExitRepository.delete(employee.getExitManagement());
        }

        // Finally delete employee
        employeeRepository.delete(employee);
    }
//    public Object getAttendance(Long id){
//
//        return null;
//    }
//
//
//
//    public Object getLeaveBalance(Long id){
//
//        return null;
//    }
//
//
//
//    public Object getSalary(Long id){
//
//        return null;
//    }
//
//
//
//    public Object getTasks(Long id){
//
//        return null;
//    }
//
//
//
//    public Object getPerformance(Long id){
//
//        return null;
//    }
//
//
//
//    public String addExpense(Expense expense){
//
//        return "Expense Added";
//    }
//
//
//
//    public String activate(Long id){
//
//        return "Activated";
//    }
//
//
//
//    public String deactivate(Long id){
//
//        return "Deactivated";
//    }
//
//
//
//    public String block(Long id){
//
//        return "Blocked";
//    }
//
//
//
//    public String unblock(Long id){
//
//        return "Unblocked";
//    }
//
//
//
//    public String terminate(Long id){
//
//        return "Terminated";
//    }
//
//
//
//    public String resigned(Long id){
//
//        return "Resigned";
//    }
//
//
//
//    public Object dashboard(Long id){
//
//        return null;
//    }


}