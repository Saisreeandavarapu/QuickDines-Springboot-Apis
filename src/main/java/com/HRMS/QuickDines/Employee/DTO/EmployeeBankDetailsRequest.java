package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeBankDetailsRequest {

    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String branchName;
    private String upiId;
    private String accountStatus;
}