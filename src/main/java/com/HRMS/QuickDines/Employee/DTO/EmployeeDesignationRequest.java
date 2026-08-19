package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDesignationRequest {

    private Long designationId;
    private LocalDate promotedDate;
    private Long previousDesignationId;
    private String salaryGrade;
}