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
import java.util.List;
import java.util.Optional;

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