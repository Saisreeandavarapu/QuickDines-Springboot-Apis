package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeSkillRequest {

    private String skillName;
    private String skillCategory;
    private String proficiencyLevel;
    private BigDecimal experienceYears;
    private LocalDate lastUsed;
    private Boolean certificationAvailable;
    private String remarks;
}