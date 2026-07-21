package com.HRMS.QuickDines.Auth.services;

import com.HRMS.QuickDines.Auth.Config.JwtService;
import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;
import com.HRMS.QuickDines.Auth.model.*;
import com.HRMS.QuickDines.Auth.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.HRMS.QuickDines.Auth.Config.JwtService;
import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {



    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PermissionRepository permissionRepo;

    private final UserRoleRepository userRoleRepo;

    private final OtpVerificationRepository otpRepo;

    private final RefreshTokenRepository tokenRepo;

    private final LoginHistoryRepository historyRepo;

    private final UserDeviceRepository deviceRepo;


    //Spring Security

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder;




    public Users register(Users user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        user.setActive(true);
        user.setVerified(false);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);
        user.setLoginAttempts(0);
        user.setCreatedAt(LocalDateTime.now());

        return userRepo.save(user);

    }



    //=================================================
    // LOGIN
    //=================================================

    public LoginResponse login(LoginRequest request) {


        //SPRING SECURITY AUTHENTICATION

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmployeeId(),
                        request.getPassword()

                )
        );


        //GENERATE JWT TOKEN

        String token = jwtService.generateToken(

                request.getEmployeeId()

        );


        //LOGIN HISTORY CAN BE SAVED HERE

        /*
        LoginHistory history=new LoginHistory();

        history.setLoginDate(LocalDate.now());
        history.setLoginTime(LocalTime.now());
        history.setLoginStatus("SUCCESS");

        historyRepo.save(history);

         */


        return LoginResponse.builder()

                .token(token)
                .message("Login Successful")

                .build();

    }



    //=================================================
    // OTP
    //=================================================

    public String sendOTP() {

        //EMAIL SERVICE

        return "OTP Sent Successfully";

    }


    public String verifyOTP() {

        return "OTP Verified Successfully";

    }



    //=================================================
    // PASSWORD
    //=================================================

    public String forgotPassword() {

        return "Reset Password Link Sent";

    }


    public String resetPassword() {

        return "Password Updated Successfully";

    }



    //=================================================
    // JWT TOKEN
    //=================================================

    public String refreshToken() {

        //JWT LOGIC

        return "New Token Generated Successfully";

    }



    //=================================================
    // LOGOUT
    //=================================================

    public String logout() {

        return "Logout Successful";

    }



    //=================================================
    // USERS
    //=================================================

    public List<Users> getAllUsers() {

        return userRepo.findAll();

    }


    public Users getUser(Long id) {

        return userRepo.findById(id)

                .orElseThrow(

                        () -> new RuntimeException("User Not Found")

                );

    }



    //=================================================
    // ROLES
    //=================================================

    public List<Role> getRoles() {

        return roleRepo.findAll();

    }



    //=================================================
    // PERMISSIONS
    //=================================================

    public List<Permission> getPermissions() {

        return permissionRepo.findAll();

    }



    //=================================================
    // LOGIN HISTORY
    //=================================================

    public List<LoginHistory> loginHistory() {

        return historyRepo.findAll();

    }



    //=================================================
    // USER DEVICES
    //=================================================

    public List<UserDevice> userDevices() {

        return deviceRepo.findAll();

    }

}


