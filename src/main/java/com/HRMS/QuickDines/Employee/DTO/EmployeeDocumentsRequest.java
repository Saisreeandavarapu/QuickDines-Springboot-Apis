package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeDocumentsRequest {

    private String aadhaarNumber;
    private String panNumber;
    private String passportNumber;

    private String resumeUrl;

    private String aadhaarDocument;
    private String panDocument;
    private String degreeCertificate;
    private String pgCertificate;
    private String offerLetter;
    private String joiningLetter;
    private String salarySlips;
    private String experienceLetter;

    private String status;
}