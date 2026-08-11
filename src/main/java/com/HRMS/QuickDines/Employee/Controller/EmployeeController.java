package com.HRMS.QuickDines.Employee.Controller;

import com.HRMS.QuickDines.Employee.Service.EmployeeService;
import com.HRMS.QuickDines.Employee.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;


    //-------------------------
    // Employee CRUD
    //-------------------------

    @PostMapping("/create")
    @PreAuthorize("hasAuthority(EMPLOYEE_CREATE)")
    public ResponseEntity<?> createEmployee(
            @RequestBody Employee employee){

        return ResponseEntity.ok(
                service.createEmployee(employee));
    }



    @GetMapping
    @PreAuthorize("hasAuthority(EMPLOYEE_READ)")
    public ResponseEntity<?> getAllEmployees(){

        return ResponseEntity.ok(
                service.getAllEmployees());
    }



    @GetMapping("/{id}")
   @PreAuthorize("hasAuthority(EMPLOYEE_READ)")
    public ResponseEntity<?> getEmployee(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getEmployee(id));
    }



    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_UPDATE)")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee){

        return ResponseEntity.ok(
                service.updateEmployee(id,employee));
    }



    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DELETE)")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deleteEmployee(id));
    }



    //-------------------------
    // Complete Details
    //-------------------------

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_READ)")
    public ResponseEntity<?> getEmployeeDetails(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getEmployeeDetails(id));
    }



    // ----------------------------------
// Employee Documents APIs
// ----------------------------------

    @PostMapping("/upload-document/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DOCUMENT_CREATE)")
    public ResponseEntity<?> uploadDocument(@PathVariable Long employeeId, @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType) {

        return ResponseEntity.ok(
                service.uploadDocument(employeeId, file, documentType));
    }


    @GetMapping("/documents/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DOCUMENT_READ)")
    public ResponseEntity<?> getDocuments(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getDocuments(employeeId));
    }


    @DeleteMapping("/document/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DOCUMENT_DELETE)")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDocument(id));
    }
    //=========================================
// EMPLOYEE PROFILE
//=========================================

    @PostMapping("/profile/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROFILE_CREATE)")
    public ResponseEntity<?> createProfile(
            @PathVariable String employeeId,
            @RequestBody EmployeeProfile profile) {

        return ResponseEntity.ok(
                service.createProfile(employeeId, profile));
    }
    @GetMapping("/profiles")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROFILE_READ)")
    public List<EmployeeProfile> getAllProfiles() {
        return service.getAllProfiles();
    }


    @GetMapping("/profile/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROFILE_READ)")
    public ResponseEntity<?> getProfile(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getProfile(employeeId));
    }


    @PutMapping("/profile/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROFILE_UPDATE)")
    public ResponseEntity<?> updateProfile(
            @PathVariable String employeeId,
            @RequestBody EmployeeProfile profile) {

        return ResponseEntity.ok(
                service.updateProfile(employeeId, profile));
    }


    @DeleteMapping("/profile/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROFILE_DELETE)")
    public ResponseEntity<?> deleteProfile(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteProfile(employeeId));
    }


    //=========================================
// BANK DETAILS
//=========================================

    @PostMapping("/bank-details/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_BANK_CREATE)")
    public ResponseEntity<?> createBankDetails(
            @PathVariable String employeeId,
            @RequestBody EmployeeBankDetails bankDetails) {

        return ResponseEntity.ok(
                service.createBankDetails(employeeId, bankDetails));
    }


    @GetMapping("/bank-details/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_BANK_READ)")
    public ResponseEntity<?> getBankDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getBankDetails(employeeId));
    }


    @PutMapping("/bank-details/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_BANK_UPDATE)")
    public ResponseEntity<?> updateBankDetails(
            @PathVariable String employeeId,
            @RequestBody EmployeeBankDetails bankDetails) {

        return ResponseEntity.ok(
                service.updateBankDetails(employeeId, bankDetails));
    }


    @DeleteMapping("/bank-details/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_BANK_DELETE)")
    public ResponseEntity<?> deleteBankDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteBankDetails(employeeId));
    }


    //=========================================
// EMERGENCY CONTACTS
//=========================================

    @PostMapping("/contacts/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CONTACT_CREATE)")
    public ResponseEntity<?> createContacts(
            @PathVariable String employeeId,
            @RequestBody EmployeeContacts contacts) {

        return ResponseEntity.ok(
                service.createContacts(employeeId, contacts));
    }


    @GetMapping("/contacts/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CONTACT_READ)")
    public ResponseEntity<?> getContacts(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getContacts(employeeId));
    }


    @PutMapping("/contacts/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CONTACT_UPDATE)")
    public ResponseEntity<?> updateContacts(
            @PathVariable String id,
            @RequestBody EmployeeContacts contacts) {

        return ResponseEntity.ok(
                service.updateContacts(id, contacts));
    }


    @DeleteMapping("/contacts/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CONTACT_DELETE)")
    public ResponseEntity<?> deleteContacts(
            @PathVariable String id) {

        return ResponseEntity.ok(
                service.deleteContacts(id));
    }


    //=========================================
// DESIGNATION
//=========================================

    @PostMapping("/designation/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DESIGNATION_CREATE)")
    public ResponseEntity<?> createDesignation(
            @PathVariable String employeeId,
            @RequestBody EmployeeDesignation designation) {

        return ResponseEntity.ok(
                service.createDesignation(employeeId, designation));
    }


    @GetMapping("/designation/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DESIGNATION_READ)")
    public ResponseEntity<?> getDesignation(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getDesignation(employeeId));
    }


    @PutMapping("/designation/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DESIGNATION_UPDATE)")
    public ResponseEntity<?> updateDesignation(
            @PathVariable String id,
            @RequestBody EmployeeDesignation designation) {

        return ResponseEntity.ok(
                service.updateDesignation(id, designation));
    }


    @DeleteMapping("/designation/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_DESIGNATION_DELETE)")
    public ResponseEntity<?> deleteDesignation(
            @PathVariable String id) {

        return ResponseEntity.ok(
                service.deleteDesignation(id));
    }


    //=========================================
// EXIT MANAGEMENT
//=========================================

    @PostMapping("/exit-management/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXIT_CREATE)")
    public ResponseEntity<?> createExitManagement(
            @PathVariable String employeeId,
            @RequestBody EmployeeExitManagement exitManagement) {

        return ResponseEntity.ok(
                service.createExitManagement(employeeId, exitManagement));
    }


    @GetMapping("/exit-management/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXIT_READ)")
    public ResponseEntity<?> getExitManagement(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getExitManagement(employeeId));
    }


    @PutMapping("/exit-management/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXIT_UPDATE)")
    public ResponseEntity<?> updateExitManagement(
            @PathVariable String employeeId,
            @RequestBody EmployeeExitManagement exitManagement) {

        return ResponseEntity.ok(
                service.updateExitManagement(employeeId, exitManagement));
    }


    @DeleteMapping("/exit-management/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXIT_DELETE)")
    public ResponseEntity<?> deleteExitManagement(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteExitManagement(employeeId));
    }

//=================================
// EMPLOYEE SKILLS
//=================================

    @PostMapping("/skill/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_SKILL_CREATE)")
    public ResponseEntity<?> createSkill(
            @PathVariable String employeeId,
            @RequestBody EmployeeSkill employeeSkill) {

        return ResponseEntity.ok(
                service.createSkill(employeeId, employeeSkill));
    }

    @GetMapping("/skills")
    @PreAuthorize("hasAuthority(EMPLOYEE_SKILL_READ)")
    public ResponseEntity<?> getSkills() {

        return ResponseEntity.ok(service.getSkills());
    }

    @GetMapping("/skill/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_SKILL_READ)")
    public ResponseEntity<?> getSkill(@PathVariable Long id) {

        return ResponseEntity.ok(service.getSkill(id));
    }

    @PutMapping("/skill/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_SKILL_UPDATE)")
    public ResponseEntity<?> updateSkill(
            @PathVariable Long id,
            @RequestBody EmployeeSkill employeeSkill) {

        return ResponseEntity.ok(service.updateSkill(id, employeeSkill));
    }

    @DeleteMapping("/skill/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_SKILL_DELETE)")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteSkill(id));
    }

    //=================================
// EMPLOYEE CERTIFICATIONS
//=================================

    @PostMapping("/certification/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CERTIFICATION_CREATE)")
    public ResponseEntity<?> createCertification(
            @PathVariable String employeeId,
            @RequestBody EmployeeCertification employeeCertification) {

        return ResponseEntity.ok(service.createCertification(employeeId, employeeCertification));
    }

    @GetMapping("/certifications")
    @PreAuthorize("hasAuthority(EMPLOYEE_CERTIFICATION_READ)")
    public ResponseEntity<?> getCertifications() {

        return ResponseEntity.ok(service.getCertifications());
    }

    @GetMapping("/certification/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CERTIFICATION_READ)")
    public ResponseEntity<?> getCertification(@PathVariable Long id) {

        return ResponseEntity.ok(service.getCertification(id));
    }

    @PutMapping("/certification/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CERTIFICATION_UPDATE)")
    public ResponseEntity<?> updateCertification(
            @PathVariable Long id,
            @RequestBody EmployeeCertification employeeCertification) {

        return ResponseEntity.ok(service.updateCertification(id, employeeCertification));
    }

    @DeleteMapping("/certification/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_CERTIFICATION_DELETE)")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteCertification(id));
    }

    //=================================
// EMPLOYEE EXPERIENCE
//=================================

    @PostMapping("/experience/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXPERIENCE_CREATE)")
    public ResponseEntity<?> createExperience(
            @PathVariable String employeeId,
            @RequestBody EmployeeExperience employeeExperience) {

        return ResponseEntity.ok(
                service.createExperience(employeeId, employeeExperience));
    }

    @GetMapping("/experiences")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXPERIENCE_READ)")
    public ResponseEntity<?> getExperiences() {

        return ResponseEntity.ok(
                service.getExperiences());
    }

    @GetMapping("/experience/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXPERIENCE_READ)")
    public ResponseEntity<?> getExperience(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getExperience(id));
    }

    @PutMapping("/experience/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXPERIENCE_UPDATE)")
    public ResponseEntity<?> updateExperience(
            @PathVariable Long id,
            @RequestBody EmployeeExperience employeeExperience) {

        return ResponseEntity.ok(
                service.updateExperience(id, employeeExperience));
    }

    @DeleteMapping("/experience/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_EXPERIENCE_DELETE)")
    public ResponseEntity<?> deleteExperience(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteExperience(id));
    }

    //=================================
// EMPLOYEE LANGUAGES
//=================================

    @PostMapping("/language/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_LANGUAGE_CREATE)")
    public ResponseEntity<?> createLanguage(
            @PathVariable String employeeId,
            @RequestBody EmployeeLanguage employeeLanguage) {

        return ResponseEntity.ok(
                service.createLanguage(employeeId, employeeLanguage));
    }

    @GetMapping("/languages")
    @PreAuthorize("hasAuthority(EMPLOYEE_LANGUAGE_READ)")
    public ResponseEntity<?> getLanguages() {

        return ResponseEntity.ok(
                service.getLanguages());
    }

    @GetMapping("/language/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_LANGUAGE_READ)")
    public ResponseEntity<?> getLanguage(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLanguage(id));
    }

    @PutMapping("/language/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_LANGUAGE_UPDATE)")
    public ResponseEntity<?> updateLanguage(
            @PathVariable Long id,
            @RequestBody EmployeeLanguage employeeLanguage) {

        return ResponseEntity.ok(
                service.updateLanguage(id, employeeLanguage));
    }

    @DeleteMapping("/language/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_LANGUAGE_DELETE)")
    public ResponseEntity<?> deleteLanguage(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLanguage(id));
    }

    //=================================
// EMPLOYEE FAMILY MEMBERS
//=================================

    @PostMapping("/family/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_FAMILY_CREATE)")
    public ResponseEntity<?> createFamilyMember(
            @PathVariable String employeeId,
            @RequestBody EmployeeFamilyMember employeeFamilyMember) {

        return ResponseEntity.ok(
                service.createFamilyMember(employeeId, employeeFamilyMember));
    }

    @GetMapping("/families")
    @PreAuthorize("hasAuthority(EMPLOYEE_FAMILY_READ)")
    public ResponseEntity<?> getFamilyMembers() {

        return ResponseEntity.ok(
                service.getFamilyMembers());
    }

    @GetMapping("/family/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_FAMILY_READ)")
    public ResponseEntity<?> getFamilyMember(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getFamilyMember(id));
    }

    @PutMapping("/family/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_FAMILY_UPDATE)")
    public ResponseEntity<?> updateFamilyMember(
            @PathVariable Long id,
            @RequestBody EmployeeFamilyMember employeeFamilyMember) {

        return ResponseEntity.ok(
                service.updateFamilyMember(id, employeeFamilyMember));
    }

    @DeleteMapping("/family/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_FAMILY_DELETE)")
    public ResponseEntity<?> deleteFamilyMember(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteFamilyMember(id));
    }

    //=================================
// EMPLOYEE ADDRESSES
//=================================

    @PostMapping("/address/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_ADDRESS_CREATE)")
    public ResponseEntity<?> createAddress(
            @PathVariable String employeeId,
            @RequestBody EmployeeAddress employeeAddress) {

        return ResponseEntity.ok(
                service.createAddress(employeeId, employeeAddress));
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasAuthority(EMPLOYEE_ADDRESS_READ)")
    public ResponseEntity<?> getAddresses() {

        return ResponseEntity.ok(
                service.getAddresses());
    }

    @GetMapping("/address/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_ADDRESS_READ)")
    public ResponseEntity<?> getAddress(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAddress(id));
    }

    @PutMapping("/address/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_ADDRESS_UPDATE)")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long id,
            @RequestBody EmployeeAddress employeeAddress) {

        return ResponseEntity.ok(
                service.updateAddress(id, employeeAddress));
    }

    @DeleteMapping("/address/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_ADDRESS_DELETE)")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAddress(id));
    }

    //=================================
// EMPLOYEE PROMOTIONS
//=================================

    @PostMapping("/promotion/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROMOTION_CREATE)")
    public ResponseEntity<?> createPromotion(
            @PathVariable String employeeId,
            @RequestBody EmployeePromotion employeePromotion) {

        return ResponseEntity.ok(
                service.createPromotion(employeeId, employeePromotion));
    }

    @GetMapping("/promotions")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROMOTION_READ)")
    public ResponseEntity<?> getPromotions() {

        return ResponseEntity.ok(
                service.getPromotions());
    }

    @GetMapping("/promotion/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_READ)")
    public ResponseEntity<?> getPromotion(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getPromotion(id));
    }

    @PutMapping("/promotion/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROMOTION_UPDATE)")
    public ResponseEntity<?> updatePromotion(
            @PathVariable Long id,
            @RequestBody EmployeePromotion employeePromotion) {

        return ResponseEntity.ok(
                service.updatePromotion(id, employeePromotion));
    }

    @DeleteMapping("/promotion/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_PROMOTION_DELETE)")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deletePromotion(id));
    }

    //=================================
// EMPLOYEE TRANSFERS
//=================================

    @PostMapping("/transfer/{employeeId}")
    @PreAuthorize("hasAuthority(EMPLOYEE_TRANSFER_CREATE)")
    public ResponseEntity<?> createTransfer(
            @PathVariable String employeeId,
            @RequestBody EmployeeTransfer employeeTransfer) {

        return ResponseEntity.ok(
                service.createTransfer(employeeId, employeeTransfer));
    }

    @GetMapping("/transfers")
    @PreAuthorize("hasAuthority(EMPLOYEE_TRANSFER_READ)")
    public ResponseEntity<?> getTransfers() {

        return ResponseEntity.ok(
                service.getTransfers());
    }

    @GetMapping("/transfer/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_READ)")
    public ResponseEntity<?> getTransfer(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTransfer(id));
    }

    @PutMapping("/transfer/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_TRANSFER_UPDATE)")
    public ResponseEntity<?> updateTransfer(
            @PathVariable Long id,
            @RequestBody EmployeeTransfer employeeTransfer) {

        return ResponseEntity.ok(
                service.updateTransfer(id, employeeTransfer));
    }

    @DeleteMapping("/transfer/{id}")
    @PreAuthorize("hasAuthority(EMPLOYEE_TRANSFER_DELETE)")
    public ResponseEntity<?> deleteTransfer(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTransfer(id));
    }

    //=================================
// REPORTS
//=================================

    @GetMapping("/skills/certified")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> certifiedSkills() {
        return ResponseEntity.ok(service.certifiedSkills());
    }

    @GetMapping("/certifications/active")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> activeCertifications() {
        return ResponseEntity.ok(service.activeCertifications());
    }

    @GetMapping("/certifications/expired")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> expiredCertifications() {
        return ResponseEntity.ok(service.expiredCertifications());
    }

    @GetMapping("/experience/current")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> currentExperiences() {
        return ResponseEntity.ok(service.currentExperiences());
    }

    @GetMapping("/experience/previous")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> previousExperiences() {
        return ResponseEntity.ok(service.previousExperiences());
    }

    @GetMapping("/promotions/latest")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> latestPromotions() {
        return ResponseEntity.ok(service.latestPromotions());
    }

    @GetMapping("/transfers/latest")
    @PreAuthorize("hasAuthority(EMPLOYEE_REPORT_READ)")
    public ResponseEntity<?> latestTransfers() {
        return ResponseEntity.ok(service.latestTransfers());
    }

    //=================================
// DASHBOARD
//=================================

    @GetMapping("/counts")
    @PreAuthorize("hasAuthority(EMPLOYEE_DASHBOARD_READ)")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> searchEmployees(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                service.searchEmployees(keyword)
        );
    }
    @GetMapping("/filter/department")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> getEmployeesByDepartmentName(
            @RequestParam String departmentName) {

        return ResponseEntity.ok(
                service
                        .getEmployeesByDepartmentName(
                                departmentName)
        );
    }
    @GetMapping("/filter/designation")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> getEmployeesByDesignation(
            @RequestParam String designationName) {

        return ResponseEntity.ok(
                service
                        .getEmployeesByDesignation(
                                designationName));
    }


//    //-------------------------
//    // Attendance
//    //-------------------------
//
//    @GetMapping("/attendance/{id}")
//    public ResponseEntity<?> attendance(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.getAttendance(id));
//    }
//
//
//
//    //-------------------------
//    // Leaves
//    //-------------------------
//
//    @GetMapping("/leave-balance/{id}")
//    public ResponseEntity<?> leaveBalance(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.getLeaveBalance(id));
//    }
//
//
//
//    //-------------------------
//    // Payroll
//    //-------------------------
//
//    @GetMapping("/salary/{id}")
//    public ResponseEntity<?> salary(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.getSalary(id));
//    }
//
//
//
//    //-------------------------
//    // Tasks
//    //-------------------------
//
//    @GetMapping("/tasks/{id}")
//    public ResponseEntity<?> tasks(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.getTasks(id));
//    }
//
//
//
//    //-------------------------
//    // Performance
//    //-------------------------
//
//    @GetMapping("/performance/{id}")
//    public ResponseEntity<?> performance(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.getPerformance(id));
//    }
//
//
//
//    //-------------------------
//    // Expenses
//    //-------------------------
//
//    @PostMapping("/expense")
//    public ResponseEntity<?> addExpense(
//            @RequestBody Expense expense){
//
//        return ResponseEntity.ok(
//                service.addExpense(expense));
//    }
//
//
//
//    //-------------------------
//    // Employee Status
//    //-------------------------
//
//    @PutMapping("/activate/{id}")
//    public ResponseEntity<?> activate(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.activate(id));
//    }
//
//
//
//    @PutMapping("/deactivate/{id}")
//    public ResponseEntity<?> deactivate(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.deactivate(id));
//    }
//
//
//
//    @PutMapping("/block/{id}")
//    public ResponseEntity<?> block(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.block(id));
//    }
//
//
//
//    @PutMapping("/unblock/{id}")
//    public ResponseEntity<?> unblock(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.unblock(id));
//    }
//
//
//
//    @PutMapping("/terminate/{id}")
//    public ResponseEntity<?> terminate(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.terminate(id));
//    }
//
//
//
//    @PutMapping("/resigned/{id}")
//    public ResponseEntity<?> resigned(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.resigned(id));
//    }
//
//
//
//    //-------------------------
//    // Dashboard
//    //-------------------------
//
//    @GetMapping("/dashboard/{id}")
//    public ResponseEntity<?> dashboard(
//            @PathVariable Long id){
//
//        return ResponseEntity.ok(
//                service.dashboard(id));
//    }


}
