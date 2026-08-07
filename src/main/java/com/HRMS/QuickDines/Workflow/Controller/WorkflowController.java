package com.HRMS.QuickDines.Workflow.Controller;

import com.HRMS.QuickDines.Workflow.Entity.*;
import com.HRMS.QuickDines.Workflow.Service.WorkflowService;
import com.HRMS.QuickDines.Workflow.model.ApprovalHistory;
import com.HRMS.QuickDines.Workflow.model.ApprovalRequest;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflow;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflowLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;


    // =========================================================
    // APPROVAL WORKFLOW APIs
    // =========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createWorkflow(
            @RequestBody ApprovalWorkflow workflow) {

        return ResponseEntity.ok(
                workflowService.createWorkflow(workflow)
        );
    }


    @GetMapping("/all")
    public ResponseEntity<List<ApprovalWorkflow>> getAllWorkflows() {

        return ResponseEntity.ok(
                workflowService.getAllWorkflows()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkflowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.getWorkflowById(id)
        );
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateWorkflow(
            @PathVariable Long id,
            @RequestBody ApprovalWorkflow workflow) {

        return ResponseEntity.ok(
                workflowService.updateWorkflow(id, workflow)
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkflow(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.deleteWorkflow(id)
        );
    }


    // =========================================================
    // APPROVAL WORKFLOW LEVEL APIs
    // =========================================================

    @PostMapping("/level/create/{workflowId}")
    public ResponseEntity<?> createWorkflowLevel(
            @PathVariable Long workflowId,
            @RequestBody ApprovalWorkflowLevel level) {

        return ResponseEntity.ok(
                workflowService.createWorkflowLevel(
                        workflowId,
                        level
                )
        );
    }


    @GetMapping("/level/all/{workflowId}")
    public ResponseEntity<List<ApprovalWorkflowLevel>> getWorkflowLevels(
            @PathVariable Long workflowId) {

        return ResponseEntity.ok(
                workflowService.getWorkflowLevels(workflowId)
        );
    }


    @GetMapping("/level/{id}")
    public ResponseEntity<?> getWorkflowLevelById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.getWorkflowLevelById(id)
        );
    }


    @PutMapping("/level/update/{id}")
    public ResponseEntity<?> updateWorkflowLevel(
            @PathVariable Long id,
            @RequestBody ApprovalWorkflowLevel level) {

        return ResponseEntity.ok(
                workflowService.updateWorkflowLevel(id, level)
        );
    }


    @DeleteMapping("/level/delete/{id}")
    public ResponseEntity<?> deleteWorkflowLevel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.deleteWorkflowLevel(id)
        );
    }


    // =========================================================
    // APPROVAL REQUEST APIs
    // =========================================================

    @PostMapping("/request/create")
    public ResponseEntity<?> createApprovalRequest(
            @RequestBody ApprovalRequest request) {

        return ResponseEntity.ok(
                workflowService.createApprovalRequest(request)
        );
    }


    @GetMapping("/request/all")
    public ResponseEntity<List<ApprovalRequest>> getAllApprovalRequests() {

        return ResponseEntity.ok(
                workflowService.getAllApprovalRequests()
        );
    }


    @GetMapping("/request/{id}")
    public ResponseEntity<?> getApprovalRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.getApprovalRequestById(id)
        );
    }


    @GetMapping("/request/employee/{employeeId}")
    public ResponseEntity<List<ApprovalRequest>> getRequestsByEmployee(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                workflowService.getRequestsByEmployee(employeeId)
        );
    }


    // =========================================================
    // APPROVAL ACTION APIs
    // =========================================================

    @PutMapping("/request/{requestId}/{EmployeeId}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String remarks,
            @PathVariable String EmployeeId) {

        return ResponseEntity.ok(
                workflowService.approveRequest(requestId, remarks,EmployeeId)
        );
    }


    @PutMapping("/request/{requestId}/{EmployeeId}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String remarks,
    @PathVariable String EmployeeId){

        return ResponseEntity.ok(
                workflowService.rejectRequest(requestId, remarks,EmployeeId)
        );
    }


    @PutMapping("/request/{requestId}/{EmployeeId}/cancel")
    public ResponseEntity<?> cancelRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String remarks,
            @PathVariable String EmployeeId) {

        return ResponseEntity.ok(
                workflowService.cancelRequest(
                        requestId,
                        remarks,EmployeeId
                )
        );
    }


    // =========================================================
    // APPROVAL HISTORY APIs
    // =========================================================

    @GetMapping("/history/all")
    public ResponseEntity<List<ApprovalHistory>> getAllApprovalHistory() {

        return ResponseEntity.ok(
                workflowService.getAllApprovalHistory()
        );
    }


    @GetMapping("/history/request/{requestId}")
    public ResponseEntity<List<ApprovalHistory>> getHistoryByRequest(
            @PathVariable Long requestId) {

        return ResponseEntity.ok(
                workflowService.getApprovalHistory(requestId)
        );
    }


    @GetMapping("/history/approver/{employeeId}")
    public ResponseEntity<List<ApprovalHistory>> getHistoryByApprover(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(workflowService.getHistoryByApprover(employeeId)
        );
    }
}