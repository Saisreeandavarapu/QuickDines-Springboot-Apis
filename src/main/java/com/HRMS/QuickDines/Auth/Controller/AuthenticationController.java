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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('PROFILE_READ')")
    public ResponseEntity<Users> getProfile(@RequestParam String employeeId) {
        return ResponseEntity.ok(service.getProfile(employeeId));}



    @PutMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    public ResponseEntity<String> updateProfile(@PathVariable String employeeId,@RequestBody Users request) {

        return ResponseEntity.ok(
                service.updateProfile(employeeId,request));

    }


    // Login History

    @GetMapping("/login-history")
    @PreAuthorize("hasAuthority('LOGIN_HISTORY_READ')")
    public ResponseEntity<LoginHistory> loginHistory() {
        return ResponseEntity.ok((LoginHistory) service.getLoginHistory());

    }


    // Devices

    @GetMapping("/devices")
    @PreAuthorize("hasAuthority('DEVICE_READ')")
    public ResponseEntity<UserDevice> devices() {

        return ResponseEntity.ok(
                (UserDevice) service.getDevices());

    }


    @DeleteMapping("/remove-device/{id}")
    @PreAuthorize("hasAuthority('DEVICE_DELETE')")
    public ResponseEntity<?> removeDevice(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                service.removeDevice(id));
    }
    // User Management

    @PostMapping("/create-user")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<String> createUser(@RequestBody Users request) {

        return ResponseEntity.ok(
                service.createUser(request));

    }


    @PutMapping("/block-user/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<?> blockUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.blockUser(id));

    }


    @PutMapping("/unblock-user/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<?> unblockUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.unblockUser(id));

    }


    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteUser(id));

    }


    // Roles

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<?> createRole(@RequestBody Role role) {

        return ResponseEntity.ok(
                service.createRole(role));

    }


    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<?> getRoles() {

        return ResponseEntity.ok(
                service.getRoles());

    }


    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<?> getRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getRole(id));

    }


    @PutMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,@RequestBody Role role) {

        return ResponseEntity.ok(
                service.updateRole(id,role));

    }


    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
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

    // PERMISSION APIs

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<?> createPermission(
            @RequestBody Permission permission){

        return ResponseEntity.ok(
                service.createPermission(permission)
        );

    }


    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public ResponseEntity<?> getPermissions(){

        return ResponseEntity.ok(
                service.getPermissions()
        );

    }


    @GetMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public ResponseEntity<?> getPermission(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getPermission(id)
        );

    }


    @PutMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long id,
            @RequestBody Permission permission){

        return ResponseEntity.ok(
                service.updatePermission(id,permission)
        );

    }


    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<?> deletePermission(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deletePermission(id)
        );

    }


    @GetMapping("/permissions/module/{moduleName}")
    public ResponseEntity<?> getModulePermissions(
            @PathVariable String moduleName){

        return ResponseEntity.ok(
                service.getModulePermissions(moduleName)
        );

    }



}
