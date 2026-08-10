package com.HRMS.QuickDines.Workflow.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Workflow.Entity.*;
import com.HRMS.QuickDines.Workflow.model.*;
import com.HRMS.QuickDines.Workflow.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowService {

    private final ApprovalWorkflowRepository workflowRepository;

    private final ApprovalWorkflowLevelRepository levelRepository;

    private final ApprovalRequestRepository requestRepository;

    private final ApprovalHistoryRepository historyRepository;

    private final EmployeeRepository employeeRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


// =========================================================
// 1. CREATE WORKFLOW
// =========================================================

    public ApprovalWorkflow createWorkflow(
            ApprovalWorkflow workflow) {

        if (workflow.getWorkflowName() == null ||
                workflow.getWorkflowName().isBlank()) {

            throw new RuntimeException(
                    "Workflow name is required");
        }

        if (workflow.getWorkflowType() == null ||
                workflow.getWorkflowType().isBlank()) {

            throw new RuntimeException(
                    "Workflow type is required");
        }

        if (workflow.getTotalLevels() == null ||
                workflow.getTotalLevels() <= 0) {

            throw new RuntimeException(
                    "Total levels must be greater than zero");
        }

        if (workflow.getAutoApprove() == null) {
            workflow.setAutoApprove(false);
        }

        if (workflow.getStatus() == null) {
            workflow.setStatus(
                    WorkflowStatus.ACTIVE);
        }

        ApprovalWorkflow savedWorkflow =
                workflowRepository.save(workflow);

        String performedBy =
                getLoggedInEmployeeId();

        String newValue =
                convertToJson(savedWorkflow);

        auditLogsService.logCreate(
                "WORKFLOW",
                String.valueOf(savedWorkflow.getId()),
                performedBy,
                savedWorkflow.getId().toString(),
                "Workflow created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_WORKFLOW",
                "WORKFLOW",
                "Workflow created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WORKFLOW",
                "WorkflowService",
                "Workflow created successfully"
        );

        return savedWorkflow;
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

    public ApprovalWorkflow getWorkflowById(
            Long id) {

        return workflowRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Workflow not found with ID: " + id));
    }


// =========================================================
// 4. UPDATE WORKFLOW
// =========================================================

    public ApprovalWorkflow updateWorkflow(
            Long id,
            ApprovalWorkflow updatedWorkflow) {

        ApprovalWorkflow workflow =
                getWorkflowById(id);

        // Capture OLD value before modification
        String oldValue =
                convertToJson(workflow);

        if (updatedWorkflow.getWorkflowName() != null) {

            workflow.setWorkflowName(
                    updatedWorkflow.getWorkflowName());
        }

        if (updatedWorkflow.getWorkflowType() != null) {

            workflow.setWorkflowType(
                    updatedWorkflow.getWorkflowType());
        }

        if (updatedWorkflow.getCompanyId() != null) {

            workflow.setCompanyId(
                    updatedWorkflow.getCompanyId());
        }

        if (updatedWorkflow.getDepartmentId() != null) {

            workflow.setDepartmentId(
                    updatedWorkflow.getDepartmentId());
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

        ApprovalWorkflow savedWorkflow =
                workflowRepository.save(workflow);

        // Capture NEW value after modification
        String newValue =
                convertToJson(savedWorkflow);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate(
                "WORKFLOW",
                String.valueOf(savedWorkflow.getId()),
                performedBy,
                null,
                "Workflow updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_WORKFLOW",
                "WORKFLOW",
                "Workflow updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WORKFLOW",
                "WorkflowService",
                "Workflow updated successfully"
        );

        return savedWorkflow;
    }


    // =========================================================
    // 5. DELETE WORKFLOW
    // =========================================================

    public String deleteWorkflow(Long id) {

        ApprovalWorkflow workflow =
                getWorkflowById(id);

        workflowRepository.delete(workflow);
        String deletedValue = convertToJson(workflow);
        String performedBy = getLoggedInEmployeeId();
        workflowRepository.delete(workflow);
        auditLogsService.createAuditLog("WORKFLOW", String.valueOf(id), AuditActionType.DELETE, performedBy, workflow.getId().toString(), "Workflow deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());
        auditLogsService.logActivity(performedBy, "DELETE_WORKFLOW", "WORKFLOW", "Workflow deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("WORKFLOW", "WorkflowService", "Workflow deleted successfully");

        return "Workflow deleted successfully";
    }


    // =========================================================
    // 6. ACTIVATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow activateWorkflow(
            Long id) {

        ApprovalWorkflow workflow =
                getWorkflowById(id);

        workflow.setStatus(
                WorkflowStatus.ACTIVE);

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 7. DEACTIVATE WORKFLOW
    // =========================================================

    public ApprovalWorkflow deactivateWorkflow(
            Long id) {

        ApprovalWorkflow workflow =
                getWorkflowById(id);

        workflow.setStatus(
                WorkflowStatus.INACTIVE);

        return workflowRepository.save(workflow);
    }


    // =========================================================
    // 8. GET ACTIVE WORKFLOWS
    // =========================================================

    public List<ApprovalWorkflow> getActiveWorkflows() {

        return workflowRepository.findByStatus(WorkflowStatus.ACTIVE);
    }


    // =========================================================
    // 9. GET WORKFLOWS BY TYPE
    // =========================================================

    public List<ApprovalWorkflow> getWorkflowsByType(
            String type) {

        return workflowRepository.findByWorkflowType(type);
    }


    // =========================================================
    // 10. CREATE WORKFLOW LEVEL
    // =========================================================

    public ApprovalWorkflowLevel createWorkflowLevel(
            Long workflowId,
            ApprovalWorkflowLevel level) {

        ApprovalWorkflow workflow =
                getWorkflowById(workflowId);

        if (workflow.getStatus()
                == WorkflowStatus.INACTIVE) {

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

        if (level.getApproverRole() == null ||
                level.getApproverRole().getId() == null) {

            throw new RuntimeException(
                    "Approver role is required");
        }

        if (levelRepository
                .existsByWorkflowIdAndLevelNumber(
                        workflowId,
                        level.getLevelNumber())) {

            throw new RuntimeException(
                    "This workflow level already exists");
        }

        level.setWorkflow(workflow);

        if (level.getRequired() == null) {
            level.setRequired(true);
        }

        if (level.getStatus() == null) {
            level.setStatus(
                    WorkflowStatus.ACTIVE);
        }

        ApprovalWorkflowLevel saved =
                levelRepository.save(level);

        updateWorkflowTotalLevels(workflow);
        String performedBy = getLoggedInEmployeeId();
        String newValue = convertToJson(saved);
        // =====================================================
        // AUDIT LOG
        // =====================================================
        auditLogsService.logCreate("WORKFLOW_LEVEL", String.valueOf(saved.getId()), performedBy, saved.getId().toString(), "Workflow level created successfully");
        auditLogsService.logActivity(performedBy, "CREATE_WORKFLOW_LEVEL", "WORKFLOW_LEVEL", "Workflow level created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("WORKFLOW_LEVEL", "WorkflowService", "Workflow level created successfully");

        return saved;
    }


    // =========================================================
    // 11. GET ALL LEVELS
    // =========================================================

    public List<ApprovalWorkflowLevel>
    getWorkflowLevels(Long workflowId) {

        getWorkflowById(workflowId);

        return levelRepository
                .findByWorkflowIdOrderByLevelNumberAsc(
                        workflowId);
    }


    // =========================================================
    // 12. GET LEVEL BY ID
    // =========================================================

    public ApprovalWorkflowLevel
    getWorkflowLevelById(Long id) {

        return levelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Workflow level not found with ID: "
                                        + id));
    }


    // =========================================================
    // 13. UPDATE LEVEL
    // =========================================================

    public ApprovalWorkflowLevel
    updateWorkflowLevel(
            Long id,
            ApprovalWorkflowLevel updatedLevel) {

        ApprovalWorkflowLevel level =
                getWorkflowLevelById(id);

        if (updatedLevel.getLevelNumber() != null) {

            level.setLevelNumber(
                    updatedLevel.getLevelNumber());
        }

        if (updatedLevel.getLevelName() != null) {

            level.setLevelName(
                    updatedLevel.getLevelName());
        }

        if (updatedLevel.getApproverRole() != null) {

            if (updatedLevel.getApproverRole().getId()
                    == null) {

                throw new RuntimeException(
                        "Approver role ID is required");
            }

            level.setApproverRole(
                    updatedLevel.getApproverRole());
        }

        if (updatedLevel.getRequired() != null) {

            level.setRequired(
                    updatedLevel.getRequired());
        }

        if (updatedLevel.getStatus() != null) {

            level.setStatus(
                    updatedLevel.getStatus());
        }
        ApprovalWorkflowLevel saved = levelRepository.save(level);
        String oldValue = convertToJson(level);
        // =====================================================
        // NEW VALUE
        // =====================================================
        String newValue = convertToJson(saved);
        String performedBy = getLoggedInEmployeeId();
        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate("WORKFLOW_LEVEL", String.valueOf(saved.getId()), performedBy, null, "Workflow level updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_WORKFLOW_LEVEL", "WORKFLOW_LEVEL", "Workflow level updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("WORKFLOW_LEVEL", "WorkflowService", "Workflow level updated successfully");
        return saved;
    }


    // =========================================================
    // 14. DELETE LEVEL
    // =========================================================


    public String deleteWorkflowLevel(
            Long id) {

        ApprovalWorkflowLevel level =
                getWorkflowLevelById(id);

        String deletedValue =
                convertToJson(level);

        String performedBy =
                getLoggedInEmployeeId();

        ApprovalWorkflow workflow =
                level.getWorkflow();

        levelRepository.delete(level);

        if (workflow != null) {

            updateWorkflowTotalLevels(workflow);
        }

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete(
                "WORKFLOW_LEVEL",
                String.valueOf(id),
                performedBy,
                deletedValue,
                "Workflow level deleted successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_WORKFLOW_LEVEL",
                "WORKFLOW_LEVEL",
                "Workflow level deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "WORKFLOW_LEVEL",
                "WorkflowService",
                "Workflow level deleted successfully"
        );

        return "Workflow level deleted successfully";
    }


    // =========================================================
    // 15. ACTIVATE LEVEL
    // =========================================================

    public ApprovalWorkflowLevel activateLevel(
            Long levelId) {

        ApprovalWorkflowLevel level =
                getWorkflowLevelById(levelId);

        level.setStatus(
                WorkflowStatus.ACTIVE);

        return levelRepository.save(level);
    }


    // =========================================================
    // 16. DEACTIVATE LEVEL
    // =========================================================

    public ApprovalWorkflowLevel deactivateLevel(
            Long levelId) {

        ApprovalWorkflowLevel level =
                getWorkflowLevelById(levelId);

        level.setStatus(
                WorkflowStatus.INACTIVE);

        return levelRepository.save(level);
    }


    // =========================================================
    // 17. CREATE APPROVAL REQUEST
    // =========================================================



    public ApprovalRequest createApprovalRequest(
            ApprovalRequest request) {

        if (request.getWorkflow() == null ||
                request.getWorkflow().getId() == null) {

            throw new RuntimeException(
                    "Workflow is required");
        }

        ApprovalWorkflow workflow =
                getWorkflowById(
                        request.getWorkflow().getId());

        if (workflow.getStatus()
                == WorkflowStatus.INACTIVE) {

            throw new RuntimeException(
                    "Workflow is inactive");
        }

        if (request.getEmployee() == null ||
                request.getEmployee()
                        .getEmployeeId() == null) {

            throw new RuntimeException(
                    "Employee is required");
        }

        Employee employee =
                employeeRepository.findById(
                                request.getEmployee()
                                        .getEmployeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        List<ApprovalWorkflowLevel> levels =
                levelRepository
                        .findByWorkflowIdAndStatus(
                                workflow.getId(),
                                WorkflowStatus.ACTIVE);

        if (levels.isEmpty()) {

            throw new RuntimeException(
                    "No active approval levels configured");
        }

        request.setWorkflow(workflow);

        request.setEmployee(employee);

        request.setCurrentLevel(1);

        if (request.getStatus() == null) {

            request.setStatus(
                    ApprovalRequestStatus.PENDING);
        }

        if (request.getSubmittedDate() == null) {

            request.setSubmittedDate(
                    LocalDateTime.now());
        }

        ApprovalRequest saved =
                requestRepository.save(request);

        String performedBy =
                getLoggedInEmployeeId();

        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate(
                "APPROVAL_REQUEST",
                String.valueOf(saved.getId()),
                performedBy,
                saved.getId().toString(),
                "Approval request created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_APPROVAL_REQUEST",
                "APPROVAL_REQUEST",
                "Approval request created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "APPROVAL_REQUEST",
                "WorkflowService",
                "Approval request created successfully"
        );

        return saved;
    }



    // =========================================================
    // 18. GET ALL REQUESTS
    // =========================================================

    public List<ApprovalRequest>
    getAllApprovalRequests() {

        return requestRepository.findAll();
    }


    // =========================================================
    // 19. GET REQUEST BY ID
    // =========================================================

    public ApprovalRequest
    getApprovalRequestById(Long id) {

        return requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approval request not found with ID: "
                                        + id));
    }


    // =========================================================
    // 20. GET EMPLOYEE REQUESTS
    // =========================================================

    public List<ApprovalRequest>
    getRequestsByEmployee(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found"));

        return requestRepository
                .findByEmployee_EmployeeId(
                        employeeId);
    }


    // =========================================================
    // 21. GET PENDING REQUESTS
    // =========================================================

    public List<ApprovalRequest>
    getPendingRequests() {

        return requestRepository
                .findByStatus(
                        ApprovalRequestStatus.PENDING);
    }


    // =========================================================
    // 22. GET REQUESTS BY STATUS
    // =========================================================

    public List<ApprovalRequest>
    getRequestsByStatus(
            ApprovalRequestStatus status) {

        return requestRepository
                .findByStatus(status);
    }


    // =========================================================
    // 23. GET REQUESTS BY TYPE
    // =========================================================

    public List<ApprovalRequest>
    getRequestsByType(
            String type) {

        return requestRepository
                .findByRequestType(type);
    }


    // =========================================================
    // 24. APPROVE REQUEST
    // =========================================================

    public ApprovalRequest approveRequest(
            Long requestId,
            String employeeId,
            String remarks) {

        return processApproval(
                requestId,
                employeeId,
                remarks,
                ApprovalAction.APPROVED);
    }


    // =========================================================
    // 25. REJECT REQUEST
    // =========================================================

    public ApprovalRequest rejectRequest(
            Long requestId,
            String employeeId,
            String remarks) {

        return processApproval(
                requestId,
                employeeId,
                remarks,
                ApprovalAction.REJECTED);
    }


    // =========================================================
    // 26. RETURN REQUEST
    // =========================================================

    public ApprovalRequest returnRequest(
            Long requestId,
            String employeeId,
            String remarks) {

        return processApproval(
                requestId,
                employeeId,
                remarks,
                ApprovalAction.RETURNED);
    }


    // =========================================================
    // 27. CANCEL REQUEST
    // =========================================================

    public ApprovalRequest cancelRequest(
            Long requestId,
            String employeeId,
            String remarks) {

        ApprovalRequest request =
                getApprovalRequestById(requestId);

        if (request.getEmployee() == null ||
                !request.getEmployee()
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
// 28. COMMON APPROVAL PROCESS
// =========================================================

    private ApprovalRequest processApproval(
            Long requestId,
            String employeeId,
            String remarks,
            ApprovalAction action) {

        // =====================================================
        // 1. GET APPROVAL REQUEST
        // =====================================================

        ApprovalRequest request =
                getApprovalRequestById(requestId);

        // =====================================================
        // 2. REQUEST MUST BE PENDING
        // =====================================================

        if (request.getStatus()
                != ApprovalRequestStatus.PENDING) {

            throw new RuntimeException(
                    "This request is no longer pending");
        }

        // =====================================================
        // 3. GET APPROVER EMPLOYEE
        // =====================================================

        Employee approver =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Approver employee not found"));

        // =====================================================
        // 4. GET CURRENT WORKFLOW LEVEL
        // =====================================================

        ApprovalWorkflowLevel level =
                levelRepository
                        .findByWorkflowIdAndLevelNumber(
                                request.getWorkflow().getId(),
                                request.getCurrentLevel())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Current approval level not found"));

        // =====================================================
        // 5. CHECK CURRENT LEVEL STATUS
        // =====================================================

        if (level.getStatus()
                == WorkflowStatus.INACTIVE) {

            throw new RuntimeException(
                    "Current approval level is inactive");
        }

        // =====================================================
        // 6. CREATE APPROVAL HISTORY
        // =====================================================

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
        // 7. REJECT REQUEST
        // =====================================================

        if (action == ApprovalAction.REJECTED) {

            request.setStatus(
                    ApprovalRequestStatus.REJECTED);

            request.setRemarks(remarks);

            request.setUpdatedAt(
                    LocalDateTime.now());

            return requestRepository.save(request);
        }

        // =====================================================
        // 8. APPROVE REQUEST
        // =====================================================

        if (action == ApprovalAction.APPROVED) {

            /*
             * Get all ACTIVE levels for this workflow.
             */
            List<ApprovalWorkflowLevel> levels =
                    levelRepository
                            .findByWorkflowIdAndStatus(
                                    request.getWorkflow().getId(),
                                    WorkflowStatus.ACTIVE);

            if (levels.isEmpty()) {

                throw new RuntimeException(
                        "No active approval levels found");
            }

            /*
             * Current approval level.
             */
            int currentLevel =
                    request.getCurrentLevel();

            /*
             * Find the next active level.
             */
            ApprovalWorkflowLevel nextLevel = null;

            for (ApprovalWorkflowLevel workflowLevel : levels) {

                if (workflowLevel.getLevelNumber()
                        > currentLevel) {

                    nextLevel = workflowLevel;

                    break;
                }
            }

            // =================================================
            // 9. FINAL LEVEL APPROVED
            // =================================================

            if (nextLevel == null) {

                request.setStatus(
                        ApprovalRequestStatus.APPROVED);
            }

            // =================================================
            // 10. MOVE TO NEXT LEVEL
            // =================================================

            else {

                request.setCurrentLevel(
                        nextLevel.getLevelNumber());

                /*
                 * Request stays PENDING until
                 * the next approver takes action.
                 */
                request.setStatus(
                        ApprovalRequestStatus.PENDING);
            }

            request.setRemarks(remarks);

            request.setUpdatedAt(
                    LocalDateTime.now());

            return requestRepository.save(request);
        }

        // =====================================================
        // 11. INVALID ACTION
        // =====================================================

        throw new RuntimeException(
                "Invalid approval action: " + action);
    }


    // =========================================================
    // 29. REQUEST HISTORY
    // =========================================================

    public List<ApprovalHistory>
    getApprovalHistory(Long requestId) {

        getApprovalRequestById(requestId);

        return historyRepository
                .findByApprovalRequestIdOrderByActionDateAsc(
                        requestId);
    }


    // =========================================================
    // 30. ALL HISTORY
    // =========================================================

    public List<ApprovalHistory>
    getAllApprovalHistory() {

        return historyRepository.findAll();
    }


    // =========================================================
    // 31. APPROVER HISTORY
    // =========================================================

    public List<ApprovalHistory>
    getHistoryByApprover(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found"));

        return historyRepository
                .findByApprover_EmployeeId(
                        employeeId);
    }


    // =========================================================
    // HELPER
    // =========================================================

    private void updateWorkflowTotalLevels(
            ApprovalWorkflow workflow) {

        long count =
                levelRepository.countByWorkflowId(
                        workflow.getId());

        workflow.setTotalLevels(
                (int) count);

        workflowRepository.save(workflow);
    }
}