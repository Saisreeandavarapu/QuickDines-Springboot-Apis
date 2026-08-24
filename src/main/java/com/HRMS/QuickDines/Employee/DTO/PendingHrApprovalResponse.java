package com.HRMS.QuickDines.Employee.DTO;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import lombok.Data;

@Data
public class PendingHrApprovalResponse {

    private Long approvalId;
    private Long employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;

    private String hrStatus;
    private String finalStatus;
    private String hrApprovedBy;
    private String hrRemarks;

    // getters and setters
}
