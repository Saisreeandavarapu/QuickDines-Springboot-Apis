package com.HRMS.QuickDines.Auth.Controller;

import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;
import com.HRMS.QuickDines.Auth.model.*;
import com.HRMS.QuickDines.Auth.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController{


    private final AuthenticationService service;



    @PostMapping("/register")
    public Users register(
            @RequestBody Users user){

        return service.register(user);

    }


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest dto){

        return service.login(dto);

    }


    @PostMapping("/send-otp")
    public String sendOTP(){

        return service.sendOTP();

    }


    @PostMapping("/verify-otp")
    public String verifyOTP(){

        return service.verifyOTP();

    }


    @PostMapping("/forgot-password")
    public String forgotPassword(){

        return service.forgotPassword();

    }


    @PostMapping("/reset-password")
    public String resetPassword(){

        return service.resetPassword();

    }


    @PostMapping("/refresh-token")
    public String refreshToken(){

        return service.refreshToken();

    }


    @PostMapping("/logout")
    public String logout(){

        return service.logout();

    }


    @GetMapping("/users")
    public List<Users> users(){

        return service.getAllUsers();

    }


    @GetMapping("/users/{id}")
    public Users user(@PathVariable Long id){

        return service.getUser(id);

    }


    @GetMapping("/roles")
    public List<Role> roles(){

        return service.getRoles();

    }


    @GetMapping("/permissions")
    public List<Permission> permissions(){

        return service.getPermissions();

    }


    @GetMapping("/login-history")
    public List<LoginHistory> history(){

        return service.loginHistory();

    }


    @GetMapping("/devices")
    public List<UserDevice> devices(){

        return service.userDevices();

    }


}
