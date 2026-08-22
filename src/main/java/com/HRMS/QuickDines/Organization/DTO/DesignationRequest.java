package com.HRMS.QuickDines.Organization.DTO;

import lombok.Data;

@Data
public class DesignationRequest {

    private String designationName;

    private String designationCode;

    private String level;

    private String salaryGrade;

    private Long companyId;

    private Long departmentId;
}