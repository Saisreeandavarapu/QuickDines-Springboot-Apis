package com.HRMS.QuickDines.Organization.DTO;

import lombok.Data;

@Data
public class DepartmentRequest {

    private String departmentName;
    private String departmentCode;
    private Long companyId;
    private Long branchId;
    private String description;
    private String status;
}