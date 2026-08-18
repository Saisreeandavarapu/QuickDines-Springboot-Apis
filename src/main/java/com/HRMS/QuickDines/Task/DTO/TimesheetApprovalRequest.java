package com.HRMS.QuickDines.Task.DTO;

import lombok.Data;

@Data
public class TimesheetApprovalRequest {

    private String approverId;

    private String status;

    private String remarks;
}