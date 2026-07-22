package com.HRMS.QuickDines.Auth.services;

import com.HRMS.QuickDines.Auth.Config.JwtService;
import com.HRMS.QuickDines.Auth.DTO.*;
import com.HRMS.QuickDines.Auth.model.*;
import com.HRMS.QuickDines.Auth.repo.*;
import com.HRMS.QuickDines.Email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HRMS.QuickDines.Auth.Config.JwtService;
import com.HRMS.QuickDines.Auth.DTO.LoginRequest;
import com.HRMS.QuickDines.Auth.DTO.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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


    // Email Service

    private final EmailService emailService;

    //------------------------------------
    // REGISTRATION
    //------------------------------------

    public String registerSuperAdmin(Users request) {

        // Check email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }
        // Encrypt Password
        request.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        // Set default values
        request.setRole("SUPER_ADMIN");
        request.setActive(true);
        request.setVerified(false);
        request.setAccountLocked(false);
        request.setAccountExpired(false);
        request.setCredentialsExpired(false);
        request.setLoginAttempts(0);
        request.setCreatedAt(LocalDateTime.now());
        // Save User
        Users savedUser = userRepository.save(request);
        // Assign SUPER_ADMIN Role
        Role role = roleRepository.findByRoleName("SUPER_ADMIN").orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

        UserRole userRole = new UserRole();

        userRole.setRole(role);
        userRole.setAssignedBy("SYSTEM");
        userRole.setAssignedDate(LocalDateTime.now());
        userRole.setStatus("ACTIVE");
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleRepository.save(userRole);
        // Generate OTP
        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000);
        // Save OTP
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
        // Send Registration Mail
        emailService.sendMail(
                savedUser.getEmail(),

                "Registration Successful",

                "Hello " + savedUser.getFirstName()
                        + ",\n\n"
                        + "Your Super Admin account has been created successfully.\n\n"
                        + "Employee ID : " + savedUser.getEmployeeId()
                        + "\nRole : SUPER_ADMIN\n\n"
                        + "Thank You.\n"
                        + "QuickDines Team");
        // Send Welcome Mail
        emailService.sendMail(

                savedUser.getEmail(),

                "Welcome to QuickDines",

                "Welcome " + savedUser.getFirstName()
                        + "!\n\n"
                        + "We're excited to have you onboard.\n\n"
                        + "Please verify your account using the OTP below.\n\n"
                        + "OTP : " + otp
                        + "\n\n"
                        + "OTP is valid for 10 minutes.");
        return "SUPER_ADMIN registered successfully.";}



    //------------------------------------
    // LOGIN
    //------------------------------------

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        // Authenticate User
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmployeeId(), request.getPassword()));
        // Get User Details
        Users user = userRepository.findByEmployeeId(request.getEmployeeId()).orElseThrow(() ->
                        new RuntimeException("User Not Found"));
        // Generate JWT Token

        String token = jwtService.generateToken(user.getEmployeeId());

        // Update Last Login Time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
//        // Generate Login OTP
//        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
//        // Save OTP Details
//        OtpVerification verification = new OtpVerification();
//        verification.setUsers(user);
//        verification.setEmail(user.getEmail());
//        verification.setMobileNumber(user.getMobileNumber());
//        verification.setOtp(otp);
//        verification.setOtpType("LOGIN OTP");
//        verification.setVerificationStatus("PENDING");
//        verification.setExpiryTime(LocalDateTime.now().plusMinutes(10));
//        verification.setCreatedAt(LocalDateTime.now());
//        otpRepository.save(verification);
//
//        // Send Login OTP Mail
//
//        emailService.sendMail(
//
//                user.getEmail(),
//
//                "QuickDines Login OTP",
//
//                "Hello " + user.getFirstName()
//
//                        + "\n\n"
//
//                        + "Your Login OTP is : "
//
//                        + otp
//
//                        + "\n\n"
//
//                        + "OTP is valid for 10 minutes."
//
//        );
        // Get User-Agent

        String userAgent = httpRequest.getHeader("User-Agent");

// Browser Name

        String browserName = "Unknown Browser";

        if (userAgent != null) {

            if (userAgent.contains("Chrome")) {
                browserName = "Chrome";
            } else if (userAgent.contains("Firefox")) {
                browserName = "Firefox";
            } else if (userAgent.contains("Safari")) {
                browserName = "Safari";
            } else if (userAgent.contains("Edge")) {
                browserName = "Microsoft Edge";
            }

        }


// Operating System
        String operatingSystem = "Unknown OS";
        if (userAgent != null) {
            if (userAgent.contains("Windows")) {
                operatingSystem = "Windows";
            } else if (userAgent.contains("Mac")) {
                operatingSystem = "Mac OS";
            } else if (userAgent.contains("Linux")) {
                operatingSystem = "Linux";
            } else if (userAgent.contains("Android")) {
                operatingSystem = "Android";
            } else if (userAgent.contains("iPhone")) {
                operatingSystem = "iOS";
            }
        }


// IP Address
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();}


// Device Name
        String deviceName = "Unknown Device";
        if (operatingSystem.equals("Windows")
                || operatingSystem.equals("Mac OS")
                || operatingSystem.equals("Linux")) {

            deviceName = "Desktop";
        } else if (operatingSystem.equals("Android")
                || operatingSystem.equals("iOS")) {

            deviceName = "Mobile";

        }
        // Save Device Details
        UserDevice device = new UserDevice();
        device.setUsers(user);
        device.setDeviceName(deviceName);
        device.setBrowserName(browserName);
        device.setOperatingSystem(operatingSystem);
        device.setIpAddress(ipAddress);
        device.setDeviceStatus("ACTIVE");
        device.setLastLogin(LocalDateTime.now());
        device.setCreatedAt(LocalDateTime.now());

        deviceRepository.save(device);
        // Save Login History
        LoginHistory history = new LoginHistory();

        history.setUsers(user);
        history.setLoginDate(LocalDate.now());
        history.setLoginTime(LocalTime.now());
        history.setIpAddress(ipAddress);
        history.setBrowserName(browserName);
        history.setOperatingSystem(operatingSystem);
        history.setLoginStatus("SUCCESS");
        history.setRemarks("Login Successful");
        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        // Send Device Login Alert Mail
        emailService.sendMail(

                user.getEmail(),

                "New Device Login Alert",

                "A new login was detected in your account."

                        + "\n\n"

                        + "Date : "
                        + LocalDate.now()

                        + "\n"

                        + "Time : "
                        + LocalTime.now()

        );
        // Return Response
        return LoginResponse.builder()
                .token(token)
                .message("Login Successful")
                .build();}



    //------------------------------------
    // LOGOUT
    //------------------------------------

    public String logout(HttpServletRequest request) {
        // Get JWT token from header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Token.");}

        String jwtToken = authHeader.substring(7);
        // Extract employee ID or email
        String employeeId = jwtService.extractUsername(jwtToken);
        // Get User
        Users user = userRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Update Login History
        LoginHistory history = (LoginHistory) historyRepository.findTopByUsersOrderByIdDesc(user).orElse(null);
        if (history != null) {
            history.setLogoutTime(LocalTime.now());
            historyRepository.save(history);}
        // Update Device Status
        UserDevice device = (UserDevice) deviceRepository.findTopByUsersOrderByIdDesc(user).orElse(null);

        if (device != null) {
            device.setDeviceStatus("LOGGED OUT");
            deviceRepository.save(device);}
        // Revoke Refresh Token
        RefreshToken refreshToken = (RefreshToken) tokenRepository.findByUsers(user).orElse(null);

        if (refreshToken != null) {
            refreshToken.setRevoked(true);
            tokenRepository.save(refreshToken);
        }
//        // Send Logout Mail
//        emailService.sendMail(
//
//                user.getEmail(),
//
//                "Logout Successful",
//
//                "You have successfully logged out of QuickDines."
//
//        );
        return "Logout Successful.";
    }



    //------------------------------------
    // PASSWORD APIs
    //------------------------------------

    public String forgotPassword(String email) {
        // Check User Exists
        Users user = (Users) userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Generate OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        // Save OTP Details
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setUsers(user);
        otpVerification.setEmail(user.getEmail());
        otpVerification.setMobileNumber(user.getMobileNumber());
        otpVerification.setOtp(otp);
        otpVerification.setOtpType("PASSWORD RESET OTP");
        otpVerification.setVerificationStatus("PENDING");
        otpVerification.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        otpVerification.setCreatedAt(LocalDateTime.now());
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

                        + "Do not share this OTP with anyone.");

        return "Forgot Password OTP sent successfully.";}


    public String resetPassword(String email, String otp, String newPassword) {
        // Check User Exists
        Users user = (Users) userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Check OTP Exists
        OtpVerification otpVerification = otpRepository.findByEmailAndOtp(email, otp).orElseThrow(() -> new RuntimeException("Invalid OTP"));
        // Check OTP Expiry
        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP Expired");}
        // Encrypt Password
        String encodedPassword = passwordEncoder.encode(newPassword);
        // Update Password
        user.setPassword(encodedPassword);
        userRepository.save(user);
        // Update OTP Status
        otpVerification.setVerificationStatus("VERIFIED");
        otpRepository.save(otpVerification);
        // Send Password Reset Mail
        emailService.sendMail(

                user.getEmail(),

                "Password Reset Successful",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your password has been reset successfully."

                        + "\n\n"

                        + "If this was not you, please contact support immediately.");

        return "Password Updated Successfully.";}


    public String changePassword(ChangePasswordRequest request) {
        // Check User Exists
        Users user = (Users) userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Check Old Password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException(
                    "Old Password is Incorrect");}
        // Check New Password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException(
                    "Passwords do not match");}
        // Prevent Same Password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException(
                    "New password cannot be the same as the old password");}
        // Encrypt Password
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        // Update Database
        userRepository.save(user);
        // Send Password Changed Email
        emailService.sendMail(

                user.getEmail(),

                "Password Changed Successfully",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your QuickDines account password has been changed successfully."

                        + "\n\n"

                        + "If you did not perform this action, please contact the administrator immediately."

        );
        return "Password Changed Successfully.";}



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
                        + "Role : " + request.getRole());
        return "User Created Successfully.";}

    public String blockUser(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setAccountLocked(true);
        user.setActive(false);
        userRepository.save(user);
        emailService.sendMail(

                user.getEmail(),

                "Account Blocked",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been blocked by the administrator.");
        return "User Blocked Successfully.";}


    public String unblockUser(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setAccountLocked(false);
        user.setActive(true);
        userRepository.save(user);
        emailService.sendMail(
                user.getEmail(),

                "Account Unblocked",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been unblocked successfully.");
        return "User Unblocked Successfully.";}


    public String deleteUser(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setActive(false);
        userRepository.save(user);
        emailService.sendMail(
                user.getEmail(),

                "Account Deleted",

                "Hello " + user.getFirstName()
                        + "\n\n"
                        + "Your QuickDines account has been deleted by the administrator.");
        return "User Deleted Successfully.";}



    //------------------------------------
    // ROLES
    //------------------------------------

    public String createRole(Role role) {

        role.setCreatedAt(LocalDateTime.now());

        roleRepository.save(role);

        return "Role Created Successfully.";

    }


    public List<Role> getRoles(){

        return roleRepository.findAll();

    }


    public Role getRole(Long id){

        return roleRepository.findById(Math.toIntExact(id)).orElseThrow();

    }


    public String updateRole(Long id, Role request) {
        Role role = roleRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Role Not Found"));
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
        return "Role Updated Successfully.";
    }


    public String deleteRole(Long id) {

        Role role = roleRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Role Not Found"));
        roleRepository.delete(role);
        return "Role Deleted Successfully.";

    }



    //------------------------------------
    // USER ROLES
    //------------------------------------

    public String assignRole(String userId, Long roleId) {
        // Get User
        Users user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User Not Found"));
        // Get Role
        Role role = roleRepository.findById(Math.toIntExact(roleId)).orElseThrow(() -> new RuntimeException("Role Not Found"));
        // Create User Role
        UserRole userRole = new UserRole();
        userRole.setUsers(user);
        userRole.setRole(role);
        userRole.setAssignedBy("SUPER_ADMIN");
        userRole.setAssignedDate(LocalDateTime.now());
        userRole.setStatus("ACTIVE");
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleRepository.save(userRole);
        // Optional (if you store role in Users table)
        user.setRole(role.getRoleName());
        userRepository.save(user);
        // Send Mail
        emailService.sendMail(
                user.getEmail(),

                "Role Assigned Successfully",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your role has been assigned successfully."

                        + "\n\n"

                        + "Assigned Role : "
                        + role.getRoleName());
        return "Role Assigned Successfully.";

    }


    public String removeRole(String userId) {

        Users user = (Users) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        UserRole userRole = (UserRole) userRoleRepository.findByUsers(user).orElseThrow(() -> new RuntimeException("Role Not Assigned"));
        // Soft Delete
        userRole.setStatus("REMOVED");
        userRoleRepository.save(userRole);
        // Optional
        user.setRole(null);
        userRepository.save(user);
        // Send Mail
        emailService.sendMail(

                user.getEmail(),

                "Role Removed Successfully",

                "Hello " + user.getFirstName()

                        + "\n\n"

                        + "Your role has been removed successfully.");
        return "Role Removed Successfully.";
    }

// CREATE

    public Permission createPermission(
            Permission permission){

        return permissionRepository.save(permission);

    }


// GET ALL

    public List<Permission> getPermissions(){

        return permissionRepository.findAll();

    }


// GET BY ID

    public Permission getPermission(Long id){

        return permissionRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Permission Not Found"));

    }


// UPDATE

    public Permission updatePermission(Long id, Permission request){
        Permission permission = permissionRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Permission Not Found"));

        permission.setPermissionName(request.getPermissionName());
        permission.setModuleName(request.getModuleName());
        permission.setDescription(request.getDescription());
        return permissionRepository.save(permission);}


// DELETE

    public String deletePermission(Long id){
        permissionRepository.deleteById(Math.toIntExact(id));
        return "Permission Deleted Successfully";
    }


// MODULE WISE

    public List<Permission> getModulePermissions(String moduleName){

        return permissionRepository.findByModuleName(moduleName);

    }



}


