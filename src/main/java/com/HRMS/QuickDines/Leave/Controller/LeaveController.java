package com.HRMS.QuickDines.Leave.Controller;

import com.HRMS.QuickDines.Leave.Service.LeaveService;
import com.HRMS.QuickDines.Leave.model.LeaveBalance;
import com.HRMS.QuickDines.Leave.model.LeaveRequest;
import com.HRMS.QuickDines.Leave.model.LeaveType;
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

}
