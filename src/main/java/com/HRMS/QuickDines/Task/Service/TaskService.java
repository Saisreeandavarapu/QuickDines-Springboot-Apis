package com.HRMS.QuickDines.Task.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Task.Entity.TaskAssignmentStatus;
import com.HRMS.QuickDines.Task.model.TaskAssignments;
import com.HRMS.QuickDines.Task.model.TaskReports;
import com.HRMS.QuickDines.Task.model.Tasks;
import com.HRMS.QuickDines.Task.repo.TaskAssignmentsRepository;
import com.HRMS.QuickDines.Task.repo.TaskReportsRepository;
import com.HRMS.QuickDines.Task.repo.TasksRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TasksRepository tasksRepository;
    private final TaskAssignmentsRepository taskAssignmentsRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskReportsRepository taskReportsRepository;
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

//=================================
// TASKS
//=================================

    public String createTask(Tasks task) {

        task.setStatus("PENDING");

        tasksRepository.save(task);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - CREATE
        // ================================

        auditLogsService.logCreate(
                "TASK",
                String.valueOf(task.getId()),
                performedBy,
                String.valueOf(task.getId()),
                "Task created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_TASK",
                "TASK",
                "Task created successfully: "
                        + task.getTaskName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task created successfully: "
                        + task.getTaskName()
        );

        return "Task Created Successfully";
    }


    public List<Tasks> getAllTasks() {

        return tasksRepository.findAll();
    }


    public Tasks getTask(Long id) {

        return tasksRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));
    }


    public String updateTask(Long id, Tasks task) {

        Tasks existingTask = tasksRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        // Capture old value BEFORE update
        String oldValue = convertToJson(existingTask);

        existingTask.setTaskName(task.getTaskName());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setStatus(task.getStatus());

        tasksRepository.save(existingTask);

        // Capture new value AFTER update
        String newValue = convertToJson(existingTask);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - UPDATE
        // ================================

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(id),
                performedBy,
                String.valueOf(id),
                "Task updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TASK",
                "TASK",
                "Task updated successfully: "
                        + existingTask.getTaskName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task updated successfully: "
                        + existingTask.getTaskName()
        );

        return "Task Updated Successfully";
    }


    public String deleteTask(Long id) {

        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        // Capture deleted data BEFORE delete
        String deletedValue = convertToJson(task);

        tasksRepository.delete(task);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - DELETE
        // ================================

        auditLogsService.createAuditLog(
                "TASK",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Task deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TASK",
                "TASK",
                "Task deleted successfully: "
                        + task.getTaskName(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task deleted successfully: "
                        + task.getTaskName()
        );

        return "Task Deleted Successfully";
    }



//=================================
// TASK ASSIGNMENTS
//=================================

    public String assignTask(
            Long taskId,
            TaskAssignments assignment) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        Employee assignedBy =
                employeeRepository.findById(
                                assignment.getAssignedBy().getEmployeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Assigned By Employee Not Found"));

        Employee assignedTo =
                employeeRepository.findById(
                                assignment.getAssignedTo().getEmployeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Assigned To Employee Not Found"));

        assignment.setTask(task);
        assignment.setAssignedBy(assignedBy);
        assignment.setAssignedTo(assignedTo);
        assignment.setAssignedDate(LocalDate.now());

        taskAssignmentsRepository.save(assignment);

        task.setStatus("ASSIGNED");
        tasksRepository.save(task);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - CREATE ASSIGNMENT
        // ================================

        auditLogsService.logCreate(
                "TASK",
                String.valueOf(assignment.getId()),
                performedBy,
                String.valueOf(taskId),
                "Task assigned successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "ASSIGN_TASK",
                "TASK",
                "Task assigned successfully. Task ID: "
                        + taskId
                        + ", Assigned To: "
                        + assignedTo.getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task assigned successfully. Task ID: "
                        + taskId
        );

        return "Task Assigned Successfully";
    }


    public List<TaskAssignments> getAssignedTasks() {

        return taskAssignmentsRepository.findAll();
    }


    public List<TaskAssignments> getEmployeeTasks(
            String employeeId) {

        return taskAssignmentsRepository
                .findByAssignedToEmployeeId(employeeId);
    }


    public String updateTaskStatus(
            Long taskId,
            String status) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        // Capture old status before update
        String oldValue = convertToJson(task);

        task.setStatus(status);

        tasksRepository.save(task);

        // Capture new status after update
        String newValue = convertToJson(task);

        String performedBy = getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - STATUS UPDATE
        // ================================

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(taskId),
                performedBy,
                String.valueOf(taskId),
                "Task status updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TASK_STATUS",
                "TASK",
                "Task status updated successfully. "
                        + "Task ID: " + taskId
                        + ", New Status: " + status,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task status updated successfully. "
                        + "Task ID: " + taskId
        );

        return "Task Status Updated Successfully";
    }


    public String deleteAssignment(Long id) {

        TaskAssignments assignment =
                taskAssignmentsRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task Assignment Not Found"));

        // Capture deleted data BEFORE delete
        String deletedValue =
                convertToJson(assignment);

        taskAssignmentsRepository.delete(assignment);

        String performedBy =
                getLoggedInEmployeeId();

        // ================================
        // AUDIT LOG - DELETE ASSIGNMENT
        // ================================

        auditLogsService.createAuditLog(
                "TASK",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Task assignment deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TASK_ASSIGNMENT",
                "TASK",
                "Task assignment deleted successfully. "
                        + "Assignment ID: " + id,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task assignment deleted successfully. "
                        + "Assignment ID: " + id
        );

        return "Task Assignment Deleted Successfully";
    }



//=================================
// TASK REPORTS
//=================================

    public String generateTaskReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        List<TaskAssignments> assignments =
                taskAssignmentsRepository
                        .findByAssignedToEmployeeId(employeeId);

        Integer completedTasks = 0;
        Integer pendingTasks = 0;

        for (TaskAssignments assignment : assignments) {

            String status = assignment.getTask().getStatus();

            if ("COMPLETED".equalsIgnoreCase(status)) {
                completedTasks++;
            } else {
                pendingTasks++;
            }
        }

        Integer totalTasks =
                completedTasks + pendingTasks;

        Double performancePercentage = 0.0;

        if (totalTasks > 0) {
            performancePercentage =
                    ((double) completedTasks / totalTasks) * 100;
        }

        TaskReports report = new TaskReports();

        report.setEmployee(employee);
        report.setCompletedTasks(completedTasks);
        report.setPendingTasks(pendingTasks);
        report.setPerformancePercentage(
                BigDecimal.valueOf(performancePercentage));

        taskReportsRepository.save(report);

        // ================================
        // AUDIT LOG - GENERATE REPORT
        // ================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "TASK",
                String.valueOf(report.getId()),
                performedBy,
                employeeId,
                "Task performance report generated successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "GENERATE_TASK_REPORT",
                "TASK",
                "Task performance report generated for employee: "
                        + employeeId
                        + ". Completed: "
                        + completedTasks
                        + ", Pending: "
                        + pendingTasks
                        + ", Performance: "
                        + performancePercentage
                        + "%",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task performance report generated for employee: "
                        + employeeId
        );

        return "Task Report Generated Successfully";
    }


    public Object getTaskReport(String employeeId) {

        return taskReportsRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Task Report Not Found"));
    }


    public Object getPerformanceReport() {

        return taskReportsRepository.findAll();
    }


//=================================
// START TASK
//=================================

    public String startTask(Long taskId) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        // Capture old value before update
        String oldValue = convertToJson(task);

        task.setStatus("IN_PROGRESS");

        tasksRepository.save(task);

        // Capture new value after update
        String newValue = convertToJson(task);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(taskId),
                performedBy,
                String.valueOf(taskId),
                "Task started successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "START_TASK",
                "TASK",
                "Task started successfully. Task ID: "
                        + taskId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task started successfully. Task ID: "
                        + taskId
        );

        return "Task Started Successfully";
    }


//=================================
// COMPLETE TASK
//=================================

    public String completeTask(Long taskId) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        String oldValue = convertToJson(task);

        task.setStatus("COMPLETED");

        tasksRepository.save(task);

        String newValue = convertToJson(task);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(taskId),
                performedBy,
                String.valueOf(taskId),
                "Task completed successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "COMPLETE_TASK",
                "TASK",
                "Task completed successfully. Task ID: "
                        + taskId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task completed successfully. Task ID: "
                        + taskId
        );

        return "Task Completed Successfully";
    }


//=================================
// HOLD TASK
//=================================

    public String holdTask(Long taskId) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        String oldValue = convertToJson(task);

        task.setStatus("ON_HOLD");

        tasksRepository.save(task);

        String newValue = convertToJson(task);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(taskId),
                performedBy,
                String.valueOf(taskId),
                "Task put on hold successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "HOLD_TASK",
                "TASK",
                "Task put on hold successfully. Task ID: "
                        + taskId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task put on hold successfully. Task ID: "
                        + taskId
        );

        return "Task Put On Hold Successfully";
    }


//=================================
// REJECT TASK
//=================================

    public String rejectTask(Long taskId) {

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task Not Found"));

        String oldValue = convertToJson(task);

        task.setStatus("REJECTED");

        tasksRepository.save(task);

        String newValue = convertToJson(task);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TASK",
                String.valueOf(taskId),
                performedBy,
                String.valueOf(taskId),
                "Task rejected successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "REJECT_TASK",
                "TASK",
                "Task rejected successfully. Task ID: "
                        + taskId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TASK",
                "TaskService",
                "Task rejected successfully. Task ID: "
                        + taskId
        );

        return "Task Rejected Successfully";
    }


    //=================================
// TASK STATUS REPORTS
//=================================

    public Object getCompletedTasks(String employeeId) {

        return taskAssignmentsRepository.findByAssignedToEmployeeIdAndTaskStatus(employeeId, "COMPLETED");
    }


    public Object getPendingTasks(String employeeId) {

        return taskAssignmentsRepository.findByAssignedToEmployeeIdAndTaskStatus(employeeId, "PENDING");
    }


    public Object getRejectedTasks(String employeeId) {

        return taskAssignmentsRepository.findByAssignedToEmployeeIdAndTaskStatus(employeeId, "REJECTED");
    }


    public Object getInProgressTasks(String employeeId) {

        return taskAssignmentsRepository.findByAssignedToEmployeeIdAndTaskStatus(employeeId, "IN_PROGRESS");
    }


    public Object getOnHoldTasks(String employeeId) {

        return taskAssignmentsRepository.findByAssignedToEmployeeIdAndTaskStatus(employeeId, "ON_HOLD");
    }

    //=================================
// TASK COUNTS
//=================================

    public Long getCompletedTaskCount(String employeeId) {

        return taskAssignmentsRepository.countByAssignedToEmployeeIdAndTaskStatus(employeeId, "COMPLETED");
    }


    public Long getPendingTaskCount(String employeeId) {

        return taskAssignmentsRepository.countByAssignedToEmployeeIdAndTaskStatus(employeeId, "PENDING");
    }


    public Long getRejectedTaskCount(String employeeId) {

        return taskAssignmentsRepository.countByAssignedToEmployeeIdAndTaskStatus(employeeId, "REJECTED");
    }


    public Long getInProgressTaskCount(String employeeId) {

        return taskAssignmentsRepository.countByAssignedToEmployeeIdAndTaskStatus(employeeId, "IN_PROGRESS");
    }


    public Long getOnHoldTaskCount(String employeeId) {

        return taskAssignmentsRepository.countByAssignedToEmployeeIdAndTaskStatus(employeeId, "ON_HOLD");
    }
    // =========================================================
// FILTER TASK ASSIGNMENTS BY STATUS
// =========================================================

    public List<TaskAssignments> getTasksByStatus(
            TaskAssignmentStatus status) {

        if (status == null) {
            throw new RuntimeException(
                    "Task status is required");
        }

        return taskAssignmentsRepository
                .findByStatus(status);
    }
}