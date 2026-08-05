package com.HRMS.QuickDines.Leave.Controller;

import com.HRMS.QuickDines.Leave.Service.LeaveService;
import com.HRMS.QuickDines.Leave.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;


//==================================
// LEAVE TYPES
//==================================

    @PostMapping("/type")
    public ResponseEntity<?> createLeaveType(
            @RequestBody LeaveType leaveType){

        return ResponseEntity.ok(
                service.createLeaveType(leaveType));
    }


    @GetMapping("/types")
    public ResponseEntity<?> getAllLeaveTypes(){

        return ResponseEntity.ok(
                service.getAllLeaveTypes());
    }


    @GetMapping("/type/{id}")
    public ResponseEntity<?> getLeaveType(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getLeaveType(id));
    }


    @PutMapping("/type/{id}")
    public ResponseEntity<?> updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveType leaveType){

        return ResponseEntity.ok(
                service.updateLeaveType(id, leaveType));
    }


    @DeleteMapping("/type/{id}")
    public ResponseEntity<?> deleteLeaveType(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deleteLeaveType(id));
    }



//==================================
// LEAVE REQUESTS
//==================================

    @PostMapping("/request/{employeeId}")
    public ResponseEntity<?> applyLeave(
            @PathVariable String employeeId,
            @RequestBody LeaveRequest leaveRequest) {

        return ResponseEntity.ok(service.applyLeave(employeeId, leaveRequest));
    }


    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests() {

        return ResponseEntity.ok(service.getAllRequests());
    }


    @GetMapping("/request/{id}")
    public ResponseEntity<?> getLeaveRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getLeaveRequest(id));
    }


    @PutMapping("/request/{id}")
    public ResponseEntity<?> updateLeaveRequest(
            @PathVariable Long id,
            @RequestBody LeaveRequest leaveRequest) {

        return ResponseEntity.ok(service.updateLeaveRequest(id, leaveRequest));
    }


    @DeleteMapping("/request/{id}")
    public ResponseEntity<?> deleteLeaveRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.deleteLeaveRequest(id));
    }



//==================================
// LEAVE BALANCES
//==================================

    @PostMapping("/balance/{employeeId}")
    public ResponseEntity<?> createLeaveBalance(
            @PathVariable String employeeId,
            @RequestBody LeaveBalance leaveBalance) {

        return ResponseEntity.ok(service.createLeaveBalance(employeeId, leaveBalance));
    }


    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> getLeaveBalance(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getLeaveBalance(employeeId));
    }


    @PutMapping("/balance/{employeeId}")
    public ResponseEntity<?> updateLeaveBalance(
            @PathVariable String employeeId,
            @RequestBody LeaveBalance leaveBalance) {

        return ResponseEntity.ok(service.updateLeaveBalance(employeeId, leaveBalance));
    }



//==================================
// LEAVE APPROVALS
//==================================

    @PostMapping("/approve/{leaveRequestId}/{approvedById}")
    public ResponseEntity<?> approveLeave(
            @PathVariable Long leaveRequestId,
            @PathVariable String approvedById){

        return ResponseEntity.ok(service.approveLeave(leaveRequestId, approvedById));
    }


    @PostMapping("/reject/{leaveRequestId}/{approvedById}")
    public ResponseEntity<?> rejectLeave(@PathVariable Long leaveRequestId,
            @RequestParam String reason,@PathVariable String approvedById){

        return ResponseEntity.ok(service.rejectLeave(leaveRequestId, reason,approvedById));
    }


    @GetMapping("/approvals")
    public ResponseEntity<?> getApprovals(){

        return ResponseEntity.ok(service.getApprovals());
    }


    @GetMapping("/approval/{id}")
    public ResponseEntity<?> getApproval(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getApproval(id));
    }



///==================================
// REPORTS
//==================================

@GetMapping("/employee/{employeeId}")
public ResponseEntity<?> getEmployeeLeaves(
        @PathVariable String employeeId){

    return ResponseEntity.ok(service.getEmployeeLeaves(employeeId));
}


    @GetMapping("/pending")
    public ResponseEntity<?> getPendingLeaves(){

        return ResponseEntity.ok(service.getPendingLeaves());
    }


    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedLeaves(){

        return ResponseEntity.ok(service.getApprovedLeaves());
    }


    @GetMapping("/rejected")
    public ResponseEntity<?> getRejectedLeaves(){

        return ResponseEntity.ok(service.getRejectedLeaves());
    }

//=========================================================
// LEAVE POLICIES
//=========================================================

    @PostMapping("/policy/{companyId}")
    public ResponseEntity<?> createLeavePolicy(
            @PathVariable Long companyId,
            @RequestBody LeavePolicy leavePolicy) {

        return ResponseEntity.ok(
                service.createLeavePolicy(companyId, leavePolicy));
    }


    @GetMapping("/policies")
    public ResponseEntity<?> getLeavePolicies() {

        return ResponseEntity.ok(
                service.getLeavePolicies());
    }


    @GetMapping("/policy/{id}")
    public ResponseEntity<?> getLeavePolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeavePolicy(id));
    }


    @PutMapping("/policy/{id}")
    public ResponseEntity<?> updateLeavePolicy(
            @PathVariable Long id,
            @RequestBody LeavePolicy leavePolicy) {

        return ResponseEntity.ok(
                service.updateLeavePolicy(id, leavePolicy));
    }


    @DeleteMapping("/policy/{id}")
    public ResponseEntity<?> deleteLeavePolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeavePolicy(id));
    }


//=========================================================
// LEAVE ENCASHMENT
//=========================================================

    @PostMapping("/encashment/{employeeId}")
    public ResponseEntity<?> createLeaveEncashment(
            @PathVariable String employeeId,
            @RequestBody LeaveEncashment leaveEncashment) {

        return ResponseEntity.ok(
                service.createLeaveEncashment(employeeId, leaveEncashment));
    }


    @GetMapping("/encashments")
    public ResponseEntity<?> getLeaveEncashments() {

        return ResponseEntity.ok(
                service.getLeaveEncashments());
    }


    @GetMapping("/encashment/{id}")
    public ResponseEntity<?> getLeaveEncashment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveEncashment(id));
    }


    @PutMapping("/encashment/{id}")
    public ResponseEntity<?> updateLeaveEncashment(
            @PathVariable Long id,
            @RequestBody LeaveEncashment leaveEncashment) {

        return ResponseEntity.ok(
                service.updateLeaveEncashment(id, leaveEncashment));
    }


    @DeleteMapping("/encashment/{id}")
    public ResponseEntity<?> deleteLeaveEncashment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveEncashment(id));
    }


//=========================================================
// LEAVE CANCELLATIONS
//=========================================================

    @PostMapping("/cancellation/{leaveRequestId}/{employeeId}")
    public ResponseEntity<?> createLeaveCancellation(
            @PathVariable Long leaveRequestId,
            @PathVariable String employeeId,
            @RequestBody LeaveCancellation leaveCancellation) {

        return ResponseEntity.ok(
                service.createLeaveCancellation(
                        leaveRequestId,
                        employeeId,
                        leaveCancellation));
    }


    @GetMapping("/cancellations")
    public ResponseEntity<?> getLeaveCancellations() {

        return ResponseEntity.ok(
                service.getLeaveCancellations());
    }


    @GetMapping("/cancellation/{id}")
    public ResponseEntity<?> getLeaveCancellation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveCancellation(id));
    }


    @PutMapping("/cancellation/{id}")
    public ResponseEntity<?> updateLeaveCancellation(
            @PathVariable Long id,
            @RequestBody LeaveCancellation leaveCancellation) {

        return ResponseEntity.ok(
                service.updateLeaveCancellation(id, leaveCancellation));
    }


    @DeleteMapping("/cancellation/{id}")
    public ResponseEntity<?> deleteLeaveCancellation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveCancellation(id));
    }


//=========================================================
// LEAVE ENCASHMENT REPORTS
//=========================================================

    @GetMapping("/encashment/pending")
    public ResponseEntity<?> getPendingEncashments() {

        return ResponseEntity.ok(
                service.getPendingEncashments());
    }


    @GetMapping("/encashment/approved")
    public ResponseEntity<?> getApprovedEncashments() {

        return ResponseEntity.ok(
                service.getApprovedEncashments());
    }


//=========================================================
// LEAVE CANCELLATION REPORTS
//=========================================================

    @GetMapping("/cancellation/pending")
    public ResponseEntity<?> getPendingCancellations() {

        return ResponseEntity.ok(
                service.getPendingCancellations());
    }


    @GetMapping("/cancellation/approved")
    public ResponseEntity<?> getApprovedCancellations() {

        return ResponseEntity.ok(
                service.getApprovedCancellations());
    }


//=========================================================
// LEAVE POLICY REPORT
//=========================================================

    @GetMapping("/policies/active")
    public ResponseEntity<?> getActiveLeavePolicies() {

        return ResponseEntity.ok(
                service.getActiveLeavePolicies());
    }


}
