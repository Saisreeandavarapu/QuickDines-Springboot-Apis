package com.HRMS.QuickDines.Leave.Controller;

import com.HRMS.QuickDines.Leave.Service.LeaveService;
import com.HRMS.QuickDines.Leave.model.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;

    //=========================================================
    // LEAVE TYPES
    //=========================================================

    @PostMapping("/type")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_CREATE')")
    public ResponseEntity<?> createLeaveType(
            @RequestBody LeaveType leaveType) {

        return ResponseEntity.ok(
                service.createLeaveType(leaveType));
    }

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_VIEW')")
    public ResponseEntity<?> getAllLeaveTypes() {

        return ResponseEntity.ok(
                service.getAllLeaveTypes());
    }

    @GetMapping("/type/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_VIEW')")
    public ResponseEntity<?> getLeaveType(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveType(id));
    }

    @PutMapping("/type/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_UPDATE')")
    public ResponseEntity<?> updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveType leaveType) {

        return ResponseEntity.ok(
                service.updateLeaveType(id, leaveType));
    }

    @DeleteMapping("/type/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_DELETE')")
    public ResponseEntity<?> deleteLeaveType(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveType(id));
    }


    //=========================================================
    // LEAVE REQUESTS
    //=========================================================

    @PostMapping("/request/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_REQUEST_CREATE')")
    public ResponseEntity<?> applyLeave(
            @PathVariable String employeeId,
            @RequestBody LeaveRequest leaveRequest) {

        return ResponseEntity.ok(
                service.applyLeave(employeeId, leaveRequest));
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAuthority('LEAVE_REQUEST_VIEW')")
    public ResponseEntity<?> getAllRequests() {

        return ResponseEntity.ok(
                service.getAllRequests());
    }

    @GetMapping("/request/{id}")
    @PreAuthorize("hasAuthority('LEAVE_REQUEST_VIEW')")
    public ResponseEntity<?> getLeaveRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveRequest(id));
    }

    @PutMapping("/request/{id}")
    @PreAuthorize("hasAuthority('LEAVE_REQUEST_UPDATE')")
    public ResponseEntity<?> updateLeaveRequest(
            @PathVariable Long id,
            @RequestBody LeaveRequest leaveRequest) {

        return ResponseEntity.ok(
                service.updateLeaveRequest(id, leaveRequest));
    }

    @DeleteMapping("/request/{id}")
    @PreAuthorize("hasAuthority('LEAVE_REQUEST_DELETE')")
    public ResponseEntity<?> deleteLeaveRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveRequest(id));
    }


    //=========================================================
    // LEAVE BALANCES
    //=========================================================

    @PostMapping("/balance/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_BALANCE_CREATE')")
    public ResponseEntity<?> createLeaveBalance(
            @PathVariable String employeeId,
            @RequestBody LeaveBalance leaveBalance) {

        return ResponseEntity.ok(
                service.createLeaveBalance(employeeId, leaveBalance));
    }

    @GetMapping("/balance/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_BALANCE_VIEW')")
    public ResponseEntity<?> getLeaveBalance(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getLeaveBalance(employeeId));
    }

    @PutMapping("/balance/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_BALANCE_UPDATE')")
    public ResponseEntity<?> updateLeaveBalance(
            @PathVariable String employeeId,
            @RequestBody LeaveBalance leaveBalance) {

        return ResponseEntity.ok(
                service.updateLeaveBalance(employeeId, leaveBalance));
    }


    //=========================================================
    // LEAVE APPROVALS
    //=========================================================

    @PostMapping("/approve/{leaveRequestId}/{approvedById}")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<?> approveLeave(
            @PathVariable Long leaveRequestId,
            @PathVariable String approvedById) {

        return ResponseEntity.ok(
                service.approveLeave(
                        leaveRequestId,
                        approvedById));
    }

    @PostMapping("/reject/{leaveRequestId}/{approvedById}")
    @PreAuthorize("hasAuthority('LEAVE_REJECT')")
    public ResponseEntity<?> rejectLeave(
            @PathVariable Long leaveRequestId,
            @RequestParam String reason,
            @PathVariable String approvedById) {

        return ResponseEntity.ok(
                service.rejectLeave(
                        leaveRequestId,
                        reason,
                        approvedById));
    }

    @GetMapping("/approvals")
    @PreAuthorize("hasAuthority('LEAVE_APPROVAL_VIEW')")
    public ResponseEntity<?> getApprovals() {

        return ResponseEntity.ok(
                service.getApprovals());
    }

    @GetMapping("/approval/{id}")
    @PreAuthorize("hasAuthority('LEAVE_APPROVAL_VIEW')")
    public ResponseEntity<?> getApproval(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getApproval(id));
    }


    //=========================================================
    // REPORTS
    //=========================================================

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_REPORT_VIEW')")
    public ResponseEntity<?> getEmployeeLeaves(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeLeaves(employeeId));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('LEAVE_REPORT_VIEW')")
    public ResponseEntity<?> getPendingLeaves() {

        return ResponseEntity.ok(
                service.getPendingLeaves());
    }

    @GetMapping("/approved")
    @PreAuthorize("hasAuthority('LEAVE_REPORT_VIEW')")
    public ResponseEntity<?> getApprovedLeaves() {

        return ResponseEntity.ok(
                service.getApprovedLeaves());
    }

    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('LEAVE_REPORT_VIEW')")
    public ResponseEntity<?> getRejectedLeaves() {

        return ResponseEntity.ok(
                service.getRejectedLeaves());
    }


    //=========================================================
    // LEAVE POLICIES
    //=========================================================

    @PostMapping("/policy/{companyId}")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_CREATE')")
    public ResponseEntity<?> createLeavePolicy(
            @PathVariable Long companyId,
            @RequestBody LeavePolicy leavePolicy) {

        return ResponseEntity.ok(
                service.createLeavePolicy(companyId, leavePolicy));
    }

    @GetMapping("/policies")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_VIEW')")
    public ResponseEntity<?> getLeavePolicies() {

        return ResponseEntity.ok(
                service.getLeavePolicies());
    }

    @GetMapping("/policy/{id}")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_VIEW')")
    public ResponseEntity<?> getLeavePolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeavePolicy(id));
    }

    @PutMapping("/policy/{id}")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_UPDATE')")
    public ResponseEntity<?> updateLeavePolicy(
            @PathVariable Long id,
            @RequestBody LeavePolicy leavePolicy) {

        return ResponseEntity.ok(
                service.updateLeavePolicy(id, leavePolicy));
    }

    @DeleteMapping("/policy/{id}")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_DELETE')")
    public ResponseEntity<?> deleteLeavePolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeavePolicy(id));
    }


    //=========================================================
    // LEAVE ENCASHMENT
    //=========================================================

    @PostMapping("/encashment/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_CREATE')")
    public ResponseEntity<?> createLeaveEncashment(
            @PathVariable String employeeId,
            @RequestBody LeaveEncashment leaveEncashment) {

        return ResponseEntity.ok(
                service.createLeaveEncashment(
                        employeeId,
                        leaveEncashment));
    }

    @GetMapping("/encashments")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_VIEW')")
    public ResponseEntity<?> getLeaveEncashments() {

        return ResponseEntity.ok(
                service.getLeaveEncashments());
    }

    @GetMapping("/encashment/{id}")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_VIEW')")
    public ResponseEntity<?> getLeaveEncashment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveEncashment(id));
    }

    @PutMapping("/encashment/{id}")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_UPDATE')")
    public ResponseEntity<?> updateLeaveEncashment(
            @PathVariable Long id,
            @RequestBody LeaveEncashment leaveEncashment) {

        return ResponseEntity.ok(
                service.updateLeaveEncashment(
                        id,
                        leaveEncashment));
    }

    @DeleteMapping("/encashment/{id}")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_DELETE')")
    public ResponseEntity<?> deleteLeaveEncashment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveEncashment(id));
    }


    //=========================================================
    // LEAVE CANCELLATIONS
    //=========================================================

    @PostMapping("/cancellation/{leaveRequestId}/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_CREATE')")
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
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_VIEW')")
    public ResponseEntity<?> getLeaveCancellations() {

        return ResponseEntity.ok(
                service.getLeaveCancellations());
    }

    @GetMapping("/cancellation/{id}")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_VIEW')")
    public ResponseEntity<?> getLeaveCancellation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getLeaveCancellation(id));
    }

    @PutMapping("/cancellation/{id}")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_UPDATE')")
    public ResponseEntity<?> updateLeaveCancellation(
            @PathVariable Long id,
            @RequestBody LeaveCancellation leaveCancellation) {

        return ResponseEntity.ok(
                service.updateLeaveCancellation(
                        id,
                        leaveCancellation));
    }

    @DeleteMapping("/cancellation/{id}")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_DELETE')")
    public ResponseEntity<?> deleteLeaveCancellation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteLeaveCancellation(id));
    }


    //=========================================================
    // LEAVE ENCASHMENT REPORTS
    //=========================================================

    @GetMapping("/encashment/pending")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_REPORT_VIEW')")
    public ResponseEntity<?> getPendingEncashments() {

        return ResponseEntity.ok(
                service.getPendingEncashments());
    }

    @GetMapping("/encashment/approved")
    @PreAuthorize("hasAuthority('LEAVE_ENCASHMENT_REPORT_VIEW')")
    public ResponseEntity<?> getApprovedEncashments() {

        return ResponseEntity.ok(
                service.getApprovedEncashments());
    }


    //=========================================================
    // LEAVE CANCELLATION REPORTS
    //=========================================================

    @GetMapping("/cancellation/pending")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_REPORT_VIEW')")
    public ResponseEntity<?> getPendingCancellations() {

        return ResponseEntity.ok(
                service.getPendingCancellations());
    }

    @GetMapping("/cancellation/approved")
    @PreAuthorize("hasAuthority('LEAVE_CANCELLATION_REPORT_VIEW')")
    public ResponseEntity<?> getApprovedCancellations() {

        return ResponseEntity.ok(
                service.getApprovedCancellations());
    }


    //=========================================================
    // LEAVE POLICY REPORT
    //=========================================================

    @GetMapping("/policies/active")
    @PreAuthorize("hasAuthority('LEAVE_POLICY_REPORT_VIEW')")
    public ResponseEntity<?> getActiveLeavePolicies() {

        return ResponseEntity.ok(
                service.getActiveLeavePolicies());
    }
    // =========================================================
// LEAVE TYPE-WISE EMPLOYEE FILTER
// =========================================================

    @GetMapping("/leave-type/{leaveTypeId}")
    @PreAuthorize("hasAuthority('LEAVE_READ')")
    public ResponseEntity<?> getEmployeesByLeaveType(
            @PathVariable Long leaveTypeId) {

        return ResponseEntity.ok(
                service.getEmployeesByLeaveType(
                        leaveTypeId));
    }
}