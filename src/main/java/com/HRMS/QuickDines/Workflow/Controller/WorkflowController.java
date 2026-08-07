package com.HRMS.QuickDines.Workflow.Controller;

import com.HRMS.QuickDines.Workflow.Entity.*;
import com.HRMS.QuickDines.Workflow.Service.WorkflowService;
import com.HRMS.QuickDines.Workflow.model.*;
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
    // 1. WORKFLOW MANAGEMENT
    // =========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createWorkflow(
            @RequestBody ApprovalWorkflow workflow) {

        return ResponseEntity.ok(
                workflowService.createWorkflow(workflow)
        );
    }


    @GetMapping("/all")
    public ResponseEntity<List<ApprovalWorkflow>>
    getAllWorkflows() {

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
                workflowService.updateWorkflow(
                        id,
                        workflow
                )
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkflow(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.deleteWorkflow(id)
        );
    }


    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateWorkflow(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.activateWorkflow(id)
        );
    }


    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateWorkflow(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.deactivateWorkflow(id)
        );
    }


    @GetMapping("/active")
    public ResponseEntity<List<ApprovalWorkflow>>
    getActiveWorkflows() {

        return ResponseEntity.ok(
                workflowService.getActiveWorkflows()
        );
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<ApprovalWorkflow>>
    getWorkflowsByType(
            @PathVariable String type) {

        return ResponseEntity.ok(
                workflowService.getWorkflowsByType(type)
        );
    }


    // =========================================================
    // 2. WORKFLOW LEVEL MANAGEMENT
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
    public ResponseEntity<List<ApprovalWorkflowLevel>>
    getWorkflowLevels(
            @PathVariable Long workflowId) {

        return ResponseEntity.ok(
                workflowService.getWorkflowLevels(
                        workflowId
                )
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
                workflowService.updateWorkflowLevel(
                        id,
                        level
                )
        );
    }


    @DeleteMapping("/level/delete/{id}")
    public ResponseEntity<?> deleteWorkflowLevel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.deleteWorkflowLevel(id)
        );
    }


    @PutMapping("/level/{levelId}/activate")
    public ResponseEntity<?> activateLevel(
            @PathVariable Long levelId) {

        return ResponseEntity.ok(
                workflowService.activateLevel(
                        levelId
                )
        );
    }


    @PutMapping("/level/{levelId}/deactivate")
    public ResponseEntity<?> deactivateLevel(
            @PathVariable Long levelId) {

        return ResponseEntity.ok(
                workflowService.deactivateLevel(
                        levelId
                )
        );
    }


    // =========================================================
    // 3. APPROVAL REQUEST MANAGEMENT
    // =========================================================

    @PostMapping("/request/create")
    public ResponseEntity<?> createApprovalRequest(
            @RequestBody ApprovalRequest request) {

        return ResponseEntity.ok(
                workflowService.createApprovalRequest(
                        request
                )
        );
    }


    @GetMapping("/request/all")
    public ResponseEntity<List<ApprovalRequest>>
    getAllApprovalRequests() {

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
    public ResponseEntity<List<ApprovalRequest>>
    getRequestsByEmployee(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                workflowService.getRequestsByEmployee(
                        employeeId
                )
        );
    }


    @GetMapping("/request/pending")
    public ResponseEntity<List<ApprovalRequest>>
    getPendingRequests() {

        return ResponseEntity.ok(
                workflowService.getPendingRequests()
        );
    }


    @GetMapping("/request/status/{status}")
    public ResponseEntity<List<ApprovalRequest>>
    getRequestsByStatus(
            @PathVariable ApprovalRequestStatus status) {

        return ResponseEntity.ok(
                workflowService.getRequestsByStatus(
                        status
                )
        );
    }


    @GetMapping("/request/type/{type}")
    public ResponseEntity<List<ApprovalRequest>>
    getRequestsByType(
            @PathVariable String type) {

        return ResponseEntity.ok(
                workflowService.getRequestsByType(type)
        );
    }


    // =========================================================
    // 4. APPROVAL ACTIONS
    // =========================================================

    @PutMapping(
            "/request/{requestId}/{employeeId}/approve"
    )
    public ResponseEntity<?> approveRequest(
            @PathVariable Long requestId,
            @PathVariable String employeeId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                workflowService.approveRequest(
                        requestId,
                        employeeId,
                        remarks
                )
        );
    }


    @PutMapping(
            "/request/{requestId}/{employeeId}/reject"
    )
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long requestId,
            @PathVariable String employeeId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                workflowService.rejectRequest(
                        requestId,
                        employeeId,
                        remarks
                )
        );
    }


    @PutMapping(
            "/request/{requestId}/{employeeId}/return"
    )
    public ResponseEntity<?> returnRequest(
            @PathVariable Long requestId,
            @PathVariable String employeeId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                workflowService.returnRequest(
                        requestId,
                        employeeId,
                        remarks
                )
        );
    }


    @PutMapping(
            "/request/{requestId}/{employeeId}/cancel"
    )
    public ResponseEntity<?> cancelRequest(
            @PathVariable Long requestId,
            @PathVariable String employeeId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                workflowService.cancelRequest(
                        requestId,
                        employeeId,
                        remarks
                )
        );
    }


    // =========================================================
    // 5. APPROVAL HISTORY
    // =========================================================

    @GetMapping("/history/all")
    public ResponseEntity<List<ApprovalHistory>>
    getAllApprovalHistory() {

        return ResponseEntity.ok(
                workflowService.getAllApprovalHistory()
        );
    }


    @GetMapping("/request/{id}/history")
    public ResponseEntity<List<ApprovalHistory>>
    getHistoryByRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workflowService.getApprovalHistory(id)
        );
    }


    @GetMapping(
            "/history/approver/{employeeId}"
    )
    public ResponseEntity<List<ApprovalHistory>>
    getHistoryByApprover(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                workflowService.getHistoryByApprover(
                        employeeId
                )
        );
    }
}