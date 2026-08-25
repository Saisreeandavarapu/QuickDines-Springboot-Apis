package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class PendingApprovalResponse {

    private Long approvalId;

    // Employee
    private Long employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;

    // Organization
    private String department;
    private String role;

    // Approval
    private String approvalType;

    private String hrStatus;
    private String salesManagerStatus;
    private String superAdminStatus;

    private String finalStatus;

    // HR
    private String hrApprovedBy;
    private String hrRemarks;

    // Sales Manager
    private String salesManagerApprovedBy;
    private String salesManagerRemarks;

    // Super Admin
    private String superAdminApprovedBy;
    private String superAdminRemarks;
}