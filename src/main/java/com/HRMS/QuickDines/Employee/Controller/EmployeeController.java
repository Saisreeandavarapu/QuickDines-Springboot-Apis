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
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public ResponseEntity<?> createEmployee(
            @RequestBody Employee employee){

        return ResponseEntity.ok(
                service.createEmployee(employee));
    }



    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> getAllEmployees(){

        return ResponseEntity.ok(
                service.getAllEmployees());
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> getEmployee(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getEmployee(id));
    }



    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee){

        return ResponseEntity.ok(
                service.updateEmployee(id,employee));
    }



    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deleteEmployee(id));
    }



    //-------------------------
    // Complete Details
    //-------------------------

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<?> getEmployeeDetails(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getEmployeeDetails(id));
    }



    // ----------------------------------
// Employee Documents APIs
// ----------------------------------

    @PostMapping("/upload-document/{employeeId}")
    public ResponseEntity<?> uploadDocument(@PathVariable Long employeeId, @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType) {

        return ResponseEntity.ok(
                service.uploadDocument(employeeId, file, documentType));
    }


    @GetMapping("/documents/{employeeId}")
    public ResponseEntity<?> getDocuments(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getDocuments(employeeId));
    }


    @DeleteMapping("/document/{id}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDocument(id));
    }
    //=========================================
// EMPLOYEE PROFILE
//=========================================

    @PostMapping("/profile/{employeeId}")
    public ResponseEntity<?> createProfile(
            @PathVariable String employeeId,
            @RequestBody EmployeeProfile profile) {

        return ResponseEntity.ok(
                service.createProfile(employeeId, profile));
    }
    @GetMapping("/profiles")
    public List<EmployeeProfile> getAllProfiles() {
        return service.getAllProfiles();
    }


    @GetMapping("/profile/{employeeId}")
    public ResponseEntity<?> getProfile(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getProfile(employeeId));
    }


    @PutMapping("/profile/{employeeId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable String employeeId,
            @RequestBody EmployeeProfile profile) {

        return ResponseEntity.ok(
                service.updateProfile(employeeId, profile));
    }


    @DeleteMapping("/profile/{employeeId}")
    public ResponseEntity<?> deleteProfile(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteProfile(employeeId));
    }


    //=========================================
// BANK DETAILS
//=========================================

    @PostMapping("/bank-details/{employeeId}")
    public ResponseEntity<?> createBankDetails(
            @PathVariable String employeeId,
            @RequestBody EmployeeBankDetails bankDetails) {

        return ResponseEntity.ok(
                service.createBankDetails(employeeId, bankDetails));
    }


    @GetMapping("/bank-details/{employeeId}")
    public ResponseEntity<?> getBankDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getBankDetails(employeeId));
    }


    @PutMapping("/bank-details/{employeeId}")
    public ResponseEntity<?> updateBankDetails(
            @PathVariable String employeeId,
            @RequestBody EmployeeBankDetails bankDetails) {

        return ResponseEntity.ok(
                service.updateBankDetails(employeeId, bankDetails));
    }


    @DeleteMapping("/bank-details/{employeeId}")
    public ResponseEntity<?> deleteBankDetails(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteBankDetails(employeeId));
    }


    //=========================================
// EMERGENCY CONTACTS
//=========================================

    @PostMapping("/contacts/{employeeId}")
    public ResponseEntity<?> createContacts(
            @PathVariable String employeeId,
            @RequestBody EmployeeContacts contacts) {

        return ResponseEntity.ok(
                service.createContacts(employeeId, contacts));
    }


    @GetMapping("/contacts/{employeeId}")
    public ResponseEntity<?> getContacts(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getContacts(employeeId));
    }


    @PutMapping("/contacts/{id}")
    public ResponseEntity<?> updateContacts(
            @PathVariable String id,
            @RequestBody EmployeeContacts contacts) {

        return ResponseEntity.ok(
                service.updateContacts(id, contacts));
    }


    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> deleteContacts(
            @PathVariable String id) {

        return ResponseEntity.ok(
                service.deleteContacts(id));
    }


    //=========================================
// DESIGNATION
//=========================================

    @PostMapping("/designation/{employeeId}")
    public ResponseEntity<?> createDesignation(
            @PathVariable String employeeId,
            @RequestBody EmployeeDesignation designation) {

        return ResponseEntity.ok(
                service.createDesignation(employeeId, designation));
    }


    @GetMapping("/designation/{employeeId}")
    public ResponseEntity<?> getDesignation(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getDesignation(employeeId));
    }


    @PutMapping("/designation/{id}")
    public ResponseEntity<?> updateDesignation(
            @PathVariable String id,
            @RequestBody EmployeeDesignation designation) {

        return ResponseEntity.ok(
                service.updateDesignation(id, designation));
    }


    @DeleteMapping("/designation/{id}")
    public ResponseEntity<?> deleteDesignation(
            @PathVariable String id) {

        return ResponseEntity.ok(
                service.deleteDesignation(id));
    }


    //=========================================
// EXIT MANAGEMENT
//=========================================

    @PostMapping("/exit-management/{employeeId}")
    public ResponseEntity<?> createExitManagement(
            @PathVariable String employeeId,
            @RequestBody EmployeeExitManagement exitManagement) {

        return ResponseEntity.ok(
                service.createExitManagement(employeeId, exitManagement));
    }


    @GetMapping("/exit-management/{employeeId}")
    public ResponseEntity<?> getExitManagement(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getExitManagement(employeeId));
    }


    @PutMapping("/exit-management/{employeeId}")
    public ResponseEntity<?> updateExitManagement(
            @PathVariable String employeeId,
            @RequestBody EmployeeExitManagement exitManagement) {

        return ResponseEntity.ok(
                service.updateExitManagement(employeeId, exitManagement));
    }


    @DeleteMapping("/exit-management/{employeeId}")
    public ResponseEntity<?> deleteExitManagement(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteExitManagement(employeeId));
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
