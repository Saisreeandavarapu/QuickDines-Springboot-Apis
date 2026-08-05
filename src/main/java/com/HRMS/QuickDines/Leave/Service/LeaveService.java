package com.HRMS.QuickDines.Leave.Service;

import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.CRM.repo.CustomerRepository;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.Service.EmployeeService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Leave.model.*;
import com.HRMS.QuickDines.Leave.repo.*;
import lombok.RequiredArgsConstructor;
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

    public String createLeaveType(LeaveType leaveType){

        leaveTypeRepository.save(leaveType);

        return "Leave Type Created Successfully";
    }



    public Object getAllLeaveTypes(){

        return leaveTypeRepository.findAll();
    }



    public Object getLeaveType(Long id){

        return leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));
    }



    public String updateLeaveType(Long id, LeaveType leaveType){

        LeaveType existingLeaveType = leaveTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Type Not Found"));

        existingLeaveType.setLeaveName(leaveType.getLeaveName());

        existingLeaveType.setTotalLeaves(leaveType.getTotalLeaves());

        existingLeaveType.setDescription(leaveType.getDescription());

        existingLeaveType.setStatus(leaveType.getStatus());

        leaveTypeRepository.save(existingLeaveType);

        return "Leave Type Updated Successfully";
    }



    public String deleteLeaveType(Long id){

        leaveTypeRepository.deleteById(id);

        return "Leave Type Deleted Successfully";
    }



//==================================
// LEAVE REQUESTS
//==================================

    public String applyLeave(String employeeId, LeaveRequest leaveRequest) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        leaveRequest.setEmployee(employee);

        leaveRequest.setStatus("PENDING");

        leaveRequestRepository.save(leaveRequest);

        return "Leave Applied Successfully";
    }



    public Object getAllRequests() {

        return leaveRequestRepository.findAll();
    }



    public Object getLeaveRequest(Long id) {

        return leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
    }



    public String updateLeaveRequest(Long id, LeaveRequest leaveRequest) {

        LeaveRequest existingRequest = leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

        existingRequest.setFromDate(leaveRequest.getFromDate());

        existingRequest.setToDate(leaveRequest.getToDate());

        existingRequest.setNumberOfDays(leaveRequest.getNumberOfDays());

        existingRequest.setReason(leaveRequest.getReason());

        existingRequest.setRemarks(leaveRequest.getRemarks());

        leaveRequestRepository.save(existingRequest);

        return "Leave Request Updated Successfully";
    }



    public String deleteLeaveRequest(Long id) {

        leaveRequestRepository.deleteById(id);

        return "Leave Request Deleted Successfully";
    }



//==================================
// LEAVE BALANCES
//==================================

    public String createLeaveBalance(String employeeId, LeaveBalance leaveBalance) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        leaveBalance.setEmployee(employee);

        leaveBalanceRepository.save(leaveBalance);

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

        return "Leave Balance Updated Successfully";
    }



    public String approveLeave(Long leaveRequestId,String approvedBy) {

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
        return "Leave Approved Successfully";
    }
    public String rejectLeave(Long leaveRequestId, String reason,String approvedBy) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() ->
                new RuntimeException("Leave Request Not Found"));
        Employee approvedByEmployee = employeeRepository.findById(approvedBy).orElseThrow(() ->new RuntimeException("Employee Not Found"));
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


        return "Leave Rejected Successfully";

    }
    public Object getApprovals(){

        return leaveApprovalRepository.findAll();

    }

    public Object getApproval(Long id){

        return leaveApprovalRepository.findById(id).orElseThrow(() -> new RuntimeException("Approval Not Found"));

    }



//==================================
// REPORTS
//==================================

    public Object getEmployeeLeaves(
            String employeeId){

        return leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
    }



    public Object getPendingLeaves(){

        return leaveRequestRepository.findByStatus("PENDING");
    }



    public Object getApprovedLeaves(){

        return leaveRequestRepository.findByStatus("APPROVED");
    }



    public Object getRejectedLeaves(){

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

        return "Leave Policy Updated Successfully";
    }


    public String deleteLeavePolicy(Long id) {

        LeavePolicy existingPolicy =
                leavePolicyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave Policy Not Found"));

        leavePolicyRepository.delete(existingPolicy);

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

        leaveEncashmentRepository.save(existingEncashment);

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



}
