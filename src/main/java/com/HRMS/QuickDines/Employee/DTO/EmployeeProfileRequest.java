package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeProfileRequest {

    private String profileImage;
    private String fatherName;
    private String motherName;
    private String maritalStatus;
    private String bloodGroup;
    private String nationality;
    private String emergencyContact;
    private String alternateMobile;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String country;
}