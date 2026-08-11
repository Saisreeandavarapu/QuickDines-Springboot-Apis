package com.HRMS.QuickDines.Task.Controller;

import com.HRMS.QuickDines.Task.Entity.TaskAssignmentStatus;
import com.HRMS.QuickDines.Task.Service.TaskService;
import com.HRMS.QuickDines.Task.model.TaskAssignments;
import com.HRMS.QuickDines.Task.model.Tasks;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    //=========================================================
    // TASKS
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(
            @RequestBody Tasks task) {

        return ResponseEntity.ok(
                service.createTask(task));
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping
    public ResponseEntity<?> getAllTasks() {

        return ResponseEntity.ok(
                service.getAllTasks());
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTask(id));
    }

    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody Tasks task) {

        return ResponseEntity.ok(
                service.updateTask(id, task));
    }

    @PreAuthorize("hasAuthority('TASK_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTask(id));
    }


    //=========================================================
    // TASK ASSIGNMENTS
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_ASSIGN')")
    @PostMapping("/assign/{taskId}")
    public ResponseEntity<?> assignTask(
            @PathVariable Long taskId,
            @RequestBody TaskAssignments assignment) {

        return ResponseEntity.ok(
                service.assignTask(taskId, assignment));
    }

    @PreAuthorize("hasAuthority('TASK_ASSIGNMENT_READ')")
    @GetMapping("/assigned")
    public ResponseEntity<?> getAssignedTasks() {

        return ResponseEntity.ok(
                service.getAssignedTasks());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_TASK_READ')")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeTasks(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_STATUS_UPDATE')")
    @PutMapping("/update-status/{taskId}")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                service.updateTaskStatus(taskId, status));
    }

    @PreAuthorize("hasAuthority('TASK_ASSIGNMENT_DELETE')")
    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAssignment(id));
    }


    //=========================================================
    // TASK REPORTS
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_REPORT_CREATE')")
    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> generateTaskReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.generateTaskReport(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_REPORT_READ')")
    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getTaskReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getTaskReport(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_PERFORMANCE_REPORT_READ')")
    @GetMapping("/performance-report")
    public ResponseEntity<?> getPerformanceReport() {

        return ResponseEntity.ok(
                service.getPerformanceReport());
    }


    //=========================================================
    // TASK STATUS MANAGEMENT
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_START')")
    @PostMapping("/start/{taskId}")
    public ResponseEntity<?> startTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                service.startTask(taskId));
    }

    @PreAuthorize("hasAuthority('TASK_COMPLETE')")
    @PostMapping("/complete/{taskId}")
    public ResponseEntity<?> completeTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                service.completeTask(taskId));
    }

    @PreAuthorize("hasAuthority('TASK_HOLD')")
    @PostMapping("/on-hold/{taskId}")
    public ResponseEntity<?> holdTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                service.holdTask(taskId));
    }

    @PreAuthorize("hasAuthority('TASK_REJECT')")
    @PostMapping("/reject/{taskId}")
    public ResponseEntity<?> rejectTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                service.rejectTask(taskId));
    }


    //=========================================================
    // TASK STATUS REPORTS
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_COMPLETED_READ')")
    @GetMapping("/completed/{employeeId}")
    public ResponseEntity<?> getCompletedTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getCompletedTasks(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_PENDING_READ')")
    @GetMapping("/pending/{employeeId}")
    public ResponseEntity<?> getPendingTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getPendingTasks(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_REJECTED_READ')")
    @GetMapping("/rejected/{employeeId}")
    public ResponseEntity<?> getRejectedTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getRejectedTasks(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_IN_PROGRESS_READ')")
    @GetMapping("/in-progress/{employeeId}")
    public ResponseEntity<?> getInProgressTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getInProgressTasks(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_ON_HOLD_READ')")
    @GetMapping("/on-hold/{employeeId}")
    public ResponseEntity<?> getOnHoldTasks(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getOnHoldTasks(employeeId));
    }


    //=========================================================
    // TASK COUNTS
    //=========================================================

    @PreAuthorize("hasAuthority('TASK_COMPLETED_COUNT_READ')")
    @GetMapping("/completed-count/{employeeId}")
    public ResponseEntity<?> getCompletedTaskCount(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getCompletedTaskCount(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_PENDING_COUNT_READ')")
    @GetMapping("/pending-count/{employeeId}")
    public ResponseEntity<?> getPendingTaskCount(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getPendingTaskCount(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_REJECTED_COUNT_READ')")
    @GetMapping("/rejected-count/{employeeId}")
    public ResponseEntity<?> getRejectedTaskCount(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getRejectedTaskCount(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_IN_PROGRESS_COUNT_READ')")
    @GetMapping("/in-progress-count/{employeeId}")
    public ResponseEntity<?> getInProgressTaskCount(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getInProgressTaskCount(employeeId));
    }

    @PreAuthorize("hasAuthority('TASK_ON_HOLD_COUNT_READ')")
    @GetMapping("/on-hold-count/{employeeId}")
    public ResponseEntity<?> getOnHoldTaskCount(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getOnHoldTaskCount(employeeId));
    }
    // =========================================================
// TASK STATUS FILTER
// =========================================================

    @GetMapping("/filter/status")
    @PreAuthorize("hasAuthority('TASK_READ')")
    public ResponseEntity<?> getTasksByStatus(
            @RequestParam TaskAssignmentStatus status) {

        return ResponseEntity.ok(
                service.getTasksByStatus(status));
    }
}
