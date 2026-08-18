package com.HRMS.QuickDines.Task.Controller;

import com.HRMS.QuickDines.Task.DTO.*;
import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import com.HRMS.QuickDines.Task.Service.TimesheetService;
import com.HRMS.QuickDines.Task.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;


    // =====================================================
    // TIMESHEET
    // =====================================================

    @PostMapping
    public ResponseEntity<EmployeeTimesheet> create(
            @RequestBody TimesheetRequest request) {

        return ResponseEntity.ok(
                timesheetService.createTimesheet(request)
        );
    }


    @GetMapping
    public ResponseEntity<List<EmployeeTimesheet>> getAll() {

        return ResponseEntity.ok(
                timesheetService.getAllTimesheets()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeTimesheet> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timesheetService.getTimesheet(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTimesheet> update(
            @PathVariable Long id,
            @RequestBody TimesheetRequest request) {

        return ResponseEntity.ok(
                timesheetService.updateTimesheet(
                        id,
                        request
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        timesheetService.deleteTimesheet(id);

        return ResponseEntity.ok(
                "Timesheet deleted successfully"
        );
    }


    // =====================================================
    // EMPLOYEE / DATE
    // =====================================================

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeTimesheet>>
    getByEmployee(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                timesheetService.getEmployeeTimesheets(
                        employeeId
                )
        );
    }


    @GetMapping("/date/{date}")
    public ResponseEntity<List<EmployeeTimesheet>>
    getByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                timesheetService.getByDate(date)
        );
    }


    @GetMapping("/employee/{employeeId}/date/{date}")
    public ResponseEntity<EmployeeTimesheet>
    getEmployeeDate(
            @PathVariable String employeeId,
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                timesheetService.getEmployeeDate(
                        employeeId,
                        date
                )
        );
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeTimesheet>>
    getByStatus(
            @PathVariable TimesheetStatus status) {

        return ResponseEntity.ok(
                timesheetService.getByStatus(status)
        );
    }


    // =====================================================
    // STATUS
    // =====================================================

    @GetMapping("/drafts")
    public ResponseEntity<List<EmployeeTimesheet>>
    drafts() {

        return ResponseEntity.ok(
                timesheetService.getDrafts()
        );
    }


    @GetMapping("/submitted")
    public ResponseEntity<List<EmployeeTimesheet>>
    submitted() {

        return ResponseEntity.ok(
                timesheetService.getSubmitted()
        );
    }


    @GetMapping("/pending")
    public ResponseEntity<List<EmployeeTimesheet>>
    pending() {

        return ResponseEntity.ok(
                timesheetService.getPending()
        );
    }


    @GetMapping("/approved")
    public ResponseEntity<List<EmployeeTimesheet>>
    approved() {

        return ResponseEntity.ok(
                timesheetService.getApproved()
        );
    }


    @GetMapping("/rejected")
    public ResponseEntity<List<EmployeeTimesheet>>
    rejected() {

        return ResponseEntity.ok(
                timesheetService.getRejected()
        );
    }


    // =====================================================
    // WORKFLOW
    // =====================================================

    @PutMapping("/{id}/submit")
    public ResponseEntity<EmployeeTimesheet>
    submit(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timesheetService.submitTimesheet(id)
        );
    }


    @PutMapping("/{id}/approve")
    public ResponseEntity<EmployeeTimesheet>
    approve(
            @PathVariable Long id,
            @RequestBody TimesheetApprovalRequest request) {

        return ResponseEntity.ok(
                timesheetService.approveTimesheet(
                        id,
                        request
                )
        );
    }


    @PutMapping("/{id}/reject")
    public ResponseEntity<EmployeeTimesheet>
    reject(
            @PathVariable Long id,
            @RequestBody TimesheetApprovalRequest request) {

        return ResponseEntity.ok(
                timesheetService.rejectTimesheet(
                        id,
                        request
                )
        );
    }


    @PutMapping("/{id}/return")
    public ResponseEntity<EmployeeTimesheet>
    returnTimesheet(
            @PathVariable Long id,
            @RequestBody TimesheetApprovalRequest request) {

        return ResponseEntity.ok(
                timesheetService.returnTimesheet(
                        id,
                        request
                )
        );
    }


    // =====================================================
    // TASKS
    // =====================================================

    @PostMapping("/{timesheetId}/tasks")
    public ResponseEntity<TimesheetTask>
    addTask(
            @PathVariable Long timesheetId,
            @RequestBody TimesheetTaskRequest request) {

        return ResponseEntity.ok(
                timesheetService.addTask(
                        timesheetId,
                        request
                )
        );
    }


    @GetMapping("/{timesheetId}/tasks")
    public ResponseEntity<List<TimesheetTask>>
    getTasks(
            @PathVariable Long timesheetId) {

        return ResponseEntity.ok(
                timesheetService.getTasks(timesheetId)
        );
    }


    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TimesheetTask>
    getTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                timesheetService.getTask(taskId)
        );
    }


    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TimesheetTask>
    updateTask(
            @PathVariable Long taskId,
            @RequestBody TimesheetTaskRequest request) {

        return ResponseEntity.ok(
                timesheetService.updateTask(
                        taskId,
                        request
                )
        );
    }


    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String>
    deleteTask(
            @PathVariable Long taskId) {

        timesheetService.deleteTask(taskId);

        return ResponseEntity.ok(
                "Task deleted successfully"
        );
    }


    // =====================================================
    // APPROVALS
    // =====================================================

    @GetMapping("/{timesheetId}/approvals")
    public ResponseEntity<List<TimesheetApproval>>
    getApprovals(
            @PathVariable Long timesheetId) {

        return ResponseEntity.ok(
                timesheetService.getApprovals(
                        timesheetId
                )
        );
    }


    @GetMapping("/approvals/pending")
    public ResponseEntity<List<TimesheetApproval>>
    pendingApprovals() {

        return ResponseEntity.ok(
                timesheetService.getPendingApprovals()
        );
    }


    @GetMapping("/approvals/approver/{employeeId}/pending")
    public ResponseEntity<List<TimesheetApproval>>
    approverPending(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                timesheetService.getApproverPending(
                        employeeId
                )
        );
    }


    @GetMapping("/approvals/employee/{employeeId}")
    public ResponseEntity<List<TimesheetApproval>>
    employeeApprovals(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                timesheetService.getEmployeeApprovals(
                        employeeId
                )
        );
    }


    // =====================================================
    // ATTACHMENTS
    // =====================================================

    @PostMapping("/{timesheetId}/attachments")
    public ResponseEntity<TimesheetAttachment>
    addAttachment(
            @PathVariable Long timesheetId,
            @RequestBody TimesheetAttachmentRequest request) {

        return ResponseEntity.ok(
                timesheetService.addAttachment(
                        timesheetId,
                        request
                )
        );
    }


    @GetMapping("/{timesheetId}/attachments")
    public ResponseEntity<List<TimesheetAttachment>>
    getAttachments(
            @PathVariable Long timesheetId) {

        return ResponseEntity.ok(
                timesheetService.getAttachments(
                        timesheetId
                )
        );
    }


    @GetMapping("/attachments/{attachmentId}")
    public ResponseEntity<TimesheetAttachment>
    getAttachment(
            @PathVariable Long attachmentId) {

        return ResponseEntity.ok(
                timesheetService.getAttachment(
                        attachmentId
                )
        );
    }


    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<String>
    deleteAttachment(
            @PathVariable Long attachmentId) {

        timesheetService.deleteAttachment(
                attachmentId
        );

        return ResponseEntity.ok(
                "Attachment deleted successfully"
        );
    }

}