package com.HRMS.QuickDines.Leave.Service;

import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Leave.DTO.LeaveRequestResponse;
import com.HRMS.QuickDines.Leave.DTO.LeaveTypeRequest;
import com.HRMS.QuickDines.Leave.model.*;
import com.HRMS.QuickDines.Leave.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    private final BranchRepository branchRepository;

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

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


    public String createLeaveType(LeaveTypeRequest request) {

        // Find Company
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company Not Found: " + request.getCompanyId()));

        // Find Branch
        Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new RuntimeException("Branch Not Found: " + request.getBranchId()));

        // Create LeaveType entity
        LeaveType leaveType = new LeaveType();

        leaveType.setLeaveName(request.getLeaveName());
        leaveType.setTotalLeaves(request.getTotalLeaves());
        leaveType.setDescription(request.getDescription());
        leaveType.setStatus(request.getStatus());

        // Set relationships
        leaveType.setCompany(company);
        leaveType.setBranch(branch);

        // Save
        leaveTypeRepository.save(leaveType);

//        String performedBy = getLoggedInEmployeeId();
//
//        // Audit Log
//        auditLogsService.logCreate(
//                "LEAVE",
//                String.valueOf(leaveType.getId()),
//                performedBy,
//                leaveType.getId().toString(),
//                "Leave Type created successfully"
//        );
//
//        // Activity Log
//        auditLogsService.logActivity(
//                performedBy,
//                "CREATE_LEAVE_TYPE",
//                "LEAVE",
//                "Leave Type created successfully",
//                ActivityStatus.SUCCESS,
//                getIpAddress(),
//                getBrowser(),
//                getOperatingSystem()
//        );
//
//        // System Log
//        auditLogsService.logInfo(
//                "LEAVE",
//                "LeaveService",
//                "Leave Type created successfully. ID: "
//                        + leaveType.getId()
//        );

        return "Leave Type Created Successfully";
    }

    public Object getAllLeaveTypes() {

        return leaveTypeRepository.findAll();
    }


    public Object getLeaveType(Long id) {

        return leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));
    }


    public String updateLeaveType(Long id, LeaveType leaveType) {

        LeaveType existingLeaveType = leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));

        String oldValue = convertToJson(existingLeaveType);

        existingLeaveType.setLeaveName(leaveType.getLeaveName());

        existingLeaveType.setTotalLeaves(leaveType.getTotalLeaves());

        existingLeaveType.setDescription(leaveType.getDescription());

        existingLeaveType.setStatus(leaveType.getStatus());

        LeaveType updatedLeaveType = leaveTypeRepository.save(existingLeaveType);

        String newValue = convertToJson(updatedLeaveType);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("LEAVE", String.valueOf(id), performedBy, null, "Leave Type updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_LEAVE_TYPE", "LEAVE", "Leave Type updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave Type updated successfully");

        return "Leave Type Updated Successfully";
    }


    public String deleteLeaveType(Long id) {

        LeaveType existingLeaveType = leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));

        String deletedValue = convertToJson(existingLeaveType);

        String performedBy = getLoggedInEmployeeId();

        leaveTypeRepository.delete(existingLeaveType);

        auditLogsService.createAuditLog("LEAVE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingLeaveType.getId().toString(), "Leave Type deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(performedBy, "DELETE_LEAVE_TYPE", "LEAVE", "Leave Type deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave Type deleted successfully");

        return "Leave Type Deleted Successfully";
    }

    public String createLeaveTypes(List<LeaveTypeRequest> requests) {

        List<LeaveType> leaveTypes = new ArrayList<>();

        for (LeaveTypeRequest request : requests) {

            Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company Not Found: " + request.getCompanyId()));

            Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new RuntimeException("Branch Not Found: " + request.getBranchId()));

            LeaveType leaveType = new LeaveType();

            leaveType.setLeaveName(request.getLeaveName());
            leaveType.setTotalLeaves(request.getTotalLeaves());
            leaveType.setDescription(request.getDescription());
            leaveType.setStatus(request.getStatus());

            leaveType.setCompany(company);
            leaveType.setBranch(branch);

            leaveTypes.add(leaveType);
        }

        leaveTypeRepository.saveAll(leaveTypes);

        return leaveTypes.size() + " Leave Types Created Successfully";
    }


//==================================
// LEAVE REQUESTS
//==================================


    @Transactional
    public Object applyLeave(String employeeId, LeaveRequest leaveRequest) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // =====================================================
        // 2. VALIDATE DATES
        // =====================================================

        if (leaveRequest.getFromDate() == null || leaveRequest.getToDate() == null) {

            throw new RuntimeException("From date and To date are required");
        }

        if (leaveRequest.getToDate().isBefore(leaveRequest.getFromDate())) {

            throw new RuntimeException("To date cannot be before From date");
        }

        if (leaveRequest.getLeaveType() == null || leaveRequest.getLeaveType().getId() == null) {

            throw new RuntimeException("Leave type is required");
        }

        Long leaveTypeId = leaveRequest.getLeaveType().getId();

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId).orElseThrow(() -> new RuntimeException("Leave type not found with ID: " + leaveTypeId));

        leaveRequest.setLeaveType(leaveType);

        // =====================================================
        // 3. CALCULATE NUMBER OF DAYS
        // =====================================================

        long days = ChronoUnit.DAYS.between(leaveRequest.getFromDate(), leaveRequest.getToDate()) + 1;

        leaveRequest.setNumberOfDays((int) days);

        // =====================================================
        // 4. EMPLOYEE DEPARTMENT / ROLE
        // =====================================================

        String department = employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null;

        String role = employee.getRole() != null ? employee.getRole().getRoleName() : null;

        // =====================================================
        // 5. BASIC LEAVE DETAILS
        // =====================================================

        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus("PENDING");

        // =====================================================
        // 6. CREATE APPROVAL FLOW
        // =====================================================

        List<String> approvalRoles = new ArrayList<>();

        // -----------------------------------------------------
        // IT EMPLOYEE
        // ANY DAYS → HR
        // -----------------------------------------------------

        if ("IT".equalsIgnoreCase(department)) {

            approvalRoles.add("HR");
        }

        // -----------------------------------------------------
        // SALES EMPLOYEE
        // 1-3 DAYS → SALES MANAGER
        // -----------------------------------------------------

        else if ("SALES".equalsIgnoreCase(department) && days <= 3) {

            approvalRoles.add("SALES_MANAGER");
        }

        // -----------------------------------------------------
        // SALES EMPLOYEE
        // MORE THAN 3 DAYS
        // SALES MANAGER → HR
        // -----------------------------------------------------

        else if ("SALES".equalsIgnoreCase(department) && days > 3) {

            approvalRoles.add("SALES_MANAGER");
            approvalRoles.add("HR");
        }

        // -----------------------------------------------------
        // HR EMPLOYEE → SUPER ADMIN
        // -----------------------------------------------------

        else if ("HR".equalsIgnoreCase(role) || "HR".equalsIgnoreCase(department)) {

            approvalRoles.add("SUPER_ADMIN");
        }

        // -----------------------------------------------------
        // SALES MANAGER → SUPER ADMIN
        // -----------------------------------------------------

        else if ("SALES_MANAGER".equalsIgnoreCase(role)) {

            approvalRoles.add("SUPER_ADMIN");
        }

        // -----------------------------------------------------
        // SUPER ADMIN → DIRECT APPROVAL
        // -----------------------------------------------------

        else if ("SUPER_ADMIN".equalsIgnoreCase(role)) {

            leaveRequest.setStatus("APPROVED");
            leaveRequest.setApprovalLevel("COMPLETED");
            leaveRequest.setCurrentApprover(null);

            leaveRequest.setSalesManagerStatus("NOT_REQUIRED");

            leaveRequest.setHrStatus("NOT_REQUIRED");

            leaveRequest.setSuperAdminStatus("NOT_REQUIRED");

            return leaveRequestRepository.save(leaveRequest);
        }

        // -----------------------------------------------------
        // NO WORKFLOW
        // -----------------------------------------------------

        else {

            throw new RuntimeException("Leave approval workflow not configured for employee");
        }

        // =====================================================
        // 7. SAVE LEAVE REQUEST FIRST
        // =====================================================

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);

        // =====================================================
        // 8. CREATE LEAVE APPROVAL RECORDS
        // =====================================================

        for (int i = 0; i < approvalRoles.size(); i++) {

            String approvalRole = approvalRoles.get(i);

            LeaveApproval approval = new LeaveApproval();

            approval.setLeaveRequest(savedRequest);

            approval.setApprovalRole(approvalRole);

            approval.setApprovalOrder(i + 1);

            // First approval is active
            if (i == 0) {

                approval.setStatus("PENDING");

            } else {

                // Future approval waits
                approval.setStatus("WAITING");
            }

            leaveApprovalRepository.save(approval);
        }

        // =====================================================
        // 9. SET CURRENT APPROVER
        // =====================================================

        String firstApprover = approvalRoles.get(0);

        leaveRequest.setApprovalLevel(firstApprover);

        leaveRequest.setCurrentApprover(firstApprover);

        // =====================================================
        // 10. SET DISPLAY STATUS FIELDS
        // =====================================================

        leaveRequest.setHrStatus("NOT_REQUIRED");

        leaveRequest.setSalesManagerStatus("NOT_REQUIRED");

        leaveRequest.setSuperAdminStatus("NOT_REQUIRED");

        if ("HR".equalsIgnoreCase(firstApprover)) {

            leaveRequest.setHrStatus("PENDING");

        } else if ("SALES_MANAGER".equalsIgnoreCase(firstApprover)) {

            leaveRequest.setSalesManagerStatus("PENDING");

        } else if ("SUPER_ADMIN".equalsIgnoreCase(firstApprover)) {

            leaveRequest.setSuperAdminStatus("PENDING");
        }

        // =====================================================
        // 11. IF SECOND APPROVAL EXISTS
        // =====================================================

        if (approvalRoles.size() > 1) {

            String secondApprover = approvalRoles.get(1);

            if ("HR".equalsIgnoreCase(secondApprover)) {

                leaveRequest.setHrStatus("WAITING");

            } else if ("SALES_MANAGER".equalsIgnoreCase(secondApprover)) {

                leaveRequest.setSalesManagerStatus("WAITING");

            } else if ("SUPER_ADMIN".equalsIgnoreCase(secondApprover)) {

                leaveRequest.setSuperAdminStatus("WAITING");
            }
        }

        // =====================================================
        // 12. SAVE UPDATED LEAVE REQUEST
        // =====================================================

        leaveRequestRepository.save(leaveRequest);

        return LeaveRequestResponse.builder()
                .id(savedRequest.getId())

                .employeeId(employee.getEmployeeId())

                .employeeName(
                        employee.getFirstName() + " " +
                                employee.getLastName()
                )

                .fromDate(savedRequest.getFromDate())
                .toDate(savedRequest.getToDate())
                .numberOfDays(savedRequest.getNumberOfDays())

                .reason(savedRequest.getReason())
                .status(savedRequest.getStatus())

                .approvalLevel(savedRequest.getApprovalLevel())
                .currentApprover(savedRequest.getCurrentApprover())

                .salesManagerStatus(
                        savedRequest.getSalesManagerStatus()
                )

                .hrStatus(
                        savedRequest.getHrStatus()
                )

                .superAdminStatus(
                        savedRequest.getSuperAdminStatus()
                )

                .approvedBy(savedRequest.getApprovedBy())

                .salesManagerApprovedBy(
                        savedRequest.getSalesManagerApprovedBy()
                )

                .hrApprovedBy(
                        savedRequest.getHrApprovedBy()
                )

                .superAdminApprovedBy(
                        savedRequest.getSuperAdminApprovedBy()
                )

                .salesManagerApprovedAt(
                        savedRequest.getSalesManagerApprovedAt()
                )

                .hrApprovedAt(
                        savedRequest.getHrApprovedAt()
                )

                .superAdminApprovedAt(
                        savedRequest.getSuperAdminApprovedAt()
                )

                .remarks(savedRequest.getRemarks())

                .salesManagerRemarks(
                        savedRequest.getSalesManagerRemarks()
                )

                .hrRemarks(
                        savedRequest.getHrRemarks()
                )

                .superAdminRemarks(
                        savedRequest.getSuperAdminRemarks()
                )

                .leaveTypeId(
                        savedRequest.getLeaveType().getId()
                )

                .leaveTypeName(
                        savedRequest.getLeaveType().getLeaveName()
                )

                .createdAt(savedRequest.getCreatedAt())

                .build();
    }


    // =====================================================
    // 6. AUDIT
    // =====================================================

    //String performedBy = getLoggedInEmployeeId();

    // auditLogsService.logCreate("LEAVE", String.valueOf(savedRequest.getId()), performedBy, employeeId, "Leave request created successfully");

    // auditLogsService.logActivity(performedBy, "CREATE_LEAVE_REQUEST", "LEAVE", "Employee applied for leave. Approval level: " + savedRequest.getApprovalLevel(), ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

    // auditLogsService.logInfo("LEAVE", "LeaveService", "Leave request created successfully. ID: " + savedRequest.getId());


    public Object getAllRequests() {

        return leaveRequestRepository.findAll();
    }


    public Object getLeaveRequest(Long id) {

        return leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
    }


    public String updateLeaveRequest(Long id, LeaveRequest leaveRequest) {

        LeaveRequest existingRequest = leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

        String oldValue = convertToJson(existingRequest);

        existingRequest.setFromDate(leaveRequest.getFromDate());

        existingRequest.setToDate(leaveRequest.getToDate());

        existingRequest.setNumberOfDays(leaveRequest.getNumberOfDays());

        existingRequest.setReason(leaveRequest.getReason());

        existingRequest.setRemarks(leaveRequest.getRemarks());

        LeaveRequest updatedRequest = leaveRequestRepository.save(existingRequest);

        String newValue = convertToJson(updatedRequest);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = existingRequest.getEmployee() != null ? existingRequest.getEmployee().getEmployeeId() : null;

        auditLogsService.logUpdate("LEAVE", String.valueOf(id), performedBy, employeeId, "Leave request updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_LEAVE_REQUEST", "LEAVE", "Leave request updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave request updated successfully");

        return "Leave Request Updated Successfully";
    }


    public String deleteLeaveRequest(Long id) {

        LeaveRequest existingRequest = leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

        String deletedValue = convertToJson(existingRequest);

        String performedBy = getLoggedInEmployeeId();

        String employeeId = existingRequest.getEmployee() != null ? existingRequest.getEmployee().getEmployeeId() : null;

        leaveRequestRepository.delete(existingRequest);

        auditLogsService.createAuditLog("LEAVE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingRequest.getId().toString(), "Leave request deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(performedBy, "DELETE_LEAVE_REQUEST", "LEAVE", "Leave request deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave request deleted successfully");

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

        auditLogsService.logCreate("LEAVE", String.valueOf(employee.getId()), performedBy, employee.getId().toString(), "Leave balance created successfully"

        );

        auditLogsService.logActivity(performedBy, "CREATE_BALANCE_TYPE", "LEAVE", "Leave balance created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave balance created successfully");

        return "Leave Balance Created Successfully";
    }


    public Object getLeaveBalance(String employeeId) {

        return leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Leave Balance Not Found"));
    }


    public String updateLeaveBalance(String employeeId, LeaveBalance leaveBalance) {

        LeaveBalance existingBalance = (LeaveBalance) leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Leave Balance Not Found"));
        existingBalance.setTotalLeaves(leaveBalance.getTotalLeaves());

        existingBalance.setUsedLeaves(leaveBalance.getUsedLeaves());

        existingBalance.setAvailableLeaves(leaveBalance.getAvailableLeaves());

        existingBalance.setPaidLeaves(leaveBalance.getPaidLeaves());

        existingBalance.setUnpaidLeaves(leaveBalance.getUnpaidLeaves());

        leaveBalanceRepository.save(existingBalance);
        LeaveBalance updatedBalance = leaveBalanceRepository.save(existingBalance);
        String oldvalue = convertToJson(existingBalance);

        String newValue = convertToJson(updatedBalance);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("LEAVE", employeeId, performedBy, employeeId, "Leave balance updated successfully", oldvalue, newValue);

        return "Leave Balance Updated Successfully";
    }


    public String approveLeave(Long leaveRequestId, String approvedBy) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
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

        LeaveBalance balance = (LeaveBalance) leaveBalanceRepository.findByEmployeeId(employee.getEmployeeId()).orElseThrow(() -> new RuntimeException("Leave Balance Not Found"));


        Integer usedLeaves = balance.getUsedLeaves() + leaveRequest.getNumberOfDays();

        Integer availableLeaves = balance.getAvailableLeaves() - leaveRequest.getNumberOfDays();

        balance.setUsedLeaves(usedLeaves);

        balance.setAvailableLeaves(availableLeaves);

        leaveBalanceRepository.save(balance);

        // Send Mail

        emailService.sendMail(

                employee.getEmail(),

                "Leave Approved",

                "Hi " + employee.getFirstName() + ",\n\n" + "Your leave request has been approved." + "\n\n" + "Leave Type : " + leaveRequest.getLeaveType().getLeaveName() + "\n" + "From Date : " + leaveRequest.getFromDate() + "\n" + "To Date : " + leaveRequest.getToDate() + "\n\n" + "Thank You.");
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logApprove("LEAVE", String.valueOf(leaveRequestId), performedBy, leaveRequest.getEmployee().getEmployeeId(), "Leave request approved"

        );
        return "Leave Approved Successfully";
    }

    public String rejectLeave(Long leaveRequestId, String reason, String approvedBy) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
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

                "Hi " + employee.getFirstName() + ",\n\n" + "Your leave request has been rejected." + "\n\n" + "Reason : " + reason + "\n\n" + "Please contact HR for more information."

        );
        String newValue = convertToJson(leaveRequest);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logReject("LEAVE", String.valueOf(leaveRequestId), performedBy, leaveRequest.getEmployee().getEmployeeId(), "Leave request rejected"

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

    public LeaveRequest getEmployeeLeaves(String employeeId) {

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

    public String createLeavePolicy(Long companyId, LeavePolicy leavePolicy) {

        // If Company entity is available:
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company Not Found"));

        leavePolicy.setCompany(company);

        leavePolicyRepository.save(leavePolicy);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("LEAVE", String.valueOf(leavePolicy.getId()), performedBy, leavePolicy.getId().toString(), "Leave policy created successfully"

        );

        auditLogsService.logActivity(performedBy, "CREATE_POLICY_TYPE", "LEAVE", "Leave policy created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave policy created successfully");

        return "Leave Policy Created Successfully";
    }


    public List<LeavePolicy> getLeavePolicies() {

        return leavePolicyRepository.findAll();
    }


    public LeavePolicy getLeavePolicy(Long id) {

        return leavePolicyRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Policy Not Found"));
    }


    public String updateLeavePolicy(Long id, LeavePolicy leavePolicy) {

        LeavePolicy existingPolicy = leavePolicyRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Policy Not Found"));

        existingPolicy.setPolicyName(leavePolicy.getPolicyName());

        existingPolicy.setAnnualLimit(leavePolicy.getAnnualLimit());

        existingPolicy.setCarryForwardLimit(leavePolicy.getCarryForwardLimit());

        existingPolicy.setEncashmentAllowed(leavePolicy.getEncashmentAllowed());

        existingPolicy.setApprovalRequired(leavePolicy.getApprovalRequired());

        existingPolicy.setStatus(leavePolicy.getStatus());

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

        LeavePolicy existingPolicy = leavePolicyRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Policy Not Found"));

        leavePolicyRepository.delete(existingPolicy);
        String deletedValue = convertToJson(existingPolicy);

        String performedBy = getLoggedInEmployeeId();

//        //String employeeId =
//                existingPolicy.getId() != null
//                        ? existingPolicy.getEmployee().getEmployeeId()
//                        : null;

        // leaveRequestRepository.delete(existingRequest);

        auditLogsService.createAuditLog("LEAVE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingPolicy.getId().toString(), "Leave policy deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(performedBy, "DELETE_LEAVE_POLICY", "LEAVE", "Leave policy deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave policy deleted successfully");

        return "Leave Policy Deleted Successfully";
    }


//=========================================================
// LEAVE ENCASHMENT
//=========================================================

    public String createLeaveEncashment(String employeeId, LeaveEncashment leaveEncashment) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        leaveEncashment.setEmployee(employee);

        if (leaveEncashment.getStatus() == null) {
            leaveEncashment.setStatus("PENDING");
        }

        leaveEncashmentRepository.save(leaveEncashment);
        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("LEAVE", String.valueOf(employee.getId()), performedBy, employeeId, "Leave encashment created successfully"

        );

        auditLogsService.logActivity(performedBy, "CREATE_ENCASHMENT_TYPE", "LEAVE", "Leave encashment created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave encashment created successfully");

        return "Leave Encashment Request Created Successfully";
    }


    public List<LeaveEncashment> getLeaveEncashments() {

        return leaveEncashmentRepository.findAll();
    }


    public LeaveEncashment getLeaveEncashment(Long id) {

        return leaveEncashmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Encashment Not Found"));
    }


    public String updateLeaveEncashment(Long id, LeaveEncashment leaveEncashment) {

        LeaveEncashment existingEncashment = leaveEncashmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Encashment Not Found"));

        existingEncashment.setEncashedDays(leaveEncashment.getEncashedDays());

        existingEncashment.setAmount(leaveEncashment.getAmount());

        existingEncashment.setStatus(leaveEncashment.getStatus());

        existingEncashment.setApprovedBy(leaveEncashment.getApprovedBy());

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

        LeaveEncashment existingEncashment = leaveEncashmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Encashment Not Found"));

        leaveEncashmentRepository.delete(existingEncashment);

        return "Leave Encashment Deleted Successfully";
    }


//=========================================================
// LEAVE CANCELLATIONS
//=========================================================

    public String createLeaveCancellation(Long leaveRequestId, String employeeId, LeaveCancellation leaveCancellation) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        leaveCancellation.setLeaveRequest(leaveRequest);
        leaveCancellation.setEmployee(employee);

        if (leaveCancellation.getCancellationDate() == null) {
            leaveCancellation.setCancellationDate(LocalDate.now());
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

        return leaveCancellationRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Cancellation Not Found"));
    }


    public String updateLeaveCancellation(Long id, LeaveCancellation leaveCancellation) {

        LeaveCancellation existingCancellation = leaveCancellationRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Cancellation Not Found"));

        existingCancellation.setCancellationReason(leaveCancellation.getCancellationReason());

        existingCancellation.setCancellationDate(leaveCancellation.getCancellationDate());

        existingCancellation.setRefundLeaveDays(leaveCancellation.getRefundLeaveDays());

        existingCancellation.setStatus(leaveCancellation.getStatus());

        existingCancellation.setCancelledBy(leaveCancellation.getCancelledBy());

        leaveCancellationRepository.save(existingCancellation);

        return "Leave Cancellation Updated Successfully";
    }


    public String deleteLeaveCancellation(Long id) {

        LeaveCancellation existingCancellation = leaveCancellationRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Cancellation Not Found"));

        leaveCancellationRepository.delete(existingCancellation);
        String deletedValue = convertToJson(existingCancellation);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog("LEAVE", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingCancellation.getId().toString(), "Leave cancellation deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem()

        );

        auditLogsService.logActivity(performedBy, "DELETE_LEAVE_CANCELLATION", "LEAVE", "Leave cancellation deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("LEAVE", "LeaveService", "Leave cancellation deleted successfully");

        return "Leave Cancellation Deleted Successfully";
    }


//=========================================================
// LEAVE ENCASHMENT REPORTS
//=========================================================

    public List<LeaveEncashment> getPendingEncashments() {

        return leaveEncashmentRepository.findAll().stream().filter(encashment -> "PENDING".equalsIgnoreCase(encashment.getStatus())).toList();
    }


    public List<LeaveEncashment> getApprovedEncashments() {

        return leaveEncashmentRepository.findAll().stream().filter(encashment -> "APPROVED".equalsIgnoreCase(encashment.getStatus())).toList();
    }


//=========================================================
// LEAVE CANCELLATION REPORTS
//=========================================================

    public List<LeaveCancellation> getPendingCancellations() {

        return leaveCancellationRepository.findAll().stream().filter(cancellation -> "PENDING".equalsIgnoreCase(cancellation.getStatus())).toList();
    }


    public List<LeaveCancellation> getApprovedCancellations() {

        return leaveCancellationRepository.findAll().stream().filter(cancellation -> "APPROVED".equalsIgnoreCase(cancellation.getStatus())).toList();
    }


//=========================================================
// ACTIVE LEAVE POLICIES
//=========================================================

    public List<LeavePolicy> getActiveLeavePolicies() {

        return leavePolicyRepository.findAll().stream().filter(policy -> "ACTIVE".equalsIgnoreCase(policy.getStatus())).toList();
    }
// =========================================================
// FILTER EMPLOYEES BY LEAVE TYPE
// =========================================================

    public List<LeaveRequest> getEmployeesByLeaveType(Long leaveTypeId) {

        return leaveRequestRepository.findByLeaveTypeId(leaveTypeId);
    }

    @Transactional
    public List<LeaveApproval> getPendingLeavesForRole(String role) {

        return leaveApprovalRepository.findByApprovalRoleAndStatus(role, "PENDING");
    }

    @Transactional
    public String processApproval(Long approvalId, String expectedRole, String action, String reason) {

        // =====================================================
        // 1. FIND APPROVAL
        // =====================================================

        LeaveApproval approval = leaveApprovalRepository.findById(approvalId).orElseThrow(() -> new RuntimeException("Leave approval not found"));


        // =====================================================
        // 2. CHECK APPROVAL ROLE
        // =====================================================

        if (!expectedRole.equalsIgnoreCase(approval.getApprovalRole())) {

            throw new RuntimeException("You are not authorized for this approval");
        }


        // =====================================================
        // 3. ONLY PENDING APPROVAL CAN BE PROCESSED
        // =====================================================

        if (!"PENDING".equalsIgnoreCase(approval.getStatus())) {

            throw new RuntimeException("This leave approval is not pending");
        }


        // =====================================================
        // 4. GET LOGGED-IN APPROVER
        // =====================================================

        String loggedInEmployeeId = getLoggedInEmployeeId();


        Employee approver = employeeRepository.findByEmployeeId(loggedInEmployeeId).orElseThrow(() -> new RuntimeException("Approver not found"));


        LeaveRequest leaveRequest = approval.getLeaveRequest();


        // =====================================================
        // 5. REJECT
        // =====================================================

        if ("REJECT".equalsIgnoreCase(action)) {

            // -------------------------------------------------
            // Update approval
            // -------------------------------------------------

            approval.setStatus("REJECTED");

            approval.setRejectionReason(reason);

            approval.setApprovedBy(approver);

            approval.setApprovedDate(LocalDate.now());

            leaveApprovalRepository.save(approval);


            // -------------------------------------------------
            // Create cancellation record
            // -------------------------------------------------

            LeaveCancellation cancellation = new LeaveCancellation();

            cancellation.setLeaveRequest(leaveRequest);

            cancellation.setEmployee(leaveRequest.getEmployee());

            cancellation.setCancelledBy(approver);

            cancellation.setCancellationReason(reason);

            cancellation.setCancellationDate(LocalDate.now());

            cancellation.setRefundLeaveDays(BigDecimal.valueOf(leaveRequest.getNumberOfDays()));

            cancellation.setStatus("REJECTED");

            leaveCancellationRepository.save(cancellation);


            // -------------------------------------------------
            // Update LeaveRequest
            // -------------------------------------------------

            leaveRequest.setStatus("REJECTED");

            leaveRequest.setCurrentApprover(null);

            leaveRequest.setApprovalLevel("REJECTED");

            leaveRequest.setApprovedBy(approver.getEmployeeId());

            leaveRequest.setRemarks(reason);


            // -------------------------------------------------
            // Update role status
            // -------------------------------------------------

            if ("HR".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setHrStatus("REJECTED");

            } else if ("SALES_MANAGER".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setSalesManagerStatus("REJECTED");

            } else if ("SUPER_ADMIN".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setSuperAdminStatus("REJECTED");
            }


            leaveRequestRepository.save(leaveRequest);


            return "Leave Rejected Successfully";
        }


        // =====================================================
        // 6. APPROVE
        // =====================================================

        if ("APPROVE".equalsIgnoreCase(action)) {

            // -------------------------------------------------
            // Update current approval
            // -------------------------------------------------

            approval.setStatus("APPROVED");

            approval.setApprovedBy(approver);

            approval.setApprovedDate(LocalDate.now());

            leaveApprovalRepository.save(approval);


            // -------------------------------------------------
            // Update role status
            // -------------------------------------------------

            if ("HR".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setHrStatus("APPROVED");

            } else if ("SALES_MANAGER".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setSalesManagerStatus("APPROVED");

            } else if ("SUPER_ADMIN".equalsIgnoreCase(expectedRole)) {

                leaveRequest.setSuperAdminStatus("APPROVED");
            }


            // =================================================
            // 7. FIND NEXT APPROVAL
            // =================================================

            List<LeaveApproval> approvals = leaveApprovalRepository.findByLeaveRequestId(leaveRequest.getId());


            Optional<LeaveApproval> nextApproval = approvals.stream().filter(a -> "WAITING".equalsIgnoreCase(a.getStatus())).min(Comparator.comparing(LeaveApproval::getApprovalOrder));


            // =================================================
            // 8. NEXT APPROVER EXISTS
            // =================================================

            if (nextApproval.isPresent()) {

                LeaveApproval next = nextApproval.get();


                // ---------------------------------------------
                // IMPORTANT:
                // WAITING → PENDING
                // ---------------------------------------------

                next.setStatus("PENDING");

                leaveApprovalRepository.save(next);


                // ---------------------------------------------
                // Update LeaveRequest
                // ---------------------------------------------

                leaveRequest.setCurrentApprover(next.getApprovalRole());

                leaveRequest.setApprovalLevel(next.getApprovalRole());


                // ---------------------------------------------
                // Update next role status
                // ---------------------------------------------

                if ("HR".equalsIgnoreCase(next.getApprovalRole())) {

                    leaveRequest.setHrStatus("PENDING");

                } else if ("SALES_MANAGER".equalsIgnoreCase(next.getApprovalRole())) {

                    leaveRequest.setSalesManagerStatus("PENDING");

                } else if ("SUPER_ADMIN".equalsIgnoreCase(next.getApprovalRole())) {

                    leaveRequest.setSuperAdminStatus("PENDING");
                }


                leaveRequestRepository.save(leaveRequest);


                return "Leave Approved. Waiting for " + next.getApprovalRole();
            }


            // =================================================
            // 9. ALL APPROVALS COMPLETED
            // =================================================

            leaveRequest.setStatus("APPROVED");

            leaveRequest.setApprovalLevel("COMPLETED");

            leaveRequest.setCurrentApprover(null);

            leaveRequest.setApprovedBy(approver.getEmployeeId());

            leaveRequest.setRemarks("Leave fully approved");


            leaveRequestRepository.save(leaveRequest);


            return "Leave Fully Approved";
        }


        // =====================================================
        // 10. INVALID ACTION
        // =====================================================

        throw new RuntimeException("Invalid approval action. Use APPROVE or REJECT");
    }
}
