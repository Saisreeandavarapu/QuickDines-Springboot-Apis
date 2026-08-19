package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeExperienceRequest {

    private String companyName;
    private String designation;
    private String employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalExperience;
    private Boolean currentCompany;
    private BigDecimal salary;
    private String reasonForLeaving;
}