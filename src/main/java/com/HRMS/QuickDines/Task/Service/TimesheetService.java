package com.HRMS.QuickDines.Task.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Task.DTO.*;
import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import com.HRMS.QuickDines.Task.repo.*;
import com.HRMS.QuickDines.Task.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final EmployeeTimesheetRepository timesheetRepository;
    private final TimesheetTaskRepository taskRepository;
    private final TimesheetApprovalRepository approvalRepository;
    private final TimesheetAttachmentRepository attachmentRepository;
    private final EmployeeRepository employeeRepository;


    // =====================================================
    // CREATE TIMESHEET
    // =====================================================

    @Transactional
    public EmployeeTimesheet createTimesheet(
            TimesheetRequest request) {

        Employee employee = getEmployee(request.getEmployeeId());

        if (timesheetRepository
                .findByEmployee_IdAndWorkDate(
                        request.getEmployeeId(),
                        request.getWorkDate())
                .isPresent()) {

            throw new RuntimeException(
                    "Timesheet already exists for this employee on "
                            + request.getWorkDate()
            );
        }

        EmployeeTimesheet timesheet =
                new EmployeeTimesheet();

        timesheet.setEmployee(employee);
        timesheet.setWorkDate(request.getWorkDate());
        timesheet.setStartTime(request.getStartTime());
        timesheet.setEndTime(request.getEndTime());
        timesheet.setBreakMinutes(request.getBreakMinutes());
        timesheet.setProjectName(request.getProjectName());
        timesheet.setWorkDescription(
                request.getWorkDescription()
        );

        timesheet.setStatus(
                TimesheetStatus.DRAFT
        );

        calculateTotalHours(timesheet);

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<EmployeeTimesheet> getAllTimesheets() {

        return timesheetRepository.findAll();
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public EmployeeTimesheet getTimesheet(Long id) {

        return timesheetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timesheet not found: " + id
                        )
                );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public EmployeeTimesheet updateTimesheet(
            Long id,
            TimesheetRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        if (timesheet.getStatus()
                == TimesheetStatus.APPROVED) {

            throw new RuntimeException(
                    "Approved timesheet cannot be modified"
            );
        }

        timesheet.setWorkDate(
                request.getWorkDate()
        );

        timesheet.setStartTime(
                request.getStartTime()
        );

        timesheet.setEndTime(
                request.getEndTime()
        );

        timesheet.setBreakMinutes(
                request.getBreakMinutes()
        );

        timesheet.setProjectName(
                request.getProjectName()
        );

        timesheet.setWorkDescription(
                request.getWorkDescription()
        );

        calculateTotalHours(timesheet);

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deleteTimesheet(Long id) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        if (timesheet.getStatus()
                == TimesheetStatus.APPROVED) {

            throw new RuntimeException(
                    "Approved timesheet cannot be deleted"
            );
        }

        timesheetRepository.delete(timesheet);
    }


    // =====================================================
    // EMPLOYEE TIMESHEETS
    // =====================================================

    public List<EmployeeTimesheet> getEmployeeTimesheets(
            String employeeId) {

        return timesheetRepository
                .findByEmployee_Id(employeeId);
    }


    // =====================================================
    // DATE
    // =====================================================

    public List<EmployeeTimesheet> getByDate(
            LocalDate date) {

        return timesheetRepository
                .findByWorkDate(date);
    }


    // =====================================================
    // EMPLOYEE + DATE
    // =====================================================

    public EmployeeTimesheet getEmployeeDate(
            String employeeId,
            LocalDate date) {

        return timesheetRepository
                .findByEmployee_IdAndWorkDate(
                        employeeId,
                        date
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timesheet not found"
                        )
                );
    }


    // =====================================================
    // STATUS
    // =====================================================

    public List<EmployeeTimesheet> getByStatus(
            TimesheetStatus status) {

        return timesheetRepository
                .findByStatus(status);
    }


    // =====================================================
    // DRAFTS
    // =====================================================

    public List<EmployeeTimesheet> getDrafts() {

        return getByStatus(
                TimesheetStatus.DRAFT
        );
    }


    // =====================================================
    // SUBMITTED
    // =====================================================

    public List<EmployeeTimesheet> getSubmitted() {

        return getByStatus(
                TimesheetStatus.SUBMITTED
        );
    }


    // =====================================================
    // PENDING
    // =====================================================

    public List<EmployeeTimesheet> getPending() {

        return getByStatus(
                TimesheetStatus.PENDING
        );
    }


    // =====================================================
    // APPROVED
    // =====================================================

    public List<EmployeeTimesheet> getApproved() {

        return getByStatus(
                TimesheetStatus.APPROVED
        );
    }


    // =====================================================
    // REJECTED
    // =====================================================

    public List<EmployeeTimesheet> getRejected() {

        return getByStatus(
                TimesheetStatus.REJECTED
        );
    }


    // =====================================================
    // SUBMIT
    // =====================================================

    @Transactional
    public EmployeeTimesheet submitTimesheet(
            Long id) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        if (timesheet.getStatus()
                != TimesheetStatus.DRAFT
                && timesheet.getStatus()
                != TimesheetStatus.RETURNED) {

            throw new RuntimeException(
                    "Only draft or returned timesheets can be submitted"
            );
        }

        timesheet.setStatus(
                TimesheetStatus.SUBMITTED
        );

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // APPROVE
    // =====================================================

    @Transactional
    public EmployeeTimesheet approveTimesheet(
            Long id,
            TimesheetApprovalRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        if (timesheet.getStatus()
                != TimesheetStatus.SUBMITTED
                && timesheet.getStatus()
                != TimesheetStatus.PENDING) {

            throw new RuntimeException(
                    "Timesheet is not awaiting approval"
            );
        }

        Employee approver =
                getEmployee(request.getApproverId());

        timesheet.setStatus(
                TimesheetStatus.APPROVED
        );

        timesheet.setApprovedBy(approver);
        timesheet.setApprovedAt(
                LocalDateTime.now()
        );

        TimesheetApproval approval =
                new TimesheetApproval();

        approval.setTimesheet(timesheet);
        approval.setApprover(approver);
        approval.setStatus(
                TimesheetStatus.APPROVED
        );
        approval.setRemarks(
                request.getRemarks()
        );
        approval.setActionAt(
                LocalDateTime.now()
        );

        approvalRepository.save(approval);

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // REJECT
    // =====================================================

    @Transactional
    public EmployeeTimesheet rejectTimesheet(
            Long id,
            TimesheetApprovalRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        Employee approver =
                getEmployee(request.getApproverId());

        timesheet.setStatus(
                TimesheetStatus.REJECTED
        );

        timesheet.setRejectionReason(
                request.getRemarks()
        );

        TimesheetApproval approval =
                new TimesheetApproval();

        approval.setTimesheet(timesheet);
        approval.setApprover(approver);
        approval.setStatus(
                TimesheetStatus.REJECTED
        );
        approval.setRemarks(
                request.getRemarks()
        );
        approval.setActionAt(
                LocalDateTime.now()
        );

        approvalRepository.save(approval);

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // RETURN
    // =====================================================

    @Transactional
    public EmployeeTimesheet returnTimesheet(
            Long id,
            TimesheetApprovalRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(id);

        Employee approver =
                getEmployee(request.getApproverId());

        timesheet.setStatus(
                TimesheetStatus.RETURNED
        );

        TimesheetApproval approval =
                new TimesheetApproval();

        approval.setTimesheet(timesheet);
        approval.setApprover(approver);
        approval.setStatus(
                TimesheetStatus.RETURNED
        );
        approval.setRemarks(
                request.getRemarks()
        );
        approval.setActionAt(
                LocalDateTime.now()
        );

        approvalRepository.save(approval);

        return timesheetRepository.save(timesheet);
    }


    // =====================================================
    // ADD TASK
    // =====================================================

    @Transactional
    public TimesheetTask addTask(
            Long timesheetId,
            TimesheetTaskRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(timesheetId);

        TimesheetTask task =
                new TimesheetTask();

        task.setTimesheet(timesheet);
        task.setProjectName(
                request.getProjectName()
        );
        task.setTaskName(
                request.getTaskName()
        );
        task.setWorkDescription(
                request.getWorkDescription()
        );
        task.setStartTime(
                request.getStartTime()
        );
        task.setEndTime(
                request.getEndTime()
        );
        task.setBreakMinutes(
                request.getBreakMinutes()
        );
        task.setTaskStatus(
                request.getTaskStatus()
        );

        task.setHours(
                calculateTaskHours(request)
        );

        return taskRepository.save(task);
    }


    // =====================================================
    // GET TASKS
    // =====================================================

    public List<TimesheetTask> getTasks(
            Long timesheetId) {

        getTimesheet(timesheetId);

        return taskRepository
                .findByTimesheet_Id(timesheetId);
    }


    // =====================================================
    // GET TASK
    // =====================================================

    public TimesheetTask getTask(Long taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timesheet task not found"
                        )
                );
    }


    // =====================================================
    // UPDATE TASK
    // =====================================================

    @Transactional
    public TimesheetTask updateTask(
            Long taskId,
            TimesheetTaskRequest request) {

        TimesheetTask task =
                getTask(taskId);

        task.setProjectName(
                request.getProjectName()
        );

        task.setTaskName(
                request.getTaskName()
        );

        task.setWorkDescription(
                request.getWorkDescription()
        );

        task.setStartTime(
                request.getStartTime()
        );

        task.setEndTime(
                request.getEndTime()
        );

        task.setBreakMinutes(
                request.getBreakMinutes()
        );

        task.setTaskStatus(
                request.getTaskStatus()
        );

        task.setHours(
                calculateTaskHours(request)
        );

        return taskRepository.save(task);
    }


    // =====================================================
    // DELETE TASK
    // =====================================================

    @Transactional
    public void deleteTask(Long taskId) {

        TimesheetTask task =
                getTask(taskId);

        taskRepository.delete(task);
    }


    // =====================================================
    // GET APPROVALS
    // =====================================================

    public List<TimesheetApproval> getApprovals(
            Long timesheetId) {

        getTimesheet(timesheetId);

        return approvalRepository
                .findByTimesheet_Id(timesheetId);
    }


    // =====================================================
    // PENDING APPROVALS
    // =====================================================

    public List<TimesheetApproval> getPendingApprovals() {

        return approvalRepository
                .findByStatus(
                        TimesheetStatus.PENDING
                );
    }


    // =====================================================
    // APPROVER PENDING
    // =====================================================

    public List<TimesheetApproval>
    getApproverPending(String employeeId) {

        return approvalRepository
                .findByApprover_IdAndStatus(
                        employeeId,
                        TimesheetStatus.PENDING
                );
    }


    // =====================================================
    // EMPLOYEE APPROVALS
    // =====================================================

    public List<TimesheetApproval>
    getEmployeeApprovals(String employeeId) {

        return approvalRepository
                .findByTimesheet_Employee_Id(
                        employeeId
                );
    }


    // =====================================================
    // ADD ATTACHMENT
    // =====================================================

    @Transactional
    public TimesheetAttachment addAttachment(
            Long timesheetId,
            TimesheetAttachmentRequest request) {

        EmployeeTimesheet timesheet =
                getTimesheet(timesheetId);

        TimesheetAttachment attachment =
                new TimesheetAttachment();

        attachment.setTimesheet(timesheet);
        attachment.setFileName(
                request.getFileName()
        );
        attachment.setFileUrl(
                request.getFileUrl()
        );
        attachment.setFileType(
                request.getFileType()
        );
        attachment.setFileSize(
                request.getFileSize()
        );

        return attachmentRepository.save(
                attachment
        );
    }


    // =====================================================
    // GET ATTACHMENTS
    // =====================================================

    public List<TimesheetAttachment>
    getAttachments(Long timesheetId) {

        getTimesheet(timesheetId);

        return attachmentRepository
                .findByTimesheet_Id(timesheetId);
    }


    // =====================================================
    // GET ATTACHMENT
    // =====================================================

    public TimesheetAttachment getAttachment(
            Long attachmentId) {

        return attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attachment not found"
                        )
                );
    }


    // =====================================================
    // DELETE ATTACHMENT
    // =====================================================

    @Transactional
    public void deleteAttachment(
            Long attachmentId) {

        TimesheetAttachment attachment =
                getAttachment(attachmentId);

        attachmentRepository.delete(attachment);
    }


    // =====================================================
    // CALCULATE HOURS
    // =====================================================

    private void calculateTotalHours(
            EmployeeTimesheet timesheet) {

        if (timesheet.getStartTime() == null
                || timesheet.getEndTime() == null) {

            timesheet.setTotalHours(
                    BigDecimal.ZERO
            );

            return;
        }

        long minutes =
                Duration.between(
                        timesheet.getStartTime(),
                        timesheet.getEndTime()
                ).toMinutes();

        int breakMinutes =
                timesheet.getBreakMinutes() == null
                        ? 0
                        : timesheet.getBreakMinutes();

        minutes -= breakMinutes;

        BigDecimal hours =
                BigDecimal.valueOf(minutes)
                        .divide(
                                BigDecimal.valueOf(60),
                                2,
                                RoundingMode.HALF_UP
                        );

        timesheet.setTotalHours(hours);
    }


    // =====================================================
    // TASK HOURS
    // =====================================================

    private BigDecimal calculateTaskHours(
            TimesheetTaskRequest request) {

        if (request.getStartTime() == null
                || request.getEndTime() == null) {

            return BigDecimal.ZERO;
        }

        long minutes =
                Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes();

        int breakMinutes =
                request.getBreakMinutes() == null
                        ? 0
                        : request.getBreakMinutes();

        minutes -= breakMinutes;

        return BigDecimal.valueOf(minutes)
                .divide(
                        BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // =====================================================
    // EMPLOYEE HELPER
    // =====================================================

    private Employee getEmployee(
            String employeeId) {

        return employeeRepository
                .findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found: "
                                        + employeeId
                        )
                );
    }
}