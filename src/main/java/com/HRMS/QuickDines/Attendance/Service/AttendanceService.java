package com.HRMS.QuickDines.Attendance.Service;

import com.HRMS.QuickDines.Attendance.DTO.AttendanceDashboardDTO;
import com.HRMS.QuickDines.Attendance.DTO.CheckInRequest;
import com.HRMS.QuickDines.Attendance.DTO.CheckInResponse;
import com.HRMS.QuickDines.Attendance.Entity.AttendanceStatus;
import com.HRMS.QuickDines.Attendance.Entity.OvertimeStatus;
import com.HRMS.QuickDines.Attendance.model.*;
import com.HRMS.QuickDines.Attendance.repo.*;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceReportsRepository attendanceReportsRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final GpsTrackingRepository gpsTrackingRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final HolidayRepository holidayRepository;
    private final WeekendConfigurationRepository weekendConfigurationRepository;
    private final AttendanceRegularizationRepository attendanceRegularizationRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;
    private final ObjectMapper objectMapper;

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }

//---------------------------------
// EMPLOYEE ATTENDANCE
//---------------------------------

    @Transactional
    public CheckInResponse checkIn(String employeeId, CheckInRequest request) {

        // =====================================================
        // 1. FIND EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));


        // =====================================================
        // 2. CURRENT DATE/TIME
        // =====================================================

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);


        // =====================================================
        // 3. CHECK TODAY'S ATTENDANCE
        // =====================================================

        Optional<Attendance> attendanceExists = attendanceRepository.findByEmployee_EmployeeIdAndCreatedAtBetween(employeeId, start, end);

        if (attendanceExists.isPresent()) {

            throw new RuntimeException("Attendance Already Marked Today");
        }


        // =====================================================
        // 4. VALIDATE GPS
        // =====================================================

        if (request.getLatitude() == null || request.getLatitude().isBlank()) {

            throw new RuntimeException("Latitude is required");
        }

        if (request.getLongitude() == null || request.getLongitude().isBlank()) {

            throw new RuntimeException("Longitude is required");
        }


        // =====================================================
        // 5. FIND CURRENT SHIFT
        // =====================================================

        EmployeeShift employeeShift = employeeShiftRepository.findByEmployee_EmployeeIdAndIsCurrentTrue(employeeId).orElseThrow(() -> new RuntimeException("No current shift assigned to employee"));


        Shift shift = employeeShift.getShift();

        if (shift == null) {

            throw new RuntimeException("Shift is not assigned");
        }


        // =====================================================
        // 6. CALCULATE LATE STATUS
        // =====================================================

        LocalDateTime shiftStart = today.atTime(shift.getStartTime());

        int graceMinutes = shift.getGraceTime() != null ? shift.getGraceTime() : 0;

        LocalDateTime allowedTime = shiftStart.plusMinutes(graceMinutes);

        boolean late = now.isAfter(allowedTime);


        // =====================================================
        // 7. CREATE ATTENDANCE
        // =====================================================

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);

        // Company
        attendance.setCompany(employee.getCompany());

        // Branch
        attendance.setBranch(employee.getBranch());

        // Department
        attendance.setDepartment(employee.getDepartment());

        // Shift
        attendance.setShift(shift);

        // Login
        attendance.setLoginTime(now);

        // Status
        attendance.setAttendanceStatus(AttendanceStatus.PRESENT);

        // Late
        attendance.setLate(late);

        attendance.setEarlyLeaving(false);

        attendance.setRemarks(late ? "Checked In - Late" : "Checked In On Time");


        Attendance savedAttendance = attendanceRepository.save(attendance);


        // =====================================================
        // 8. CREATE GPS TRACKING
        // =====================================================

        GpsTracking gpsTracking = new GpsTracking();

        gpsTracking.setEmployee(employee);

        gpsTracking.setLatitude(request.getLatitude());

        gpsTracking.setLongitude(request.getLongitude());

        gpsTracking.setLoginLocation(request.getLatitude() + "," + request.getLongitude());

        gpsTracking.setTrackingStatus("CHECKED_IN");

        gpsTracking.setDeviceName(request.getDeviceName());


        GpsTracking savedGps = gpsTrackingRepository.save(gpsTracking);


        // =====================================================
        // 9. RETURN COMPLETE JSON
        // =====================================================

        return CheckInResponse.builder()

                .message(late ? "Check-in successful - Late" : "Check-in successful")

                .employeeId(employee.getEmployeeId())

                .employeeName(employee.getFirstName() + " " + employee.getLastName())

                .attendanceId(savedAttendance.getId())

                .attendanceStatus(savedAttendance.getAttendanceStatus().name())

                .loginTime(savedAttendance.getLoginTime())

                .late(savedAttendance.getLate())

                .shiftName(shift.getShiftName())

                .shiftCode(shift.getShiftCode())

                .shiftStartTime(shift.getStartTime().toString())

                .shiftEndTime(shift.getEndTime().toString())

                .gpsTrackingId(savedGps.getId())

                .latitude(savedGps.getLatitude())

                .longitude(savedGps.getLongitude())

                .trackingStatus(savedGps.getTrackingStatus())

                .deviceName(savedGps.getDeviceName())

                .build();
    }


    public String checkOut(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        Attendance attendance = attendanceRepository.findTopByEmployeeIdOrderByIdDesc(employeeId).orElseThrow(() -> new RuntimeException("Attendance Record Not Found"));
        com.HRMS.QuickDines.AuditLogs.model.ClientInfoDTO clientInfo = clientInfoService.getClientInfo();
        if (attendance.getLogoutTime() != null) {
            auditLogsService.logActivity(employee.getEmployeeId(), "CHECK_OUT", "ATTENDANCE", "Employee attempted to check out, but was already checked out", ActivityStatus.FAILED, clientInfo.getIpAddress(), clientInfo.getBrowser(), clientInfo.getOperatingSystem());
            // System log
            auditLogsService.logWarning("ATTENDANCE", "AttendanceService", "Check-out failed: Employee already checked out. " + "Employee ID: " + employeeId);
            return "Already Checked Out Today";
        }
// =====================================================
// OLD ATTENDANCE DATA
// =====================================================

        String oldValue;

        try {

            oldValue = objectMapper.writeValueAsString(Map.of("id", attendance.getId(), "employeeId", attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null,

                    "loginTime", attendance.getLoginTime(),

                    "logoutTime", attendance.getLogoutTime(),

                    "attendanceStatus", attendance.getAttendanceStatus(),

                    "totalHours", attendance.getTotalHours(),

                    "remarks", attendance.getRemarks()));

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to create old attendance JSON", e);
        }

// =====================================================
// NEW ATTENDANCE DATA
// =====================================================


        attendance.setLogoutTime(LocalDateTime.now());
        double totalHours = Duration.between(attendance.getLoginTime(), attendance.getLogoutTime()).toMinutes() / 60.0;

        attendance.setTotalHours(BigDecimal.valueOf(totalHours));
        attendance.setRemarks("Checked Out");
        attendanceRepository.save(attendance);
        String newValue;

        try {

            newValue = objectMapper.writeValueAsString(Map.of("id", attendance.getId(), "employeeId", attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null,

                    "loginTime", attendance.getLoginTime(),

                    "logoutTime", attendance.getLogoutTime(),

                    "attendanceStatus", attendance.getAttendanceStatus(),

                    "totalHours", attendance.getTotalHours(),

                    "remarks", attendance.getRemarks()));

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to create new attendance JSON", e);
        }

        // =====================================================
        // AUDIT LOG
        // =====================================================
        auditLogsService.logUpdate("ATTENDANCE", String.valueOf(attendance.getId()), employee.getEmployeeId(), employee.getEmployeeId(), "Employee checked out successfully", oldValue, newValue);
        // =====================================================
        // ACTIVITY LOG
        // =====================================================
        auditLogsService.logActivity(employee.getEmployeeId(), "CHECK_OUT", "ATTENDANCE", "Employee checked out successfully. " + "Total hours: " + totalHours, ActivityStatus.SUCCESS, clientInfo.getIpAddress(), clientInfo.getBrowser(), clientInfo.getOperatingSystem());
        // =====================================================
        // SYSTEM LOG
        // =====================================================
        auditLogsService.logInfo("ATTENDANCE", "AttendanceService", "Employee check-out completed successfully." + " Employee ID: " + employeeId + ", Total hours: " + totalHours);

        return "Check Out Successful";
    }


    public List<Attendance> getAttendance(String employeeId) {
        com.HRMS.QuickDines.AuditLogs.model.ClientInfoDTO clientInfo = clientInfoService.getClientInfo();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        List<Attendance> attendanceList = attendanceRepository.findByEmployeeId(employeeId);

        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(employee.getEmployeeId(), "VIEW_ATTENDANCE", "ATTENDANCE", "Employee attendance records viewed", ActivityStatus.SUCCESS, clientInfo.getIpAddress(), clientInfo.getBrowser(), clientInfo.getOperatingSystem());

        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE", "AttendanceService", "Attendance records retrieved successfully. Employee ID: " + employeeId);

        return attendanceList;
    }


//---------------------------------
// HR / ADMIN / SUPER ADMIN
//---------------------------------
// =========================================================
// GET ALL ATTENDANCE
// =========================================================

    public List<Attendance> getAllAttendance() {
        String performedBy = getLoggedInEmployeeId();
        List<Attendance> attendanceList = attendanceRepository.findAll();

        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "VIEW_ALL_ATTENDANCE", "ATTENDANCE", "All attendance records viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE", "AttendanceService", "All attendance records retrieved successfully");

        return attendanceList;
    }


// =========================================================
// UPDATE ATTENDANCE
// =========================================================

    public String updateAttendance(Long attendanceId, Attendance attendance) {

        Attendance existingAttendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance Not Found"));

        existingAttendance.setLoginTime(attendance.getLoginTime());

        existingAttendance.setLogoutTime(attendance.getLogoutTime());

        existingAttendance.setAttendanceStatus(attendance.getAttendanceStatus());

        existingAttendance.setRemarks(attendance.getRemarks());

        // Calculate Total Working Hours

        if (attendance.getLoginTime() != null && attendance.getLogoutTime() != null) {

            double totalHours = Duration.between(attendance.getLoginTime(), attendance.getLogoutTime()).toMinutes() / 60.0;

            existingAttendance.setTotalHours(BigDecimal.valueOf(totalHours));
        }

        attendanceRepository.save(existingAttendance);


        // =====================================================
        // AUDIT LOG
        // =====================================================
// =====================================================
// OLD ATTENDANCE DATA
// =====================================================

        String oldValue;

        try {

            oldValue = objectMapper.writeValueAsString(Map.of("id", existingAttendance.getId(), "employeeId", existingAttendance.getEmployee() != null ? existingAttendance.getEmployee().getEmployeeId() : null,

                    "loginTime", existingAttendance.getLoginTime(),

                    "logoutTime", existingAttendance.getLogoutTime(),

                    "attendanceStatus", existingAttendance.getAttendanceStatus(),

                    "totalHours", existingAttendance.getTotalHours(),

                    "remarks", existingAttendance.getRemarks()));

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to create old attendance JSON", e);
        }
// =====================================================
// NEW ATTENDANCE DATA
// =====================================================

        String newValue;

        try {

            newValue = objectMapper.writeValueAsString(Map.of("id", existingAttendance.getId(), "employeeId", existingAttendance.getEmployee() != null ? existingAttendance.getEmployee().getEmployeeId() : null,

                    "loginTime", existingAttendance.getLoginTime(),

                    "logoutTime", existingAttendance.getLogoutTime(),

                    "attendanceStatus", existingAttendance.getAttendanceStatus(),

                    "totalHours", existingAttendance.getTotalHours(),

                    "remarks", existingAttendance.getRemarks()));

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to create new attendance JSON", e);
        }

        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logUpdate("ATTENDANCE", existingAttendance.getId().toString(), performedBy, attendance.getEmployee().getEmployeeId(), "Attendance updated successfully", oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(attendance.getEmployee().getEmployeeId(), "UPDATE_ATTENDANCE", "ATTENDANCE", "Attendance record updated successfully. Attendance ID: " + attendanceId, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE", "AttendanceService", "Attendance updated successfully. Attendance ID: " + attendanceId);

        return "Attendance Updated Successfully";
    }


// =========================================================
// DELETE ATTENDANCE
// =========================================================

    public String deleteAttendance(Long attendanceId) {

        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance Not Found"));

        attendanceRepository.delete(attendance);


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logDelete("ATTENDANCE", attendance.getId().toString(), performedBy, attendance.getEmployee().getEmployeeId(), "Attendance deleted successfully");


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(attendance.getEmployee().getEmployeeId(), "DELETE_ATTENDANCE", "ATTENDANCE", "Attendance record deleted successfully. Attendance ID: " + attendanceId, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE", "AttendanceService", "Attendance deleted successfully. Attendance ID: " + attendanceId);

        return "Attendance Deleted Successfully";
    }


//---------------------------------
// SYSTEM
//---------------------------------

//    public String markAbsentEmployees() {
//
//        // Logic:
//        // Find employees who have not checked in today.
//        // Create attendance records with ABSENT status.
//
//        return "Absent Employees Marked Successfully";
//    }
//
//
//    public String markHoliday() {
//
//        // Logic:
//        // Mark today's attendance as HOLIDAY.
//
//        return "Holiday Marked Successfully";
//    }
//
//
//    public String markWeekend() {
//
//        // Logic:
//        // Mark Saturday and Sunday as WEEKEND.
//
//        return "Weekend Marked Successfully";
//    }

    public String markLeave(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setAttendanceStatus(AttendanceStatus.valueOf("LEAVE"));
        attendance.setRemarks("Approved Leave");
        attendance.setTotalHours(BigDecimal.valueOf(0.0));

        attendanceRepository.save(attendance);

        return "Leave Marked Successfully";
    }


    //---------------------------------
// REPORTS
//---------------------------------
// =====================================================
// GET ATTENDANCE REPORT
// =====================================================

    public Object getReports(String employeeId) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Object report = attendanceReportsRepository.findByEmployeeId(employeeId);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("ATTENDANCE", null, AuditActionType.LOGIN, performedBy, employeeId, "Attendance report viewed successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_ATTENDANCE_REPORT", "ATTENDANCE", "Employee attendance report viewed successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("ATTENDANCE", "AttendanceReportsService", "Attendance report viewed successfully. Employee ID: " + employeeId);


            return report;

        } catch (Exception e) {

            // =====================================================
            // ACTIVITY FAILED LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_ATTENDANCE_REPORT", "ATTENDANCE", "Failed to retrieve attendance report. Employee ID: " + employeeId + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM ERROR LOG
            // =====================================================

            auditLogsService.logError("ATTENDANCE", "AttendanceReportsService", "Failed to retrieve attendance report. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


// =====================================================
// GET MONTHLY ATTENDANCE REPORT
// =====================================================

    public Object getMonthlyReport(String employeeId) {

        String performedBy = getLoggedInEmployeeId();

        String currentMonth = YearMonth.now().toString();

        try {

            Object report = attendanceReportsRepository.findByEmployeeIdAndMonth(employeeId, currentMonth);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("ATTENDANCE", null, AuditActionType.LOGIN, performedBy, employeeId, "Monthly attendance report viewed successfully. Month: " + currentMonth, null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_MONTHLY_ATTENDANCE_REPORT", "ATTENDANCE", "Monthly attendance report viewed successfully. Month: " + currentMonth, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("ATTENDANCE", "AttendanceReportsService", "Monthly attendance report viewed successfully. " + "Employee ID: " + employeeId + ", Month: " + currentMonth);


            return report;

        } catch (Exception e) {

            // =====================================================
            // ACTIVITY FAILED LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_MONTHLY_ATTENDANCE_REPORT", "ATTENDANCE", "Failed to retrieve monthly attendance report. " + "Employee ID: " + employeeId + ", Month: " + currentMonth + ", Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM ERROR LOG
            // =====================================================

            auditLogsService.logError("ATTENDANCE", "AttendanceReportsService", "Failed to retrieve monthly attendance report. " + "Employee ID: " + employeeId + ", Month: " + currentMonth, e.toString());

            throw e;
        }
    }


//---------------------------------
// WORKING HOURS
//---------------------------------

    public Object getWorkingHours(String employeeId) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Object workingHours = workingHoursRepository.findByEmployeeId(employeeId);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("WORKING_HOURS", null, AuditActionType.VIEW, performedBy, employeeId, "Working hours viewed successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_WORKING_HOURS", "WORKING_HOURS", "Working hours viewed successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("WORKING_HOURS", "AttendanceService", "Working hours viewed successfully. Employee ID: " + employeeId);


            return workingHours;

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "VIEW_WORKING_HOURS", "WORKING_HOURS", "Failed to retrieve working hours. Employee ID: " + employeeId + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("WORKING_HOURS", "AttendanceService", "Failed to retrieve working hours. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


    public String updateWorkingHours(String employeeId, WorkingHours workingHours) {

        String performedBy = getLoggedInEmployeeId();

        try {

            WorkingHours existingWorkingHours = (WorkingHours) workingHoursRepository.findByEmployeeId(employeeId);


            // =====================================================
            // OLD VALUE
            // =====================================================

            String oldValue;

            try {

                oldValue = objectMapper.writeValueAsString(Map.of("employeeId", employeeId, "expectedHours", existingWorkingHours.getExpectedHours(),

                        "completedHours", existingWorkingHours.getCompletedHours(),

                        "overtimeHours", existingWorkingHours.getOvertimeHours(),

                        "breakHours", existingWorkingHours.getBreakHours(),

                        "status", existingWorkingHours.getStatus()));

            } catch (JsonProcessingException e) {

                throw new RuntimeException("Unable to create old working hours JSON", e);
            }


            existingWorkingHours.setExpectedHours(workingHours.getExpectedHours());

            existingWorkingHours.setCompletedHours(workingHours.getCompletedHours());

            existingWorkingHours.setOvertimeHours(workingHours.getOvertimeHours());

            existingWorkingHours.setBreakHours(workingHours.getBreakHours());

            existingWorkingHours.setStatus(workingHours.getStatus());


            workingHoursRepository.save(existingWorkingHours);


            // =====================================================
            // NEW VALUE
            // =====================================================

            String newValue;

            try {

                newValue = objectMapper.writeValueAsString(Map.of("employeeId", employeeId, "expectedHours", existingWorkingHours.getExpectedHours(),

                        "completedHours", existingWorkingHours.getCompletedHours(),

                        "overtimeHours", existingWorkingHours.getOvertimeHours(),

                        "breakHours", existingWorkingHours.getBreakHours(),

                        "status", existingWorkingHours.getStatus()));

            } catch (JsonProcessingException e) {

                throw new RuntimeException("Unable to create new working hours JSON", e);
            }


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.logUpdate("WORKING_HOURS", String.valueOf(existingWorkingHours.getId()), performedBy, employeeId, "Working hours updated successfully", oldValue, newValue);


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "UPDATE_WORKING_HOURS", "WORKING_HOURS", "Working hours updated successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("WORKING_HOURS", "AttendanceService", "Working hours updated successfully. Employee ID: " + employeeId);


            return "Working Hours Updated Successfully";

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "UPDATE_WORKING_HOURS", "WORKING_HOURS", "Failed to update working hours. Employee ID: " + employeeId + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("WORKING_HOURS", "AttendanceService", "Failed to update working hours. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


//---------------------------------
// GPS TRACKING
//---------------------------------

    public String gpsLogin(String employeeId, GpsTracking gpsTracking) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));


            gpsTracking.setEmployee(employee);

            gpsTracking.setTrackingStatus("ACTIVE");

            gpsTrackingRepository.save(gpsTracking);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("GPS_TRACKING", String.valueOf(gpsTracking.getId()), AuditActionType.CREATE, performedBy, employeeId, "GPS login tracking started successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "GPS_LOGIN", "GPS_TRACKING", "GPS tracking started successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "GPS login successful. Employee ID: " + employeeId);


            return "GPS Login Successful";

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "GPS_LOGIN", "GPS_TRACKING", "GPS login failed. Employee ID: " + employeeId + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("GPS_TRACKING", "AttendanceService", "GPS login failed. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


    public String gpsLogout(String employeeId, GpsTracking gpsTracking) {

        String performedBy = getLoggedInEmployeeId();

        try {

            GpsTracking existingGps = gpsTrackingRepository.findTopByEmployeeIdOrderByIdDesc(Long.valueOf(employeeId)).orElseThrow(() -> new RuntimeException("GPS Record Not Found"));


            existingGps.setLogoutLocation(gpsTracking.getLogoutLocation());

            existingGps.setLatitude(gpsTracking.getLatitude());

            existingGps.setLongitude(gpsTracking.getLongitude());

            existingGps.setTrackingStatus("INACTIVE");


            gpsTrackingRepository.save(existingGps);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.logUpdate("GPS_TRACKING", String.valueOf(existingGps.getId()), performedBy, employeeId, "GPS logout completed successfully", null, null);


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "GPS_LOGOUT", "GPS_TRACKING", "GPS tracking stopped successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "GPS logout successful. Employee ID: " + employeeId);


            return "GPS Logout Successful";

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "GPS_LOGOUT", "GPS_TRACKING", "GPS logout failed. Employee ID: " + employeeId + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("GPS_TRACKING", "AttendanceService", "GPS logout failed. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


    public Object getGps(String employeeId) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Object gps = gpsTrackingRepository.findByEmployeeId(employeeId);


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("GPS_TRACKING", null, AuditActionType.VIEW, performedBy, employeeId, "GPS tracking records viewed successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_GPS", "GPS_TRACKING", "GPS tracking records viewed successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "GPS tracking records viewed successfully. Employee ID: " + employeeId);


            return gps;

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "VIEW_GPS", "GPS_TRACKING", "Failed to retrieve GPS tracking records. Employee ID: " + employeeId, ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("GPS_TRACKING", "AttendanceService", "Failed to retrieve GPS tracking records. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


    public Object liveLocation(String employeeId) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Object location = gpsTrackingRepository.findTopByEmployeeIdOrderByIdDesc(Long.valueOf(employeeId));


            // =====================================================
            // AUDIT LOG
            // =====================================================

            auditLogsService.createAuditLog("GPS_TRACKING", null, AuditActionType.VIEW, performedBy, employeeId, "Live GPS location viewed successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // ACTIVITY LOG
            // =====================================================

            auditLogsService.logActivity(employeeId, "VIEW_LIVE_LOCATION", "GPS_TRACKING", "Live GPS location viewed successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            // =====================================================
            // SYSTEM LOG
            // =====================================================

            auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "Live GPS location viewed successfully. Employee ID: " + employeeId);


            return location;

        } catch (Exception e) {

            auditLogsService.logActivity(employeeId, "VIEW_LIVE_LOCATION", "GPS_TRACKING", "Failed to retrieve live GPS location. Employee ID: " + employeeId, ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("GPS_TRACKING", "AttendanceService", "Failed to retrieve live GPS location. Employee ID: " + employeeId, e.toString());

            throw e;
        }
    }


    //=========================================
// SHIFT MANAGEMENT
//=========================================


//=========================================
// CREATE SHIFT
//=========================================

    public String createShift(Shift shift) {

        String performedBy = getLoggedInEmployeeId();

        try {

            shiftRepository.save(shift);


            //=========================================
            // AUDIT LOG
            //=========================================

         //   auditLogsService.logCreate("SHIFT", String.valueOf(shift.getId()), performedBy, null, "Shift created successfully");


            //=========================================
            // ACTIVITY LOG
            //=========================================

          //  auditLogsService.logActivity(performedBy, "CREATE_SHIFT", "SHIFT", "Shift created successfully. Shift ID: " + shift.getId(), ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // SYSTEM LOG
            //=========================================

          //  auditLogsService.logInfo("SHIFT", "ShiftService", "Shift created successfully. Shift ID: " + shift.getId());


            return "Shift Created Successfully";

        } catch (Exception e) {

           // auditLogsService.logActivity(performedBy, "CREATE_SHIFT", "SHIFT", "Failed to create shift. Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

          //  auditLogsService.logError("SHIFT", "ShiftService", "Failed to create shift", e.toString());

            throw e;
        }
    }


//=========================================
// GET ALL SHIFTS
//=========================================

    public List<Shift> getShifts() {

        String performedBy = getLoggedInEmployeeId();

        try {

            List<Shift> shifts = shiftRepository.findAll();


            //=========================================
            // AUDIT LOG
            //=========================================

            auditLogsService.createAuditLog("SHIFT", null, AuditActionType.VIEW, performedBy, performedBy, "All shifts viewed successfully", null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // ACTIVITY LOG
            //=========================================

            auditLogsService.logActivity(performedBy, "VIEW_SHIFTS", "SHIFT", "All shifts viewed successfully", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // SYSTEM LOG
            //=========================================

            auditLogsService.logInfo("SHIFT", "ShiftService", "All shifts retrieved successfully");


            return shifts;

        } catch (Exception e) {

            auditLogsService.logActivity(performedBy, "VIEW_SHIFTS", "SHIFT", "Failed to retrieve shifts. Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("SHIFT", "ShiftService", "Failed to retrieve shifts", e.toString());

            throw e;
        }
    }


//=========================================
// GET SHIFT BY ID
//=========================================

    public Shift getShift(Long id) {

        String performedBy = getLoggedInEmployeeId();

        try {

            Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift Not Found"));


            //=========================================
            // AUDIT LOG
            //=========================================

            auditLogsService.createAuditLog("SHIFT", String.valueOf(shift.getId()), AuditActionType.VIEW, performedBy, null, "Shift viewed successfully. Shift ID: " + id, null, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // ACTIVITY LOG
            //=========================================

            auditLogsService.logActivity(performedBy, "VIEW_SHIFT", "SHIFT", "Shift viewed successfully. Shift ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // SYSTEM LOG
            //=========================================

            auditLogsService.logInfo("SHIFT", "ShiftService", "Shift retrieved successfully. Shift ID: " + id);


            return shift;

        } catch (Exception e) {

            auditLogsService.logActivity(performedBy, "VIEW_SHIFT", "SHIFT", "Failed to retrieve shift. Shift ID: " + id + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("SHIFT", "ShiftService", "Failed to retrieve shift. Shift ID: " + id, e.toString());

            throw e;
        }
    }


//=========================================
// UPDATE SHIFT
//=========================================

    public String updateShift(Long id, Shift shift) throws JsonProcessingException {

        String performedBy = getLoggedInEmployeeId();

        try {

            Shift existingShift = shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift Not Found"));


            //=========================================
            // OLD VALUE
            //=========================================

            String oldValue = objectMapper.writeValueAsString(existingShift);


            existingShift.setShiftName(shift.getShiftName());

            existingShift.setShiftCode(shift.getShiftCode());

            existingShift.setStartTime(shift.getStartTime());

            existingShift.setEndTime(shift.getEndTime());

            existingShift.setBreakStart(shift.getBreakStart());

            existingShift.setBreakEnd(shift.getBreakEnd());

            existingShift.setGraceTime(shift.getGraceTime());

            existingShift.setWorkingHours(shift.getWorkingHours());

            existingShift.setStatus(shift.getStatus());


            shiftRepository.save(existingShift);


            //=========================================
            // NEW VALUE
            //=========================================

            String newValue = objectMapper.writeValueAsString(existingShift);


            //=========================================
            // AUDIT LOG
            //=========================================

            auditLogsService.logUpdate("SHIFT", String.valueOf(existingShift.getId()), performedBy, null, "Shift updated successfully", oldValue, newValue);


            //=========================================
            // ACTIVITY LOG
            //=========================================

            auditLogsService.logActivity(performedBy, "UPDATE_SHIFT", "SHIFT", "Shift updated successfully. Shift ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // SYSTEM LOG
            //=========================================

            auditLogsService.logInfo("SHIFT", "ShiftService", "Shift updated successfully. Shift ID: " + id);


            return "Shift Updated Successfully";

        } catch (Exception e) {

            auditLogsService.logActivity(performedBy, "UPDATE_SHIFT", "SHIFT", "Failed to update shift. Shift ID: " + id + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("SHIFT", "ShiftService", "Failed to update shift. Shift ID: " + id, e.toString());

            throw e;
        }
    }


//=========================================
// DELETE SHIFT
//=========================================

    public String deleteShift(Long id) throws JsonProcessingException {

        String performedBy = getLoggedInEmployeeId();

        try {

            Shift existingShift = shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift Not Found"));


            //=========================================
            // OLD VALUE BEFORE DELETE
            //=========================================

            String oldValue = objectMapper.writeValueAsString(existingShift);


            shiftRepository.delete(existingShift);


            //=========================================
            // AUDIT LOG
            //=========================================

            auditLogsService.createAuditLog("SHIFT", String.valueOf(existingShift.getId()), AuditActionType.DELETE, performedBy, null, "Shift deleted successfully", oldValue, null, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser() + " | " + clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // ACTIVITY LOG
            //=========================================

            auditLogsService.logActivity(null, "DELETE_SHIFT", "SHIFT", "Shift deleted successfully. Shift ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


            //=========================================
            // SYSTEM LOG
            //=========================================

            auditLogsService.logInfo("SHIFT", "ShiftService", "Shift deleted successfully. Shift ID: " + id);


            return "Shift Deleted Successfully";

        } catch (Exception e) {

            auditLogsService.logActivity(null, "DELETE_SHIFT", "SHIFT", "Failed to delete shift. Shift ID: " + id + ". Error: " + e.getMessage(), ActivityStatus.FAILED, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

            auditLogsService.logError("SHIFT", "ShiftService", "Failed to delete shift. Shift ID: " + id, e.toString());

            throw e;
        }
    }


    //=========================================
//=========================================
// EMPLOYEE SHIFT
//=========================================

    public String assignShift(String employeeId, EmployeeShift employeeShift) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Shift shift = shiftRepository.findById(employeeShift.getShift().getId()).orElseThrow(() -> new RuntimeException("Shift Not Found"));

        employeeShift.setEmployee(employee);
        employeeShift.setShift(shift);

        employeeShiftRepository.save(employeeShift);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate("EMPLOYEE_SHIFT", employeeShift.getEmployee().getEmployeeId(), performedBy, employeeId, "Shift assigned successfully. Employee ID: " + employeeId + ", Shift ID: " + shift.getId());


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "ASSIGN_SHIFT", "EMPLOYEE_SHIFT", "Shift assigned successfully. Employee ID: " + employeeId + ", Shift ID: " + shift.getId(), ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE_SHIFT", "EmployeeShiftService", "Shift assigned successfully. Employee ID: " + employeeId + ", Shift ID: " + shift.getId());

        return "Shift Assigned Successfully";
    }


//=========================================
// GET ALL EMPLOYEE SHIFTS
//=========================================

    public List<EmployeeShift> getEmployeeShifts() {

        List<EmployeeShift> employeeShifts = employeeShiftRepository.findAll();


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE_SHIFT", "EmployeeShiftService", "All employee shifts retrieved successfully");

        return employeeShifts;
    }


//=========================================
// GET EMPLOYEE SHIFT BY ID
//=========================================

    public EmployeeShift getEmployeeShift(Long id) {

        EmployeeShift employeeShift = employeeShiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Shift Not Found"));


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE_SHIFT", "EmployeeShiftService", "Employee shift retrieved successfully. ID: " + id);

        return employeeShift;
    }


//=========================================
// UPDATE EMPLOYEE SHIFT
//=========================================

    public String updateEmployeeShift(Long id, EmployeeShift employeeShift) {

        EmployeeShift existingShift = employeeShiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Shift Not Found"));

        Shift shift = shiftRepository.findById(employeeShift.getShift().getId()).orElseThrow(() -> new RuntimeException("Shift Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = "Shift ID: " + (existingShift.getShift() != null ? existingShift.getShift().getId() : null) + ", Effective From: " + existingShift.getEffectiveFrom() + ", Effective To: " + existingShift.getEffectiveTo() + ", Is Current: " + existingShift.getIsCurrent() + ", Assigned By: " + existingShift.getAssignedBy();


        existingShift.setShift(shift);
        existingShift.setEffectiveFrom(employeeShift.getEffectiveFrom());
        existingShift.setEffectiveTo(employeeShift.getEffectiveTo());
        existingShift.setIsCurrent(employeeShift.getIsCurrent());
        existingShift.setAssignedBy(employeeShift.getAssignedBy());

        employeeShiftRepository.save(existingShift);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = "Shift ID: " + shift.getId() + ", Effective From: " + employeeShift.getEffectiveFrom() + ", Effective To: " + employeeShift.getEffectiveTo() + ", Is Current: " + employeeShift.getIsCurrent() + ", Assigned By: " + employeeShift.getAssignedBy();


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate("EMPLOYEE_SHIFT", String.valueOf(existingShift.getId()), performedBy, existingShift.getEmployee().getEmployeeId(), "Employee shift updated successfully", oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "UPDATE_EMPLOYEE_SHIFT", "EMPLOYEE_SHIFT", "Employee shift updated successfully. Employee Shift ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE_SHIFT", "EmployeeShiftService", "Employee shift updated successfully. ID: " + id);

        return "Employee Shift Updated Successfully";
    }


//=========================================
// DELETE EMPLOYEE SHIFT
//=========================================

    public String deleteEmployeeShift(Long id) {

        EmployeeShift existingShift = employeeShiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Shift Not Found"));


        // =====================================================
        // SAVE INFORMATION BEFORE DELETE
        // =====================================================

        String oldValue = "Employee ID: " + (existingShift.getEmployee() != null ? existingShift.getEmployee().getEmployeeId() : null) + ", Shift ID: " + (existingShift.getShift() != null ? existingShift.getShift().getId() : null) + ", Effective From: " + existingShift.getEffectiveFrom() + ", Effective To: " + existingShift.getEffectiveTo() + ", Is Current: " + existingShift.getIsCurrent() + ", Assigned By: " + existingShift.getAssignedBy();


        String employeeId = existingShift.getEmployee().getEmployeeId();


        employeeShiftRepository.delete(existingShift);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete("EMPLOYEE_SHIFT", String.valueOf(existingShift.getId()), performedBy, employeeId, "Employee shift deleted successfully. " + "Employee Shift ID: " + id);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "DELETE_EMPLOYEE_SHIFT", "EMPLOYEE_SHIFT", "Employee shift deleted successfully. " + "Employee Shift ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("EMPLOYEE_SHIFT", "EmployeeShiftService", "Employee shift deleted successfully. ID: " + id);

        return "Employee Shift Deleted Successfully";
    }


//=========================================
// HOLIDAY MANAGEMENT
//=========================================

    public String createHoliday(Holiday holiday) {

        holidayRepository.save(holiday);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate("HOLIDAY", String.valueOf(holiday.getId()), performedBy, null, "Holiday created successfully. Holiday ID: " + holiday.getId() + ", Holiday Name: " + holiday.getHolidayName());


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "CREATE_HOLIDAY", "HOLIDAY", "Holiday created successfully. Holiday ID: " + holiday.getId() + ", Holiday Name: " + holiday.getHolidayName(), ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("HOLIDAY", "HolidayService", "Holiday created successfully. Holiday ID: " + holiday.getId());

        return "Holiday Created Successfully";
    }


//=========================================
// GET ALL HOLIDAYS
//=========================================

    public List<Holiday> getHolidays() {

        List<Holiday> holidays = holidayRepository.findAll();


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("HOLIDAY", "HolidayService", "All holidays retrieved successfully");

        return holidays;
    }


//=========================================
// GET HOLIDAY BY ID
//=========================================

    public Holiday getHoliday(Long id) {

        Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday Not Found"));


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("HOLIDAY", "HolidayService", "Holiday retrieved successfully. Holiday ID: " + id);

        return holiday;
    }


//=========================================
// UPDATE HOLIDAY
//=========================================

    public String updateHoliday(Long id, Holiday holiday) {

        Holiday existingHoliday = holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = "Holiday Name: " + existingHoliday.getHolidayName() + ", Holiday Date: " + existingHoliday.getHolidayDate() + ", Holiday Type: " + existingHoliday.getHolidayType() + ", Company: " + existingHoliday.getCompany() + ", Branch: " + existingHoliday.getBranch() + ", Description: " + existingHoliday.getDescription() + ", Status: " + existingHoliday.getStatus();


        existingHoliday.setHolidayName(holiday.getHolidayName());

        existingHoliday.setHolidayDate(holiday.getHolidayDate());

        existingHoliday.setHolidayType(holiday.getHolidayType());

        existingHoliday.setCompany(holiday.getCompany());

        existingHoliday.setBranch(holiday.getBranch());

        existingHoliday.setDescription(holiday.getDescription());

        existingHoliday.setStatus(holiday.getStatus());

        holidayRepository.save(existingHoliday);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = "Holiday Name: " + holiday.getHolidayName() + ", Holiday Date: " + holiday.getHolidayDate() + ", Holiday Type: " + holiday.getHolidayType() + ", Company: " + holiday.getCompany() + ", Branch: " + holiday.getBranch() + ", Description: " + holiday.getDescription() + ", Status: " + holiday.getStatus();


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate("HOLIDAY", String.valueOf(existingHoliday.getId()), performedBy, null, "Holiday updated successfully. Holiday ID: " + id, oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "UPDATE_HOLIDAY", "HOLIDAY", "Holiday updated successfully. Holiday ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("HOLIDAY", "HolidayService", "Holiday updated successfully. Holiday ID: " + id);

        return "Holiday Updated Successfully";
    }


//=========================================
// DELETE HOLIDAY
//=========================================

    public String deleteHoliday(Long id) {

        Holiday existingHoliday = holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday Not Found"));


        // =====================================================
        // SAVE OLD INFORMATION BEFORE DELETE
        // =====================================================

        String oldValue = "Holiday Name: " + existingHoliday.getHolidayName() + ", Holiday Date: " + existingHoliday.getHolidayDate() + ", Holiday Type: " + existingHoliday.getHolidayType() + ", Company: " + existingHoliday.getCompany() + ", Branch: " + existingHoliday.getBranch() + ", Description: " + existingHoliday.getDescription() + ", Status: " + existingHoliday.getStatus();


        holidayRepository.delete(existingHoliday);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete("HOLIDAY", String.valueOf(existingHoliday.getId()), performedBy, null, "Holiday deleted successfully. Holiday ID: " + id);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "DELETE_HOLIDAY", "HOLIDAY", "Holiday deleted successfully. Holiday ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("HOLIDAY", "HolidayService", "Holiday deleted successfully. Holiday ID: " + id);

        return "Holiday Deleted Successfully";
    }


//=========================================
// WEEKEND CONFIGURATION
//=========================================

    public String createWeekend(WeekendConfiguration weekendConfiguration) {

        weekendConfigurationRepository.save(weekendConfiguration);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate("WEEKEND_CONFIGURATION", String.valueOf(weekendConfiguration.getId()), performedBy, performedBy, "Weekend configuration created successfully. ID: " + weekendConfiguration.getId());


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "CREATE_WEEKEND_CONFIGURATION", "WEEKEND_CONFIGURATION", "Weekend configuration created successfully. ID: " + weekendConfiguration.getId(), ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("WEEKEND_CONFIGURATION", "WeekendConfigurationService", "Weekend configuration created successfully. ID: " + weekendConfiguration.getId());


        return "Weekend Configuration Created Successfully";
    }


//=========================================
// GET ALL WEEKENDS
//=========================================

    public List<WeekendConfiguration> getWeekends() {

        List<WeekendConfiguration> weekends = weekendConfigurationRepository.findAll();


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("WEEKEND_CONFIGURATION", "WeekendConfigurationService", "All weekend configurations retrieved successfully");


        return weekends;
    }


//=========================================
// GET WEEKEND BY ID
//=========================================

    public WeekendConfiguration getWeekend(Long id) {

        WeekendConfiguration weekend = weekendConfigurationRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("WEEKEND_CONFIGURATION", "WeekendConfigurationService", "Weekend configuration retrieved successfully. ID: " + id);


        return weekend;
    }


//=========================================
// UPDATE WEEKEND
//=========================================

    public String updateWeekend(Long id, WeekendConfiguration weekendConfiguration) {

        WeekendConfiguration existingWeekend = weekendConfigurationRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = "Company: " + existingWeekend.getCompany() + ", Branch: " + existingWeekend.getBranch() + ", Week Number: " + existingWeekend.getWeekNumber() + ", Day Name: " + existingWeekend.getDayName() + ", Is Weekend: " + existingWeekend.getIsWeekend();


        existingWeekend.setCompany(weekendConfiguration.getCompany());

        existingWeekend.setBranch(weekendConfiguration.getBranch());

        existingWeekend.setWeekNumber(weekendConfiguration.getWeekNumber());

        existingWeekend.setDayName(weekendConfiguration.getDayName());

        existingWeekend.setIsWeekend(weekendConfiguration.getIsWeekend());


        weekendConfigurationRepository.save(existingWeekend);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = "Company: " + weekendConfiguration.getCompany() + ", Branch: " + weekendConfiguration.getBranch() + ", Week Number: " + weekendConfiguration.getWeekNumber() + ", Day Name: " + weekendConfiguration.getDayName() + ", Is Weekend: " + weekendConfiguration.getIsWeekend();


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logUpdate("WEEKEND_CONFIGURATION", String.valueOf(existingWeekend.getId()), performedBy, null, "Weekend configuration updated successfully. ID: " + id, oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "UPDATE_WEEKEND_CONFIGURATION", "WEEKEND_CONFIGURATION", "Weekend configuration updated successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("WEEKEND_CONFIGURATION", "WeekendConfigurationService", "Weekend configuration updated successfully. ID: " + id);


        return "Weekend Configuration Updated Successfully";
    }


//=========================================
// DELETE WEEKEND
//=========================================

    public String deleteWeekend(Long id) {

        WeekendConfiguration existingWeekend = weekendConfigurationRepository.findById(id).orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));


        // =====================================================
        // OLD VALUE BEFORE DELETE
        // =====================================================

        String oldValue = "Company: " + existingWeekend.getCompany() + ", Branch: " + existingWeekend.getBranch() + ", Week Number: " + existingWeekend.getWeekNumber() + ", Day Name: " + existingWeekend.getDayName() + ", Is Weekend: " + existingWeekend.getIsWeekend();


        weekendConfigurationRepository.delete(existingWeekend);


        // =====================================================
        // GET LOGGED-IN EMPLOYEE
        // =====================================================

        String performedBy = getLoggedInEmployeeId();


        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logDelete("WEEKEND_CONFIGURATION", String.valueOf(existingWeekend.getId()), performedBy, performedBy, "Weekend configuration deleted successfully. ID: " + id);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(performedBy, "DELETE_WEEKEND_CONFIGURATION", "WEEKEND_CONFIGURATION", "Weekend configuration deleted successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("WEEKEND_CONFIGURATION", "WeekendConfigurationService", "Weekend configuration deleted successfully. ID: " + id);


        return "Weekend Configuration Deleted Successfully";
    }


    //=========================================
// ATTENDANCE REGULARIZATION
//=========================================

    public String createRegularization(Long attendanceId, AttendanceRegularization regularization) {

        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance Not Found"));

        regularization.setAttendance(attendance);
        regularization.setEmployee(attendance.getEmployee());

        if (regularization.getStatus() == null) {
            regularization.setStatus("PENDING");
        }

        attendanceRegularizationRepository.save(regularization);

        String performedBy = getLoggedInEmployeeId();
        // =====================================================
        // AUDIT LOG
        // =====================================================

        auditLogsService.logCreate("ATTENDANCE_REGULARIZATION", String.valueOf(regularization.getId()), performedBy, attendance.getEmployee().getEmployeeId(), "Attendance regularization request created successfully");


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(attendance.getEmployee().getEmployeeId(), "CREATE_REGULARIZATION", "ATTENDANCE_REGULARIZATION", "Attendance regularization request created. Attendance ID: " + attendanceId, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Attendance regularization request created successfully. ID: " + regularization.getId());


        return "Attendance Regularization Request Created Successfully";
    }


    public List<AttendanceRegularization> getRegularizations() {

        List<AttendanceRegularization> regularizations = attendanceRegularizationRepository.findAll();


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "All attendance regularization requests retrieved successfully");


        return regularizations;
    }


    public AttendanceRegularization getRegularization(Long id) {

        AttendanceRegularization regularization = attendanceRegularizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Attendance regularization retrieved successfully. ID: " + id);


        return regularization;
    }


    public String updateRegularization(Long id, AttendanceRegularization regularization) {

        AttendanceRegularization existingRegularization = attendanceRegularizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = "{" + "\"requestedLoginTime\":\"" + existingRegularization.getRequestedLoginTime() + "\"," + "\"requestedLogoutTime\":\"" + existingRegularization.getRequestedLogoutTime() + "\"," + "\"reason\":\"" + existingRegularization.getReason() + "\"," + "\"status\":\"" + existingRegularization.getStatus() + "\"," + "\"remarks\":\"" + existingRegularization.getRemarks() + "\"" + "}";


        existingRegularization.setRequestedLoginTime(regularization.getRequestedLoginTime());

        existingRegularization.setRequestedLogoutTime(regularization.getRequestedLogoutTime());

        existingRegularization.setReason(regularization.getReason());

        existingRegularization.setStatus(regularization.getStatus());

        existingRegularization.setApprovedBy(regularization.getApprovedBy());

        existingRegularization.setApprovedDate(regularization.getApprovedDate());

        existingRegularization.setRemarks(regularization.getRemarks());

        attendanceRegularizationRepository.save(existingRegularization);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = "{" + "\"requestedLoginTime\":\"" + existingRegularization.getRequestedLoginTime() + "\"," + "\"requestedLogoutTime\":\"" + existingRegularization.getRequestedLogoutTime() + "\"," + "\"reason\":\"" + existingRegularization.getReason() + "\"," + "\"status\":\"" + existingRegularization.getStatus() + "\"," + "\"approvedBy\":\"" + existingRegularization.getApprovedBy() + "\"," + "\"approvedDate\":\"" + existingRegularization.getApprovedDate() + "\"," + "\"remarks\":\"" + existingRegularization.getRemarks() + "\"" + "}";


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logUpdate("ATTENDANCE_REGULARIZATION", String.valueOf(existingRegularization.getId()), performedBy, existingRegularization.getEmployee().getEmployeeId(), "Attendance regularization updated successfully", oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(existingRegularization.getEmployee().getEmployeeId(), "UPDATE_REGULARIZATION", "ATTENDANCE_REGULARIZATION", "Attendance regularization updated successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Attendance regularization updated successfully. ID: " + id);


        return "Attendance Regularization Updated Successfully";
    }


    public String deleteRegularization(Long id) {

        AttendanceRegularization existingRegularization = attendanceRegularizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));


        String employeeId = existingRegularization.getEmployee().getEmployeeId();


        attendanceRegularizationRepository.delete(existingRegularization);


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logDelete("ATTENDANCE_REGULARIZATION", String.valueOf(existingRegularization.getId()), performedBy, employeeId, "Attendance regularization deleted successfully");


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(employeeId, "DELETE_REGULARIZATION", "ATTENDANCE_REGULARIZATION", "Attendance regularization deleted successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Attendance regularization deleted successfully. ID: " + id);


        return "Attendance Regularization Deleted Successfully";
    }

//=========================================
// OVERTIME REQUESTS
//=========================================

    public String createOvertime(Long attendanceId, OvertimeRequest overtimeRequest) {

        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance Not Found"));

        // Set Attendance
        overtimeRequest.setAttendance(attendance);

        // Automatically set Employee from Attendance
        overtimeRequest.setEmployee(attendance.getEmployee());

        // Default status
        if (overtimeRequest.getStatus() == null) {
            overtimeRequest.setStatus(OvertimeStatus.valueOf("PENDING"));
        }

        overtimeRequestRepository.save(overtimeRequest);


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logCreate("OVERTIME_REQUEST", String.valueOf(overtimeRequest.getId()), performedBy, attendance.getEmployee().getEmployeeId(), "Overtime request created successfully. Overtime ID: " + overtimeRequest.getId());


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(attendance.getEmployee().getEmployeeId(), "CREATE_OVERTIME_REQUEST", "OVERTIME", "Overtime request created successfully. Attendance ID: " + attendanceId, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("OVERTIME", "AttendanceService", "Overtime request created successfully. Overtime ID: " + overtimeRequest.getId());


        return "Overtime Request Created Successfully";
    }


//=========================================
// GET ALL OVERTIME REQUESTS
//=========================================

    public List<OvertimeRequest> getOvertimes() {

        List<OvertimeRequest> overtimes = overtimeRequestRepository.findAll();


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("OVERTIME", "AttendanceService", "All overtime requests retrieved successfully");


        return overtimes;
    }


//=========================================
// GET OVERTIME BY ID
//=========================================

    public OvertimeRequest getOvertime(Long id) {

        OvertimeRequest overtime = overtimeRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Overtime Request Not Found"));


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("OVERTIME", "AttendanceService", "Overtime request retrieved successfully. ID: " + id);


        return overtime;
    }


//=========================================
// UPDATE OVERTIME REQUEST
//=========================================

    public String updateOvertime(Long id, OvertimeRequest overtimeRequest) {

        OvertimeRequest existingOvertime = overtimeRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Overtime Request Not Found"));


        // =====================================================
        // OLD VALUE
        // =====================================================

        String oldValue = "{" + "\"requestDate\":\"" + existingOvertime.getRequestDate() + "\"," + "\"requestedHours\":\"" + existingOvertime.getRequestedHours() + "\"," + "\"approvedHours\":\"" + existingOvertime.getApprovedHours() + "\"," + "\"reason\":\"" + existingOvertime.getReason() + "\"," + "\"status\":\"" + existingOvertime.getStatus() + "\"," + "\"approvedBy\":\"" + existingOvertime.getApprovedBy() + "\"," + "\"approvedAt\":\"" + existingOvertime.getApprovedAt() + "\"" + "}";


        existingOvertime.setRequestDate(overtimeRequest.getRequestDate());

        existingOvertime.setRequestedHours(overtimeRequest.getRequestedHours());

        existingOvertime.setApprovedHours(overtimeRequest.getApprovedHours());

        existingOvertime.setReason(overtimeRequest.getReason());

        existingOvertime.setStatus(overtimeRequest.getStatus());

        existingOvertime.setApprovedBy(overtimeRequest.getApprovedBy());

        existingOvertime.setApprovedAt(overtimeRequest.getApprovedAt());

        overtimeRequestRepository.save(existingOvertime);


        // =====================================================
        // NEW VALUE
        // =====================================================

        String newValue = "{" + "\"requestDate\":\"" + existingOvertime.getRequestDate() + "\"," + "\"requestedHours\":\"" + existingOvertime.getRequestedHours() + "\"," + "\"approvedHours\":\"" + existingOvertime.getApprovedHours() + "\"," + "\"reason\":\"" + existingOvertime.getReason() + "\"," + "\"status\":\"" + existingOvertime.getStatus() + "\"," + "\"approvedBy\":\"" + existingOvertime.getApprovedBy() + "\"," + "\"approvedAt\":\"" + existingOvertime.getApprovedAt() + "\"" + "}";


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logUpdate("OVERTIME_REQUEST", String.valueOf(existingOvertime.getId()), performedBy, existingOvertime.getEmployee().getEmployeeId(), "Overtime request updated successfully", oldValue, newValue);


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(existingOvertime.getEmployee().getEmployeeId(), "UPDATE_OVERTIME_REQUEST", "OVERTIME", "Overtime request updated successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("OVERTIME", "AttendanceService", "Overtime request updated successfully. ID: " + id);


        return "Overtime Request Updated Successfully";
    }


//=========================================
// DELETE OVERTIME REQUEST
//=========================================

    public String deleteOvertime(Long id) {

        OvertimeRequest existingOvertime = overtimeRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Overtime Request Not Found"));


        String employeeId = existingOvertime.getEmployee().getEmployeeId();


        overtimeRequestRepository.delete(existingOvertime);


        // =====================================================
        // AUDIT LOG
        // =====================================================
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logDelete("OVERTIME_REQUEST", String.valueOf(existingOvertime.getId()), performedBy, employeeId, "Overtime request deleted successfully");


        // =====================================================
        // ACTIVITY LOG
        // =====================================================

        auditLogsService.logActivity(employeeId, "DELETE_OVERTIME_REQUEST", "OVERTIME", "Overtime request deleted successfully. ID: " + id, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());


        // =====================================================
        // SYSTEM LOG
        // =====================================================

        auditLogsService.logInfo("OVERTIME", "AttendanceService", "Overtime request deleted successfully. ID: " + id);


        return "Overtime Request Deleted Successfully";
    }


    //=========================================
    // ATTENDANCE
    //=========================================

    //    public String checkIn(String employeeId,
//                          Attendance attendance) {
//    }
//
//    public String checkOut(Long attendanceId) {
//    }
//
//    public Object getEmployeeAttendance(String employeeId) {
//    }
//
//    public Object getAttendance() {
//    }
    public Attendance getAttendance(Long id) {

        return attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Not Found"));
    }


//    public String deleteAttendance(Long id) {
//
//        Attendance attendance = attendanceRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Attendance Not Found"));
//
//        attendanceRepository.delete(attendance);
//
//        return "Attendance Deleted Successfully";
//    }
    //=========================================
    // REPORTS
    //=========================================


    public List<Attendance> presentEmployees() {

        List<Attendance> result = attendanceRepository.findAll().stream().filter(attendance -> AttendanceStatus.PRESENT.equals(String.valueOf(attendance.getAttendanceStatus()))).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_PRESENT_EMPLOYEES", "ATTENDANCE_REPORTS", "Present employee attendance report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REPORTS", "AttendanceService", "Present employees report retrieved successfully");

        return result;
    }

    public List<Attendance> absentEmployees() {

        List<Attendance> result = attendanceRepository.findAll().stream().filter(attendance -> "ABSENT".equalsIgnoreCase(String.valueOf(attendance.getAttendanceStatus()))).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_ABSENT_EMPLOYEES", "ATTENDANCE_REPORTS", "Absent employee attendance report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REPORTS", "AttendanceService", "Absent employees report retrieved successfully");

        return result;
    }

    public List<Attendance> lateEmployees() {

        List<Attendance> result = attendanceRepository.findAll().stream().filter(attendance -> Boolean.TRUE.equals(attendance.getLate())).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_LATE_EMPLOYEES", "ATTENDANCE_REPORTS", "Late employee attendance report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REPORTS", "AttendanceService", "Late employees report retrieved successfully");

        return result;
    }

    public List<Attendance> earlyLeavingEmployees() {

        List<Attendance> result = attendanceRepository.findAll().stream().filter(attendance -> Boolean.TRUE.equals(attendance.getEarlyLeaving())).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_EARLY_LEAVING_EMPLOYEES", "ATTENDANCE_REPORTS", "Early leaving employee report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REPORTS", "AttendanceService", "Early leaving employees report retrieved successfully");

        return result;
    }

    public List<OvertimeRequest> approvedOvertime() {

        List<OvertimeRequest> result = overtimeRequestRepository.findAll().stream().filter(overtime -> "APPROVED".equalsIgnoreCase(String.valueOf(overtime.getStatus()))).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_APPROVED_OVERTIME", "OVERTIME_REPORTS", "Approved overtime report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("OVERTIME_REPORTS", "AttendanceService", "Approved overtime report retrieved successfully");

        return result;
    }

    public List<OvertimeRequest> pendingOvertime() {

        List<OvertimeRequest> result = overtimeRequestRepository.findAll().stream().filter(overtime -> "PENDING".equalsIgnoreCase(String.valueOf(overtime.getStatus()))).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_PENDING_OVERTIME", "OVERTIME_REPORTS", "Pending overtime report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("OVERTIME_REPORTS", "AttendanceService", "Pending overtime report retrieved successfully");

        return result;
    }

    public List<AttendanceRegularization> pendingRegularization() {

        List<AttendanceRegularization> result = attendanceRegularizationRepository.findAll().stream().filter(regularization -> "PENDING".equalsIgnoreCase(regularization.getStatus())).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_PENDING_REGULARIZATION", "ATTENDANCE_REGULARIZATION", "Pending attendance regularization report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Pending regularization report retrieved successfully");

        return result;
    }

    public List<AttendanceRegularization> approvedRegularization() {

        List<AttendanceRegularization> result = attendanceRegularizationRepository.findAll().stream().filter(regularization -> "APPROVED".equalsIgnoreCase(regularization.getStatus())).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_APPROVED_REGULARIZATION", "ATTENDANCE_REGULARIZATION", "Approved attendance regularization report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REGULARIZATION", "AttendanceService", "Approved regularization report retrieved successfully");

        return result;
    }

    public List<Attendance> todayAttendance() {

        LocalDate today = LocalDate.now();

        List<Attendance> result = attendanceRepository.findAll().stream().filter(attendance -> {

            if (attendance.getLoginTime() == null) {
                return false;
            }

            return attendance.getLoginTime().toLocalDate().equals(today);
        }).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_TODAY_ATTENDANCE", "ATTENDANCE_REPORTS", "Today's attendance report viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("ATTENDANCE_REPORTS", "AttendanceService", "Today's attendance report retrieved successfully");

        return result;
    }

    public List<GpsTracking> liveTracking() {

        List<GpsTracking> result = gpsTrackingRepository.findAll().stream().filter(gps -> "ACTIVE".equalsIgnoreCase(gps.getTrackingStatus()) && gps.getLogoutLocation() == null).toList();
        String performedBy = getLoggedInEmployeeId();
        auditLogsService.logActivity(performedBy, "VIEW_LIVE_GPS", "GPS_TRACKING", "Live employee GPS tracking viewed", ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "Live GPS tracking retrieved successfully");

        return result;
    }

    public List<GpsTracking> gpsHistory(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        List<GpsTracking> result = gpsTrackingRepository.findAll().stream().filter(gps -> gps.getEmployee() != null && gps.getEmployee().getEmployeeId().equals(employee.getEmployeeId())).toList();

        auditLogsService.logActivity(employeeId, "VIEW_GPS_HISTORY", "GPS_TRACKING", "Employee GPS history viewed. Employee ID: " + employeeId, ActivityStatus.SUCCESS, clientInfoService.getClientInfo().getIpAddress(), clientInfoService.getClientInfo().getBrowser(), clientInfoService.getClientInfo().getOperatingSystem());

        auditLogsService.logInfo("GPS_TRACKING", "AttendanceService", "GPS history retrieved successfully. Employee ID: " + employeeId);

        return result;
    }

    //=========================================
    // DASHBOARD
    //=========================================

    public AttendanceDashboardDTO getCounts() {

        LocalDate today = LocalDate.now();

        List<Attendance> allAttendance = attendanceRepository.findAll();

        List<OvertimeRequest> allOvertime = overtimeRequestRepository.findAll();

        List<AttendanceRegularization> allRegularizations = attendanceRegularizationRepository.findAll();

        List<GpsTracking> allGps = gpsTrackingRepository.findAll();

        // Total Employees
        long totalEmployees = employeeRepository.count();

        // Present Employees
        long presentEmployees = allAttendance.stream().filter(attendance -> "PRESENT".equalsIgnoreCase(String.valueOf(attendance.getAttendanceStatus()))).filter(attendance -> attendance.getLoginTime() != null && attendance.getLoginTime().toLocalDate().equals(today)).count();

        // Absent Employees
        long absentEmployees = allAttendance.stream().filter(attendance -> "ABSENT".equalsIgnoreCase(String.valueOf(attendance.getAttendanceStatus()))).filter(attendance -> attendance.getCreatedAt() != null && attendance.getCreatedAt().toLocalDate().equals(today)).count();

        // Late Employees
        long lateEmployees = allAttendance.stream().filter(attendance -> Boolean.TRUE.equals(attendance.getLate())).filter(attendance -> attendance.getLoginTime() != null && attendance.getLoginTime().toLocalDate().equals(today)).count();

        // Early Leaving Employees
        long earlyLeavingEmployees = allAttendance.stream().filter(attendance -> Boolean.TRUE.equals(attendance.getEarlyLeaving())).filter(attendance -> attendance.getLoginTime() != null && attendance.getLoginTime().toLocalDate().equals(today)).count();

        // Today's Attendance
        long todayAttendance = allAttendance.stream().filter(attendance -> attendance.getLoginTime() != null && attendance.getLoginTime().toLocalDate().equals(today)).count();

        // Approved Overtime
        long approvedOvertime = allOvertime.stream().filter(overtime -> "APPROVED".equalsIgnoreCase(String.valueOf(overtime.getStatus()))).count();

        // Pending Overtime
        long pendingOvertime = allOvertime.stream().filter(overtime -> "PENDING".equalsIgnoreCase(String.valueOf(overtime.getStatus()))).count();

        // Pending Regularization
        long pendingRegularization = allRegularizations.stream().filter(regularization -> "PENDING".equalsIgnoreCase(regularization.getStatus())).count();

        // Approved Regularization
        long approvedRegularization = allRegularizations.stream().filter(regularization -> "APPROVED".equalsIgnoreCase(regularization.getStatus())).count();

        // Live GPS Tracking
        long liveGpsTracking = allGps.stream().filter(gps -> "ACTIVE".equalsIgnoreCase(gps.getTrackingStatus())).filter(gps -> gps.getLogoutLocation() == null).count();

        return new AttendanceDashboardDTO(totalEmployees, presentEmployees, absentEmployees, lateEmployees, earlyLeavingEmployees, todayAttendance, approvedOvertime, pendingOvertime, pendingRegularization, approvedRegularization, liveGpsTracking);
    }

    // =========================================================
// ATTENDANCE FILTER
// =========================================================

    public List<Attendance> filterAttendance(String period, Integer year, Integer month, String date) {

        LocalDateTime start;
        LocalDateTime end;

        switch (period.toUpperCase()) {

            // =================================================
            // TODAY
            // =================================================

            case "TODAY":

                LocalDate today = LocalDate.now();

                start = today.atStartOfDay();

                end = today.plusDays(1).atStartOfDay();

                break;


            // =================================================
            // YESTERDAY
            // =================================================

            case "YESTERDAY":

                LocalDate yesterday = LocalDate.now().minusDays(1);

                start = yesterday.atStartOfDay();

                end = yesterday.plusDays(1).atStartOfDay();

                break;


            // =================================================
            // THIS WEEK
            // =================================================

            case "THIS_WEEK":

                LocalDate currentDate = LocalDate.now();

                LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);

                LocalDate weekEnd = weekStart.plusDays(7);

                start = weekStart.atStartOfDay();

                end = weekEnd.atStartOfDay();

                break;


            // =================================================
            // THIS MONTH
            // =================================================

            case "THIS_MONTH":

                LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

                LocalDate nextMonth = currentMonth.plusMonths(1);

                start = currentMonth.atStartOfDay();

                end = nextMonth.atStartOfDay();

                break;


            // =================================================
            // MONTH WISE
            // =================================================

            case "MONTH":

                if (year == null || month == null) {

                    throw new RuntimeException("Year and month are required");
                }

                LocalDate monthStart = LocalDate.of(year, month, 1);

                LocalDate monthEnd = monthStart.plusMonths(1);

                start = monthStart.atStartOfDay();

                end = monthEnd.atStartOfDay();

                break;


            // =================================================
            // WEEK WISE
            // =================================================

            case "WEEK":

                if (date == null) {

                    throw new RuntimeException("Date is required");
                }

                LocalDate selectedDate = LocalDate.parse(date);

                LocalDate selectedWeekStart = selectedDate.with(DayOfWeek.MONDAY);

                LocalDate selectedWeekEnd = selectedWeekStart.plusDays(7);

                start = selectedWeekStart.atStartOfDay();

                end = selectedWeekEnd.atStartOfDay();

                break;


            // =================================================
            // INVALID PERIOD
            // =================================================

            default:

                throw new RuntimeException("Invalid period. Use TODAY, YESTERDAY, " + "THIS_WEEK, THIS_MONTH, MONTH or WEEK");
        }


        return attendanceRepository.findByDateRange(start, end);
    }

    public List<Attendance> getAttendanceByStatus(AttendanceStatus status) {

        return attendanceRepository.findByAttendanceStatus(status);
    }

    public List<EmployeeShift> getEmployeesByShiftCode(String shiftCode) {

        if (shiftCode == null || shiftCode.isBlank()) {

            throw new RuntimeException("Shift code is required");
        }

        return employeeShiftRepository.findByShiftShiftCode(shiftCode);
    }

    public List<OvertimeRequest> getOvertimeByStatus(OvertimeStatus status) {

        if (status == null) {
            throw new RuntimeException("Overtime status is required");
        }

        return overtimeRequestRepository.findByStatus(status);
    }


    public byte[] exportEmployeeAttendance(String employeeId) {

        // =====================================================
        // CHECK EMPLOYEE
        // =====================================================

        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // =====================================================
        // GET EMPLOYEE ATTENDANCE
        // =====================================================

        List<Attendance> attendanceList = attendanceRepository.findByEmployeeEmployeeId(employeeId);

        try (Workbook workbook = new XSSFWorkbook();

             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Employee Attendance");

            // =================================================
            // EMPLOYEE INFORMATION
            // =================================================

            Row employeeRow = sheet.createRow(0);

            employeeRow.createCell(0).setCellValue("Employee ID");

            employeeRow.createCell(1).setCellValue(employee.getEmployeeId());

            Row nameRow = sheet.createRow(1);

            nameRow.createCell(0).setCellValue("Employee Name");

            String employeeName = (employee.getFirstName() != null ? employee.getFirstName() : "") + " " + (employee.getLastName() != null ? employee.getLastName() : "");

            nameRow.createCell(1).setCellValue(employeeName.trim());

            Row emailRow = sheet.createRow(2);

            emailRow.createCell(0).setCellValue("Email");

            emailRow.createCell(1).setCellValue(employee.getEmail() != null ? employee.getEmail() : "");

            // =================================================
            // HEADER
            // =================================================

            Row headerRow = sheet.createRow(4);

            String[] headers = {

                    "Date", "Login Time", "Logout Time", "Total Hours", "Attendance Status", "Late", "Early Leaving", "Remarks"};

            for (int i = 0; i < headers.length; i++) {

                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // =================================================
            // ATTENDANCE DATA
            // =================================================

            int rowNumber = 5;

            for (Attendance attendance : attendanceList) {

                Row row = sheet.createRow(rowNumber++);

                // Date
                row.createCell(0).setCellValue(attendance.getLoginTime() != null ? attendance.getLoginTime().toLocalDate().toString() : "");

                // Login
                row.createCell(1).setCellValue(attendance.getLoginTime() != null ? attendance.getLoginTime().toLocalTime().toString() : "");

                // Logout
                row.createCell(2).setCellValue(attendance.getLogoutTime() != null ? attendance.getLogoutTime().toLocalTime().toString() : "");

                // Total Hours
                row.createCell(3).setCellValue(attendance.getTotalHours() != null ? attendance.getTotalHours().doubleValue() : 0);

                // Status
                row.createCell(4).setCellValue(attendance.getAttendanceStatus() != null ? attendance.getAttendanceStatus().toString() : "");

                // Late
                row.createCell(5).setCellValue(attendance.getLate() != null ? attendance.getLate() : false);

                // Early Leaving
                row.createCell(6).setCellValue(attendance.getEarlyLeaving() != null ? attendance.getEarlyLeaving() : false);

                // Remarks
                row.createCell(7).setCellValue(attendance.getRemarks() != null ? attendance.getRemarks() : "");
            }

            // =================================================
            // AUTO SIZE
            // =================================================

            for (int i = 0; i < headers.length; i++) {

                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException("Failed to generate employee attendance Excel", e);
        }
    }


    public byte[] exportAttendanceToExcel() {

        List<Attendance> attendanceList = attendanceRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Attendance Report");

            // =====================================================
            // HEADER
            // =====================================================

            Row headerRow = sheet.createRow(0);

            String[] headers = {"Employee ID", "Employee Name", "Mobile Number", "Department ID", "Login Time", "Logout Time", "Total Hours", "Attendance Status", "Late", "Early Leaving", "Remarks", "Created At"};

            for (int i = 0; i < headers.length; i++) {

                Cell cell = headerRow.createCell(i);

                cell.setCellValue(headers[i]);
            }

            // =====================================================
            // DATA
            // =====================================================

            int rowNumber = 1;

            for (Attendance attendance : attendanceList) {

                Row row = sheet.createRow(rowNumber++);

                Employee employee = attendance.getEmployee();

                row.createCell(0).setCellValue(employee != null ? employee.getEmployeeId() : "");

                String employeeName = "";

                if (employee != null) {

                    employeeName = (employee.getFirstName() != null ? employee.getFirstName() : "") + " " + (employee.getLastName() != null ? employee.getLastName() : "");
                }

                row.createCell(1).setCellValue(employeeName.trim());

                row.createCell(2).setCellValue(employee != null ? employee.getMobileNumber() : "");

                row.createCell(3).setCellValue(String.valueOf(((employee != null && employee.getDepartment().getId() != null ? employee.getDepartment().getId() : 0))));

                row.createCell(4).setCellValue(attendance.getLoginTime() != null ? attendance.getLoginTime().toString() : "");

                row.createCell(5).setCellValue(attendance.getLogoutTime() != null ? attendance.getLogoutTime().toString() : "");

                row.createCell(6).setCellValue(attendance.getTotalHours() != null ? attendance.getTotalHours().doubleValue() : 0);

                row.createCell(7).setCellValue(attendance.getAttendanceStatus() != null ? attendance.getAttendanceStatus().toString() : "");

                row.createCell(8).setCellValue(attendance.getLate() != null ? attendance.getLate() : false);

                row.createCell(9).setCellValue(attendance.getEarlyLeaving() != null ? attendance.getEarlyLeaving() : false);

                row.createCell(10).setCellValue(attendance.getRemarks() != null ? attendance.getRemarks() : "");

                row.createCell(11).setCellValue(attendance.getCreatedAt() != null ? attendance.getCreatedAt().toString() : "");
            }

            // =====================================================
            // AUTO SIZE
            // =====================================================

            for (int i = 0; i < headers.length; i++) {

                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException("Failed to generate attendance Excel", e);
        }
    }


}



