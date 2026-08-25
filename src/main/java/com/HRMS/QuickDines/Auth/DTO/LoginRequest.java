package com.HRMS.QuickDines.Auth.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;

    private String password;
}
