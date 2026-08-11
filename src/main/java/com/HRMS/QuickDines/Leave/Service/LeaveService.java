package com.HRMS.QuickDines.Leave.Service;

import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.CRM.repo.CustomerRepository;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.Service.EmployeeService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Leave.model.*;
import com.HRMS.QuickDines.Leave.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveEncashmentRepository leaveEncashmentRepository;
    private final LeaveCancellationRepository leaveCancellationRepository;
    private final CompanyRepository companyRepository;

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


    public String createLeaveType(LeaveType leaveType) {

        leaveTypeRepository.save(leaveType);

        String performedBy = getLoggedInEmployeeId();

        //String newValue = convertToJson(leaveType);

        auditLogsService.logCreate(
                "LEAVE",
                String.valueOf(leaveType.getId()),
                performedBy,
                leaveType.getId().toString(),
                "Leave Type created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_LEAVE_TYPE",
                "LEAVE",
                "Leave Type created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave Type created successfully"
        );

        return "Leave Type Created Successfully";
    }


    public Object getAllLeaveTypes() {

        return leaveTypeRepository.findAll();
    }


    public Object getLeaveType(Long id) {

        return leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));
    }


    public String updateLeaveType(Long id, LeaveType leaveType) {

        LeaveType existingLeaveType =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Type Not Found"));

        String oldValue =
                convertToJson(existingLeaveType);

        existingLeaveType.setLeaveName(
                leaveType.getLeaveName());

        existingLeaveType.setTotalLeaves(
                leaveType.getTotalLeaves());

        existingLeaveType.setDescription(
                leaveType.getDescription());

        existingLeaveType.setStatus(
                leaveType.getStatus());

        LeaveType updatedLeaveType =
                leaveTypeRepository.save(existingLeaveType);

        String newValue =
                convertToJson(updatedLeaveType);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "LEAVE",
                String.valueOf(id),
                performedBy,
                null,
                "Leave Type updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_LEAVE_TYPE",
                "LEAVE",
                "Leave Type updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave Type updated successfully"
        );

        return "Leave Type Updated Successfully";
    }


    public String deleteLeaveType(Long id) {

        LeaveType existingLeaveType =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Type Not Found"));

        String deletedValue =
                convertToJson(existingLeaveType);

        String performedBy =
                getLoggedInEmployeeId();

        leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog(
                "LEAVE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingLeaveType.getId().toString(),
                "Leave Type deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LEAVE_TYPE",
                "LEAVE",
                "Leave Type deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave Type deleted successfully"
        );

        return "Leave Type Deleted Successfully";
    }


//==================================
// LEAVE REQUESTS
//==================================


    public String applyLeave(
            String employeeId,
            LeaveRequest leaveRequest) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        leaveRequest.setEmployee(employee);

        leaveRequest.setStatus("PENDING");

        LeaveRequest savedRequest =
                leaveRequestRepository.save(leaveRequest);

        String performedBy =
                getLoggedInEmployeeId();

        String newValue =
                convertToJson(savedRequest);

        auditLogsService.logCreate(
                "LEAVE",
                String.valueOf(savedRequest.getId()),
                performedBy,
                employeeId,
                "Leave request created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_LEAVE_REQUEST",
                "LEAVE",
                "Employee applied for leave",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave request created successfully"
        );

        return "Leave Applied Successfully";
    }


    public Object getAllRequests() {

        return leaveRequestRepository.findAll();
    }


    public Object getLeaveRequest(Long id) {

        return leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
    }


    public String updateLeaveRequest(
            Long id,
            LeaveRequest leaveRequest) {

        LeaveRequest existingRequest =
                leaveRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Request Not Found"));

        String oldValue =
                convertToJson(existingRequest);

        existingRequest.setFromDate(
                leaveRequest.getFromDate());

        existingRequest.setToDate(
                leaveRequest.getToDate());

        existingRequest.setNumberOfDays(
                leaveRequest.getNumberOfDays());

        existingRequest.setReason(
                leaveRequest.getReason());

        existingRequest.setRemarks(
                leaveRequest.getRemarks());

        LeaveRequest updatedRequest =
                leaveRequestRepository.save(existingRequest);

        String newValue =
                convertToJson(updatedRequest);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existingRequest.getEmployee() != null
                        ? existingRequest.getEmployee().getEmployeeId()
                        : null;

        auditLogsService.logUpdate(
                "LEAVE",
                String.valueOf(id),
                performedBy,
                employeeId,
                "Leave request updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_LEAVE_REQUEST",
                "LEAVE",
                "Leave request updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave request updated successfully"
        );

        return "Leave Request Updated Successfully";
    }


    public String deleteLeaveRequest(Long id) {

        LeaveRequest existingRequest =
                leaveRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Request Not Found"));

        String deletedValue =
                convertToJson(existingRequest);

        String performedBy =
                getLoggedInEmployeeId();

        String employeeId =
                existingRequest.getEmployee() != null
                        ? existingRequest.getEmployee().getEmployeeId()
                        : null;

        leaveRequestRepository.delete(existingRequest);

        auditLogsService.createAuditLog(
                "LEAVE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingRequest.getId().toString(),
                "Leave request deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LEAVE_REQUEST",
                "LEAVE",
                "Leave request deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave request deleted successfully"
        );

        return "Leave Request Deleted Successfully";
    }


//==================================
// LEAVE BALANCES
//==================================

    public String createLeaveBalance(String employeeId, LeaveBalance leaveBalance) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        leaveBalance.setEmployee(employee);

        leaveBalanceRepository.save(leaveBalance);


        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "LEAVE",
                String.valueOf(employee.getId()),
                performedBy,
                employee.getId().toString(),
                "Leave balance created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_BALANCE_TYPE",
                "LEAVE",
                "Leave balance created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave balance created successfully"
        );

        return "Leave Balance Created Successfully";
    }


    public Object getLeaveBalance(String employeeId) {

        return leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Leave Balance Not Found"));
    }


    public String updateLeaveBalance(String employeeId, LeaveBalance leaveBalance) {

        LeaveBalance existingBalance = (LeaveBalance) leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException(
                "Leave Balance Not Found"));
        existingBalance.setTotalLeaves(leaveBalance.getTotalLeaves());

        existingBalance.setUsedLeaves(leaveBalance.getUsedLeaves());

        existingBalance.setAvailableLeaves(leaveBalance.getAvailableLeaves());

        existingBalance.setPaidLeaves(leaveBalance.getPaidLeaves());

        existingBalance.setUnpaidLeaves(leaveBalance.getUnpaidLeaves());

        leaveBalanceRepository.save(existingBalance);
        LeaveBalance updatedBalance =
                leaveBalanceRepository.save(existingBalance);
        String oldvalue = convertToJson(existingBalance);

        String newValue =
                convertToJson(updatedBalance);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "LEAVE",
                employeeId,
                performedBy,
                employeeId,
                "Leave balance updated successfully",
                oldvalue,
                newValue
        );

        return "Leave Balance Updated Successfully";
    }


    public String approveLeave(Long leaveRequestId, String approvedBy) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() ->
                new RuntimeException("Leave Request Not Found"));
        Employee approvedByEmployee = employeeRepository.findById(approvedBy).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // Update Leave Request

        leaveRequest.setStatus("APPROVED");

        leaveRequestRepository.save(leaveRequest);


        // Save Leave Approval

        LeaveApproval approval = new LeaveApproval();

        approval.setLeaveRequest(leaveRequest);

        approval.setApprovedBy(approvedByEmployee);

        approval.setApprovedDate(LocalDate.now());

        approval.setStatus("APPROVED");

        leaveApprovalRepository.save(approval);

        // Update Leave Balance

        Employee employee = leaveRequest.getEmployee();

        LeaveBalance balance = (LeaveBalance) leaveBalanceRepository.findByEmployeeId(employee.getEmployeeId()).orElseThrow(() ->
                new RuntimeException("Leave Balance Not Found"));


        Integer usedLeaves = balance.getUsedLeaves() + leaveRequest.getNumberOfDays();

        Integer availableLeaves = balance.getAvailableLeaves() - leaveRequest.getNumberOfDays();

        balance.setUsedLeaves(usedLeaves);

        balance.setAvailableLeaves(availableLeaves);

        leaveBalanceRepository.save(balance);

        // Send Mail

        emailService.sendMail(

                employee.getEmail(),

                "Leave Approved",

                "Hi " + employee.getFirstName()
                        + ",\n\n"
                        + "Your leave request has been approved."
                        + "\n\n"
                        + "Leave Type : "
                        + leaveRequest.getLeaveType()
                        .getLeaveName()
                        + "\n"
                        + "From Date : "
                        + leaveRequest.getFromDate()
                        + "\n"
                        + "To Date : "
                        + leaveRequest.getToDate()
                        + "\n\n"
                        + "Thank You."
        );
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logApprove(
                "LEAVE",
                String.valueOf(leaveRequestId),
                performedBy,
                leaveRequest.getEmployee().getEmployeeId(),
                "Leave request approved"

        );
        return "Leave Approved Successfully";
    }

    public String rejectLeave(Long leaveRequestId, String reason, String approvedBy) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() ->
                new RuntimeException("Leave Request Not Found"));
        Employee approvedByEmployee = employeeRepository.findById(approvedBy).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        // Update Leave Request

        leaveRequest.setStatus("REJECTED");

        leaveRequest.setRemarks(reason);

        leaveRequestRepository.save(leaveRequest);

        // Save Approval Table

        LeaveApproval approval = new LeaveApproval();

        approval.setLeaveRequest(leaveRequest);

        approval.setApprovedBy(approvedByEmployee);

        approval.setApprovedDate(LocalDate.now());

        approval.setRejectionReason(reason);

        approval.setStatus("REJECTED");


        leaveApprovalRepository.save(approval);

        // Send Mail

        Employee employee = leaveRequest.getEmployee();


        emailService.sendMail(

                employee.getEmail(),

                "Leave Rejected",

                "Hi " + employee.getFirstName()
                        + ",\n\n"
                        + "Your leave request has been rejected."
                        + "\n\n"
                        + "Reason : "
                        + reason
                        + "\n\n"
                        + "Please contact HR for more information."

        );
        String newValue =
                convertToJson(leaveRequest);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logReject(
                "LEAVE",
                String.valueOf(leaveRequestId),
                performedBy,
                leaveRequest.getEmployee().getEmployeeId(),
                "Leave request rejected"

        );


        return "Leave Rejected Successfully";

    }

    public Object getApprovals() {

        return leaveApprovalRepository.findAll();

    }

    public Object getApproval(Long id) {

        return leaveApprovalRepository.findById(id).orElseThrow(() -> new RuntimeException("Approval Not Found"));

    }


//==================================
// REPORTS
//==================================

    public LeaveRequest getEmployeeLeaves(
            String employeeId) {

        return leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
    }


    public List<LeaveRequest> getPendingLeaves() {

        return leaveRequestRepository.findByStatus("PENDING");
    }


    public List<LeaveRequest> getApprovedLeaves() {

        return leaveRequestRepository.findByStatus("APPROVED");
    }


    public List<LeaveRequest> getRejectedLeaves() {

        return leaveRequestRepository.findByStatus("REJECTED");
    }

//=========================================================
// LEAVE POLICIES
//=========================================================

    public String createLeavePolicy(
            Long companyId,
            LeavePolicy leavePolicy) {

        // If Company entity is available:
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));

        leavePolicy.setCompany(company);

        leavePolicyRepository.save(leavePolicy);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "LEAVE",
                String.valueOf(leavePolicy.getId()),
                performedBy,
                leavePolicy.getId().toString(),
                "Leave policy created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_POLICY_TYPE",
                "LEAVE",
                "Leave policy created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave policy created successfully"
        );

        return "Leave Policy Created Successfully";
    }


    public List<LeavePolicy> getLeavePolicies() {

        return leavePolicyRepository.findAll();
    }


    public LeavePolicy getLeavePolicy(Long id) {

        return leavePolicyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Leave Policy Not Found"));
    }


    public String updateLeavePolicy(
            Long id,
            LeavePolicy leavePolicy) {

        LeavePolicy existingPolicy =
                leavePolicyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Policy Not Found"));

        existingPolicy.setPolicyName(
                leavePolicy.getPolicyName());

        existingPolicy.setAnnualLimit(
                leavePolicy.getAnnualLimit());

        existingPolicy.setCarryForwardLimit(
                leavePolicy.getCarryForwardLimit());

        existingPolicy.setEncashmentAllowed(
                leavePolicy.getEncashmentAllowed());

        existingPolicy.setApprovalRequired(
                leavePolicy.getApprovalRequired());

        existingPolicy.setStatus(
                leavePolicy.getStatus());

        leavePolicyRepository.save(existingPolicy);
        // OLD JSON
        String oldValue = convertToJson(existingPolicy);
        existingPolicy.setPolicyName(leavePolicy.getPolicyName());
        existingPolicy.setAnnualLimit(leavePolicy.getAnnualLimit());
        existingPolicy.setCarryForwardLimit(leavePolicy.getCarryForwardLimit());
        existingPolicy.setEncashmentAllowed(leavePolicy.getEncashmentAllowed());
        existingPolicy.setApprovalRequired(leavePolicy.getApprovalRequired());
        existingPolicy.setStatus(leavePolicy.getStatus());
        LeavePolicy updatedPolicy = leavePolicyRepository.save(existingPolicy);
        // NEW JSON
        String newValue = convertToJson(updatedPolicy);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logUpdate("LEAVE", String.valueOf(id), performedBy, existingPolicy.getId().toString(), "Leave Policy updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_LEAVE_POLICY", "LEAVE", "Leave Policy updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave Policy updated successfully");

        return "Leave Policy Updated Successfully";
    }


    public String deleteLeavePolicy(Long id) {

        LeavePolicy existingPolicy =
                leavePolicyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Policy Not Found"));

        leavePolicyRepository.delete(existingPolicy);
        String deletedValue =
                convertToJson(existingPolicy);

        String performedBy =
                getLoggedInEmployeeId();

//        //String employeeId =
//                existingPolicy.getId() != null
//                        ? existingPolicy.getEmployee().getEmployeeId()
//                        : null;

       // leaveRequestRepository.delete(existingRequest);

        auditLogsService.createAuditLog(
                "LEAVE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingPolicy.getId().toString(),
                "Leave policy deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LEAVE_POLICY",
                "LEAVE",
                "Leave policy deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave policy deleted successfully"
        );

        return "Leave Policy Deleted Successfully";
    }


//=========================================================
// LEAVE ENCASHMENT
//=========================================================

    public String createLeaveEncashment(
            String employeeId,
            LeaveEncashment leaveEncashment) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        leaveEncashment.setEmployee(employee);

        if (leaveEncashment.getStatus() == null) {
            leaveEncashment.setStatus("PENDING");
        }

        leaveEncashmentRepository.save(leaveEncashment);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "LEAVE",
                String.valueOf(employee.getId()),
                performedBy,
              employeeId,
                "Leave encashment created successfully"

        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_ENCASHMENT_TYPE",
                "LEAVE",
                "Leave encashment created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave encashment created successfully"
        );

        return "Leave Encashment Request Created Successfully";
    }


    public List<LeaveEncashment> getLeaveEncashments() {

        return leaveEncashmentRepository.findAll();
    }


    public LeaveEncashment getLeaveEncashment(Long id) {

        return leaveEncashmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Leave Encashment Not Found"));
    }


    public String updateLeaveEncashment(
            Long id,
            LeaveEncashment leaveEncashment) {

        LeaveEncashment existingEncashment =
                leaveEncashmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Encashment Not Found"));

        existingEncashment.setEncashedDays(
                leaveEncashment.getEncashedDays());

        existingEncashment.setAmount(
                leaveEncashment.getAmount());

        existingEncashment.setStatus(
                leaveEncashment.getStatus());

        existingEncashment.setApprovedBy(
                leaveEncashment.getApprovedBy());

       // leaveEncashmentRepository.save(existingEncashment);

        // OLD JSON
        String oldValue = convertToJson(existingEncashment);

       LeaveEncashment updatedleave = leaveEncashmentRepository.save(existingEncashment);
        // NEW JSON
        String newValue = convertToJson(updatedleave);
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logUpdate("LEAVE", String.valueOf(id), performedBy, existingEncashment.getId().toString(), "Leave Encashment updated successfully", oldValue, newValue);
        auditLogsService.logActivity(performedBy, "UPDATE_LEAVE_ENCASHMENT", "LEAVE", "Leave Encashment updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());
        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave Encashment updated successfully");

        return "Leave Encashment Updated Successfully";
    }


    public String deleteLeaveEncashment(Long id) {

        LeaveEncashment existingEncashment =
                leaveEncashmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Encashment Not Found"));

        leaveEncashmentRepository.delete(existingEncashment);

        return "Leave Encashment Deleted Successfully";
    }


//=========================================================
// LEAVE CANCELLATIONS
//=========================================================

    public String createLeaveCancellation(
            Long leaveRequestId,
            String employeeId,
            LeaveCancellation leaveCancellation) {

        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(leaveRequestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Request Not Found"));

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        leaveCancellation.setLeaveRequest(leaveRequest);
        leaveCancellation.setEmployee(employee);

        if (leaveCancellation.getCancellationDate() == null) {
            leaveCancellation.setCancellationDate(
                    LocalDate.now());
        }

        if (leaveCancellation.getStatus() == null) {
            leaveCancellation.setStatus("PENDING");
        }

        leaveCancellationRepository.save(leaveCancellation);

        return "Leave Cancellation Request Created Successfully";
    }


    public List<LeaveCancellation> getLeaveCancellations() {

        return leaveCancellationRepository.findAll();
    }


    public LeaveCancellation getLeaveCancellation(Long id) {

        return leaveCancellationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Leave Cancellation Not Found"));
    }


    public String updateLeaveCancellation(
            Long id,
            LeaveCancellation leaveCancellation) {

        LeaveCancellation existingCancellation =
                leaveCancellationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Cancellation Not Found"));

        existingCancellation.setCancellationReason(
                leaveCancellation.getCancellationReason());

        existingCancellation.setCancellationDate(
                leaveCancellation.getCancellationDate());

        existingCancellation.setRefundLeaveDays(
                leaveCancellation.getRefundLeaveDays());

        existingCancellation.setStatus(
                leaveCancellation.getStatus());

        existingCancellation.setCancelledBy(
                leaveCancellation.getCancelledBy());

        leaveCancellationRepository.save(existingCancellation);

        return "Leave Cancellation Updated Successfully";
    }


    public String deleteLeaveCancellation(Long id) {

        LeaveCancellation existingCancellation =
                leaveCancellationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Cancellation Not Found"));

        leaveCancellationRepository.delete(existingCancellation);
        String deletedValue =
                convertToJson(existingCancellation);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.createAuditLog(
                "LEAVE",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingCancellation.getId().toString(),
                "Leave cancellation deleted successfully",
                deletedValue,
                null,
                getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_LEAVE_CANCELLATION",
                "LEAVE",
                "Leave cancellation deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "LEAVE",
                "LeaveService",
                "Leave cancellation deleted successfully"
        );

        return "Leave Cancellation Deleted Successfully";
    }


//=========================================================
// LEAVE ENCASHMENT REPORTS
//=========================================================

    public List<LeaveEncashment> getPendingEncashments() {

        return leaveEncashmentRepository.findAll()
                .stream()
                .filter(encashment ->
                        "PENDING".equalsIgnoreCase(
                                encashment.getStatus()))
                .toList();
    }


    public List<LeaveEncashment> getApprovedEncashments() {

        return leaveEncashmentRepository.findAll()
                .stream()
                .filter(encashment ->
                        "APPROVED".equalsIgnoreCase(
                                encashment.getStatus()))
                .toList();
    }


//=========================================================
// LEAVE CANCELLATION REPORTS
//=========================================================

    public List<LeaveCancellation> getPendingCancellations() {

        return leaveCancellationRepository.findAll()
                .stream()
                .filter(cancellation ->
                        "PENDING".equalsIgnoreCase(
                                cancellation.getStatus()))
                .toList();
    }


    public List<LeaveCancellation> getApprovedCancellations() {

        return leaveCancellationRepository.findAll()
                .stream()
                .filter(cancellation ->
                        "APPROVED".equalsIgnoreCase(
                                cancellation.getStatus()))
                .toList();
    }


//=========================================================
// ACTIVE LEAVE POLICIES
//=========================================================

    public List<LeavePolicy> getActiveLeavePolicies() {

        return leavePolicyRepository.findAll()
                .stream()
                .filter(policy ->
                        "ACTIVE".equalsIgnoreCase(
                                policy.getStatus()))
                .toList();
    }
// =========================================================
// FILTER EMPLOYEES BY LEAVE TYPE
// =========================================================

    public List<LeaveRequest> getEmployeesByLeaveType(
            Long leaveTypeId) {

        return leaveRequestRepository
                .findByLeaveTypeId(leaveTypeId);
    }

}
