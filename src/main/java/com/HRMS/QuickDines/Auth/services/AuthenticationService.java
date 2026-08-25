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
import com.HRMS.QuickDines.Employee.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.model.EmployeeApproval;
import com.HRMS.QuickDines.Employee.repo.EmployeeApprovalRepository;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.repo.DepartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
import java.util.Date;
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

    //private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    // private final UserRoleRepository userRoleRepository;
    private final OtpVerificationRepository otpRepository;
    private final RefreshTokenRepository tokenRepository;
    private final LoginHistoryRepository historyRepository;
    private final UserDeviceRepository deviceRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper;
    private final EmployeeApprovalRepository employeeApprovalRepository;

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


    // =========================================================
    // CLIENT INFORMATION
    // =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }


    // Email Service

    private final EmailService emailService;

    @Transactional
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository
                .findByEmail(request.getEmailId());


        // =====================================================
        // 2. CHECK EMPLOYEE STATUS
        // =====================================================

        if (employee.getStatus() == null ||
                !employee.getStatus().equalsIgnoreCase("ACTIVE")) {

            throw new RuntimeException(
                    "Employee account is not active");
        }


        // =====================================================
        // 3. FIND EMPLOYEE APPROVAL
        // =====================================================

        EmployeeApproval approval =
                employeeApprovalRepository
                        .findByEmployee_EmployeeId(
                                employee.getEmployeeId());


        // =====================================================
        // 4. CHECK APPROVAL
        // =====================================================

        if (approval == null ||
                approval.getFinalStatus() != ApprovalStatus.APPROVED) {

            throw new RuntimeException(
                    "Your employee profile is not approved yet");
        }


        // =====================================================
        // 5. CHECK PASSWORD
        // =====================================================

        boolean passwordValid =
                passwordEncoder.matches(
                        request.getPassword(),
                        employee.getPassword());

        if (!passwordValid) {

            throw new RuntimeException(
                    "Invalid employee ID or password");
        }


        // =====================================================
        // 6. LOGIN HISTORY
        // =====================================================

        LoginHistory history = new LoginHistory();

        history.setEmployee(employee);
        history.setLoginDate(LocalDate.now());
        history.setLoginTime(LocalTime.now());
        history.setIpAddress(getIpAddress());
        history.setBrowserName(getBrowser());
        history.setOperatingSystem(getOperatingSystem());
        history.setLoginStatus(LoginStatus.SUCCESS);
        history.setRemarks("Login successful");

        historyRepository.save(history);


        // =====================================================
        // 7. DEVICE
        // =====================================================

        String deviceId =
                httpRequest.getHeader("X-Device-Id");

        if (deviceId == null || deviceId.isBlank()) {
            deviceId = "UNKNOWN";
        }

        UserDevice device =
                deviceRepository
                        .findByEmployeeAndDeviceId(
                                employee,
                                deviceId)
                        .orElseGet(UserDevice::new);

        device.setEmployee(employee);
        device.setDeviceId(deviceId);
        device.setDeviceName(
                httpRequest.getHeader("X-Device-Name"));
        device.setBrowserName(getBrowser());
        device.setOperatingSystem(getOperatingSystem());
        device.setIpAddress(getIpAddress());
        device.setLastLogin(LocalDateTime.now());
        device.setDeviceStatus("ACTIVE");

        deviceRepository.save(device);


        // =====================================================
        // 8. GENERATE JWT
        // =====================================================

        String token =
                jwtService.generateToken(
                        employee.getEmployeeId());

        String refreshToken =
                jwtService.generateRefreshToken(
                        employee.getEmployeeId());


        // =====================================================
        // 9. RESPONSE
        // =====================================================

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .employeeId(employee.getEmployeeId())
                .email(employee.getEmail())
                .role(
                        employee.getRole() != null
                                ? employee.getRole().getRoleName()
                                : null
                )
                .build();
    }

    @Transactional
    public String logout(HttpServletRequest httpRequest) {

        // =====================================================
        // 1. GET SESSION
        // =====================================================

        HttpSession session = httpRequest.getSession(false);

        if (session == null) {

            return "No active login session";
        }


        // =====================================================
        // 2. GET EMPLOYEE ID
        // =====================================================

        String employeeId = (String) session.getAttribute("employeeId");

        if (employeeId == null) {

            return "No active login session";
        }


        // =====================================================
        // 3. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));


        // =====================================================
        // 4. FIND ACTIVE LOGIN HISTORY
        // =====================================================

        LoginHistory history = historyRepository.findTopByEmployeeAndLogoutTimeIsNullOrderByIdDesc(employee).orElseThrow(() -> new RuntimeException("Active login history not found"));


        // =====================================================
        // 5. SAVE LOGOUT TIME
        // =====================================================

        history.setLogoutTime(LocalTime.now());

        history.setRemarks("Logout successful");

        historyRepository.save(history);


        // =====================================================
        // 6. UPDATE DEVICE STATUS
        // =====================================================

        String deviceId = httpRequest.getHeader("X-Device-Id");

        if (deviceId != null && !deviceId.isBlank()) {

            deviceRepository.findByEmployeeAndDeviceId(employee, deviceId).ifPresent(device -> {

                device.setDeviceStatus("INACTIVE");

                deviceRepository.save(device);
            });
        }


        // =====================================================
        // 7. INVALIDATE SESSION
        // =====================================================

        session.invalidate();


        return "Logout Successful";
    }


    @Transactional
    public String forgotPassword(String email) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {

            throw new RuntimeException("Employee with this email does not exist");
        }


        // =====================================================
        // 2. GENERATE OTP
        // =====================================================

        String otp = String.format("%06d", new Random().nextInt(1000000));


        // =====================================================
        // 3. SAVE OTP
        // =====================================================

        OtpVerification otpVerification = new OtpVerification();

        otpVerification.setEmployee(employee);

        otpVerification.setEmail(employee.getEmail());

        otpVerification.setOtp(otp);

        otpVerification.setOtpType("PASSWORD_RESET");

        otpVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpVerification.setVerificationStatus("PENDING");

        otpRepository.save(otpVerification);


        String subject = "QuickDines HRMS - Password Reset OTP";

        String body = """
                Dear %s,
                
                We received a request to reset your password for your
                QuickDines HRMS account.
                
                Your One-Time Password (OTP) is:
                
                %s
                
                This OTP is valid for 5 minutes.
                
                For your security:
                • Do not share this OTP with anyone.
                • QuickDines HRMS will never ask you to share your OTP.
                • If you did not request a password reset, please ignore this email
                  or contact your HR administrator.
                
                Regards,
                
                QuickDines HRMS
                Human Resources Team
                
                This is an automated email. Please do not reply to this email.
                """.formatted(employee.getFirstName(), otp);


        emailService.sendMail(employee.getEmail(), subject, body);


        return "Password reset OTP sent successfully";
    }


    @Transactional
    public String resetPassword(ResetPasswordRequest request) {

        // =====================================================
        // 1. FIND OTP
        // =====================================================

        OtpVerification otpVerification = otpRepository.findTopByEmailAndOtpTypeAndVerificationStatusOrderByCreatedAtDesc(request.getEmail(), "PASSWORD_RESET", "PENDING").orElseThrow(() -> new RuntimeException("OTP not found or already used"));


        // =====================================================
        // 2. CHECK OTP
        // =====================================================

        if (!otpVerification.getOtp().equals(request.getOtp())) {

            throw new RuntimeException("Invalid OTP");
        }


        // =====================================================
        // 3. CHECK OTP EXPIRY
        // =====================================================

        if (LocalDateTime.now().isAfter(otpVerification.getExpiryTime())) {

            throw new RuntimeException("OTP has expired");
        }


        // =====================================================
        // 4. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmail(request.getEmail());

        if (employee == null) {

            throw new RuntimeException("Employee not found");
        }


        // =====================================================
        // 5. ENCODE NEW PASSWORD
        // =====================================================

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());


        employee.setPassword(encodedPassword);

        employeeRepository.save(employee);


        // =====================================================
        // 6. MARK OTP AS USED
        // =====================================================

        otpVerification.setVerificationStatus("VERIFIED");

        otpRepository.save(otpVerification);


        // =====================================================
        // 7. SEND CONFIRMATION EMAIL
        // =====================================================

        String subject = "QuickDines HRMS - Password Changed Successfully";

        String body = """
                Dear %s,
                
                Your QuickDines HRMS account password has been changed
                successfully.
                
                If you made this change, no further action is required.
                
                If you did not change your password, please contact your
                HR administrator immediately to secure your account.
                
                For security reasons, please do not share your password
                or any verification code with anyone.
                
                Regards,
                
                QuickDines HRMS
                Human Resources Team
                
                This is an automated email. Please do not reply to this email.
                """.formatted(employee.getFirstName());

        emailService.sendMail(employee.getEmail(), subject, body);


        return "Password reset successfully";
    }


    @Transactional
    public String changePassword(ChangePasswordRequest request) {

        // =====================================================
        // 1. GET LOGGED-IN EMPLOYEE
        // =====================================================

        String employeeId = getLoggedInEmployeeId();


        // =====================================================
        // 2. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));


        // =====================================================
        // 3. VERIFY CURRENT PASSWORD
        // =====================================================

        boolean valid = passwordEncoder.matches(request.getOldPassword(), employee.getPassword());

        if (!valid) {

            throw new RuntimeException("Current password is incorrect");
        }


        // =====================================================
        // 4. ENCODE NEW PASSWORD
        // =====================================================

        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));


        // =====================================================
        // 5. SAVE
        // =====================================================

        employeeRepository.save(employee);


        // =====================================================
        // 6. SEND EMAIL
        // =====================================================

        emailService.sendMail(employee.getEmail(), "QuickDines HRMS - Password Changed Successfully", """
                Dear %s,
                
                Your QuickDines HRMS account password has been changed
                successfully.
                
                If you made this change, no further action is required.
                
                If you did not perform this action, please contact your
                HR administrator immediately to secure your account.
                
                For your security, please do not share your password
                or verification codes with anyone.
                
                Regards,
                
                QuickDines HRMS
                Human Resources Team
                
                This is an automated email. Please do not reply to this email.
                """.formatted(employee.getFirstName()));


        return "Password changed successfully";
    }


    // =========================================================
    // OTP
    // =========================================================

    @Transactional
    public String sendOTP(String email) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {

            throw new RuntimeException("Employee with this email does not exist");
        }


        // =====================================================
        // 2. GENERATE OTP
        // =====================================================

        String otp = String.format("%06d", new Random().nextInt(1000000));


        // =====================================================
        // 3. SAVE OTP
        // =====================================================

        OtpVerification verification = new OtpVerification();

        verification.setEmployee(employee);

        verification.setEmail(employee.getEmail());

        verification.setOtp(otp);

        verification.setOtpType("GENERAL");

        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        verification.setVerificationStatus("PENDING");

        otpRepository.save(verification);


        // =====================================================
        // 4. SEND EMAIL
        // =====================================================

        emailService.sendMail(employee.getEmail(), "QuickDines HRMS - OTP Verification", """
                Dear %s,
                
                Your One-Time Password (OTP) for QuickDines HRMS
                verification is:
                
                %s
                
                This OTP is valid for 5 minutes.
                
                For your security:
                - Do not share this OTP with anyone.
                - QuickDines HRMS will never ask you to share your OTP.
                - If you did not request this OTP, please contact your
                  HR administrator immediately.
                
                Regards,
                
                QuickDines HRMS
                Human Resources Team
                
                This is an automated email. Please do not reply to this email.
                """.formatted(employee.getFirstName(), otp));

        return "OTP sent successfully";
    }


    @Transactional
    public String verifyOTP(String email, String otp) {

        // =====================================================
        // 1. FIND LATEST OTP
        // =====================================================

        OtpVerification verification = otpRepository.findTopByEmailAndOtpTypeAndVerificationStatusOrderByCreatedAtDesc(email, "GENERAL", "PENDING").orElseThrow(() -> new RuntimeException("OTP not found or already verified"));


        // =====================================================
        // 2. CHECK OTP
        // =====================================================

        if (!verification.getOtp().equals(otp)) {

            verification.setVerificationStatus("FAILED");

            otpRepository.save(verification);

            throw new RuntimeException("Invalid OTP");
        }


        // =====================================================
        // 3. CHECK EXPIRY
        // =====================================================

        if (LocalDateTime.now().isAfter(verification.getExpiryTime())) {

            verification.setVerificationStatus("EXPIRED");

            otpRepository.save(verification);

            throw new RuntimeException("OTP has expired");
        }


        // =====================================================
        // 4. MARK VERIFIED
        // =====================================================

        verification.setVerificationStatus("VERIFIED");

        otpRepository.save(verification);


        return "OTP verified successfully";
    }

    @Transactional
    public String resendOTP(String email) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {

            throw new RuntimeException("Employee with this email does not exist");
        }


        // =====================================================
        // 2. INVALIDATE OLD OTP
        // =====================================================

        List<OtpVerification> oldOtps = otpRepository.findByEmailAndOtpTypeAndVerificationStatus(email, "GENERAL", "PENDING");

        for (OtpVerification oldOtp : oldOtps) {

            oldOtp.setVerificationStatus("REPLACED");
        }

        otpRepository.saveAll(oldOtps);


        // =====================================================
        // 3. GENERATE NEW OTP
        // =====================================================

        String otp = String.format("%06d", new Random().nextInt(1000000));


        // =====================================================
        // 4. SAVE NEW OTP
        // =====================================================

        OtpVerification verification = new OtpVerification();

        verification.setEmployee(employee);

        verification.setEmail(employee.getEmail());

        verification.setOtp(otp);

        verification.setOtpType("GENERAL");

        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        verification.setVerificationStatus("PENDING");

        otpRepository.save(verification);


        // =====================================================
        // 5. SEND NEW OTP
        // =====================================================

        emailService.sendMail(employee.getEmail(), "QuickDines HRMS - New OTP Verification", """
                Dear %s,
                
                Your new One-Time Password (OTP) for QuickDines HRMS
                verification is:
                
                %s
                
                Your previous OTP is no longer valid.
                
                This OTP is valid for 5 minutes.
                
                For your security:
                - Do not share this OTP with anyone.
                - QuickDines HRMS will never ask you to share your OTP.
                - If you did not request this OTP, please contact your
                  HR administrator immediately.
                
                Regards,
                
                QuickDines HRMS
                Human Resources Team
                
                This is an automated email. Please do not reply to this email.
                """.formatted(employee.getFirstName(), otp));


        return "OTP resent successfully";
    }


    // =========================================================
    // ROLE
    // =========================================================

    public Role createRole(Role request) {

        Role role = new Role();

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        return roleRepository.save(role);
    }


    public List<Role> getRoles() {

        return roleRepository.findAll();
    }


    public Role getRole(Long id) {

        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }


    public Role updateRole(Long id, Role request) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        return roleRepository.save(role);
    }


    public String deleteRole(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        roleRepository.delete(role);

        return "Role deleted successfully";
    }


    // =========================================================
    // ROLE - PERMISSIONS
    // =========================================================

    public List<Permission> getRolePermissions(Long roleId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        return role.getPermissions();
    }


    public Role assignPermission(Long roleId, Long permissionId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));

        if (!role.getPermissions().contains(permission)) {
            role.getPermissions().add(permission);
        }

        return roleRepository.save(role);
    }


    public Role removePermission(Long roleId, Long permissionId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().remove(permission);

        return roleRepository.save(role);
    }


    // =========================================================
    // PERMISSIONS
    // =========================================================

    public Permission createPermission(Permission request) {

        Permission permission = new Permission();

        permission.setPermissionName(request.getPermissionName());

        permission.setModuleName(request.getModuleName());

        permission.setDescription(request.getDescription());

        return permissionRepository.save(permission);
    }


    public List<Permission> getPermissions() {

        return permissionRepository.findAll();
    }


    public Permission getPermission(Long id) {

        return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }


    public Permission updatePermission(Long id, Permission request) {

        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));

        permission.setPermissionName(request.getPermissionName());

        permission.setModuleName(request.getModuleName());

        permission.setDescription(request.getDescription());

        return permissionRepository.save(permission);
    }


    public String deletePermission(Long id) {

        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));

        permissionRepository.delete(permission);

        return "Permission deleted successfully";
    }


    public List<Permission> getModulePermissions(String moduleName) {

        return permissionRepository.findByModuleName(moduleName);
    }


    // =========================================================
    // LOGIN HISTORY
    // =========================================================

    public List<LoginHistory> getLoginHistory() {

        return historyRepository.findAll();
    }


    public List<LoginHistory> getEmployeeLoginHistory(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        return historyRepository.findByEmployee(employee.getEmployeeId());
    }


    public List<LoginHistory> getSuccessfulLogins(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        return historyRepository.findByEmployee_EmployeeIdAndLoginStatus(employee.getEmployeeId(), LoginStatus.SUCCESS);
    }


    public List<LoginHistory> getFailedLogins(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        return historyRepository.findByEmployee_EmployeeIdAndLoginStatus(employee.getEmployeeId(), LoginStatus.FAILED);
    }


    public List<LoginHistory> getLoginHistoryByDateRange(String fromDate, String toDate) {

        LocalDate start = LocalDate.parse(fromDate);

        LocalDate end = LocalDate.parse(toDate);

        return historyRepository.findByLoginDateBetween(start, end);
    }


    // =========================================================
    // DEVICES
    // =========================================================

    public List<UserDevice> getEmployeeDevices(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

        return deviceRepository.findByEmployee(employee.getEmployeeId());
    }


    public String logoutDevice(Long id) {

        UserDevice device = (UserDevice) deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));

        device.setDeviceStatus("LOGGED_OUT");

        deviceRepository.save(device);

        return "Device logged out successfully";
    }


    public String blockDevice(Long id) {

        UserDevice device = (UserDevice) deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));

        device.setDeviceStatus("BLOCKED");

        deviceRepository.save(device);

        return "Device blocked successfully";
    }


    public String removeDevice(Long id) {

        UserDevice device = (UserDevice) deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));

        deviceRepository.delete(device);

        return "Device removed successfully";
    }

}


