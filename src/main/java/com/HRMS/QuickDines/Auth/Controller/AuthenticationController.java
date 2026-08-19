package com.HRMS.QuickDines.Auth.Controller;

import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.*;
import com.HRMS.QuickDines.Auth.model.Permission;
import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Auth.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authService;


    // =========================================================
    // AUTHENTICATION
    // =========================================================

    // Login
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                authService.login(request, httpRequest)
        );
    }


    // Logout
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                authService.logout(httpRequest)
        );
    }





    // Forgot Password
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email) {

        return ResponseEntity.ok(
                authService.forgotPassword(email)
        );
    }


    // Reset Password
    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(
                authService.resetPassword(
                        request
                )
        );
    }


    // Change Password
    @PostMapping("/auth/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                authService.changePassword(request)
        );
    }


    // =========================================================
    // OTP
    // =========================================================

    // Send OTP
    @PostMapping("/auth/send-otp")
    public ResponseEntity<?> sendOTP(
            @RequestParam String email) {

        return ResponseEntity.ok(
                authService.sendOTP(email)
        );
    }


    // Verify OTP
    @PostMapping("/auth/verify-otp")
    public ResponseEntity<?> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp) {

        return ResponseEntity.ok(
                authService.verifyOTP(email, otp)
        );
    }


    // Resend OTP
    @PostMapping("/auth/resend-otp")
    public ResponseEntity<?> resendOTP(
            @RequestParam String email) {

        return ResponseEntity.ok(
                authService.resendOTP(email)
        );
    }


    // =========================================================
    // ROLES
    // =========================================================

    // Create Role
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(
            @RequestBody Role request) {

        return ResponseEntity.ok(
                authService.createRole(request)
        );
    }


    // Get All Roles
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {

        return ResponseEntity.ok(
                authService.getRoles()
        );
    }


    // Get Role
    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.getRole(id)
        );
    }


    // Update Role
    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Role request) {

        return ResponseEntity.ok(
                authService.updateRole(id, request)
        );
    }


    // Delete Role
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRole(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.deleteRole(id)
        );
    }


    // =========================================================
    // ROLE - PERMISSIONS
    // =========================================================

    // Get permissions assigned to role
    @GetMapping("/roles/{id}/permissions")
    public ResponseEntity<?> getRolePermissions(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.getRolePermissions(id)
        );
    }


    // Assign permission to role
    @PostMapping("/roles/{id}/permissions/{permissionId}")
    public ResponseEntity<?> assignPermission(
            @PathVariable Long id,
            @PathVariable Long permissionId) {

        return ResponseEntity.ok(
                authService.assignPermission(id, permissionId)
        );
    }


    // Remove permission from role
    @DeleteMapping("/roles/{id}/permissions/{permissionId}")
    public ResponseEntity<?> removePermission(
            @PathVariable Long id,
            @PathVariable Long permissionId) {

        return ResponseEntity.ok(
                authService.removePermission(id, permissionId)
        );
    }


    // =========================================================
    // PERMISSIONS
    // =========================================================

    // Create Permission
    @PostMapping("/permissions")
    public ResponseEntity<?> createPermission(
            @RequestBody Permission request) {

        return ResponseEntity.ok(
                authService.createPermission(request)
        );
    }


    // Get All Permissions
    @GetMapping("/permissions")
    public ResponseEntity<?> getPermissions() {

        return ResponseEntity.ok(
                authService.getPermissions()
        );
    }


    // Get Permission
    @GetMapping("/permissions/{id}")
    public ResponseEntity<?> getPermission(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.getPermission(id)
        );
    }


    // Update Permission
    @PutMapping("/permissions/{id}")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long id,
            @RequestBody Permission request) {

        return ResponseEntity.ok(
                authService.updatePermission(id, request)
        );
    }


    // Delete Permission
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<?> deletePermission(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.deletePermission(id)
        );
    }


    // Get permissions by module
    @GetMapping("/permissions/module/{moduleName}")
    public ResponseEntity<?> getModulePermissions(
            @PathVariable String moduleName) {

        return ResponseEntity.ok(
                authService.getModulePermissions(moduleName)
        );
    }


    // =========================================================
    // LOGIN HISTORY
    // =========================================================

    // Get all login history
    @GetMapping("/login-history")
    public ResponseEntity<?> getLoginHistory() {

        return ResponseEntity.ok(
                authService.getLoginHistory()
        );
    }


    // Get employee login history
    @GetMapping("/login-history/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeLoginHistory(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                authService.getEmployeeLoginHistory(employeeId)
        );
    }


    // Successful logins
    @GetMapping("/login-history/employee/{employeeId}/success")
    public ResponseEntity<?> getSuccessfulLogins(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                authService.getSuccessfulLogins(employeeId)
        );
    }


    // Failed logins
    @GetMapping("/login-history/employee/{employeeId}/failed")
    public ResponseEntity<?> getFailedLogins(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                authService.getFailedLogins(employeeId)
        );
    }


    // Login history by date range
    @GetMapping("/login-history/date-range")
    public ResponseEntity<?> getLoginHistoryByDateRange(
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return ResponseEntity.ok(
                authService.getLoginHistoryByDateRange(
                        fromDate,
                        toDate
                )
        );
    }


    // =========================================================
    // DEVICES
    // =========================================================

    // Get employee devices
    @GetMapping("/devices/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeDevices(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                authService.getEmployeeDevices(employeeId)
        );
    }


    // Logout device
    @PatchMapping("/devices/{id}/logout")
    public ResponseEntity<?> logoutDevice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.logoutDevice(id)
        );
    }


    // Block device
    @PatchMapping("/devices/{id}/block")
    public ResponseEntity<?> blockDevice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.blockDevice(id)
        );
    }


    // Remove device
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<?> removeDevice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authService.removeDevice(id)
        );
    }
}