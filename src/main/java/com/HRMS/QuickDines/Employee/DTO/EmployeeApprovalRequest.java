package com.HRMS.QuickDines.Employee.DTO;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import lombok.Data;

@Data
public class EmployeeApprovalRequest {

    private Long hrApprovedBy;
    private String hrRemarks;
    private ApprovalStatus hrStatus;


    private Long adminApprovedBy;
    private String adminRemarks;
    private ApprovalStatus adminStatus;


    private Long departmentHeadApprovedBy;
    private String departmentHeadRemarks;
    private ApprovalStatus departmentHeadStatus;


}