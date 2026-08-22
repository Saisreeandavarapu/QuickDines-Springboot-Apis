package com.HRMS.QuickDines.Leave.DTO;

import lombok.Data;

@Data
public class LeaveTypeRequest {

    private String leaveName;
    private Integer totalLeaves;
    private String description;
    private String status;

    private Long companyId;
    private Long branchId;
}