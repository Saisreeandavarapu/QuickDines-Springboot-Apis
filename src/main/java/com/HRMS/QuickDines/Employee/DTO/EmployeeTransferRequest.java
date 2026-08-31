package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeTransferRequest {

    private Long fromDepartmentId;
    private Long toDepartmentId;

    private Long fromBranchId;
    private Long toBranchId;

    private Long fromTeamId;
    private Long toTeamId;

    private LocalDate transferDate;

    private Long approvedBy;

    private String reason;

}