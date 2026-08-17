package com.HRMS.QuickDines.Employee.DTO;

import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import lombok.Data;

@Data
public class ApprovalRequestdto {

    private ApprovalStatus status;

    private String remarks;
}