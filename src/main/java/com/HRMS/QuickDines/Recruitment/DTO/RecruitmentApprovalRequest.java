package com.HRMS.QuickDines.Recruitment.DTO;

import com.HRMS.QuickDines.Recruitment.Entity.ApprovalAction;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalModule;
import lombok.Data;

@Data
public class RecruitmentApprovalRequest {

    private Long companyId;

    private ApprovalModule module;

    private Long employeeId;

    private Long applicationId;

    private Long jobOpeningId;

    private Long approverId;

    private Integer approvalLevel;

    private ApprovalAction action;

    private String reason;
    private Long requestedById;
}