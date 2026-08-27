package com.HRMS.QuickDines.Leave.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LeaveRequestResponse {

    private Long id;

    private String employeeId;

    private String employeeName;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer numberOfDays;

    private String reason;

    private String status;

    private String approvalLevel;

    private String currentApprover;

    private String salesManagerStatus;

    private String hrStatus;

    private String superAdminStatus;

    private String approvedBy;

    private String salesManagerApprovedBy;

    private String hrApprovedBy;

    private String superAdminApprovedBy;

    private LocalDateTime salesManagerApprovedAt;

    private LocalDateTime hrApprovedAt;

    private LocalDateTime superAdminApprovedAt;

    private String remarks;

    private String salesManagerRemarks;

    private String hrRemarks;

    private String superAdminRemarks;

    private Long leaveTypeId;

    private String leaveTypeName;

    private LocalDateTime createdAt;
}