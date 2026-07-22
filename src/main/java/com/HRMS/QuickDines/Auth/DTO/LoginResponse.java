package com.HRMS.QuickDines.Auth.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;

    private String role;

    private String message;

    private String refreshToken;

}
