package com.HRMS.QuickDines.Recruitment.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeOnboardingRequest {

    private Long employeeId;

    private LocalDate joiningDate;
}