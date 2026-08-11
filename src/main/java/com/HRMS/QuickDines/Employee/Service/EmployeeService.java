package com.HRMS.QuickDines.Employee.Service;

import com.HRMS.QuickDines.AdvanceServices.CloudinaryService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.*;
import com.HRMS.QuickDines.Employee.repo.*;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.HRMS.QuickDines.Organization.repo.DesignationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;

import java.time.LocalDate;
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
    private final DesignationRepository  designationRepository;

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

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


    // =========================================================
    // LOGGED-IN EMPLOYEE
    // =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


    // =========================================================
    // CLIENT INFORMATION
    // =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }



    public String createEmployee(Employee employee) {

        // Fetch Department
        Department department = departmentRepository
                .findById(Long.valueOf(employee.getDepartmentId()))
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        // Department code (example: HR, DEV, SALES)
        String departmentCode = department.getDepartmentCode().toUpperCase();

        // Generate employee sequence number
        Long count = employeeRepository.count() + 1;

        // Generate Employee Code
        String employeeCode = "QD-"
                + departmentCode + "-"
                + LocalDate.now().getYear()
                + "-"
                + String.format("%03d", count);

        employee.setEmployeeId(employeeCode);
        employee.setStatus("ACTIVE");

        // Save Employee
        Employee savedEmployee = employeeRepository.save(employee);


        // Create Employee Profile
        EmployeeProfile profile = new EmployeeProfile();

        profile.setEmployee(savedEmployee);

        // You can set default values if required
        profile.setProfileCompletion(0);
        profile.setProfileStatus("INCOMPLETE");

        // Save Employee Profile
        employeeProfileRepository.save(profile);

        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate(
                "EMPLOYEE",
                savedEmployee.getEmployeeId(),
                performedBy,
                savedEmployee.getEmployeeId(),
                "Employee created successfully"
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                savedEmployee.getEmployeeId(),
                "CREATE_EMPLOYEE",
                "EMPLOYEE",
                "New employee created: "
                        + savedEmployee.getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "EMPLOYEE",
                "EmployeeService",
                "Employee created successfully: "
                        + savedEmployee.getEmployeeId()
        );

        return "Employee Created Successfully";

    }



    public List<Employee> getAllEmployees(){

        return employeeRepository.findAll();
    }



    public Employee getEmployee(Long id){

        return employeeRepository.findById(id).get();
    }




    public String updateEmployee(Long id, Employee employee) {

        // =========================================================
        // FETCH EXISTING EMPLOYEE
        // =========================================================

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));


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
        existingEmployee.setDepartmentId(employee.getDepartmentId());


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

        auditLogsService.logUpdate(
                "EMPLOYEE",
                existingEmployee.getEmployeeId(),
                performedBy,
                existingEmployee.getEmployeeId(),
                "Employee information updated",
                oldValue,
                newValue
        );


        // =========================================================
        // ACTIVITY LOG
        // =========================================================

        auditLogsService.logActivity(
                existingEmployee.getEmployeeId(),
                "UPDATE_EMPLOYEE",
                "EMPLOYEE",
                "Employee information updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );


        // =========================================================
        // SYSTEM LOG
        // =========================================================

        auditLogsService.logInfo(
                "EMPLOYEE",
                "EmployeeService",
                "Employee updated: "
                        + existingEmployee.getEmployeeId()
        );


        return "Employee Updated Successfully";
    }


// =========================================================
// CONVERT EMPLOYEE VALUES TO JSON
// =========================================================

    private String convertEmployeeToJson(Employee employee) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> employeeData =
                    new LinkedHashMap<>();

            employeeData.put(
                    "employeeId",
                    employee.getEmployeeId()
            );

            employeeData.put(
                    "firstName",
                    employee.getFirstName()
            );

            employeeData.put(
                    "lastName",
                    employee.getLastName()
            );

            employeeData.put(
                    "email",
                    employee.getEmail()
            );

            employeeData.put(
                    "mobileNumber",
                    employee.getMobileNumber()
            );

            employeeData.put(
                    "status",
                    employee.getStatus()
            );

            employeeData.put(
                    "departmentId",
                    employee.getDepartmentId()
            );

            return objectMapper.writeValueAsString(employeeData);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert employee data to JSON",
                    e
            );
        }
    }






    public String deleteEmployee(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        String employeeCode =
                employee.getEmployeeId();


        employeeRepository.delete(employee);

        String performedBy = getLoggedInEmployeeId();
        // AUDIT
        auditLogsService.logDelete(
                "EMPLOYEE",
                employeeCode,
                performedBy,
                employeeCode,
                "Employee deleted"
        );


        // ACTIVITY
        auditLogsService.logActivity(
                employeeCode,
                "DELETE_EMPLOYEE",
                "EMPLOYEE",
                "Employee deleted: " + employeeCode,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );


        // SYSTEM
        auditLogsService.logInfo(
                "EMPLOYEE",
                "EmployeeService",
                "Employee deleted: " + employeeCode
        );


        return "Employee Deleted";
    }



    public Employee getEmployeeDetails(Long id){

        return employeeProfileRepository.findById(id).orElseThrow().getEmployee();
    }



    public String uploadDocument(Long employeeId, MultipartFile file, String documentType) {

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


        auditLogsService.logCreate(
                "EMPLOYEE_DOCUMENT",
                employee.getEmployeeId(),
                performedBy,
                employee.getEmployeeId(),
                documentType + " document uploaded"
        );

        auditLogsService.logActivity(
                employee.getEmployeeId(),
                "UPLOAD_DOCUMENT",
                "EMPLOYEE_DOCUMENT",
                documentType + " document uploaded",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_DOCUMENT",
                "EmployeeService",
                documentType + " document uploaded for "
                        + employee.getEmployeeId()
        );
        return documentType + " Uploaded Successfully";

    }


    // --------------------------------------------

    public EmployeeDocuments getDocuments(Long employeeId) {

        return employeeDocumentRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Documents Not Found"));
    }


    // --------------------------------------------

    public String deleteDocument(Long id) {

        EmployeeDocuments document = employeeDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document Not Found"));
        employeeDocumentRepository.delete(document);
        // AUDIT
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "DOCUMENT",
                document.getEmployee().getEmployeeId(),
                performedBy,
                document.getEmployee().getEmployeeId(),
                "Employee documents deleted"
        );


        // ACTIVITY
        auditLogsService.logActivity(
                document.getEmployee().getEmployeeId(),
                "DELETE_DOCUMENTS",
                "DOCUMENT",
                "Employee documents deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );


        // SYSTEM
        auditLogsService.logInfo(
                "DOCUMENT",
                "DocumentService",
                "Documents deleted for employee "
                        +  document.getEmployee().getEmployeeId()
        );
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

        auditLogsService.logCreate(
                "EMPLOYEE_PROFILE",
                employeeId,
                performedBy,
                employeeId,
                "Employee profile created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_PROFILE",
                "EMPLOYEE_PROFILE",
                "Employee profile created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_PROFILE",
                "EmployeeService",
                "Employee profile created for " + employeeId
        );
        return "Employee Profile Created Successfully";
    }

    public List<EmployeeProfile> getAllProfiles() {
        return employeeProfileRepository.findAll();
    }

    public EmployeeProfile getProfile(String employeeId) {
        return employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));
    }


    public String updateProfile(String  employeeId, EmployeeProfile profile) {

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

        auditLogsService.logUpdate(
                "DOCUMENT",
                existingProfile.getEmployee().getEmployeeId(),
                performedBy,
                existingProfile.getEmployee().getEmployeeId(),
                "Employee documents updated",
                "Previous documents",
                "Updated documents"
        );

        auditLogsService.logActivity(
                existingProfile.getEmployee().getEmployeeId(),
                "UPDATE_DOCUMENTS",
                "DOCUMENT",
                "Employee documents updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "DOCUMENT",
                "DocumentService",
                "Documents updated for employee "
                        + existingProfile.getEmployee().getEmployeeId()
        );

        return "Employee Profile Updated Successfully";
    }


    public String deleteProfile(String employeeId) {

        EmployeeProfile profile = employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));
        employeeProfileRepository.delete(profile);
        String oldValue = convertToJson(profile);

        employeeProfileRepository.delete(profile);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_PROFILE",
                employeeId,
                performedBy,
                employeeId,
                "Employee profile deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_PROFILE",
                "EMPLOYEE_PROFILE",
                "Employee profile deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_PROFILE",
                "EmployeeService",
                "Employee profile deleted for " + employeeId
        );
        return "Employee Profile Deleted Successfully";
    }


    //=========================================
// BANK DETAILS
//=========================================

    public String createBankDetails(String employeeId,
                                    EmployeeBankDetails bankDetails) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        bankDetails.setEmployee(employee);
        bankDetails.setAccountStatus("ACTIVE");

        employeeBankRepository.save(bankDetails);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_BANK",
                employeeId,
                performedBy,
                employeeId,
                "Bank details created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_BANK_DETAILS",
                "EMPLOYEE_BANK",
                "Bank details created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_BANK",
                "EmployeeService",
                "Bank details created for " + employeeId
        );
        return "Bank Details Added Successfully";
    }


    public EmployeeBankDetails getBankDetails(String employeeId) {

        return employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                        new RuntimeException("Bank Details Not Found"));
    }


    public String updateBankDetails(String employeeId, EmployeeBankDetails bankDetails) {

        EmployeeBankDetails existingDetails = employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                                new RuntimeException("Bank Details Not Found"));

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
        auditLogsService.logUpdate(
                "EMPLOYEE_BANK",
                employeeId,
                performedBy,
                employeeId,
                "Bank details updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_BANK_DETAILS",
                "EMPLOYEE_BANK",
                "Bank details updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_BANK",
                "EmployeeService",
                "Bank details updated for " + employeeId
        );

        return "Bank Details Updated Successfully";
    }


    public String deleteBankDetails(String employeeId) {

        EmployeeBankDetails bankDetails = employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                                new RuntimeException("Bank Details Not Found"));

        employeeBankRepository.delete(bankDetails);
        String oldValue = convertToJson(bankDetails);

        employeeBankRepository.delete(bankDetails);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_BANK",
                employeeId,
                performedBy,
                employeeId,
                "Bank details deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_BANK_DETAILS",
                "EMPLOYEE_BANK",
                "Bank details deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_BANK",
                "EmployeeService",
                "Bank details deleted for " + employeeId
        );
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

        auditLogsService.logCreate(
                "EMPLOYEE_CONTACT",
                employeeId,
                performedBy,
                employeeId,
                "Emergency contact created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_CONTACT",
                "EMPLOYEE_CONTACT",
                "Emergency contact created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_CONTACT",
                "EmployeeService",
                "Emergency contact created for " + employeeId
        );
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
        auditLogsService.logUpdate(
                "EMPLOYEE_CONTACT",
                id,
                performedBy,
                id,
                "Emergency contact updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                id,
                "UPDATE_CONTACT",
                "EMPLOYEE_CONTACT",
                "Emergency contact updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_CONTACT",
                "EmployeeService",
                "Emergency contact updated for " + id
        );

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

        auditLogsService.logCreate(
                "EMPLOYEE_DESIGNATION",
                employeeId,
                performedBy,
                employeeId,
                "Employee designation created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_DESIGNATION",
                "EMPLOYEE_DESIGNATION",
                "Employee designation created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_DESIGNATION",
                "EmployeeService",
                "Designation created for " + employeeId
        );
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
        auditLogsService.logUpdate(
                "EMPLOYEE_DESIGNATION",
                id,
                performedBy,
                id,
                "Employee designation updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                id,
                "UPDATE_DESIGNATION",
                "EMPLOYEE_DESIGNATION",
                "Employee designation updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_DESIGNATION",
                "EmployeeService",
                "Designation updated for " + id
        );
        return "Employee Designation Updated Successfully";
    }


    public String deleteDesignation(String id) {

        EmployeeDesignation designation = employeeDesignationRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Designation Not Found"));

        employeeDesignationRepository.delete(designation);

        String oldValue = convertToJson(designation);

        employeeDesignationRepository.delete(designation);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_DESIGNATION",
                String.valueOf(designation.getId()),
                performedBy,
                designation.getEmployee().getEmployeeId(),
                "Employee designation deleted"
        );

        auditLogsService.logActivity(
                designation.getEmployee().getEmployeeId(),
                "DELETE_DESIGNATION",
                "EMPLOYEE_DESIGNATION",
                "Employee designation deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_DESIGNATION",
                "EmployeeService",
                "Designation deleted"
        );

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

        auditLogsService.logCreate(
                "EMPLOYEE_EXIT",
                employeeId,
                performedBy,
                employeeId,
                "Employee exit management created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_EXIT",
                "EMPLOYEE_EXIT",
                "Employee exit management created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXIT",
                "EmployeeService",
                "Exit management created for " + employeeId
        );
        return "Employee Exit Management Created Successfully";
    }


    public EmployeeExitManagement getExitManagement(String employeeId) {

        return employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                        new RuntimeException("Exit Management Details Not Found"));
    }


    public String updateExitManagement(String employeeId, EmployeeExitManagement exitManagement) {

        EmployeeExitManagement existingExit = employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                                new RuntimeException("Exit Management Details Not Found"));

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
        auditLogsService.logUpdate(
                "EMPLOYEE_EXIT",
                employeeId,
                performedBy,
                employeeId,
                "Employee exit management updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_EXIT",
                "EMPLOYEE_EXIT",
                "Employee exit management updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXIT",
                "EmployeeService",
                "Exit management updated for " + employeeId
        );

        return "Employee Exit Management Updated Successfully";
    }


    public String deleteExitManagement(String employeeId) {

        EmployeeExitManagement exitManagement = employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Exit Management Details Not Found"));

        employeeExitRepository.delete(exitManagement);
        String oldValue = convertToJson(exitManagement);

        employeeExitRepository.delete(exitManagement);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_EXIT",
                employeeId,
                performedBy,
                employeeId,
                "Employee exit management deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_EXIT",
                "EMPLOYEE_EXIT",
                "Employee exit management deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXIT",
                "EmployeeService",
                "Exit management deleted for " + employeeId
        );

        return "Employee Exit Management Deleted Successfully";
    }
    //=================================
// EMPLOYEE SKILLS
//=================================

    public String createSkill(String employeeId, EmployeeSkill employeeSkill) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeSkill.setEmployee(employee);

        employeeSkillRepository.save(employeeSkill);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_SKILL",
                employeeId,
                performedBy,
                employeeId,
                "Employee skill created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_SKILL",
                "EMPLOYEE_SKILL",
                "Employee skill created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_SKILL",
                "EmployeeService",
                "Skill created for " + employeeId
        );

        return "Employee Skill Added Successfully";
    }

    public List<EmployeeSkill> getSkills() {

        return employeeSkillRepository.findAll();
    }

    public EmployeeSkill getSkill(Long id) {

        return employeeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));
    }

    public String updateSkill(Long id, EmployeeSkill employeeSkill) {

        EmployeeSkill existingSkill = employeeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));

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

        auditLogsService.logUpdate(
                "EMPLOYEE_SKILL",
                String.valueOf(id),
                performedBy,
                existingSkill.getEmployee().getEmployeeId(),
                "Employee skill updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                existingSkill.getEmployee().getEmployeeId(),
                "UPDATE_SKILL",
                "EMPLOYEE_SKILL",
                "Employee skill updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_SKILL",
                "EmployeeService",
                "Skill updated: " + id
        );

        return "Employee Skill Updated Successfully";
    }

    public String deleteSkill(Long id) {

        EmployeeSkill existingSkill = employeeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));

        employeeSkillRepository.delete(existingSkill);

        String employeeId = existingSkill.getEmployee().getEmployeeId();

        employeeSkillRepository.delete(existingSkill);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_SKILL",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee skill deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_SKILL",
                "EMPLOYEE_SKILL",
                "Employee skill deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_SKILL",
                "EmployeeService",
                "Skill deleted: " + id
        );

        return "Employee Skill Deleted Successfully";
    }

    //=================================
// EMPLOYEE CERTIFICATIONS
//=================================

    public String createCertification(String employeeId, EmployeeCertification employeeCertification) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeCertification.setEmployee(employee);

        employeeCertificationRepository.save(employeeCertification);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_CERTIFICATION",
                employeeId,
                performedBy,
                employeeId,
                "Employee certification created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_CERTIFICATION",
                "EMPLOYEE_CERTIFICATION",
                "Employee certification created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_CERTIFICATION",
                "EmployeeService",
                "Certification created for " + employeeId
        );

        return "Employee Certification Added Successfully";
    }

    public List<EmployeeCertification> getCertifications() {

        return employeeCertificationRepository.findAll();
    }

    public EmployeeCertification getCertification(Long id) {

        return employeeCertificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));
    }

    public String updateCertification(Long id,
                                      EmployeeCertification employeeCertification) {

        EmployeeCertification existingCertification =
                employeeCertificationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));

        existingCertification.setCertificationName(
                employeeCertification.getCertificationName());

        existingCertification.setIssuingOrganization(
                employeeCertification.getIssuingOrganization());

        existingCertification.setCertificateNumber(
                employeeCertification.getCertificateNumber());

        existingCertification.setIssueDate(
                employeeCertification.getIssueDate());

        existingCertification.setExpiryDate(
                employeeCertification.getExpiryDate());

        existingCertification.setCredentialUrl(
                employeeCertification.getCredentialUrl());

        existingCertification.setAttachmentUrl(
                employeeCertification.getAttachmentUrl());

        existingCertification.setStatus(
                employeeCertification.getStatus());

        employeeCertificationRepository.save(existingCertification);
        String newValue = convertToJson(existingCertification);
        String performedBy = getLoggedInEmployeeId();
        String employeeId = existingCertification.getEmployee().getEmployeeId();
        String oldValue = convertToJson(existingCertification);

        auditLogsService.logUpdate(
                "EMPLOYEE_CERTIFICATION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee certification updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_CERTIFICATION",
                "EMPLOYEE_CERTIFICATION",
                "Employee certification updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_CERTIFICATION",
                "EmployeeService",
                "Certification updated: " + id
        );

        return "Employee Certification Updated Successfully";
    }

    public String deleteCertification(Long id) {

        EmployeeCertification existingCertification =
                employeeCertificationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));

        employeeCertificationRepository.delete(existingCertification);
        String employeeId =
                existingCertification.getEmployee().getEmployeeId();

        employeeCertificationRepository.delete(existingCertification);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_CERTIFICATION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee certification deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_CERTIFICATION",
                "EMPLOYEE_CERTIFICATION",
                "Employee certification deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_CERTIFICATION",
                "EmployeeService",
                "Certification deleted: " + id
        );

        return "Employee Certification Deleted Successfully";
    }

    //=================================
// EMPLOYEE EXPERIENCE
//=================================

    public String createExperience(String employeeId,
                                   EmployeeExperience employeeExperience) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeExperience.setEmployee(employee);

        employeeExperienceRepository.save(employeeExperience);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_EXPERIENCE",
                employeeId,
                performedBy,
                employeeId,
                "Employee experience created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_EXPERIENCE",
                "EMPLOYEE_EXPERIENCE",
                "Employee experience created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXPERIENCE",
                "EmployeeService",
                "Experience created for " + employeeId
        );

        return "Employee Experience Added Successfully";
    }

    public List<EmployeeExperience> getExperiences() {

        return employeeExperienceRepository.findAll();
    }

    public EmployeeExperience getExperience(Long id) {

        return employeeExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));
    }

    public String updateExperience(Long id,
                                   EmployeeExperience employeeExperience) {

        EmployeeExperience existingExperience = employeeExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));

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

        auditLogsService.logUpdate(
                "EMPLOYEE_EXPERIENCE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee experience updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_EXPERIENCE",
                "EMPLOYEE_EXPERIENCE",
                "Employee experience updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXPERIENCE",
                "EmployeeService",
                "Experience updated: " + id
        );

        return "Employee Experience Updated Successfully";
    }

    public String deleteExperience(Long id) {

        EmployeeExperience existingExperience = employeeExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));

        employeeExperienceRepository.delete(existingExperience);
        String employeeId =
                existingExperience.getEmployee().getEmployeeId();

        employeeExperienceRepository.delete(existingExperience);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_EXPERIENCE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee experience deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_EXPERIENCE",
                "EMPLOYEE_EXPERIENCE",
                "Employee experience deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_EXPERIENCE",
                "EmployeeService",
                "Experience deleted: " + id
        );

        return "Employee Experience Deleted Successfully";
    }

    //=================================
// EMPLOYEE LANGUAGES
//=================================

    public String createLanguage(String employeeId,
                                 EmployeeLanguage employeeLanguage) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeLanguage.setEmployee(employee);

        employeeLanguageRepository.save(employeeLanguage);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_LANGUAGE",
                employeeId,
                performedBy,
                employeeId,
                "Employee language created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_LANGUAGE",
                "EMPLOYEE_LANGUAGE",
                "Employee language created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_LANGUAGE",
                "EmployeeService",
                "Language created for " + employeeId
        );

        return "Employee Language Added Successfully";
    }

    public List<EmployeeLanguage> getLanguages() {

        return employeeLanguageRepository.findAll();
    }

    public EmployeeLanguage getLanguage(Long id) {

        return employeeLanguageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Language Not Found"));
    }

    public String updateLanguage(Long id,
                                 EmployeeLanguage employeeLanguage) {

        EmployeeLanguage existingLanguage = employeeLanguageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Language Not Found"));

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
        auditLogsService.logUpdate(
                "EMPLOYEE_LANGUAGE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee language updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_LANGUAGE",
                "EMPLOYEE_LANGUAGE",
                "Employee language updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_LANGUAGE",
                "EmployeeService",
                "Language updated: " + id
        );

        return "Employee Language Updated Successfully";
    }

    public String deleteLanguage(Long id) {

        EmployeeLanguage existingLanguage = employeeLanguageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Language Not Found"));

        employeeLanguageRepository.delete(existingLanguage);
        String employeeId =
                existingLanguage.getEmployee().getEmployeeId();

        employeeLanguageRepository.delete(existingLanguage);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_LANGUAGE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee language deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_LANGUAGE",
                "EMPLOYEE_LANGUAGE",
                "Employee language deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_LANGUAGE",
                "EmployeeService",
                "Language deleted: " + id
        );

        return "Employee Language Deleted Successfully";
    }

    //=================================
// EMPLOYEE FAMILY MEMBERS
//=================================

    public String createFamilyMember(String employeeId,
                                     EmployeeFamilyMember employeeFamilyMember) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeFamilyMember.setEmployee(employee);

        employeeFamilyMemberRepository.save(employeeFamilyMember);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_FAMILY_MEMBER",
                employeeId,
                performedBy,
                employeeId,
                "Employee family member created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_FAMILY_MEMBER",
                "EMPLOYEE_FAMILY_MEMBER",
                "Employee family member created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_FAMILY_MEMBER",
                "EmployeeService",
                "Family member created for " + employeeId
        );

        return "Employee Family Member Added Successfully";
    }

    public List<EmployeeFamilyMember> getFamilyMembers() {

        return employeeFamilyMemberRepository.findAll();
    }

    public EmployeeFamilyMember getFamilyMember(Long id) {

        return employeeFamilyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));
    }

    public String updateFamilyMember(Long id,
                                     EmployeeFamilyMember employeeFamilyMember) {

        EmployeeFamilyMember existingMember = employeeFamilyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));

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

        auditLogsService.logUpdate(
                "EMPLOYEE_FAMILY_MEMBER",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee family member updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_FAMILY_MEMBER",
                "EMPLOYEE_FAMILY_MEMBER",
                "Employee family member updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_FAMILY_MEMBER",
                "EmployeeService",
                "Family member updated: " + id
        );

        return "Employee Family Member Updated Successfully";
    }

    public String deleteFamilyMember(Long id) {

        EmployeeFamilyMember existingMember = employeeFamilyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));

        employeeFamilyMemberRepository.delete(existingMember);
        String employeeId =
                existingMember.getEmployee().getEmployeeId();

        employeeFamilyMemberRepository.delete(existingMember);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_FAMILY_MEMBER",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee family member deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_FAMILY_MEMBER",
                "EMPLOYEE_FAMILY_MEMBER",
                "Employee family member deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_FAMILY_MEMBER",
                "EmployeeService",
                "Family member deleted: " + id
        );

        return "Employee Family Member Deleted Successfully";
    }

    //=================================
// EMPLOYEE ADDRESSES
//=================================

    public String createAddress(String employeeId,
                                EmployeeAddress employeeAddress) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // Save Employee Address
        employeeAddress.setEmployee(employee);
        employeeAddressRepository.save(employeeAddress);

        // Check if Profile already exists
        EmployeeProfile profile = employeeProfileRepository
                .findByEmployee(employee)
                .orElse(new EmployeeProfile());

        profile.setEmployee(employee);
        profile.setAddress(employeeAddress.getAddressLine1() + ", " + employeeAddress.getAddressLine2());
        profile.setCity(employeeAddress.getCity());
        profile.setState(employeeAddress.getState());
        profile.setPincode(employeeAddress.getPostalCode());
        profile.setCountry(employeeAddress.getCountry());

        profile.setProfileStatus("ACTIVE");

        employeeProfileRepository.save(profile);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_ADDRESS",
                employeeId,
                performedBy,
                employeeId,
                "Employee address created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_ADDRESS",
                "EMPLOYEE_ADDRESS",
                "Employee address created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_ADDRESS",
                "EmployeeService",
                "Address created for " + employeeId
        );

        return "Employee Address Created Successfully";
    }

    public List<EmployeeAddress> getAddresses() {

        return employeeAddressRepository.findAll();
    }

    public EmployeeAddress getAddress(Long id) {

        return employeeAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Address Not Found"));
    }

    public String updateAddress(Long id,
                                EmployeeAddress employeeAddress) {

        EmployeeAddress existingAddress = employeeAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Address Not Found"));

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

        EmployeeProfile profile = employeeProfileRepository
                .findByEmployee(employee)
                .orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));

        profile.setAddress(employeeAddress.getAddressLine1()
                + ", " + employeeAddress.getAddressLine2());

        profile.setCity(employeeAddress.getCity());
        profile.setState(employeeAddress.getState());
        profile.setCountry(employeeAddress.getCountry());
        profile.setPincode(employeeAddress.getPostalCode());

        employeeProfileRepository.save(profile);
        String newValue = convertToJson(existingAddress);

        String performedBy = getLoggedInEmployeeId();
        String employeeId =
                existingAddress.getEmployee().getEmployeeId();
        String oldValue = convertToJson(existingAddress);
        auditLogsService.logUpdate(
                "EMPLOYEE_ADDRESS",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee address updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_ADDRESS",
                "EMPLOYEE_ADDRESS",
                "Employee address and profile updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_ADDRESS",
                "EmployeeService",
                "Address updated: " + id
        );

        return "Employee Address & Profile Updated Successfully";
    }

    public String deleteAddress(Long id) {

        EmployeeAddress existingAddress = employeeAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Address Not Found"));

        employeeAddressRepository.delete(existingAddress);
        String employeeId =
                existingAddress.getEmployee().getEmployeeId();

        employeeAddressRepository.delete(existingAddress);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_ADDRESS",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee address deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_ADDRESS",
                "EMPLOYEE_ADDRESS",
                "Employee address deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_ADDRESS",
                "EmployeeService",
                "Address deleted: " + id
        );

        return "Employee Address Deleted Successfully";
    }

    //=================================
// EMPLOYEE PROMOTIONS
//=================================

    public String createPromotion(String employeeId,
                                  EmployeePromotion employeePromotion) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeePromotion.setEmployee(employee);

        employeePromotionRepository.save(employeePromotion);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_PROMOTION",
                employeeId,
                performedBy,
                employeeId,
                "Employee promotion created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_PROMOTION",
                "EMPLOYEE_PROMOTION",
                "Employee promotion created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_PROMOTION",
                "EmployeeService",
                "Promotion created for " + employeeId
        );

        return "Employee Promotion Created Successfully";
    }

    public List<EmployeePromotion> getPromotions() {

        return employeePromotionRepository.findAll();
    }

    public EmployeePromotion getPromotion(Long id) {

        return employeePromotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));
    }

    public String updatePromotion(Long id, EmployeePromotion employeePromotion) {

        EmployeePromotion existingPromotion = employeePromotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));

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
        String employeeId =
                existingPromotion.getEmployee().getEmployeeId();

        auditLogsService.logUpdate(
                "EMPLOYEE_PROMOTION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee promotion updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_PROMOTION",
                "EMPLOYEE_PROMOTION",
                "Employee promotion updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_PROMOTION",
                "EmployeeService",
                "Promotion updated: " + id
        );

        return "Employee Promotion Updated Successfully";
    }

    public String deletePromotion(Long id) {

        EmployeePromotion existingPromotion = employeePromotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));

        employeePromotionRepository.delete(existingPromotion);
        String employeeId =
                existingPromotion.getEmployee().getEmployeeId();

        employeePromotionRepository.delete(existingPromotion);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_PROMOTION",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee promotion deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_PROMOTION",
                "EMPLOYEE_PROMOTION",
                "Employee promotion deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_PROMOTION",
                "EmployeeService",
                "Promotion deleted: " + id
        );

        return "Employee Promotion Deleted Successfully";
    }
    //=================================
// EMPLOYEE TRANSFERS
//=================================

    public String createTransfer(String employeeId,
                                 EmployeeTransfer employeeTransfer) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        employeeTransfer.setEmployee(employee);

        employeeTransferRepository.save(employeeTransfer);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "EMPLOYEE_TRANSFER",
                employeeId,
                performedBy,
                employeeId,
                "Employee transfer created"
        );

        auditLogsService.logActivity(
                employeeId,
                "CREATE_TRANSFER",
                "EMPLOYEE_TRANSFER",
                "Employee transfer created",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_TRANSFER",
                "EmployeeService",
                "Transfer created for " + employeeId
        );

        return "Employee Transfer Created Successfully";
    }

    public List<EmployeeTransfer> getTransfers() {

        return employeeTransferRepository.findAll();
    }

    public EmployeeTransfer getTransfer(Long id) {

        return employeeTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));
    }

    public String updateTransfer(Long id,
                                 EmployeeTransfer employeeTransfer) {

        EmployeeTransfer existingTransfer = employeeTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));

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
        String employeeId =
                existingTransfer.getEmployee().getEmployeeId();

        auditLogsService.logUpdate(
                "EMPLOYEE_TRANSFER",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee transfer updated",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                employeeId,
                "UPDATE_TRANSFER",
                "EMPLOYEE_TRANSFER",
                "Employee transfer updated",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_TRANSFER",
                "EmployeeService",
                "Transfer updated: " + id
        );

        return "Employee Transfer Updated Successfully";
    }

    public String deleteTransfer(Long id) {

        EmployeeTransfer existingTransfer = employeeTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));

        employeeTransferRepository.delete(existingTransfer);
        String employeeId =
                existingTransfer.getEmployee().getEmployeeId();

        employeeTransferRepository.delete(existingTransfer);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logDelete(
                "EMPLOYEE_TRANSFER",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Employee transfer deleted"
        );

        auditLogsService.logActivity(
                employeeId,
                "DELETE_TRANSFER",
                "EMPLOYEE_TRANSFER",
                "Employee transfer deleted",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "EMPLOYEE_TRANSFER",
                "EmployeeService",
                "Transfer deleted: " + id
        );

        return "Employee Transfer Deleted Successfully";
    }

    //=================================
// REPORTS
//=================================

    public List<EmployeeSkill> certifiedSkills() {

        return employeeSkillRepository.findAll()
                .stream()
                .filter(skill -> Boolean.TRUE.equals(skill.getCertificationAvailable()))
                .toList();
    }

    public List<EmployeeCertification> activeCertifications() {

        return employeeCertificationRepository.findAll()
                .stream()
                .filter(certification ->
                        "ACTIVE".equalsIgnoreCase(certification.getStatus()))
                .toList();
    }

    public List<EmployeeCertification> expiredCertifications() {

        return employeeCertificationRepository.findAll()
                .stream()
                .filter(certification ->
                        certification.getExpiryDate() != null &&
                                certification.getExpiryDate().isBefore(LocalDate.now()))
                .toList();
    }

    public List<EmployeeExperience> currentExperiences() {

        return employeeExperienceRepository.findAll()
                .stream()
                .filter(EmployeeExperience::getCurrentCompany)
                .toList();
    }

    public List<EmployeeExperience> previousExperiences() {

        return employeeExperienceRepository.findAll()
                .stream()
                .filter(experience -> !experience.getCurrentCompany())
                .toList();
    }

    public List<EmployeePromotion> latestPromotions() {

        return employeePromotionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(EmployeePromotion::getPromotionDate).reversed())
                .toList();
    }

    public List<EmployeeTransfer> latestTransfers() {

        return employeeTransferRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(EmployeeTransfer::getTransferDate).reversed())
                .toList();
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

        counts.put("activeEmployees",
                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> "ACTIVE".equalsIgnoreCase(employee.getStatus()))
                        .count());

        counts.put("inactiveEmployees",
                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> "INACTIVE".equalsIgnoreCase(employee.getStatus()))
                        .count());

        counts.put("certifiedSkills",
                employeeSkillRepository.findAll()
                        .stream()
                        .filter(skill -> Boolean.TRUE.equals(skill.getCertificationAvailable()))
                        .count());

        counts.put("activeCertifications",
                employeeCertificationRepository.findAll()
                        .stream()
                        .filter(c -> "ACTIVE".equalsIgnoreCase(c.getStatus()))
                        .count());

        counts.put("expiredCertifications",
                employeeCertificationRepository.findAll()
                        .stream()
                        .filter(c -> c.getExpiryDate() != null
                                && c.getExpiryDate().isBefore(LocalDate.now()))
                        .count());

        counts.put("currentExperienceEmployees",
                employeeExperienceRepository.findAll()
                        .stream()
                        .filter(EmployeeExperience::getCurrentCompany)
                        .count());

        return counts;
    }
    public List<Employee> searchEmployees(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepository.findAll();
        }

        String searchKeyword =
                "%" + keyword.trim() + "%";

        return employeeRepository.searchEmployees(
                searchKeyword);
    }
    public List<Employee> getEmployeesByDepartmentName(
            String departmentName) {

        Department department =
                departmentRepository
                        .findByDepartmentNameIgnoreCase(
                                departmentName)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department Not Found"));

        return employeeRepository
                .findByDepartmentId(
                        department.getId());
    }
    public List<Employee> getEmployeesByDesignation(
            String designationName) {

        return employeeDesignationRepository
                .findEmployeesByDesignationName(
                        designationName);
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