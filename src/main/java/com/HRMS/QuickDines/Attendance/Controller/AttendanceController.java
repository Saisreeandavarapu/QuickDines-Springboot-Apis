package com.HRMS.QuickDines.Attendance.Controller;

import com.HRMS.QuickDines.Attendance.DTO.AttendanceDashboardDTO;
import com.HRMS.QuickDines.Attendance.DTO.CheckInRequest;
import com.HRMS.QuickDines.Attendance.Entity.AttendanceStatus;
import com.HRMS.QuickDines.Attendance.Entity.OvertimeStatus;
import com.HRMS.QuickDines.Attendance.Service.AttendanceSchedulerService;
import com.HRMS.QuickDines.Attendance.Service.AttendanceService;
import com.HRMS.QuickDines.Attendance.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    private final AttendanceSchedulerService attendanceSchedulerService;


    // =========================================================
    // EMPLOYEE ATTENDANCE
    // =========================================================

    @PostMapping("/check-in/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_CHECK_IN')")
    public ResponseEntity<?> checkIn(
            @PathVariable String employeeId,
            @RequestBody CheckInRequest request) {

        return ResponseEntity.ok(
                service.checkIn(employeeId, request)
        );
    }


    @PutMapping("/check-out/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_CHECK_OUT')")
    public ResponseEntity<?> checkOut(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.checkOut(employeeId));
    }


    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<?> getAttendance(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getAttendance(employeeId));
    }


    // =========================================================
    // HR / ADMIN ATTENDANCE
    // =========================================================

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<?> getAllAttendance() {

        return ResponseEntity.ok(service.getAllAttendance());
    }


    @PutMapping("/update-attendance/{attendanceId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_UPDATE')")
    public ResponseEntity<?> updateAttendance(@PathVariable Long attendanceId, @RequestBody Attendance attendance) {

        return ResponseEntity.ok(service.updateAttendance(attendanceId, attendance));
    }


    @DeleteMapping("/{attendanceId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_DELETE')")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long attendanceId) {

        return ResponseEntity.ok(service.deleteAttendance(attendanceId));
    }


    // =========================================================
    // SYSTEM / SCHEDULER
    // =========================================================

    @PostMapping("/mark-absent")
    @PreAuthorize("hasAuthority('ATTENDANCE_SCHEDULER')")
    public ResponseEntity<?> markAbsentEmployees() {

        return ResponseEntity.ok(attendanceSchedulerService.markAbsentEmployees());
    }


    @PostMapping("/mark-holiday")
    @PreAuthorize("hasAuthority('ATTENDANCE_SCHEDULER')")
    public ResponseEntity<?> markHoliday() {

        return ResponseEntity.ok(attendanceSchedulerService.markHoliday());
    }


    @PostMapping("/mark-weekend")
    @PreAuthorize("hasAuthority('ATTENDANCE_SCHEDULER')")
    public ResponseEntity<?> markWeekend() {

        return ResponseEntity.ok(attendanceSchedulerService.markWeekend());
    }


    @PostMapping("/mark-leave/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_UPDATE')")
    public ResponseEntity<?> markLeave(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.markLeave(employeeId));
    }


    // =========================================================
    // DAILY ATTENDANCE REPORT
    // =========================================================

    @PostMapping("/generate-daily-report")
    @PreAuthorize("hasAuthority('ATTENDANCE_SCHEDULER')")
    public ResponseEntity<?> generateDailyAttendanceReports() {

        attendanceSchedulerService.generateDailyAttendanceReports();

        return ResponseEntity.ok("Daily Attendance Reports Generated Successfully");
    }


    // =========================================================
    // REPORTS
    // =========================================================

    @GetMapping("/reports/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> getReports(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getReports(employeeId));
    }


    @GetMapping("/monthly-report/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> getMonthlyReport(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getMonthlyReport(employeeId));
    }


    // =========================================================
    // WORKING HOURS
    // =========================================================

    @GetMapping("/working-hours/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_WORKING_HOURS_READ')")
    public ResponseEntity<?> getWorkingHours(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getWorkingHours(employeeId));
    }


    @PutMapping("/working-hours/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_WORKING_HOURS_UPDATE')")
    public ResponseEntity<?> updateWorkingHours(@PathVariable String employeeId, @RequestBody WorkingHours workingHours) {

        return ResponseEntity.ok(service.updateWorkingHours(employeeId, workingHours));
    }


    // =========================================================
    // GPS TRACKING
    // =========================================================

    @PostMapping("/gps-login/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_CREATE')")
    public ResponseEntity<?> gpsLogin(@PathVariable String employeeId, @RequestBody GpsTracking gpsTracking) {

        return ResponseEntity.ok(service.gpsLogin(employeeId, gpsTracking));
    }


    @PutMapping("/gps-logout/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_UPDATE')")
    public ResponseEntity<?> gpsLogout(@PathVariable String employeeId, @RequestBody GpsTracking gpsTracking) {

        return ResponseEntity.ok(service.gpsLogout(employeeId, gpsTracking));
    }


    @GetMapping("/gps/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_READ')")
    public ResponseEntity<?> getGps(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.getGps(employeeId));
    }


    @GetMapping("/live-location/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_READ')")
    public ResponseEntity<?> liveLocation(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.liveLocation(employeeId));
    }


    // =========================================================
    // SHIFT
    // =========================================================

    @PostMapping("/shift")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_CREATE')")
    public ResponseEntity<?> createShift(@RequestBody Shift shift) {

        return ResponseEntity.ok(service.createShift(shift));
    }


    @GetMapping("/shifts")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_READ')")
    public ResponseEntity<?> getShifts() {

        return ResponseEntity.ok(service.getShifts());
    }


    @GetMapping("/shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_READ')")
    public ResponseEntity<?> getShift(@PathVariable Long id) {

        return ResponseEntity.ok(service.getShift(id));
    }


    @PutMapping("/shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_UPDATE')")
    public ResponseEntity<?> updateShift(@PathVariable Long id, @RequestBody Shift shift) throws JsonProcessingException {

        return ResponseEntity.ok(service.updateShift(id, shift));
    }


    @DeleteMapping("/shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_DELETE')")
    public ResponseEntity<?> deleteShift(@PathVariable Long id) throws JsonProcessingException {

        return ResponseEntity.ok(service.deleteShift(id));
    }


    // =========================================================
    // EMPLOYEE SHIFTS
    // =========================================================

    @PostMapping("/employee-shift/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_CREATE')")
    public ResponseEntity<?> assignShift(@PathVariable String employeeId, @RequestBody EmployeeShift employeeShift) {

        return ResponseEntity.ok(service.assignShift(employeeId, employeeShift));
    }


    @GetMapping("/employee-shifts")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_READ')")
    public ResponseEntity<?> getEmployeeShifts() {

        return ResponseEntity.ok(service.getEmployeeShifts());
    }


    @GetMapping("/employee-shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_READ')")
    public ResponseEntity<?> getEmployeeShift(@PathVariable Long id) {

        return ResponseEntity.ok(service.getEmployeeShift(id));
    }


    @PutMapping("/employee-shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_UPDATE')")
    public ResponseEntity<?> updateEmployeeShift(@PathVariable Long id, @RequestBody EmployeeShift employeeShift) {

        return ResponseEntity.ok(service.updateEmployeeShift(id, employeeShift));
    }


    @DeleteMapping("/employee-shift/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_SHIFT_DELETE')")
    public ResponseEntity<?> deleteEmployeeShift(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteEmployeeShift(id));
    }


    // =========================================================
    // HOLIDAYS
    // =========================================================

    @PostMapping("/holiday")
    @PreAuthorize("hasAuthority('ATTENDANCE_HOLIDAY_CREATE')")
    public ResponseEntity<?> createHoliday(@RequestBody Holiday holiday) {

        return ResponseEntity.ok(service.createHoliday(holiday));
    }


    @GetMapping("/holidays")
    @PreAuthorize("hasAuthority('ATTENDANCE_HOLIDAY_READ')")
    public ResponseEntity<?> getHolidays() {

        return ResponseEntity.ok(service.getHolidays());
    }


    @GetMapping("/holiday/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_HOLIDAY_READ')")
    public ResponseEntity<?> getHoliday(@PathVariable Long id) {

        return ResponseEntity.ok(service.getHoliday(id));
    }


    @PutMapping("/holiday/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_HOLIDAY_UPDATE')")
    public ResponseEntity<?> updateHoliday(@PathVariable Long id, @RequestBody Holiday holiday) {

        return ResponseEntity.ok(service.updateHoliday(id, holiday));
    }


    @DeleteMapping("/holiday/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_HOLIDAY_DELETE')")
    public ResponseEntity<?> deleteHoliday(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteHoliday(id));
    }


    // =========================================================
    // WEEKEND CONFIGURATION
    // =========================================================

    @PostMapping("/weekend")
    @PreAuthorize("hasAuthority('ATTENDANCE_WEEKEND_CREATE')")
    public ResponseEntity<?> createWeekend(@RequestBody WeekendConfiguration weekend) {

        return ResponseEntity.ok(service.createWeekend(weekend));
    }


    @GetMapping("/weekends")
    @PreAuthorize("hasAuthority('ATTENDANCE_WEEKEND_READ')")
    public ResponseEntity<?> getWeekends() {

        return ResponseEntity.ok(service.getWeekends());
    }


    @GetMapping("/weekend/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_WEEKEND_READ')")
    public ResponseEntity<?> getWeekend(@PathVariable Long id) {

        return ResponseEntity.ok(service.getWeekend(id));
    }


    @PutMapping("/weekend/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_WEEKEND_UPDATE')")
    public ResponseEntity<?> updateWeekend(@PathVariable Long id, @RequestBody WeekendConfiguration weekend) {

        return ResponseEntity.ok(service.updateWeekend(id, weekend));
    }


    @DeleteMapping("/weekend/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_WEEKEND_DELETE')")
    public ResponseEntity<?> deleteWeekend(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteWeekend(id));
    }


    // =========================================================
    // ATTENDANCE REGULARIZATION
    // =========================================================

    @PostMapping("/regularization/{attendanceId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REGULARIZATION_CREATE')")
    public ResponseEntity<?> createRegularization(@PathVariable Long attendanceId, @RequestBody AttendanceRegularization regularization) {

        return ResponseEntity.ok(service.createRegularization(attendanceId, regularization));
    }


    @GetMapping("/regularizations")
    @PreAuthorize("hasAuthority('ATTENDANCE_REGULARIZATION_READ')")
    public ResponseEntity<?> getRegularizations() {

        return ResponseEntity.ok(service.getRegularizations());
    }


    @GetMapping("/regularization/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REGULARIZATION_READ')")
    public ResponseEntity<?> getRegularization(@PathVariable Long id) {

        return ResponseEntity.ok(service.getRegularization(id));
    }


    @PutMapping("/regularization/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REGULARIZATION_UPDATE')")
    public ResponseEntity<?> updateRegularization(@PathVariable Long id, @RequestBody AttendanceRegularization regularization) {

        return ResponseEntity.ok(service.updateRegularization(id, regularization));
    }


    @DeleteMapping("/regularization/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_REGULARIZATION_DELETE')")
    public ResponseEntity<?> deleteRegularization(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteRegularization(id));
    }


    // =========================================================
    // OVERTIME REQUESTS
    // =========================================================

    @PostMapping("/overtime/{attendanceId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_OVERTIME_CREATE')")
    public ResponseEntity<?> createOvertime(@PathVariable Long attendanceId, @RequestBody OvertimeRequest overtimeRequest) {

        return ResponseEntity.ok(service.createOvertime(attendanceId, overtimeRequest));
    }


    @GetMapping("/overtimes")
    @PreAuthorize("hasAuthority('ATTENDANCE_OVERTIME_READ')")
    public ResponseEntity<?> getOvertimes() {

        return ResponseEntity.ok(service.getOvertimes());
    }


    @GetMapping("/overtime/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_OVERTIME_READ')")
    public ResponseEntity<?> getOvertime(@PathVariable Long id) {

        return ResponseEntity.ok(service.getOvertime(id));
    }


    @PutMapping("/overtime/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_OVERTIME_UPDATE')")
    public ResponseEntity<?> updateOvertime(@PathVariable Long id, @RequestBody OvertimeRequest overtimeRequest) {

        return ResponseEntity.ok(service.updateOvertime(id, overtimeRequest));
    }


    @DeleteMapping("/overtime/{id}")
    @PreAuthorize("hasAuthority('ATTENDANCE_OVERTIME_DELETE')")
    public ResponseEntity<?> deleteOvertime(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteOvertime(id));
    }


    // =========================================================
    // ATTENDANCE REPORTS
    // =========================================================

    @GetMapping("/present")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> presentEmployees() {

        return ResponseEntity.ok(service.presentEmployees());
    }


    @GetMapping("/absent")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> absentEmployees() {

        return ResponseEntity.ok(service.absentEmployees());
    }


    @GetMapping("/late")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> lateEmployees() {

        return ResponseEntity.ok(service.lateEmployees());
    }


    @GetMapping("/early-leaving")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> earlyLeavingEmployees() {

        return ResponseEntity.ok(service.earlyLeavingEmployees());
    }


    @GetMapping("/overtime/approved")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> approvedOvertime() {

        return ResponseEntity.ok(service.approvedOvertime());
    }


    @GetMapping("/overtime/pending")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> pendingOvertime() {

        return ResponseEntity.ok(service.pendingOvertime());
    }


    @GetMapping("/regularization/pending")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> pendingRegularization() {

        return ResponseEntity.ok(service.pendingRegularization());
    }


    @GetMapping("/regularization/approved")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> approvedRegularization() {

        return ResponseEntity.ok(service.approvedRegularization());
    }


    @GetMapping("/today-attendance")
    @PreAuthorize("hasAuthority('ATTENDANCE_REPORT_READ')")
    public ResponseEntity<?> todayAttendance() {

        return ResponseEntity.ok(service.todayAttendance());
    }


    @GetMapping("/gps/live")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_READ')")
    public ResponseEntity<?> liveTracking() {

        return ResponseEntity.ok(service.liveTracking());
    }


    @GetMapping("/gps/history/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_GPS_READ')")
    public ResponseEntity<?> gpsHistory(@PathVariable String employeeId) {

        return ResponseEntity.ok(service.gpsHistory(employeeId));
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/counts")
    @PreAuthorize("hasAuthority('ATTENDANCE_DASHBOARD_READ')")
    public ResponseEntity<AttendanceDashboardDTO> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }

    // =========================================================
// ATTENDANCE FILTER
// =========================================================

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<?> filterAttendance(@RequestParam String period, @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, @RequestParam(required = false) String date) {

        return ResponseEntity.ok(service.filterAttendance(period, year, month, date));
    }

    @GetMapping("/filter/status")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<?> filterByStatus(@RequestParam AttendanceStatus status) {

        return ResponseEntity.ok(service.getAttendanceByStatus(status));
    }

    @GetMapping("/filter/shift")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<?> getEmployeesByShiftCode(@RequestParam String shiftCode) {

        return ResponseEntity.ok(service.getEmployeesByShiftCode(shiftCode));
    }

    @GetMapping("/overtime/filter/status")
    @PreAuthorize("hasAuthority('OVERTIME_READ')")
    public ResponseEntity<?> getOvertimeByStatus(@RequestParam OvertimeStatus status) {

        return ResponseEntity.ok(service.getOvertimeByStatus(status));
    }

    @GetMapping("/export/excel/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<byte[]> exportEmployeeAttendance(@PathVariable String employeeId) {

        byte[] excelFile = service.exportEmployeeAttendance(employeeId);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + employeeId + "-attendance-report.xlsx").contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(excelFile);
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    public ResponseEntity<byte[]> exportAttendanceExcel() {

        byte[] excelFile = service.exportAttendanceToExcel();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance-report.xlsx").contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(excelFile);
    }
}
