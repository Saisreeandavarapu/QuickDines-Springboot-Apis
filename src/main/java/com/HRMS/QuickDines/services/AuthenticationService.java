package com.HRMS.QuickDines.services;

import com.HRMS.QuickDines.Config.JwtService;
import com.HRMS.QuickDines.DTO.LoginRequest;
import com.HRMS.QuickDines.DTO.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final AuthenticationManager authenticationManager;


    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(

                        request.getEmployeeId(),request.getPassword())

        );


        String token= jwtService.generateToken(
                        request.getEmployeeId()
                );


        return LoginResponse.builder()

                .token(token)

                .message("Login Successful")

                .build();

    }


}