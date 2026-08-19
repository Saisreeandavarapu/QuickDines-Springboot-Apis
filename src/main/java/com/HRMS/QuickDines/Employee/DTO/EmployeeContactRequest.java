package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeContactRequest {

    private String emergencyContactName;
    private String relation;
    private String mobileNumber;
    private String alternateNumber;
}