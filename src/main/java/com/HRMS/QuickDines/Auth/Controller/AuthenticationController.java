package com.HRMS.QuickDines.Auth.Controller;

import com.HRMS.QuickDines.Auth.DTO.ChangePasswordRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;
import com.HRMS.QuickDines.Auth.DTO.ResetPasswordRequest;
import com.HRMS.QuickDines.Auth.model.*;
import com.HRMS.QuickDines.Auth.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    // Registration

    @PostMapping("/super-admin/register")
    public ResponseEntity<String> registerSuperAdmin(
            @RequestBody Users request) {

        return ResponseEntity.ok(service.registerSuperAdmin(request));

    }


    // Login

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                service.login(request,httpRequest));

    }


    // Logout

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(
                service.logout(httpRequest));

    }


    // Password APIs

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        return ResponseEntity.ok(
                service.forgotPassword(email));

    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(
                service.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword())

        );

    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                service.changePassword(request)
        );

    }


    // OTP APIs

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {

        return ResponseEntity.ok(
                service.sendOTP(email));

    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {

        return ResponseEntity.ok(
                service.verifyOTP(email, otp));

    }


    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOTP(@RequestParam String email) {

        return ResponseEntity.ok(
                service.resendOTP(email));

    }


    // JWT

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestParam String refreshToken) {

        return ResponseEntity.ok(
                service.refreshToken(refreshToken));

    }


    // Profile

    @GetMapping("/profile")
    public ResponseEntity<Users> getProfile(@RequestParam String employeeId) {
        return ResponseEntity.ok(service.getProfile(employeeId));}



    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable String employeeId,@RequestBody Users request) {

        return ResponseEntity.ok(
                service.updateProfile(employeeId,request));

    }


    // Login History

    @GetMapping("/login-history")
    public ResponseEntity<LoginHistory> loginHistory() {

        return ResponseEntity.ok((LoginHistory) service.getLoginHistory());

    }


    // Devices

    @GetMapping("/devices")
    public ResponseEntity<UserDevice> devices() {

        return ResponseEntity.ok(
                (UserDevice) service.getDevices());

    }


    @DeleteMapping("/remove-device/{id}")
    public ResponseEntity<?> removeDevice(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                service.removeDevice(id));
    }
    // User Management

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody Users request) {

        return ResponseEntity.ok(
                service.createUser(request));

    }


    @PutMapping("/block-user/{id}")
    public ResponseEntity<?> blockUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.blockUser(id));

    }


    @PutMapping("/unblock-user/{id}")
    public ResponseEntity<?> unblockUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.unblockUser(id));

    }


    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteUser(id));

    }


    // Roles

    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody Role role) {

        return ResponseEntity.ok(
                service.createRole(role));

    }


    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {

        return ResponseEntity.ok(
                service.getRoles());

    }


    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getRole(id));

    }


    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,@RequestBody Role role) {

        return ResponseEntity.ok(
                service.updateRole(id,role));

    }


    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteRole(id));

    }


    // User Roles

    @PutMapping("/assign-role/{userId}/{roleId}")
    public ResponseEntity<?> assignRole(@PathVariable String userId, @PathVariable Long roleId) {

        return ResponseEntity.ok(
                service.assignRole(userId, roleId));

    }


    @DeleteMapping("/remove-role/{userId}")
    public ResponseEntity<?> removeRole(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                service.removeRole(userId));

    }

}
