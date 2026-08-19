package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCertificationRequest {

    private String certificationName;
    private String issuingOrganization;
    private String certificateNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String credentialUrl;
    private String attachmentUrl;
    private String status;
}