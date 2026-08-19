package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeFamilyMemberRequest {

    private String memberName;
    private String relationship;
    private LocalDate dateOfBirth;
    private String occupation;
    private String mobileNumber;
    private Boolean dependent;
    private Boolean nominee;
}