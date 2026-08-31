package com.HRMS.QuickDines.Recruitment.DTO;

import com.HRMS.QuickDines.Recruitment.Entity.ApprovalStatus;
import lombok.Data;

@Data
public class ApprovalProcessRequest {

    private ApprovalStatus status;

    private String reason;
}