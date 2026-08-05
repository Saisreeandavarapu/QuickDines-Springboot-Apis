package com.HRMS.QuickDines.Attendance.Service;

import com.HRMS.QuickDines.Attendance.DTO.AttendanceDashboardDTO;
import com.HRMS.QuickDines.Attendance.model.*;
import com.HRMS.QuickDines.Attendance.repo.*;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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


//---------------------------------
// EMPLOYEE ATTENDANCE
//---------------------------------

    public String checkIn(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        Optional<Attendance> attendanceExists = attendanceRepository.findByEmployeeIdAndCreatedAtBetween(employeeId, start, end);

        if (attendanceExists.isPresent()) {
            return "Attendance Already Marked Today";
        }

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setLoginTime(LocalDateTime.now());
        attendance.setAttendanceStatus("PRESENT");
        attendance.setRemarks("Checked In");

        attendanceRepository.save(attendance);

        return "Check In Successful";
    }



    public String checkOut(String employeeId) {

        Attendance attendance = attendanceRepository.findTopByEmployeeIdOrderByIdDesc(employeeId).orElseThrow(() -> new RuntimeException("Attendance Record Not Found"));
        if (attendance.getLogoutTime() != null) {
            return "Already Checked Out Today";
        }
        attendance.setLogoutTime(LocalDateTime.now());
        double totalHours = Duration.between(
                        attendance.getLoginTime(),
                        attendance.getLogoutTime())
                .toMinutes() / 60.0;

        attendance.setTotalHours(totalHours);
        attendance.setRemarks("Checked Out");
        attendanceRepository.save(attendance);
        return "Check Out Successful";
    }



    public List<Attendance> getAttendance(String employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }


//---------------------------------
// HR / ADMIN / SUPER ADMIN
//---------------------------------

    public List<Attendance> getAllAttendance() {

        return attendanceRepository.findAll();
    }


    public String updateAttendance(Long attendanceId, Attendance attendance) {

        Attendance existingAttendance = attendanceRepository.findById(attendanceId).orElseThrow(() ->
                new RuntimeException("Attendance Not Found"));

        existingAttendance.setLoginTime(attendance.getLoginTime());

        existingAttendance.setLogoutTime(attendance.getLogoutTime());

        existingAttendance.setAttendanceStatus(attendance.getAttendanceStatus());

        existingAttendance.setRemarks(attendance.getRemarks());

        // Calculate Total Working Hours

        if (attendance.getLoginTime() != null &&
                attendance.getLogoutTime() != null) {
            double totalHours = Duration.between(attendance.getLoginTime(), attendance.getLogoutTime())
                    .toMinutes() / 60.0;
            existingAttendance.setTotalHours(totalHours);
        }
        attendanceRepository.save(existingAttendance);

        return "Attendance Updated Successfully";
    }


    public String deleteAttendance(Long attendanceId) {

        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new RuntimeException("Attendance Not Found"));
        attendanceRepository.delete(attendance);
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

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setAttendanceStatus("LEAVE");
        attendance.setRemarks("Approved Leave");
        attendance.setTotalHours(0.0);

        attendanceRepository.save(attendance);

        return "Leave Marked Successfully";
    }



    //---------------------------------
// REPORTS
//---------------------------------

    public Object getReports(String employeeId) {
        return attendanceReportsRepository.findByEmployeeId(employeeId);
    }


    public Object getMonthlyReport(String employeeId) {

        String currentMonth = YearMonth.now().toString();

        return attendanceReportsRepository.findByEmployeeIdAndMonth(employeeId, currentMonth);
    }



    //---------------------------------
// WORKING HOURS
//---------------------------------

    public Object getWorkingHours(String employeeId) {

        return workingHoursRepository.findByEmployeeId(employeeId);
    }


    public String updateWorkingHours(
            String employeeId,
            WorkingHours workingHours) {

        WorkingHours existingWorkingHours = (WorkingHours) workingHoursRepository.findByEmployeeId(employeeId);

        existingWorkingHours.setExpectedHours(workingHours.getExpectedHours());

        existingWorkingHours.setCompletedHours(workingHours.getCompletedHours());

        existingWorkingHours.setOvertimeHours(workingHours.getOvertimeHours());

        existingWorkingHours.setBreakHours(workingHours.getBreakHours());

        existingWorkingHours.setStatus(workingHours.getStatus());

        workingHoursRepository.save(existingWorkingHours);

        return "Working Hours Updated Successfully";
    }



    //---------------------------------
// GPS TRACKING
//---------------------------------

    public String gpsLogin(String employeeId, GpsTracking gpsTracking) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        gpsTracking.setEmployee(employee);
        gpsTracking.setTrackingStatus("ACTIVE");

        gpsTrackingRepository.save(gpsTracking);

        return "GPS Login Successful";
    }



    public String gpsLogout(String employeeId, GpsTracking gpsTracking) {

        GpsTracking existingGps = gpsTrackingRepository.findTopByEmployeeIdOrderByIdDesc(Long.valueOf(employeeId))
                .orElseThrow(() -> new RuntimeException("GPS Record Not Found"));

        existingGps.setLogoutLocation(gpsTracking.getLogoutLocation());

        existingGps.setLatitude(gpsTracking.getLatitude());

        existingGps.setLongitude(gpsTracking.getLongitude());

        existingGps.setTrackingStatus("INACTIVE");

        gpsTrackingRepository.save(existingGps);

        return "GPS Logout Successful";
    }



    public Object getGps(String employeeId) {

        return gpsTrackingRepository.findByEmployeeId(employeeId);
    }



    public Object liveLocation(String employeeId) {

        return gpsTrackingRepository.findTopByEmployeeIdOrderByIdDesc(Long.valueOf(employeeId));

    }

    //=========================================
// SHIFT MANAGEMENT
//=========================================

    public String createShift(Shift shift) {

        shiftRepository.save(shift);

        return "Shift Created Successfully";
    }

    public List<Shift> getShifts() {

        return shiftRepository.findAll();
    }

    public Shift getShift(Long id) {

        return shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift Not Found"));
    }

    public String updateShift(Long id, Shift shift) {

        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift Not Found"));

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

        return "Shift Updated Successfully";
    }

    public String deleteShift(Long id) {

        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift Not Found"));

        shiftRepository.delete(existingShift);

        return "Shift Deleted Successfully";
    }

    //=========================================
    // EMPLOYEE SHIFT
    //=========================================


    public String assignShift(String employeeId,
                              EmployeeShift employeeShift) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Shift shift = shiftRepository.findById(
                        employeeShift.getShift().getId())
                .orElseThrow(() -> new RuntimeException("Shift Not Found"));

        employeeShift.setEmployee(employee);
        employeeShift.setShift(shift);

        employeeShiftRepository.save(employeeShift);

        return "Shift Assigned Successfully";
    }

    public List<EmployeeShift> getEmployeeShifts() {

        return employeeShiftRepository.findAll();
    }

    public EmployeeShift getEmployeeShift(Long id) {

        return employeeShiftRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee Shift Not Found"));
    }

    public String updateEmployeeShift(Long id,
                                      EmployeeShift employeeShift) {

        EmployeeShift existingShift = employeeShiftRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee Shift Not Found"));

        Shift shift = shiftRepository.findById(
                        employeeShift.getShift().getId())
                .orElseThrow(() ->
                        new RuntimeException("Shift Not Found"));

        existingShift.setShift(shift);
        existingShift.setEffectiveFrom(employeeShift.getEffectiveFrom());
        existingShift.setEffectiveTo(employeeShift.getEffectiveTo());
        existingShift.setIsCurrent(employeeShift.getIsCurrent());
        existingShift.setAssignedBy(employeeShift.getAssignedBy());

        employeeShiftRepository.save(existingShift);

        return "Employee Shift Updated Successfully";
    }

    public String deleteEmployeeShift(Long id) {

        EmployeeShift existingShift = employeeShiftRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee Shift Not Found"));

        employeeShiftRepository.delete(existingShift);

        return "Employee Shift Deleted Successfully";
    }

    //=========================================
// HOLIDAY MANAGEMENT
//=========================================

    public String createHoliday(Holiday holiday) {

        holidayRepository.save(holiday);

        return "Holiday Created Successfully";
    }

    public List<Holiday> getHolidays() {

        return holidayRepository.findAll();
    }

    public Holiday getHoliday(Long id) {

        return holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday Not Found"));
    }

    public String updateHoliday(Long id, Holiday holiday) {

        Holiday existingHoliday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday Not Found"));

        existingHoliday.setHolidayName(holiday.getHolidayName());
        existingHoliday.setHolidayDate(holiday.getHolidayDate());
        existingHoliday.setHolidayType(holiday.getHolidayType());
        existingHoliday.setCompany(holiday.getCompany());
        existingHoliday.setBranch(holiday.getBranch());
        existingHoliday.setDescription(holiday.getDescription());
        existingHoliday.setStatus(holiday.getStatus());

        holidayRepository.save(existingHoliday);

        return "Holiday Updated Successfully";
    }

    public String deleteHoliday(Long id) {

        Holiday existingHoliday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday Not Found"));

        holidayRepository.delete(existingHoliday);

        return "Holiday Deleted Successfully";
    }
    //=========================================
// WEEKEND CONFIGURATION
//=========================================

    public String createWeekend(WeekendConfiguration weekendConfiguration) {

        weekendConfigurationRepository.save(weekendConfiguration);

        return "Weekend Configuration Created Successfully";
    }

    public List<WeekendConfiguration> getWeekends() {

        return weekendConfigurationRepository.findAll();
    }

    public WeekendConfiguration getWeekend(Long id) {

        return weekendConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));
    }

    public String updateWeekend(Long id,
                                WeekendConfiguration weekendConfiguration) {

        WeekendConfiguration existingWeekend = weekendConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));

        existingWeekend.setCompany(weekendConfiguration.getCompany());
        existingWeekend.setBranch(weekendConfiguration.getBranch());
        existingWeekend.setWeekNumber(weekendConfiguration.getWeekNumber());
        existingWeekend.setDayName(weekendConfiguration.getDayName());
        existingWeekend.setIsWeekend(weekendConfiguration.getIsWeekend());

        weekendConfigurationRepository.save(existingWeekend);

        return "Weekend Configuration Updated Successfully";
    }

    public String deleteWeekend(Long id) {

        WeekendConfiguration existingWeekend = weekendConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekend Configuration Not Found"));

        weekendConfigurationRepository.delete(existingWeekend);

        return "Weekend Configuration Deleted Successfully";
    }

    //=========================================
// ATTENDANCE REGULARIZATION
//=========================================

    public String createRegularization(Long attendanceId,
                                       AttendanceRegularization regularization) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance Not Found"));

        regularization.setAttendance(attendance);
        regularization.setEmployee(attendance.getEmployee());

        if (regularization.getStatus() == null) {
            regularization.setStatus("PENDING");
        }

        attendanceRegularizationRepository.save(regularization);

        return "Attendance Regularization Request Created Successfully";
    }

    public List<AttendanceRegularization> getRegularizations() {

        return attendanceRegularizationRepository.findAll();
    }

    public AttendanceRegularization getRegularization(Long id) {

        return attendanceRegularizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));
    }

    public String updateRegularization(Long id,
                                       AttendanceRegularization regularization) {

        AttendanceRegularization existingRegularization =
                attendanceRegularizationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));

        existingRegularization.setRequestedLoginTime(
                regularization.getRequestedLoginTime());

        existingRegularization.setRequestedLogoutTime(
                regularization.getRequestedLogoutTime());

        existingRegularization.setReason(
                regularization.getReason());

        existingRegularization.setStatus(
                regularization.getStatus());

        existingRegularization.setApprovedBy(
                regularization.getApprovedBy());

        existingRegularization.setApprovedDate(
                regularization.getApprovedDate());

        existingRegularization.setRemarks(
                regularization.getRemarks());

        attendanceRegularizationRepository.save(existingRegularization);

        return "Attendance Regularization Updated Successfully";
    }

    public String deleteRegularization(Long id) {

        AttendanceRegularization existingRegularization =
                attendanceRegularizationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Attendance Regularization Not Found"));

        attendanceRegularizationRepository.delete(existingRegularization);

        return "Attendance Regularization Deleted Successfully";
    }

    //=========================================
// OVERTIME REQUESTS
//=========================================

    public String createOvertime(Long attendanceId,
                                 OvertimeRequest overtimeRequest) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new RuntimeException("Attendance Not Found"));

        // Set Attendance
        overtimeRequest.setAttendance(attendance);

        // Automatically set Employee from Attendance
        overtimeRequest.setEmployee(attendance.getEmployee());

        // Default status
        if (overtimeRequest.getStatus() == null) {
            overtimeRequest.setStatus("PENDING");
        }

        overtimeRequestRepository.save(overtimeRequest);

        return "Overtime Request Created Successfully";
    }


    public List<OvertimeRequest> getOvertimes() {

        return overtimeRequestRepository.findAll();
    }


    public OvertimeRequest getOvertime(Long id) {

        return overtimeRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Overtime Request Not Found"));
    }


    public String updateOvertime(Long id,
                                 OvertimeRequest overtimeRequest) {

        OvertimeRequest existingOvertime =
                overtimeRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Overtime Request Not Found"));

        existingOvertime.setRequestDate(
                overtimeRequest.getRequestDate());

        existingOvertime.setRequestedHours(
                overtimeRequest.getRequestedHours());

        existingOvertime.setApprovedHours(
                overtimeRequest.getApprovedHours());

        existingOvertime.setReason(
                overtimeRequest.getReason());

        existingOvertime.setStatus(
                overtimeRequest.getStatus());

        existingOvertime.setApprovedBy(
                overtimeRequest.getApprovedBy());

        existingOvertime.setApprovedAt(
                overtimeRequest.getApprovedAt());

        overtimeRequestRepository.save(existingOvertime);

        return "Overtime Request Updated Successfully";
    }


    public String deleteOvertime(Long id) {

        OvertimeRequest existingOvertime =
                overtimeRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Overtime Request Not Found"));

        overtimeRequestRepository.delete(existingOvertime);

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

    return attendanceRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Attendance Not Found"));
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

    //=========================================
// ATTENDANCE REPORTS
//=========================================

    public List<Attendance> presentEmployees() {

        return attendanceRepository.findAll()
                .stream()
                .filter(attendance ->
                        "PRESENT".equalsIgnoreCase(attendance.getAttendanceStatus()))
                .toList();
    }


    public List<Attendance> absentEmployees() {

        return attendanceRepository.findAll()
                .stream()
                .filter(attendance ->
                        "ABSENT".equalsIgnoreCase(attendance.getAttendanceStatus()))
                .toList();
    }


    public List<Attendance> lateEmployees() {

        return attendanceRepository.findAll()
                .stream()
                .filter(attendance ->
                        Boolean.TRUE.equals(attendance.getLate()))
                .toList();
    }


    public List<Attendance> earlyLeavingEmployees() {

        return attendanceRepository.findAll()
                .stream()
                .filter(attendance ->
                        Boolean.TRUE.equals(attendance.getEarlyLeaving()))
                .toList();
    }


//=========================================
// OVERTIME REPORTS
//=========================================

    public List<OvertimeRequest> approvedOvertime() {

        return overtimeRequestRepository.findAll()
                .stream()
                .filter(overtime ->
                        "APPROVED".equalsIgnoreCase(overtime.getStatus()))
                .toList();
    }


    public List<OvertimeRequest> pendingOvertime() {

        return overtimeRequestRepository.findAll()
                .stream()
                .filter(overtime ->
                        "PENDING".equalsIgnoreCase(overtime.getStatus()))
                .toList();
    }


//=========================================
// REGULARIZATION REPORTS
//=========================================

    public List<AttendanceRegularization> pendingRegularization() {

        return attendanceRegularizationRepository.findAll()
                .stream()
                .filter(regularization ->
                        "PENDING".equalsIgnoreCase(
                                regularization.getStatus()))
                .toList();
    }


    public List<AttendanceRegularization> approvedRegularization() {

        return attendanceRegularizationRepository.findAll()
                .stream()
                .filter(regularization ->
                        "APPROVED".equalsIgnoreCase(
                                regularization.getStatus()))
                .toList();
    }


//=========================================
// TODAY ATTENDANCE
//=========================================

    public List<Attendance> todayAttendance() {

        LocalDate today = LocalDate.now();

        return attendanceRepository.findAll()
                .stream()
                .filter(attendance -> {

                    if (attendance.getLoginTime() == null) {
                        return false;
                    }

                    return attendance.getLoginTime()
                            .toLocalDate()
                            .equals(today);
                })
                .toList();
    }


//=========================================
// LIVE GPS TRACKING
//=========================================

    public List<GpsTracking> liveTracking() {

        return gpsTrackingRepository.findAll()
                .stream()
                .filter(gps ->
                        Boolean.TRUE.equals(gps.getTrackingStatus()) &&
                                gps.getLogoutLocation() == null)
                .toList();
    }


//=========================================
// GPS HISTORY
//=========================================

    public List<GpsTracking> gpsHistory(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return gpsTrackingRepository.findAll()
                .stream()
                .filter(gps ->
                        gps.getEmployee() != null &&
                                gps.getEmployee().getEmployeeId()
                                        .equals(employee.getEmployeeId()))
                .toList();
    }
    //=========================================
    // DASHBOARD
    //=========================================

    public AttendanceDashboardDTO getCounts() {

        LocalDate today = LocalDate.now();

        List<Attendance> allAttendance =
                attendanceRepository.findAll();

        List<OvertimeRequest> allOvertime =
                overtimeRequestRepository.findAll();

        List<AttendanceRegularization> allRegularizations =
                attendanceRegularizationRepository.findAll();

        List<GpsTracking> allGps =
                gpsTrackingRepository.findAll();

        // Total Employees
        long totalEmployees =
                employeeRepository.count();

        // Present Employees
        long presentEmployees =
                allAttendance.stream()
                        .filter(attendance ->
                                "PRESENT".equalsIgnoreCase(
                                        attendance.getAttendanceStatus()))
                        .filter(attendance ->
                                attendance.getLoginTime() != null &&
                                        attendance.getLoginTime()
                                                .toLocalDate()
                                                .equals(today))
                        .count();

        // Absent Employees
        long absentEmployees =
                allAttendance.stream()
                        .filter(attendance ->
                                "ABSENT".equalsIgnoreCase(
                                        attendance.getAttendanceStatus()))
                        .filter(attendance ->
                                attendance.getCreatedAt() != null &&
                                        attendance.getCreatedAt()
                                                .toLocalDate()
                                                .equals(today))
                        .count();

        // Late Employees
        long lateEmployees =
                allAttendance.stream()
                        .filter(attendance ->
                                Boolean.TRUE.equals(
                                        attendance.getLate()))
                        .filter(attendance ->
                                attendance.getLoginTime() != null &&
                                        attendance.getLoginTime()
                                                .toLocalDate()
                                                .equals(today))
                        .count();

        // Early Leaving Employees
        long earlyLeavingEmployees =
                allAttendance.stream()
                        .filter(attendance ->
                                Boolean.TRUE.equals(
                                        attendance.getEarlyLeaving()))
                        .filter(attendance ->
                                attendance.getLoginTime() != null &&
                                        attendance.getLoginTime()
                                                .toLocalDate()
                                                .equals(today))
                        .count();

        // Today's Attendance
        long todayAttendance =
                allAttendance.stream()
                        .filter(attendance ->
                                attendance.getLoginTime() != null &&
                                        attendance.getLoginTime()
                                                .toLocalDate()
                                                .equals(today))
                        .count();

        // Approved Overtime
        long approvedOvertime =
                allOvertime.stream()
                        .filter(overtime ->
                                "APPROVED".equalsIgnoreCase(
                                        overtime.getStatus()))
                        .count();

        // Pending Overtime
        long pendingOvertime =
                allOvertime.stream()
                        .filter(overtime ->
                                "PENDING".equalsIgnoreCase(
                                        overtime.getStatus()))
                        .count();

        // Pending Regularization
        long pendingRegularization =
                allRegularizations.stream()
                        .filter(regularization ->
                                "PENDING".equalsIgnoreCase(
                                        regularization.getStatus()))
                        .count();

        // Approved Regularization
        long approvedRegularization =
                allRegularizations.stream()
                        .filter(regularization ->
                                "APPROVED".equalsIgnoreCase(
                                        regularization.getStatus()))
                        .count();

        // Live GPS Tracking
        long liveGpsTracking =
                allGps.stream()
                        .filter(gps ->
                                "ACTIVE".equalsIgnoreCase(
                                        gps.getTrackingStatus()))
                        .filter(gps ->
                                gps.getLogoutLocation() == null)
                        .count();

        return new AttendanceDashboardDTO(
                totalEmployees,
                presentEmployees,
                absentEmployees,
                lateEmployees,
                earlyLeavingEmployees,
                todayAttendance,
                approvedOvertime,
                pendingOvertime,
                pendingRegularization,
                approvedRegularization,
                liveGpsTracking
        );
    }

}
