package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeExitRequest {

    private LocalDate resignationDate;
    private LocalDate lastWorkingDay;
    private String reason;
    private String exitStatus;
    private String relievingLetter;
    private String remarks;

    private Long exitApprovedBy;
}