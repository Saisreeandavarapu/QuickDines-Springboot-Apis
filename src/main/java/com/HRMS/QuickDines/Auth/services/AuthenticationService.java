package com.HRMS.QuickDines.Auth.services;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Entity.SystemLogLevel;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Auth.Config.JwtService;
import com.HRMS.QuickDines.Auth.DTO.*;
import com.HRMS.QuickDines.Auth.Entity.LoginStatus;
import com.HRMS.QuickDines.Auth.Entity.UserStatus;
import com.HRMS.QuickDines.Auth.model.*;
import com.HRMS.QuickDines.Auth.repo.*;
import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // JWT

    private final JwtService jwtService;

    // Security

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    // Repositories

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final OtpVerificationRepository otpRepository;
    private final RefreshTokenRepository tokenRepository;
    private final LoginHistoryRepository historyRepository;
    private final UserDeviceRepository deviceRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper;

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


    // Email Service

    private final EmailService emailService;

    //------------------------------------
    // REGISTRATION
    //------------------------------------

    public String registerSuperAdmin(Users request) {

        // =====================================================
        // 1. CHECK EMAIL
        // =====================================================

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }


        // =====================================================
        // 2. SET ROLE
        // =====================================================

        request.setRole("SUPER_ADMIN");


        // =====================================================
        // 3. GENERATE ADMIN EMPLOYEE ID
        // =====================================================

        long adminCount = userRoleRepository.countByRole_RoleNameAndStatus(
                "SUPER_ADMIN",
                "ACTIVE"
        );

        long nextNumber = adminCount + 1;

        int currentYear = LocalDate.now().getYear();

        String employeeCode = String.format(
                "QD-ADMIN-%d-%03d",
                currentYear,
                nextNumber
        );

        request.setEmployeeId(employeeCode);


        // =====================================================
        // 4. ENCRYPT PASSWORD
        // =====================================================

        request.setPassword(
                passwordEncoder.encode(request.getPassword())
        );


        // =====================================================
        // 5. DEFAULT ACCOUNT VALUES
        // =====================================================

        request.setRole("SUPER_ADMIN");
        request.setActive(true);
        request.setVerified(false);
        request.setAccountLocked(false);
        request.setAccountExpired(false);
        request.setCredentialsExpired(false);
        request.setLoginAttempts(0);
        request.setCreatedAt(LocalDateTime.now());


        // =====================================================
        // 6. SAVE USER
        // =====================================================

        Users savedUser = userRepository.save(request);


        // =====================================================
        // 7. ASSIGN SUPER ADMIN ROLE
        // =====================================================

        Role role = roleRepository
                .findByRoleName("SUPER_ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("SUPER_ADMIN role not found")
                );

        UserRole userRole = new UserRole();

        userRole.setUsers(savedUser);
        userRole.setRole(role);
        userRole.setAssignedBy("SYSTEM");
        userRole.setAssignedDate(LocalDateTime.now());
        userRole.setStatus("ACTIVE");
        userRole.setCreatedAt(LocalDateTime.now());

        userRoleRepository.save(userRole);

        userRoleRepository.save(userRole);


        // =====================================================
        // 8. GENERATE OTP
        // =====================================================

        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000
        );


        // =====================================================
        // 9. SAVE OTP
        // =====================================================

        OtpVerification verification = new OtpVerification();

        verification.setEmail(savedUser.getEmail());
        verification.setMobileNumber(savedUser.getMobileNumber());
        verification.setOtp(otp);
        verification.setOtpType("REGISTER OTP");
        verification.setVerificationStatus("PENDING");
        verification.setExpiryTime(
                LocalDateTime.now().plusMinutes(10)
        );
        verification.setCreatedAt(LocalDateTime.now());

        otpRepository.save(verification);


        // =====================================================
        // 10. REGISTRATION EMAIL
        // =====================================================

        emailService.sendMail(
                savedUser.getEmail(),

                "Registration Successful",

                "Hello " + savedUser.getFirstName()
                        + ",\n\n"
                        + "Your Super Admin account has been created successfully.\n\n"
                        + "Employee ID : " + savedUser.getEmployeeId()
                        + "\nRole : SUPER_ADMIN\n\n"
                        + "Thank You.\n"
                        + "QuickDines Team"
        );


        // =====================================================
        // 11. WELCOME EMAIL + OTP
        // =====================================================

        emailService.sendMail(

                savedUser.getEmail(),

                "Welcome to QuickDines",

                "Welcome " + savedUser.getFirstName()
                        + "!\n\n"
                        + "We're excited to have you onboard.\n\n"
                        + "Please verify your account using the OTP below.\n\n"
                        + "OTP : " + otp
                        + "\n\n"
                        + "OTP is valid for 10 minutes."
        );


        // =====================================================
        // 12. AUDIT LOG
        // =====================================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "SUPER_ADMIN",
                savedUser.getEmployeeId(),
                performedBy,
                savedUser.getEmployeeId(),
                "SUPER_ADMIN account created successfully for employee ID: "
                        + savedUser.getEmployeeId()
        );


        auditLogsService.logActivity(
                savedUser.getEmployeeId(),
                "REGISTER_SUPER_ADMIN",
                "AUTHENTICATION",
                "SUPER_ADMIN account registered successfully. Employee ID: "
                        + savedUser.getEmployeeId(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        auditLogsService.logInfo(
                "AUTHENTICATION",
                "AuthService",
                "SUPER_ADMIN registration completed successfully. Employee ID: "
                        + savedUser.getEmployeeId()
        );


        // =====================================================
        // 13. RESPONSE
        // =====================================================

        return "SUPER_ADMIN registered successfully. Employee ID: "
                + savedUser.getEmployeeId();
    }



    //------------------------------------
// LOGIN
//------------------------------------

    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest) {

        // =====================================================
        // 1. AUTHENTICATE USER
        // =====================================================

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmployeeId(),
                        request.getPassword()
                )
        );


        // =====================================================
        // 2. GET USER
        // =====================================================

        Users user = userRepository
                .findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // =====================================================
        // 3. GENERATE JWT TOKEN
        // =====================================================

        String token =
                jwtService.generateToken(
                        user.getEmployeeId()
                );


        // =====================================================
        // 4. UPDATE LAST LOGIN TIME
        // =====================================================

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);


        // =====================================================
        // 5. GET USER AGENT
        // =====================================================

        String userAgent =
                httpRequest.getHeader("User-Agent");


        // =====================================================
        // 6. GET BROWSER NAME
        // =====================================================

        String browserName = "Unknown Browser";

        if (userAgent != null) {

            String agent =
                    userAgent.toLowerCase();

            if (agent.contains("edg")) {

                browserName = "Microsoft Edge";

            } else if (agent.contains("opr")
                    || agent.contains("opera")) {

                browserName = "Opera";

            } else if (agent.contains("chrome")) {

                browserName = "Google Chrome";

            } else if (agent.contains("firefox")) {

                browserName = "Mozilla Firefox";

            } else if (agent.contains("safari")) {

                browserName = "Safari";
            }
        }


        // =====================================================
        // 7. GET OPERATING SYSTEM
        // =====================================================

        String operatingSystem =
                "Unknown OS";

        if (userAgent != null) {

            String agent =
                    userAgent.toLowerCase();

            if (agent.contains("windows")) {

                operatingSystem = "Windows";

            } else if (agent.contains("mac os")
                    || agent.contains("macintosh")) {

                operatingSystem = "Mac OS";

            } else if (agent.contains("android")) {

                operatingSystem = "Android";

            } else if (agent.contains("iphone")
                    || agent.contains("ipad")
                    || agent.contains("ios")) {

                operatingSystem = "iOS";

            } else if (agent.contains("linux")) {

                operatingSystem = "Linux";
            }
        }


        // =====================================================
        // 8. GET IP ADDRESS
        // =====================================================

        String ipAddress =
                httpRequest.getHeader("X-Forwarded-For");

        if (ipAddress == null
                || ipAddress.isEmpty()
                || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress =
                    httpRequest.getHeader("X-Real-IP");
        }

        if (ipAddress == null
                || ipAddress.isEmpty()
                || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress =
                    httpRequest.getRemoteAddr();
        }

        // X-Forwarded-For can contain multiple IPs
        if (ipAddress != null
                && ipAddress.contains(",")) {

            ipAddress =
                    ipAddress.split(",")[0].trim();
        }


        // =====================================================
        // 9. GET DEVICE NAME
        // =====================================================

        String deviceName =
                "Unknown Device";

        if (operatingSystem.equals("Windows")
                || operatingSystem.equals("Mac OS")
                || operatingSystem.equals("Linux")) {

            deviceName = "Desktop";

        } else if (operatingSystem.equals("Android")
                || operatingSystem.equals("iOS")) {

            deviceName = "Mobile";
        }


        // =====================================================
        // 10. SAVE DEVICE DETAILS
        // =====================================================

        UserDevice device =
                new UserDevice();

        device.setUsers(user);
        device.setDeviceName(deviceName);
        device.setBrowserName(browserName);
        device.setOperatingSystem(operatingSystem);
        device.setIpAddress(ipAddress);
        device.setDeviceStatus("ACTIVE");
        device.setLastLogin(LocalDateTime.now());
        device.setCreatedAt(LocalDateTime.now());

        deviceRepository.save(device);


        // =====================================================
        // 11. SAVE LOGIN HISTORY
        // =====================================================

        LoginHistory history =
                new LoginHistory();

        history.setUsers(user);
        history.setLoginDate(LocalDate.now());
        history.setLoginTime(LocalTime.now());
        history.setIpAddress(ipAddress);
        history.setBrowserName(browserName);
        history.setOperatingSystem(operatingSystem);
        history.setLoginStatus(LoginStatus.valueOf("SUCCESS"));
        history.setRemarks("Login Successful");
        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);


        // =====================================================
        // 12. ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "LOGIN",

                "AUTHENTICATION",

                "Employee logged into the system successfully",

                ActivityStatus.SUCCESS,

                ipAddress,

                browserName,

                operatingSystem
        );


        // =====================================================
        // 13. AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(

                "AUTHENTICATION",

                user.getEmployeeId(),

                AuditActionType.LOGIN,

                user.getEmployeeId(),

                user.getEmployeeId(),

                "Employee logged into the system successfully",

                null,

                null,

                ipAddress,

                deviceName
        );


        // =====================================================
        // 14. SYSTEM LOG
        // =====================================================

        auditLogsService.createSystemLog(

                SystemLogLevel.INFO,

                "AUTHENTICATION",

                "AuthService",

                "/auth/login",

                "POST",

                200,

                "Login successful. Employee ID: "
                        + user.getEmployeeId(),

                null,

                "HRMS-SERVER"
        );


        // =====================================================
        // 15. SEND DEVICE LOGIN ALERT EMAIL
        // =====================================================

        emailService.sendMail(

                user.getEmail(),

                "New Device Login Alert",

                "A new login was detected in your account."

                        + "\n\n"

                        + "Employee ID : "
                        + user.getEmployeeId()

                        + "\n"

                        + "Date : "
                        + LocalDate.now()

                        + "\n"

                        + "Time : "
                        + LocalTime.now()

                        + "\n"

                        + "IP Address : "
                        + ipAddress

                        + "\n"

                        + "Browser : "
                        + browserName

                        + "\n"

                        + "Operating System : "
                        + operatingSystem

                        + "\n"

                        + "Device : "
                        + deviceName
        );


        // =====================================================
        // 16. RETURN LOGIN RESPONSE
        // =====================================================

        return LoginResponse.builder()

                .token(token)

                .message("Login Successful")

                .build();
    }


    //------------------------------------
// LOGOUT
//------------------------------------

    public String logout(HttpServletRequest request) {

        // =====================================================
        // 1. GET JWT TOKEN FROM HEADER
        // =====================================================

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            throw new RuntimeException("Invalid Token.");
        }

        String jwtToken =
                authHeader.substring(7);


        // =====================================================
        // 2. EXTRACT EMPLOYEE ID
        // =====================================================

        String employeeId =
                jwtService.extractUsername(jwtToken);


        // =====================================================
        // 3. GET USER
        // =====================================================

        Users user =
                userRepository
                        .findByEmployeeId(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));


        // =====================================================
        // 4. GET USER AGENT
        // =====================================================

        String userAgent =
                request.getHeader("User-Agent");


        // =====================================================
        // 5. GET BROWSER NAME
        // =====================================================

        String browserName =
                "Unknown Browser";

        if (userAgent != null) {

            String agent =
                    userAgent.toLowerCase();

            if (agent.contains("edg")) {

                browserName =
                        "Microsoft Edge";

            } else if (agent.contains("opr")
                    || agent.contains("opera")) {

                browserName =
                        "Opera";

            } else if (agent.contains("chrome")) {

                browserName =
                        "Google Chrome";

            } else if (agent.contains("firefox")) {

                browserName =
                        "Mozilla Firefox";

            } else if (agent.contains("safari")) {

                browserName =
                        "Safari";
            }
        }


        // =====================================================
        // 6. GET OPERATING SYSTEM
        // =====================================================

        String operatingSystem =
                "Unknown OS";

        if (userAgent != null) {

            String agent =
                    userAgent.toLowerCase();

            if (agent.contains("windows")) {

                operatingSystem =
                        "Windows";

            } else if (agent.contains("mac os")
                    || agent.contains("macintosh")) {

                operatingSystem =
                        "Mac OS";

            } else if (agent.contains("android")) {

                operatingSystem =
                        "Android";

            } else if (agent.contains("iphone")
                    || agent.contains("ipad")
                    || agent.contains("ios")) {

                operatingSystem =
                        "iOS";

            } else if (agent.contains("linux")) {

                operatingSystem =
                        "Linux";
            }
        }


        // =====================================================
        // 7. GET IP ADDRESS
        // =====================================================

        String ipAddress =
                request.getHeader("X-Forwarded-For");

        if (ipAddress == null
                || ipAddress.isEmpty()
                || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress =
                    request.getHeader("X-Real-IP");
        }

        if (ipAddress == null
                || ipAddress.isEmpty()
                || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress =
                    request.getRemoteAddr();
        }

        // X-Forwarded-For can contain multiple IPs
        if (ipAddress != null
                && ipAddress.contains(",")) {

            ipAddress =
                    ipAddress.split(",")[0].trim();
        }


        // =====================================================
        // 8. GET DEVICE NAME
        // =====================================================

        String deviceName =
                "Unknown Device";

        if (operatingSystem.equals("Windows")
                || operatingSystem.equals("Mac OS")
                || operatingSystem.equals("Linux")) {

            deviceName =
                    "Desktop";

        } else if (operatingSystem.equals("Android")
                || operatingSystem.equals("iOS")) {

            deviceName =
                    "Mobile";
        }


        // =====================================================
        // 9. UPDATE LOGIN HISTORY
        // =====================================================

        LoginHistory history =
                (LoginHistory) historyRepository
                        .findTopByUsersOrderByIdDesc(user)
                        .orElse(null);

        if (history != null) {

            history.setLogoutTime(
                    LocalTime.now());

            historyRepository.save(history);
        }


        // =====================================================
        // 10. UPDATE DEVICE STATUS
        // =====================================================

        UserDevice device =
                (UserDevice) deviceRepository
                        .findTopByUsersOrderByIdDesc(user)
                        .orElse(null);

        if (device != null) {

            device.setDeviceStatus(
                    "LOGGED OUT");

            deviceRepository.save(device);
        }


        // =====================================================
        // 11. REVOKE REFRESH TOKEN
        // =====================================================

        RefreshToken refreshToken =
                (RefreshToken) tokenRepository
                        .findByUsers(user).orElse(null);

        if (refreshToken != null) {

            refreshToken.setRevoked(true);

            tokenRepository.save(refreshToken);
        }


        // =====================================================
        // 12. ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "LOGOUT",

                "AUTHENTICATION",

                "Employee logged out of the system successfully",

                ActivityStatus.SUCCESS,

                ipAddress,

                browserName,

                operatingSystem
        );


        // =====================================================
        // 13. AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(

                "AUTHENTICATION",

                null,

                AuditActionType.LOGOUT,

                user.getEmployeeId(),

                user.getEmployeeId(),

                "Employee logged out of the system successfully",

                null,

                null,

                ipAddress,

                deviceName
        );


        // =====================================================
        // 14. SYSTEM LOG
        // =====================================================

        auditLogsService.createSystemLog(

                SystemLogLevel.INFO,

                "AUTHENTICATION",

                "AuthService",

                "/auth/logout",

                "POST",

                200,

                "Logout successful. Employee ID: "
                        + user.getEmployeeId()
                        + ", IP: "
                        + ipAddress
                        + ", Browser: "
                        + browserName
                        + ", OS: "
                        + operatingSystem,

                null,

                "HRMS-SERVER"
        );


        // =====================================================
        // 15. OPTIONAL LOGOUT EMAIL
        // =====================================================

//    emailService.sendMail(
//
//            user.getEmail(),
//
//            "Logout Successful",
//
//            "You have successfully logged out of QuickDines."
//    );


        // =====================================================
        // 16. RETURN RESPONSE
        // =====================================================

        return "Logout Successful.";
    }



    //------------------------------------
// PASSWORD APIs
//------------------------------------

    public String forgotPassword(String email) {

        // Check User Exists
        Users user = (Users) userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        // Generate OTP
        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000);

        // Save OTP Details
        OtpVerification otpVerification =
                new OtpVerification();

        otpVerification.setUsers(user);
        otpVerification.setEmail(user.getEmail());
        otpVerification.setMobileNumber(user.getMobileNumber());
        otpVerification.setOtp(otp);
        otpVerification.setOtpType("PASSWORD RESET OTP");
        otpVerification.setVerificationStatus("PENDING");
        otpVerification.setExpiryTime(
                LocalDateTime.now().plusMinutes(10));
        otpVerification.setCreatedAt(
                LocalDateTime.now());

        otpRepository.save(otpVerification);


        // Send Email
        emailService.sendMail(

                user.getEmail(),

                "QuickDines Forgot Password OTP",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your password reset OTP is : "
                        + otp

                        + "\n\n"

                        + "OTP is valid for 10 minutes."

                        + "\n\n"

                        + "Do not share this OTP with anyone."
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "FORGOT_PASSWORD",

                "AUTHENTICATION",

                "Password reset OTP generated and sent successfully",

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================

// =====================================================
// AUDIT LOG
// =====================================================


        auditLogsService.createAuditLog(

                "AUTHENTICATION",

                user.getEmployeeId(),

                AuditActionType.UPDATE,

                user.getEmployeeId(),

                user.getEmployeeId(),

                "Password reset OTP requested",

                null,
                null,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "AUTHENTICATION",

                "AuthService",

                "Forgot password OTP generated successfully. "
                        + "Employee ID: "
                        + user.getEmployeeId()
        );


        return "Forgot Password OTP sent successfully.";
    }


    public String resetPassword(
            String email,
            String otp,
            String newPassword) {

        // Check User Exists
        Users user = (Users) userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // Check OTP Exists
        OtpVerification otpVerification =
                otpRepository
                        .findByEmailAndOtp(email, otp)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid OTP"));


        // Check OTP Expiry
        if (otpVerification
                .getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            // =================================================
            // ACTIVITY LOG - FAILED
            // =================================================

            auditLogsService.logActivity(

                    user.getEmployeeId(),

                    "RESET_PASSWORD",

                    "AUTHENTICATION",

                    "Password reset failed because OTP expired",

                    ActivityStatus.FAILED,

                    clientInfoService.getClientInfo().getIpAddress(),
                    clientInfoService.getClientInfo().getBrowser(),
                    clientInfoService.getClientInfo().getOperatingSystem()
            );


            // =================================================
            // SYSTEM LOG - ERROR
            // =================================================

            auditLogsService.logError(

                    "AUTHENTICATION",

                    "AuthService",

                    "Password reset failed. OTP expired for employee: "
                            + user.getEmployeeId(),

                    null
            );


            throw new RuntimeException("OTP Expired");
        }


        // Encrypt Password
        String encodedPassword =
                passwordEncoder.encode(newPassword);


        // Update Password
        user.setPassword(encodedPassword);

        userRepository.save(user);


        // Update OTP Status
        otpVerification.setVerificationStatus(
                "VERIFIED");

        otpRepository.save(otpVerification);


        // Send Password Reset Mail
        emailService.sendMail(

                user.getEmail(),

                "Password Reset Successful",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your password has been reset successfully."

                        + "\n\n"

                        + "If this was not you, please contact support immediately."
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "RESET_PASSWORD",

                "AUTHENTICATION",

                "Password reset successfully using OTP",

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(

                "AUTHENTICATION",

                user.getEmployeeId(),

                AuditActionType.UPDATE,

                user.getEmployeeId(),

                user.getEmployeeId(),

                "Employee password reset successfully",

                null,
                "PASSWORD_RESET",

                null,
                null
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "AUTHENTICATION",

                "AuthService",

                "Password reset successful. Employee ID: "
                        + user.getEmployeeId()
        );


        return "Password Updated Successfully.";
    }


    public String changePassword(
            ChangePasswordRequest request) {

        // Check User Exists
        Users user =
                (Users) userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));


        // Check Old Password
        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {


            // =================================================
            // ACTIVITY LOG - FAILED
            // =================================================

            auditLogsService.logActivity(

                    user.getEmployeeId(),

                    "CHANGE_PASSWORD",

                    "AUTHENTICATION",

                    "Password change failed because old password is incorrect",

                    ActivityStatus.FAILED,

                    clientInfoService.getClientInfo().getIpAddress(),
                    clientInfoService.getClientInfo().getBrowser(),
                    clientInfoService.getClientInfo().getOperatingSystem()
            );


            // =================================================
            // SYSTEM LOG - WARNING
            // =================================================

            auditLogsService.logWarning(

                    "AUTHENTICATION",

                    "AuthService",

                    "Incorrect old password during password change. "
                            + "Employee ID: "
                            + user.getEmployeeId()
            );


            throw new RuntimeException(
                    "Old Password is Incorrect");
        }


        // Check New Password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {


            // =================================================
            // ACTIVITY LOG - FAILED
            // =================================================

            auditLogsService.logActivity(

                    user.getEmployeeId(),

                    "CHANGE_PASSWORD",

                    "AUTHENTICATION",

                    "Password change failed because passwords do not match",

                    ActivityStatus.FAILED,

                    clientInfoService.getClientInfo().getIpAddress(),
                    clientInfoService.getClientInfo().getBrowser(),
                    clientInfoService.getClientInfo().getOperatingSystem()
            );


            // =================================================
            // SYSTEM LOG - WARNING
            // =================================================

            auditLogsService.logWarning(

                    "AUTHENTICATION",

                    "AuthService",

                    "Password confirmation mismatch for employee: "
                            + user.getEmployeeId()
            );


            throw new RuntimeException(
                    "Passwords do not match");
        }


        // Prevent Same Password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {


            // =================================================
            // ACTIVITY LOG - FAILED
            // =================================================

            auditLogsService.logActivity(

                    user.getEmployeeId(),

                    "CHANGE_PASSWORD",

                    "AUTHENTICATION",

                    "Password change failed because new password "
                            + "is the same as old password",

                    ActivityStatus.FAILED,

                    clientInfoService.getClientInfo().getIpAddress(),
                    clientInfoService.getClientInfo().getBrowser(),
                    clientInfoService.getClientInfo().getOperatingSystem()
            );


            // =================================================
            // SYSTEM LOG - WARNING
            // =================================================

            auditLogsService.logWarning(

                    "AUTHENTICATION",

                    "AuthService",

                    "Employee attempted to reuse previous password. "
                            + "Employee ID: "
                            + user.getEmployeeId()
            );


            throw new RuntimeException(
                    "New password cannot be the same as the old password");
        }


        // Encrypt Password
        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword());


        user.setPassword(encodedPassword);


        // Update Database
        userRepository.save(user);


        // Send Password Changed Email
        emailService.sendMail(

                user.getEmail(),

                "Password Changed Successfully",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your QuickDines account password "
                        + "has been changed successfully."

                        + "\n\n"

                        + "If you did not perform this action, "
                        + "please contact the administrator immediately."
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "CHANGE_PASSWORD",

                "AUTHENTICATION",

                "Employee password changed successfully",

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(

                "AUTHENTICATION",

                user.getEmployeeId(),

                AuditActionType.UPDATE,

                user.getEmployeeId(),

                user.getEmployeeId(),

                "Employee password changed successfully",

                null,
                "PASSWORD_CHANGED",

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "AUTHENTICATION",

                "AuthService",

                "Password changed successfully. Employee ID: "
                        + user.getEmployeeId()
        );


        return "Password Changed Successfully.";
    }

    //------------------------------------
    // OTP APIs
    //------------------------------------

    public String sendOTP(String email) {

        Users user = (Users) userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Generate 6 digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        OtpVerification verification = new OtpVerification();
        verification.setUsers(user);
        verification.setEmail(user.getEmail());
        verification.setMobileNumber(user.getMobileNumber());
        verification.setOtp(otp);
        verification.setOtpType("EMAIL OTP");
        verification.setVerificationStatus("PENDING");
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        verification.setCreatedAt(LocalDateTime.now());
        otpRepository.save(verification);
        // Send Mail
        emailService.sendMail(
                user.getEmail(),

                "QuickDines OTP Verification",

                "Your OTP is : "
                        + otp
                        + "\n\nOTP is valid for 10 minutes.");
        return "OTP Sent Successfully.";
    }
    public String verifyOTP(String email,
                            String otp) {
        OtpVerification verification = otpRepository.findByEmailAndOtp(email,otp).orElseThrow(() -> new RuntimeException("Invalid OTP"));
        // Check Expiry
        if (verification.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "OTP Expired");}
        // Update Status
        verification.setVerificationStatus("VERIFIED");
        otpRepository.save(verification);
        return "OTP Verified Successfully.";
    }

    public String resendOTP(String email) {

        Users user = (Users) userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        OtpVerification verification = new OtpVerification();
        verification.setUsers(user);
        verification.setEmail(user.getEmail());
        verification.setMobileNumber(user.getMobileNumber());
        verification.setOtp(otp);
        verification.setOtpType("EMAIL OTP");
        verification.setVerificationStatus("PENDING");
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        verification.setCreatedAt(LocalDateTime.now());
        otpRepository.save(verification);
        emailService.sendMail(

                user.getEmail(),

                "QuickDines Resend OTP",

                "Your New OTP is : "
                        + otp
                        + "\n\nOTP is valid for 10 minutes.");
        return "OTP Resent Successfully.";}



    //------------------------------------
    // JWT APIs
    //------------------------------------

    public LoginResponse refreshToken(String refreshToken) {

        // Check Refresh Token
        RefreshToken token = tokenRepository.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));
        // Check Token Expiry
        if (token.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh Token Expired");
        }
        // Check Token Status
        if (token.isRevoked()) {

            throw new RuntimeException(
                    "Refresh Token Revoked");
        }
        // Get User
        Users user = token.getUsers();
        // Generate New JWT Access Token
        String accessToken = jwtService.generateToken(user.getEmployeeId());
        // Optional - Generate New Refresh Token
        String newRefreshToken =
                UUID.randomUUID().toString();
        token.setToken(newRefreshToken);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        tokenRepository.save(token);
        // Optional Email Notification
        emailService.sendMail(
                user.getEmail(),

                "Refresh Token Generated",

                "A new access token has been generated successfully.");
        // Return Response
        return LoginResponse.builder()

                .message("New Token Generated")
                .token(accessToken)
                .refreshToken(newRefreshToken)

                .build();
    }



    //------------------------------------
    // PROFILE APIs
    //------------------------------------

    public Users getProfile(String employeeId) {return userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User Not Found"));}





    public String updateProfile(String employeeId,Users request) {

        Users user = userRepository.findById(Long.valueOf(employeeId)).orElseThrow(() -> new RuntimeException("User Not Found"));


        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setProfileImage(request.getProfileImage());
        userRepository.save(user);
        // Send Profile Updated Email
        emailService.sendMail(

                user.getEmail(),

                "Profile Updated Successfully",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your QuickDines profile has been updated successfully.");
        return "Profile Updated Successfully.";}



    //------------------------------------
    // LOGIN HISTORY
    //------------------------------------

    public List<LoginHistory> getLoginHistory(){

        return historyRepository.findAll();

    }



    //------------------------------------
    // DEVICES
    //------------------------------------

    public List<UserDevice> getDevices(){

        return deviceRepository.findAll();

    }


    public String removeDevice(Long id) {

        // Check whether the device exists
        UserDevice device = (UserDevice) deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device Not Found"));

        // Update device status

        device.setDeviceStatus("REMOVED");

        // Save changes

        deviceRepository.save(device);
        // Get user details
        Users user = device.getUsers();
        // Send email notification
        emailService.sendMail(

                user.getEmail(),

                "Device Removed Successfully",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "One of your registered devices has been removed successfully."
                        + "\n\n"
                        + "If this action was not performed by you, please contact the administrator immediately.");
        return "Device Removed Successfully.";
    }



    //------------------------------------
    // USERS
    //------------------------------------

    //------------------------------------
// CREATE USER
//------------------------------------

    public String createUser(Users request) {

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setActive(true);
        request.setVerified(true);
        request.setAccountLocked(false);
        request.setAccountExpired(false);
        request.setCredentialsExpired(false);
        request.setLoginAttempts(0);
        request.setCreatedAt(LocalDateTime.now());

        userRepository.save(request);

        emailService.sendMail(
                request.getEmail(),
                "Account Created Successfully",

                "Hello " + request.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been created successfully."
                        + "\n\n"
                        + "Role : " + request.getRole()
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                request.getEmployeeId(),

                "CREATE_USER",

                "USER_MANAGEMENT",

                "New user account created successfully. Employee ID: "
                        + request.getEmployeeId(),

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.createAuditLog(

                "USER_MANAGEMENT",

                request.getEmployeeId(),

                AuditActionType.CREATE,

                performedBy,

                request.getEmployeeId(),

                "User created successfully",

                null,

                "{"
                        + "\"employeeId\":\"" + request.getEmployeeId() + "\","
                        + "\"email\":\"" + request.getEmail() + "\","
                        + "\"role\":\"" + request.getRole() + "\","
                        + "\"active\":" + request.isActive()
                        + "}",

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "USER_MANAGEMENT",

                "UserService",

                "User created successfully. Employee ID: "
                        + request.getEmployeeId()
        );


        return "User Created Successfully.";
    }

    //------------------------------------
// BLOCK USER
//------------------------------------

    public String blockUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        // OLD INFORMATION
        String oldValue =
                "{"
                        + "\"active\":" + user.isActive() + ","
                        + "\"accountLocked\":" + user.isAccountLocked()
                        + "}";


        user.setAccountLocked(true);
        user.setActive(false);

        userRepository.save(user);


        emailService.sendMail(

                user.getEmail(),

                "Account Blocked",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been blocked by the administrator."
        );


        // NEW INFORMATION
        String newValue =
                "{"
                        + "\"active\":" + user.isActive() + ","
                        + "\"accountLocked\":" + user.isAccountLocked()
                        + "}";


        // =====================================================
        // ACTIVITY LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(

                user.getEmployeeId(),

                "BLOCK_USER",

                "USER_MANAGEMENT",

                "User account blocked successfully. Employee ID: "
                        + user.getEmployeeId(),

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.createAuditLog(

                "USER_MANAGEMENT",

                user.getEmployeeId(),

                AuditActionType.UPDATE,

                performedBy,

                user.getEmployeeId(),

                "User account blocked",

                oldValue,

                newValue,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "USER_MANAGEMENT",

                "UserService",

                "User account blocked successfully. User ID: "
                        + id
        );


        return "User Blocked Successfully.";
    }


    //------------------------------------
// UNBLOCK USER
//------------------------------------

    public String unblockUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // OLD INFORMATION
        String oldValue =
                "{"
                        + "\"active\":" + user.isActive() + ","
                        + "\"accountLocked\":" + user.isAccountLocked()
                        + "}";


        user.setAccountLocked(false);
        user.setActive(true);

        userRepository.save(user);


        emailService.sendMail(

                user.getEmail(),

                "Account Unblocked",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been unblocked successfully."
        );


        // NEW INFORMATION
        String newValue =
                "{"
                        + "\"active\":" + user.isActive() + ","
                        + "\"accountLocked\":" + user.isAccountLocked()
                        + "}";


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "UNBLOCK_USER",

                "USER_MANAGEMENT",

                "User account unblocked successfully. Employee ID: "
                        + user.getEmployeeId(),

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.createAuditLog(

                "USER_MANAGEMENT",

                user.getEmployeeId(),

                AuditActionType.UPDATE,

                performedBy,

                user.getEmployeeId(),

                "User account unblocked",

                oldValue,

                newValue,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "USER_MANAGEMENT",

                "UserService",

                "User account unblocked successfully. User ID: "
                        + id
        );


        return "User Unblocked Successfully.";
    }

    //------------------------------------
// DELETE USER
//------------------------------------

    public String deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // OLD INFORMATION
        String oldValue =
                "{"
                        + "\"active\":" + user.isActive()
                        + "}";


        user.setActive(false);

        userRepository.save(user);


        emailService.sendMail(

                user.getEmail(),

                "Account Deleted",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been deleted by the administrator."
        );


        // NEW INFORMATION
        String newValue =
                "{"
                        + "\"active\":" + user.isActive()
                        + "}";


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                user.getEmployeeId(),

                "DELETE_USER",

                "USER_MANAGEMENT",

                "User account deleted successfully. Employee ID: "
                        + user.getEmployeeId(),

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.createAuditLog(

                "USER_MANAGEMENT",

                user.getEmployeeId(),

                AuditActionType.DELETE,

                performedBy,

                user.getEmployeeId(),

                "User account deleted",

                oldValue,

                newValue,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "USER_MANAGEMENT",

                "UserService",

                "User account deleted successfully. User ID: "
                        + id
        );


        return "User Deleted Successfully.";
    }


//------------------------------------
// ROLES
//------------------------------------

    public String createRole(Role role) {

        role.setCreatedAt(LocalDateTime.now());

        roleRepository.save(role);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

//        auditLogsService.logActivity(
//
//                getLoggedInEmployeeId(),
//
//                "CREATE_ROLE",
//
//                "ROLE_MANAGEMENT",
//
//                "Role created successfully. Role ID: "
//                        + role.getId()
//                        + ", Role Name: "
//                        + role.getRoleName(),
//
//                ActivityStatus.SUCCESS,
//
//                clientInfoService.getClientInfo().getIpAddress(),
//
//                clientInfoService.getClientInfo().getBrowser(),
//
//                clientInfoService.getClientInfo().getOperatingSystem()
//        );
//
//
//        // =====================================================
//        // AUDIT LOG
//        // =====================================================
//        String performedBy = getLoggedInEmployeeId();
//        auditLogsService.createAuditLog(
//
//                "ROLE_MANAGEMENT",
//
//                performedBy,
//
//                AuditActionType.CREATE,
//
//              getLoggedInEmployeeId(),
//
//                getLoggedInEmployeeId(),
//
//                "Role created successfully",
//
//                null,
//
//                "{"
//                        + "\"roleId\":\"" + role.getId() + "\","
//                        + "\"roleName\":\"" + role.getRoleName() + "\","
//                        + "\"description\":\"" + role.getDescription() + "\""
//                        + "}",
//
//                clientInfoService.getClientInfo().getIpAddress(),
//
//                clientInfoService.getClientInfo().getOperatingSystem()
//        );
//
//
//        // =====================================================
//        // SYSTEM LOG
//        // =====================================================
//
//        auditLogsService.logInfo(
//
//                "ROLE_MANAGEMENT",
//
//                "RoleService",
//
//                "Role created successfully. Role ID: "
//                        + role.getId()
//                        + ", Role Name: "
//                        + role.getRoleName()
//        );


        return "Role Created Successfully.";
    }


    public List<Role> getRoles(){

        List<Role> roles = roleRepository.findAll();


//        // =====================================================
//        // ACTIVITY LOG
//        // =====================================================
//
//        auditLogsService.logActivity(
//
//                getLoggedInEmployeeId(),
//
//                "GET_ROLES",
//
//                "ROLE_MANAGEMENT",
//
//                "All roles retrieved successfully. Total roles: "
//                        + roles.size(),
//
//                ActivityStatus.SUCCESS,
//
//                clientInfoService.getClientInfo().getIpAddress(),
//
//                clientInfoService.getClientInfo().getBrowser(),
//
//                clientInfoService.getClientInfo().getOperatingSystem()
//        );
//
//
//        // =====================================================
//        // SYSTEM LOG
//        // =====================================================
//
//        auditLogsService.logInfo(
//
//                "ROLE_MANAGEMENT",
//
//                "RoleService",
//
//                "All roles retrieved successfully. Total roles: "
//                        + roles.size()
//        );


        return roles;
    }


    public Role getRole(Long id){

        Role role =
                roleRepository.findById(Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Role Not Found"));


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                getLoggedInEmployeeId(),

                "GET_ROLE",

                "ROLE_MANAGEMENT",

                "Role retrieved successfully. Role ID: "
                        + id,

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getBrowser(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "ROLE_MANAGEMENT",

                "RoleService",

                "Role retrieved successfully. Role ID: "
                        + id
        );


        return role;
    }


    public String updateRole(Long id, Role request) {

        Role role =
                roleRepository.findById(Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Role Not Found"));


        // =====================================================
        // OLD VALUE JSON
        // =====================================================

        String oldValue =
                "{"
                        + "\"roleId\":\"" + role.getId() + "\","
                        + "\"roleName\":\"" + role.getRoleName() + "\","
                        + "\"description\":\"" + role.getDescription() + "\""
                        + "}";


        role.setRoleName(request.getRoleName());

        role.setDescription(request.getDescription());

        role.setUpdatedAt(LocalDateTime.now());

        roleRepository.save(role);


        // =====================================================
        // NEW VALUE JSON
        // =====================================================

        String newValue =
                "{"
                        + "\"roleId\":\"" + role.getId() + "\","
                        + "\"roleName\":\"" + role.getRoleName() + "\","
                        + "\"description\":\"" + role.getDescription() + "\""
                        + "}";


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                getLoggedInEmployeeId(),

                "UPDATE_ROLE",

                "ROLE_MANAGEMENT",

                "Role updated successfully. Role ID: "
                        + id,

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getBrowser(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.createAuditLog(

                "ROLE_MANAGEMENT",

                performedBy,

                AuditActionType.UPDATE,

              getLoggedInEmployeeId(),

                getLoggedInEmployeeId(),

                "Role updated successfully",

                oldValue,

                newValue,

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "ROLE_MANAGEMENT",

                "RoleService",

                "Role updated successfully. Role ID: "
                        + id
                        + ", New Role Name: "
                        + role.getRoleName()
        );


        return "Role Updated Successfully.";
    }


    public String deleteRole(Long id) {

        Role role =
                roleRepository.findById(Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Role Not Found"));


        // =====================================================
        // OLD VALUE JSON
        // =====================================================

        String oldValue =
                "{"
                        + "\"roleId\":\"" + role.getId() + "\","
                        + "\"roleName\":\"" + role.getRoleName() + "\","
                        + "\"description\":\"" + role.getDescription() + "\""
                        + "}";


        roleRepository.delete(role);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(

                getLoggedInEmployeeId(),

                "DELETE_ROLE",

                "ROLE_MANAGEMENT",

                "Role deleted successfully. Role ID: "
                        + id,

                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getBrowser(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.createAuditLog(

                "ROLE_MANAGEMENT",

                performedBy,

                AuditActionType.DELETE,

                getLoggedInEmployeeId(),

               getLoggedInEmployeeId(),

                "Role deleted successfully",

                oldValue,

                null,

                clientInfoService.getClientInfo().getIpAddress(),

                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(

                "ROLE_MANAGEMENT",

                "RoleService",

                "Role deleted successfully. Role ID: "
                        + id
        );


        return "Role Deleted Successfully.";
    }





//------------------------------------
// USER ROLES
//------------------------------------

    public String assignRole(String userId, Long roleId) {

        // =====================================================
        // GET USER
        // =====================================================

        Users user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // =====================================================
        // GET ROLE
        // =====================================================

        Role role = roleRepository.findById(Math.toIntExact(roleId))
                .orElseThrow(() ->
                        new RuntimeException("Role Not Found"));


        // =====================================================
        // CREATE USER ROLE
        // =====================================================

        UserRole userRole = new UserRole();

        userRole.setUsers(user);
        userRole.setRole(role);

        userRole.setAssignedBy("SUPER_ADMIN");
        userRole.setAssignedDate(LocalDateTime.now());
        userRole.setStatus("ACTIVE");
        userRole.setCreatedAt(LocalDateTime.now());

        userRoleRepository.save(userRole);


        // =====================================================
        // UPDATE USER ROLE
        // =====================================================

        user.setRole(role.getRoleName());

        userRepository.save(user);


        // =====================================================
        // SEND MAIL
        // =====================================================

        emailService.sendMail(
                user.getEmail(),
                "Role Assigned Successfully",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your role has been assigned successfully."
                        + "\n\n"
                        + "Assigned Role : "
                        + role.getRoleName()
        );


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate(
                "USER_ROLE",
                userRole.getId().toString(),
                performedBy,
                user.getEmployeeId(),
                "Role '" + role.getRoleName()
                        + "' assigned to user '"
                        + userId + "'"
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                String.valueOf(userId),
                "ASSIGN_ROLE",
                "USER_ROLE",
                "Role '" + role.getRoleName()
                        + "' assigned successfully",
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "USER_ROLE",
                "RoleService",
                "Role assigned successfully. User ID: "
                        + userId
                        + ", Role: "
                        + role.getRoleName()
        );


        return "Role Assigned Successfully.";
    }


    public String removeRole(String userId) {

        // =====================================================
        // GET USER
        // =====================================================

        Users user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));


        // =====================================================
        // GET USER ROLE
        // =====================================================

        UserRole userRole =
                (UserRole) userRoleRepository.findByUsers(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Role Not Assigned"));


        String oldRole = userRole.getRole().getRoleName();


        // =====================================================
        // SOFT DELETE
        // =====================================================

        userRole.setStatus("REMOVED");

        userRoleRepository.save(userRole);


        // =====================================================
        // REMOVE ROLE FROM USER
        // =====================================================

        user.setRole(null);

        userRepository.save(user);


        // =====================================================
        // SEND MAIL
        // =====================================================

        emailService.sendMail(
                user.getEmail(),
                "Role Removed Successfully",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your role has been removed successfully."
        );


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete(
                "USER_ROLE",
                userRole.getId().toString(),
                performedBy,
                userId,
                "Role '" + oldRole
                        + "' removed from user '"
                        + userId + "'"
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                userId,
                "REMOVE_ROLE",
                "USER_ROLE",
                "Role '" + oldRole
                        + "' removed successfully",
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "USER_ROLE",
                "RoleService",
                "Role removed successfully. User ID: "
                        + userId
                        + ", Role: "
                        + oldRole
        );


        return "Role Removed Successfully.";
    }

    public Permission createPermission(Permission permission) {

        Permission savedPermission =
                permissionRepository.save(permission);


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate(
                "PERMISSION",
                savedPermission.getId().toString(),
                performedBy,
                null,
                "Permission created successfully. Permission: "
                        + savedPermission.getPermissionName()
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                null,
                "CREATE_PERMISSION",
                "PERMISSION",
                "Permission created successfully: "
                        + savedPermission.getPermissionName(),
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "Permission created successfully. Permission: "
                        + savedPermission.getPermissionName()
        );


        return savedPermission;
    }


// =========================================================
// GET ALL PERMISSIONS
// =========================================================

    public List<Permission> getPermissions() {

        List<Permission> permissions =
                permissionRepository.findAll();

        // =====================================================
        // ACTIVITY LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(
                performedBy,
                "GET_ALL_PERMISSIONS",
                "PERMISSION",
                "All permissions retrieved successfully. Total permissions: "
                        + permissions.size(),
                ActivityStatus.SUCCESS,
                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );

        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "All permissions retrieved successfully. Total permissions: "
                        + permissions.size()
        );

        return permissions;
    }



    public Permission getPermission(Long id) {

        Permission permission =
                permissionRepository.findById(
                                Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Permission Not Found"));


        auditLogsService.logActivity(
                permission.getId().toString(),
                "VIEW_PERMISSION",
                "PERMISSION",
                "Permission viewed. Permission ID: "
                        + id,
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "Permission retrieved successfully. Permission ID: "
                        + id
        );


        return permission;
    }
    public Permission updatePermission(
            Long id,
            Permission request) {

        Permission permission =
                permissionRepository.findById(Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Permission Not Found"));


        // =====================================================
        // OLD VALUES
        // =====================================================

        String oldValue =
                "{"
                        + "\"permissionName\":\""
                        + permission.getPermissionName()
                        + "\","
                        + "\"moduleName\":\""
                        + permission.getModuleName()
                        + "\","
                        + "\"description\":\""
                        + permission.getDescription()
                        + "\""
                        + "}";


        // =====================================================
        // UPDATE
        // =====================================================

        permission.setPermissionName(
                request.getPermissionName());

        permission.setModuleName(
                request.getModuleName());

        permission.setDescription(
                request.getDescription());


        Permission updatedPermission =
                permissionRepository.save(permission);


        // =====================================================
        // NEW VALUES
        // =====================================================

        String newValue =
                "{"
                        + "\"permissionName\":\""
                        + updatedPermission.getPermissionName()
                        + "\","
                        + "\"moduleName\":\""
                        + updatedPermission.getModuleName()
                        + "\","
                        + "\"description\":\""
                        + updatedPermission.getDescription()
                        + "\""
                        + "}";


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate(
                "PERMISSION",
                updatedPermission.getId().toString(),
                performedBy,
                permission.getId().toString(),
                "Permission updated successfully",
                oldValue,
                newValue
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                null,
                "UPDATE_PERMISSION",
                "PERMISSION",
                "Permission updated successfully. Permission ID: "
                        + id,
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "Permission updated successfully. Permission ID: "
                        + id
        );


        return updatedPermission;
    }

    public String deletePermission(Long id) {

        Permission permission =
                permissionRepository.findById(
                                Math.toIntExact(id))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Permission Not Found"));


        String permissionName =
                permission.getPermissionName();


        permissionRepository.delete(permission);


        // =====================================================
        // LOGGED-IN USER
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete(
                "PERMISSION",
                id.toString(),
              performedBy,
                null,
                "Permission deleted successfully: "
                        + permissionName
        );


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(
                performedBy,
                "DELETE_PERMISSION",
                "PERMISSION",
                "Permission deleted successfully: "
                        + permissionName,
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "Permission deleted successfully. Permission ID: "
                        + id
        );


        return "Permission Deleted Successfully";
    }

    public List<Permission> getModulePermissions(
            String moduleName) {

        List<Permission> permissions =
                permissionRepository.findByModuleName(
                        moduleName);

        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(
                performedBy,
                "VIEW_MODULE_PERMISSIONS",
                "PERMISSION",
                "Permissions viewed for module: "
                        + moduleName,
                ActivityStatus.SUCCESS,

                clientInfoService.getClientInfo().getIpAddress(),
                clientInfoService.getClientInfo().getBrowser(),
                clientInfoService.getClientInfo().getOperatingSystem()
        );


        auditLogsService.logInfo(
                "PERMISSION",
                "PermissionService",
                "Module permissions retrieved. Module: "
                        + moduleName
        );


        return permissions;
    }

// =========================================================
    // GET USERS BY STATUS
    // =========================================================

    public List<Users> getUsersByStatus(
            UserStatus status) {

        return userRepository.findByStatus(status);
    }


    // =========================================================
    // GET USERS BY ROLE
    // =========================================================

    public List<Users> getUsersByRole(
            String role) {

        return userRepository
                .findByRole(role);
    }


    // =========================================================
    // GET USERS BY STATUS + ROLE
    // =========================================================

    public List<Users> getUsersByStatusAndRole(
            UserStatus status,
            String role) {

        return userRepository.findByStatusAndRole(status, role);
    }
    // =========================================================
    // GET BY LOGIN STATUS
    // =========================================================

    public List<LoginHistory> getByLoginStatus(
            LoginStatus status) {

        return historyRepository
                .findByLoginStatus(status);
    }


    // =========================================================
    // GET USER LOGIN HISTORY
    // =========================================================

    public List<LoginHistory> getUserLoginHistory(
            Long userId) {

        return historyRepository
                .findByUsersId(userId);
    }


    // =========================================================
    // USER + LOGIN STATUS
    // =========================================================

    public List<LoginHistory> getUserLoginHistoryByStatus(
            Long userId,
            LoginStatus status) {

        return historyRepository
                .findByUsersIdAndLoginStatus(
                        userId,
                        status);
    }


    // =========================================================
    // LOGIN HISTORY BY DATE RANGE
    // =========================================================

    public List<LoginHistory> getLoginHistoryByDateRange(
            LocalDate fromDate,
            LocalDate toDate) {

        return historyRepository
                .findByLoginDateBetween(
                        fromDate,
                        toDate);
    }

    public List<LoginHistory> searchLoginHistory(
            String search) {

        if (search == null || search.trim().isEmpty()) {
            return historyRepository.findAll();
        }

        return historyRepository
                .searchLoginHistory(search.trim());
    }


}


