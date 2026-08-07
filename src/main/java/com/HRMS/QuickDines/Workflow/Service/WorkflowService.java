package com.HRMS.QuickDines.Workflow.Service;

import com.HRMS.QuickDines.Auth.repo.RoleRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Workflow.Entity.*;
import com.HRMS.QuickDines.Workflow.model.ApprovalHistory;
import com.HRMS.QuickDines.Workflow.model.ApprovalRequest;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflow;
import com.HRMS.QuickDines.Workflow.model.ApprovalWorkflowLevel;
import com.HRMS.QuickDines.Workflow.repo.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowService {

    private final ApprovalWorkflowRepository workflowRepository;
    private final ApprovalWorkflowLevelRepository levelRepository;
    private final ApprovalRequestRepository requestRepository;
    private final ApprovalHistoryRepository historyRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;


    // =========================================================
    // 1. CREATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow createWorkflow(ApprovalWorkflow workflow) {

        if (workflow.getWorkflowName() == null ||
                workflow.getWorkflowName().isBlank()) {

            throw new RuntimeException("Workflow name is required");
        }

        if (workflow.getWorkflowType() == null ||
                workflow.getWorkflowType().isBlank()) {

            throw new RuntimeException("Workflow type is required");
        }

        if (workflow.getTotalLevels() == null ||
                workflow.getTotalLevels() <= 0) {

            throw new RuntimeException("Total levels must be greater than zero");
        }

        if (workflow.getAutoApprove() == null) {
            workflow.setAutoApprove(false);
        }

        if (workflow.getStatus() == null) {
            workflow.setStatus(WorkflowStatus.ACTIVE);
        }

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 2. GET ALL WORKFLOWS
    // =========================================================

    public List<ApprovalWorkflow> getAllWorkflows() {

        return workflowRepository.findAll();
    }


    // =========================================================
    // 3. GET WORKFLOW BY ID
    // =========================================================

    public ApprovalWorkflow getWorkflowById(Long workflowId) {

        return workflowRepository.findById(workflowId).orElseThrow(() ->
                        new RuntimeException("Workflow not found"));
    }


    // =========================================================
    // 4. UPDATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow updateWorkflow(
            Long workflowId,
            ApprovalWorkflow updatedWorkflow) {

        ApprovalWorkflow workflow =
                workflowRepository.findById(workflowId).orElseThrow(() ->
                                new RuntimeException("Workflow not found"));

        if (updatedWorkflow.getWorkflowName() != null) {
            workflow.setWorkflowName(
                    updatedWorkflow.getWorkflowName());
        }

        if (updatedWorkflow.getWorkflowType() != null) {
            workflow.setWorkflowType(
                    updatedWorkflow.getWorkflowType());
        }

        if (updatedWorkflow.getTotalLevels() != null) {
            if (updatedWorkflow.getTotalLevels() <= 0) {
                throw new RuntimeException(
                        "Total levels must be greater than zero");
            }

            workflow.setTotalLevels(
                    updatedWorkflow.getTotalLevels());
        }

        if (updatedWorkflow.getAutoApprove() != null) {
            workflow.setAutoApprove(
                    updatedWorkflow.getAutoApprove());
        }

        if (updatedWorkflow.getStatus() != null) {
            workflow.setStatus(
                    updatedWorkflow.getStatus());
        }

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 5. ACTIVATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow activateWorkflow(Long workflowId) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        workflow.setStatus(WorkflowStatus.ACTIVE);

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 6. DEACTIVATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow deactivateWorkflow(Long workflowId) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        workflow.setStatus(WorkflowStatus.INACTIVE);

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 7. DELETE WORKFLOW
    // =========================================================

    public String deleteWorkflow(Long workflowId) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        workflowRepository.delete(workflow);

        return "Workflow deleted successfully";
    }


    // =========================================================
    // 8. ADD WORKFLOW LEVEL
    // =========================================================

    public ApprovalWorkflowLevel addWorkflowLevel(
            Long workflowId,
            ApprovalWorkflowLevel level) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        if (workflow.getStatus() == WorkflowStatus.INACTIVE) {
            throw new RuntimeException(
                    "Cannot add level to inactive workflow");
        }

        if (level.getLevelNumber() == null ||
                level.getLevelNumber() <= 0) {

            throw new RuntimeException(
                    "Level number must be greater than zero");
        }

        if (level.getLevelName() == null ||
                level.getLevelName().isBlank()) {

            throw new RuntimeException(
                    "Level name is required");
        }

        if (level.getApproverType() == null) {

            throw new RuntimeException(
                    "Approver type is required");
        }

        if (levelRepository
                .existsByWorkflowIdAndLevelNumber(
                        workflowId,
                        level.getLevelNumber())) {

            throw new RuntimeException(
                    "This approval level already exists");
        }

        level.setWorkflow(workflow);

        if (level.getRequired() == null) {
            level.setRequired(true);
        }

        if (level.getStatus() == null) {
            level.setStatus(WorkflowStatus.ACTIVE);
        }

        validateApprover(level);

        return levelRepository.save(level);
    }
    public Optional<ApprovalWorkflowLevel> getWorkflowLevelById(Long id)
    {
        return levelRepository.findById(id);
    }

    // =========================================================
    // 9. VALIDATE APPROVER
    // =========================================================

    private void validateApprover(ApprovalWorkflowLevel level) {

        if (level.getApproverRole() == null) {
            throw new RuntimeException(
                    "Approver role is required");
        }

        if (level.getApproverRole().getId() == null) {
            throw new RuntimeException(
                    "Approver role ID is required");
        }

        roleRepository.findById(level.getApproverRole().getId()).orElseThrow(() ->
                        new RuntimeException(
                                "Approver role not found"));
    }


    // =========================================================
    // 10. GET WORKFLOW LEVELS
    // =========================================================

    public List<ApprovalWorkflowLevel> getWorkflowLevels(
            Long workflowId) {

        getWorkflowById(workflowId);

        return levelRepository
                .findByWorkflowIdOrderByLevelNumberAsc(
                        workflowId);
    }


    // =========================================================
    // 11. UPDATE WORKFLOW LEVEL
    // =========================================================

    public ApprovalWorkflowLevel updateWorkflowLevel(
            Long levelId,
            ApprovalWorkflowLevel updatedLevel) {

        ApprovalWorkflowLevel level =
                levelRepository.findById(levelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow level not found"));

        if (updatedLevel.getLevelName() != null) {
            level.setLevelName(
                    updatedLevel.getLevelName());
        }

        if (updatedLevel.getApproverType() != null) {
            level.setApproverType(
                    updatedLevel.getApproverType());

            validateApprover(level);
        }

        if (updatedLevel.getApproverRole().getId() != null) {
            level.setApproverRole(updatedLevel.getApproverRole());
        }

        if (updatedLevel.getApproverEmployee().getEmployeeId() != null) {
            level.setApproverEmployee(
                    updatedLevel.getApproverEmployee());
        }

        if (updatedLevel.getRequired() != null) {
            level.setRequired(
                    updatedLevel.getRequired());
        }

        if (updatedLevel.getStatus() != null) {
            level.setStatus(
                    updatedLevel.getStatus());
        }

        return levelRepository.save(level);
    }


    // =========================================================
    // 12. DELETE WORKFLOW LEVEL
    // =========================================================

    public String deleteWorkflowLevel(Long levelId) {

        ApprovalWorkflowLevel level =
                levelRepository.findById(levelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Workflow level not found"));

        levelRepository.delete(level);

        return "Workflow level deleted successfully";
    }


    // =========================================================
    // 13. SUBMIT APPROVAL REQUEST
    // =========================================================

    public ApprovalRequest submitRequest(
            Long workflowId,
            String employeeId,
            String requestType,
            Long referenceId,
            String remarks) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        if (workflow.getStatus() == WorkflowStatus.INACTIVE) {

            throw new RuntimeException(
                    "Workflow is inactive");
        }

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        List<ApprovalWorkflowLevel> levels =
                levelRepository
                        .findByWorkflowIdOrderByLevelNumberAsc(
                                workflowId);

        if (levels.isEmpty()) {

            throw new RuntimeException(
                    "No approval levels configured");
        }

        ApprovalRequest request =
                new ApprovalRequest();

        request.setWorkflow(workflow);
        request.setEmployee(employee);
        request.setRequestType(requestType);
        request.setReferenceId(referenceId);
        request.setCurrentLevel(1);
        request.setStatus(
                ApprovalRequestStatus.PENDING);
        request.setRemarks(remarks);
        request.setSubmittedDate(
                LocalDateTime.now());

        return requestRepository.save(request);
    }


    // =========================================================
    // 14. GET ALL APPROVAL REQUESTS
    // =========================================================

    public List<ApprovalRequest> getAllRequests() {

        return requestRepository.findAll();
    }


    // =========================================================
    // 15. GET REQUEST BY ID
    // =========================================================

    public ApprovalRequest getRequestById(Long requestId) {

        return requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approval request not found"));
    }


    // =========================================================
    // 16. GET EMPLOYEE REQUESTS
    // =========================================================

    public List<ApprovalRequest> getEmployeeRequests(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found"));

        return requestRepository
                .findByEmployeeEmployeeId(employeeId);
    }


    // =========================================================
    // 17. APPROVE REQUEST
    // =========================================================

    public ApprovalRequest approveRequest(
            Long requestId,
            String approverEmployeeId,
            String remarks) {

        return processApproval(
                requestId,
                approverEmployeeId,
                remarks,
                ApprovalAction.APPROVED);
    }


    // =========================================================
    // 18. REJECT REQUEST
    // =========================================================

    public ApprovalRequest rejectRequest(
            Long requestId,
            String approverEmployeeId,
            String remarks) {

        return processApproval(
                requestId,
                approverEmployeeId,
                remarks,
                ApprovalAction.REJECTED);
    }


    // =========================================================
    // 19. RETURN REQUEST
    // =========================================================

    public ApprovalRequest returnRequest(
            Long requestId,
            String approverEmployeeId,
            String remarks) {

        return processApproval(
                requestId,
                approverEmployeeId,
                remarks,
                ApprovalAction.RETURNED);
    }


    // =========================================================
    // 20. COMMON APPROVAL PROCESS
    // =========================================================

    private ApprovalRequest processApproval(
            Long requestId,
            String approverEmployeeId,
            String remarks,
            ApprovalAction action) {

        ApprovalRequest request =
                getRequestById(requestId);

        if (request.getStatus()
                != ApprovalRequestStatus.PENDING) {

            throw new RuntimeException(
                    "This request is no longer pending");
        }

        Employee approver =
                employeeRepository.findById(
                                approverEmployeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Approver employee not found"));

        ApprovalWorkflowLevel level =
                levelRepository
                        .findByWorkflowIdAndLevelNumber(
                                request.getWorkflow().getId(),
                                request.getCurrentLevel())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Current approval level not found"));

        // Create history record
        ApprovalHistory history =
                new ApprovalHistory();

        history.setApprovalRequest(request);
        history.setWorkflowLevel(level);
        history.setApprover(approver);
        history.setApprovalLevel(
                request.getCurrentLevel());
        history.setAction(action);
        history.setRemarks(remarks);
        history.setActionDate(
                LocalDateTime.now());

        historyRepository.save(history);


        // =====================================================
        // REJECT
        // =====================================================

        if (action == ApprovalAction.REJECTED) {

            request.setStatus(
                    ApprovalRequestStatus.REJECTED);

            request.setUpdatedAt(
                    LocalDateTime.now());

            return requestRepository.save(request);
        }


        // =====================================================
        // RETURN
        // =====================================================

        if (action == ApprovalAction.RETURNED) {

            request.setStatus(
                    ApprovalRequestStatus.PENDING);

            request.setRemarks(remarks);

            request.setUpdatedAt(
                    LocalDateTime.now());

            return requestRepository.save(request);
        }


        // =====================================================
        // APPROVED
        // =====================================================

        List<ApprovalWorkflowLevel> levels =
                levelRepository
                        .findByWorkflowIdOrderByLevelNumberAsc(
                                request.getWorkflow().getId());

        int currentLevel =
                request.getCurrentLevel();

        boolean lastLevel =
                currentLevel >= levels.size();

        if (lastLevel) {

            request.setStatus(
                    ApprovalRequestStatus.APPROVED);

        } else {

            request.setCurrentLevel(
                    currentLevel + 1);

            request.setStatus(
                    ApprovalRequestStatus.PENDING);
        }

        request.setUpdatedAt(
                LocalDateTime.now());

        return requestRepository.save(request);
    }


    // =========================================================
    // 21. CANCEL REQUEST
    // =========================================================

    public ApprovalRequest cancelRequest(
            Long requestId,
            String employeeId,
            String remarks) {

        ApprovalRequest request =
                getRequestById(requestId);

        if (!request.getEmployee()
                .getEmployeeId()
                .equals(employeeId)) {

            throw new RuntimeException(
                    "You cannot cancel this request");
        }

        if (request.getStatus()
                != ApprovalRequestStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending requests can be cancelled");
        }

        request.setStatus(
                ApprovalRequestStatus.CANCELLED);

        request.setRemarks(remarks);

        request.setUpdatedAt(
                LocalDateTime.now());

        return requestRepository.save(request);
    }


    // =========================================================
    // 22. GET APPROVAL HISTORY
    // =========================================================

    public List<ApprovalHistory> getApprovalHistory(
            Long requestId) {

        getRequestById(requestId);

        return historyRepository
                .findByApprovalRequestIdOrderByActionDateAsc(
                        requestId);
    }


    // =========================================================
    // 23. GET ALL HISTORY
    // =========================================================

    public List<ApprovalHistory> getAllApprovalHistory() {

        return historyRepository.findAll();
    }


    // =========================================================
    // 24. GET PENDING REQUESTS
    // =========================================================

    public List<ApprovalRequest> getPendingRequests() {

        return requestRepository
                .findByStatus(
                        ApprovalRequestStatus.PENDING);
    }
    public ApprovalWorkflowLevel createWorkflowLevel
            ( Long workflowId, ApprovalWorkflowLevel level)
    {
        ApprovalWorkflow workflow = workflowRepository.findById(workflowId) .orElseThrow(() -> new RuntimeException( "Workflow not found with ID: " + workflowId));
        level.setWorkflow(workflow);
        if (level.getStatus() == null)
        { level.setStatus(WorkflowStatus.ACTIVE); }
        if (level.getRequired() == null) { level.setRequired(true); }
        ApprovalWorkflowLevel saved = levelRepository.save(level);
        /* * Keep total_levels synchronized with actual levels. */
        long levelCount = levelRepository .countByWorkflowId(workflowId);
        workflow.setTotalLevels((int) levelCount); workflowRepository.save(workflow); return saved; }


    public ApprovalRequest createApprovalRequest(ApprovalRequest request)
    {
        return  requestRepository.save(request);
    }
    public List<ApprovalRequest> getAllApprovalRequests()
    {
        return requestRepository.findAll();
    }
    public ApprovalRequest getApprovalRequestById(Long id)
    {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + id));
    }
    public List<ApprovalRequest> getRequestsByEmployee(String employeeId)
    {
        return requestRepository.findByEmployeeId(employeeId);
    }
    public List<ApprovalHistory> getHistoryByApprover(String EmployeeId)
    {
        return historyRepository.findByEmployeeId(EmployeeId);
    }
}

