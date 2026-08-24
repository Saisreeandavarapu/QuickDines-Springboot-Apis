package com.HRMS.QuickDines.Employee.DTO;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Employee.Entity.ApprovalType;
import lombok.Data;

@Data
public class EmployeeApprovalRequest {

    private String hrApprovedBy;
    private String hrRemarks;
    private ApprovalStatus hrStatus;


    private String adminApprovedBy;
    private String adminRemarks;
    private ApprovalStatus adminStatus;


    private String salesManagerApprovedBy;
    private String salesManagerRemarks;
    private ApprovalStatus salesManagerStatus;

    private ApprovalType approvalType;
    private String reportingManagerId;
    private Long roleId;


}