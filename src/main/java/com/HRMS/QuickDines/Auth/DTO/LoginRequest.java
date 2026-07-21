package com.HRMS.QuickDines.Auth.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String employeeId;

    private String password;
}
