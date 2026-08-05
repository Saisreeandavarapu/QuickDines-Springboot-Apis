package com.HRMS.QuickDines.Employee.Service;

import com.HRMS.QuickDines.AdvanceServices.CloudinaryService;
import com.HRMS.QuickDines.Employee.model.*;
import com.HRMS.QuickDines.Employee.repo.*;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public String createEmployee(Employee employee) {

        // Fetch Department
        Department department = departmentRepository
                .findById(employee.getDepartmentId())
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


        return "Employee Created Successfully";

    }



    public List<Employee> getAllEmployees(){

        return employeeRepository.findAll();
    }



    public Employee getEmployee(Long id){

        return employeeRepository.findById(id).get();
    }



    public String updateEmployee(Long id, Employee employee) {

        // Fetch Employee
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update Employee fields
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setMobileNumber(employee.getMobileNumber());
        existingEmployee.setStatus(employee.getStatus());
        existingEmployee.setDepartmentId(employee.getDepartmentId());
        // Save Employee
        employeeRepository.save(existingEmployee);

        return "Employee Updated Successfully";
    }




    public String deleteEmployee(Long id){

        employeeRepository.deleteById(id);
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

        return "Employee Profile Updated Successfully";
    }


    public String deleteProfile(String employeeId) {

        EmployeeProfile profile = employeeProfileRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Profile Not Found"));
        employeeProfileRepository.delete(profile);
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

        return "Bank Details Updated Successfully";
    }


    public String deleteBankDetails(String employeeId) {

        EmployeeBankDetails bankDetails = employeeBankRepository.findByEmployeeId(employeeId).orElseThrow(() ->
                                new RuntimeException("Bank Details Not Found"));

        employeeBankRepository.delete(bankDetails);

        return "Bank Details Deleted Successfully";
    }


    //=========================================
// CONTACTS
//=========================================

    public String createContacts(String employeeId, EmployeeContacts contacts) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        contacts.setEmployee(employee);
        employeeContactRepository.save(contacts);
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

        return "Employee Designation Updated Successfully";
    }


    public String deleteDesignation(String id) {

        EmployeeDesignation designation = employeeDesignationRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Designation Not Found"));

        employeeDesignationRepository.delete(designation);

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

        return "Employee Exit Management Updated Successfully";
    }


    public String deleteExitManagement(String employeeId) {

        EmployeeExitManagement exitManagement = employeeExitRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Exit Management Details Not Found"));

        employeeExitRepository.delete(exitManagement);

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

        return "Employee Skill Updated Successfully";
    }

    public String deleteSkill(Long id) {

        EmployeeSkill existingSkill = employeeSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Skill Not Found"));

        employeeSkillRepository.delete(existingSkill);

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

        return "Employee Certification Updated Successfully";
    }

    public String deleteCertification(Long id) {

        EmployeeCertification existingCertification =
                employeeCertificationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Employee Certification Not Found"));

        employeeCertificationRepository.delete(existingCertification);

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

        return "Employee Experience Updated Successfully";
    }

    public String deleteExperience(Long id) {

        EmployeeExperience existingExperience = employeeExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Experience Not Found"));

        employeeExperienceRepository.delete(existingExperience);

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

        return "Employee Language Updated Successfully";
    }

    public String deleteLanguage(Long id) {

        EmployeeLanguage existingLanguage = employeeLanguageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Language Not Found"));

        employeeLanguageRepository.delete(existingLanguage);

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

        return "Employee Family Member Updated Successfully";
    }

    public String deleteFamilyMember(Long id) {

        EmployeeFamilyMember existingMember = employeeFamilyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Family Member Not Found"));

        employeeFamilyMemberRepository.delete(existingMember);

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

        return "Employee Address & Profile Updated Successfully";
    }

    public String deleteAddress(Long id) {

        EmployeeAddress existingAddress = employeeAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Address Not Found"));

        employeeAddressRepository.delete(existingAddress);

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

        return "Employee Promotion Updated Successfully";
    }

    public String deletePromotion(Long id) {

        EmployeePromotion existingPromotion = employeePromotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Promotion Not Found"));

        employeePromotionRepository.delete(existingPromotion);

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

        return "Employee Transfer Updated Successfully";
    }

    public String deleteTransfer(Long id) {

        EmployeeTransfer existingTransfer = employeeTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Transfer Not Found"));

        employeeTransferRepository.delete(existingTransfer);

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