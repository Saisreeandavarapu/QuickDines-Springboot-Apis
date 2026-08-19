package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeAddressRequest {

    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String district;
    private String state;
    private String country;
    private String postalCode;
}