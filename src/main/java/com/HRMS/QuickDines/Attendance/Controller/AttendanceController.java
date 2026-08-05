package com.HRMS.QuickDines.Attendance.Controller;

import com.HRMS.QuickDines.Attendance.DTO.AttendanceDashboardDTO;
import com.HRMS.QuickDines.Attendance.Service.AttendanceSchedulerService;
import com.HRMS.QuickDines.Attendance.Service.AttendanceService;
import com.HRMS.QuickDines.Attendance.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;
    private final AttendanceSchedulerService attendanceSchedulerService;
//1. One attendance record per employee per day.
//
//            2. Employee can check in only once per day.
//
//            3. Employee can check out only once per day.
//
//            4. Check-out updates the same attendance record.
//
//            5. Total working hours are calculated automatically.
//
//            6. Employee can view only their own attendance.
//
//7. Attendance status is automatically set to PRESENT during check-in.
//
//8. Scheduler will mark ABSENT, LEAVE, HOLIDAY, and WEEKEND when applicable.

    //---------------------------------
// EMPLOYEE ATTENDANCE APIs
//---------------------------------

    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<?> checkIn(
            @PathVariable String  employeeId) {

        return ResponseEntity.ok(
                service.checkIn(employeeId));
    }


    @PutMapping("/check-out/{employeeId}")
    public ResponseEntity<?> checkOut(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.checkOut(employeeId));
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getAttendance(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getAttendance(employeeId));
    }

///---------------------------------
// HR / ADMIN / SUPER ADMIN APIs
//---------------------------------

@GetMapping("/all")
public ResponseEntity<?> getAllAttendance() {
    return ResponseEntity.ok(service.getAllAttendance());
}


    @PutMapping("/update-attendance/{attendanceId}")
    public ResponseEntity<?> updateAttendance(
            @PathVariable Long attendanceId,
            @RequestBody Attendance attendance) {

        return ResponseEntity.ok(service.updateAttendance(attendanceId, attendance));
    }


    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long attendanceId) {
    return ResponseEntity.ok(service.deleteAttendance(attendanceId));
    }


//---------------------------------
// SYSTEM APIs (Scheduler)
//---------------------------------

    @PostMapping("/mark-absent")
    public ResponseEntity<?> markAbsentEmployees() {

        return ResponseEntity.ok(attendanceSchedulerService.markAbsentEmployees());
    }


    @PostMapping("/mark-holiday")
    public ResponseEntity<?> markHoliday() {

        return ResponseEntity.ok(attendanceSchedulerService.markHoliday());
    }


    @PostMapping("/mark-weekend")
    public ResponseEntity<?> markWeekend() {

        return ResponseEntity.ok(attendanceSchedulerService.markWeekend());
    }


    @PostMapping("/mark-leave/{employeeId}")
    public ResponseEntity<?> markLeave(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.markLeave(employeeId));
    }

    //---------------------------------
// DAILY ATTENDANCE REPORT APIs
//---------------------------------

    @PostMapping("/generate-daily-report")
    public ResponseEntity<?> generateDailyAttendanceReports() {

        attendanceSchedulerService.generateDailyAttendanceReports();

        return ResponseEntity.ok(
                "Daily Attendance Reports Generated Successfully");
    }



    //---------------------------------
// REPORTS
//---------------------------------

    @GetMapping("/reports/{employeeId}")
    public ResponseEntity<?> getReports(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getReports(employeeId));
    }


    @GetMapping("/monthly-report/{employeeId}")
    public ResponseEntity<?> getMonthlyReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getMonthlyReport(employeeId));
    }



    //---------------------------------
// WORKING HOURS
//---------------------------------

    @GetMapping("/working-hours/{employeeId}")
    public ResponseEntity<?> getWorkingHours(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getWorkingHours(employeeId));
    }


    @PutMapping("/working-hours/{employeeId}")
    public ResponseEntity<?> updateWorkingHours(
            @PathVariable String employeeId,
            @RequestBody WorkingHours workingHours) {

        return ResponseEntity.ok(
                service.updateWorkingHours(employeeId, workingHours));
    }

    //---------------------------------
// GPS TRACKING
//---------------------------------

    @PostMapping("/gps-login/{employeeId}")
    public ResponseEntity<?> gpsLogin(
            @PathVariable String employeeId,
            @RequestBody GpsTracking gpsTracking) {

        return ResponseEntity.ok(
                service.gpsLogin(employeeId, gpsTracking));
    }


    @PutMapping("/gps-logout/{employeeId}")
    public ResponseEntity<?> gpsLogout(
            @PathVariable String employeeId,
            @RequestBody GpsTracking gpsTracking) {

        return ResponseEntity.ok(
                service.gpsLogout(employeeId, gpsTracking));
    }


    @GetMapping("/gps/{employeeId}")
    public ResponseEntity<?> getGps(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getGps(employeeId));
    }


    @GetMapping("/live-location/{employeeId}")
    public ResponseEntity<?> liveLocation(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.liveLocation(employeeId));
    }

    @PostMapping("/shift")
    public ResponseEntity<?> createShift(@RequestBody Shift shift) {
        return ResponseEntity.ok(service.createShift(shift));
    }

    @GetMapping("/shifts")
    public ResponseEntity<?> getShifts() {
        return ResponseEntity.ok(service.getShifts());
    }

    @GetMapping("/shift/{id}")
    public ResponseEntity<?> getShift(@PathVariable Long id) {
        return ResponseEntity.ok(service.getShift(id));
    }

    @PutMapping("/shift/{id}")
    public ResponseEntity<?> updateShift(
            @PathVariable Long id,
            @RequestBody Shift shift) {
        return ResponseEntity.ok(service.updateShift(id, shift));
    }

    @DeleteMapping("/shift/{id}")
    public ResponseEntity<?> deleteShift(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteShift(id));
    }

    //=========================================================
    // EMPLOYEE SHIFTS
    //=========================================================

    @PostMapping("/employee-shift/{employeeId}")
    public ResponseEntity<?> assignShift(
            @PathVariable String employeeId,
            @RequestBody EmployeeShift employeeShift) {

        return ResponseEntity.ok(
                service.assignShift(employeeId, employeeShift));
    }

    @GetMapping("/employee-shifts")
    public ResponseEntity<?> getEmployeeShifts() {
        return ResponseEntity.ok(service.getEmployeeShifts());
    }

    @GetMapping("/employee-shift/{id}")
    public ResponseEntity<?> getEmployeeShift(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEmployeeShift(id));
    }

    @PutMapping("/employee-shift/{id}")
    public ResponseEntity<?> updateEmployeeShift(
            @PathVariable Long id,
            @RequestBody EmployeeShift employeeShift) {

        return ResponseEntity.ok(
                service.updateEmployeeShift(id, employeeShift));
    }

    @DeleteMapping("/employee-shift/{id}")
    public ResponseEntity<?> deleteEmployeeShift(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteEmployeeShift(id));
    }

    //=========================================================
    // HOLIDAYS
    //=========================================================

    @PostMapping("/holiday")
    public ResponseEntity<?> createHoliday(@RequestBody Holiday holiday) {
        return ResponseEntity.ok(service.createHoliday(holiday));
    }

    @GetMapping("/holidays")
    public ResponseEntity<?> getHolidays() {
        return ResponseEntity.ok(service.getHolidays());
    }

    @GetMapping("/holiday/{id}")
    public ResponseEntity<?> getHoliday(@PathVariable Long id) {
        return ResponseEntity.ok(service.getHoliday(id));
    }

    @PutMapping("/holiday/{id}")
    public ResponseEntity<?> updateHoliday(
            @PathVariable Long id,
            @RequestBody Holiday holiday) {

        return ResponseEntity.ok(service.updateHoliday(id, holiday));
    }

    @DeleteMapping("/holiday/{id}")
    public ResponseEntity<?> deleteHoliday(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteHoliday(id));
    }

    //=========================================================
    // WEEKEND CONFIGURATION
    //=========================================================

    @PostMapping("/weekend")
    public ResponseEntity<?> createWeekend(
            @RequestBody WeekendConfiguration weekend) {

        return ResponseEntity.ok(service.createWeekend(weekend));
    }

    @GetMapping("/weekends")
    public ResponseEntity<?> getWeekends() {
        return ResponseEntity.ok(service.getWeekends());
    }

    @GetMapping("/weekend/{id}")
    public ResponseEntity<?> getWeekend(@PathVariable Long id) {
        return ResponseEntity.ok(service.getWeekend(id));
    }

    @PutMapping("/weekend/{id}")
    public ResponseEntity<?> updateWeekend(
            @PathVariable Long id,
            @RequestBody WeekendConfiguration weekend) {

        return ResponseEntity.ok(service.updateWeekend(id, weekend));
    }

    @DeleteMapping("/weekend/{id}")
    public ResponseEntity<?> deleteWeekend(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteWeekend(id));
    }

    //=========================================================
    // ATTENDANCE REGULARIZATION
    //=========================================================

    @PostMapping("/regularization/{attendanceId}")
    public ResponseEntity<?> createRegularization(
            @PathVariable Long attendanceId,
            @RequestBody AttendanceRegularization regularization) {

        return ResponseEntity.ok(
                service.createRegularization(attendanceId, regularization));
    }

    @GetMapping("/regularizations")
    public ResponseEntity<?> getRegularizations() {
        return ResponseEntity.ok(service.getRegularizations());
    }

    @GetMapping("/regularization/{id}")
    public ResponseEntity<?> getRegularization(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRegularization(id));
    }

    @PutMapping("/regularization/{id}")
    public ResponseEntity<?> updateRegularization(
            @PathVariable Long id,
            @RequestBody AttendanceRegularization regularization) {

        return ResponseEntity.ok(
                service.updateRegularization(id, regularization));
    }

    @DeleteMapping("/regularization/{id}")
    public ResponseEntity<?> deleteRegularization(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteRegularization(id));
    }

    //=========================================================
    // OVERTIME REQUESTS
    //=========================================================

    @PostMapping("/overtime/{attendanceId}")
    public ResponseEntity<?> createOvertime(
            @PathVariable Long attendanceId,
            @RequestBody OvertimeRequest overtimeRequest) {

        return ResponseEntity.ok(
                service.createOvertime(attendanceId, overtimeRequest));
    }

    @GetMapping("/overtimes")
    public ResponseEntity<?> getOvertimes() {
        return ResponseEntity.ok(service.getOvertimes());
    }

    @GetMapping("/overtime/{id}")
    public ResponseEntity<?> getOvertime(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOvertime(id));
    }

    @PutMapping("/overtime/{id}")
    public ResponseEntity<?> updateOvertime(
            @PathVariable Long id,
            @RequestBody OvertimeRequest overtimeRequest) {

        return ResponseEntity.ok(
                service.updateOvertime(id, overtimeRequest));
    }

    @DeleteMapping("/overtime/{id}")
    public ResponseEntity<?> deleteOvertime(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteOvertime(id));
    }

//    //=========================================================
//    // ATTENDANCE
//    //=========================================================
//
//    @PostMapping("/checkin/{employeeId}")
//    public ResponseEntity<?> checkIn(
//            @PathVariable String employeeId,
//            @RequestParam Double latitude,
//            @RequestParam Double longitude) {
//
//        return ResponseEntity.ok(
//                service.checkIn(employeeId, latitude, longitude));
//    }
//
//    @PutMapping("/checkout/{attendanceId}")
//    public ResponseEntity<?> checkOut(
//            @PathVariable Long attendanceId,
//            @RequestParam Double latitude,
//            @RequestParam Double longitude) {
//
//        return ResponseEntity.ok(
//                service.checkOut(attendanceId, latitude, longitude));
//    }
//
//    @GetMapping("/attendance/{employeeId}")
//    public ResponseEntity<?> getEmployeeAttendance(
//            @PathVariable String employeeId) {
//
//        return ResponseEntity.ok(
//                service.getEmployeeAttendance(employeeId));
//    }
//
//    @GetMapping("/attendance")
//    public ResponseEntity<?> getAttendance() {
//        return ResponseEntity.ok(service.getAttendance());
//    }
//
//    @GetMapping("/attendance/details/{id}")
//    public ResponseEntity<?> getAttendanceById(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(
//                service.getAttendanceById(id));
//    }
//
//    @DeleteMapping("/attendance/{id}")
//    public ResponseEntity<?> deleteAttendance(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(
//                service.deleteAttendance(id));
//    }

    //=========================================================
    // REPORTS
    //=========================================================

    @GetMapping("/present")
    public ResponseEntity<?> presentEmployees() {
        return ResponseEntity.ok(service.presentEmployees());
    }

    @GetMapping("/absent")
    public ResponseEntity<?> absentEmployees() {
        return ResponseEntity.ok(service.absentEmployees());
    }

    @GetMapping("/late")
    public ResponseEntity<?> lateEmployees() {
        return ResponseEntity.ok(service.lateEmployees());
    }

    @GetMapping("/early-leaving")
    public ResponseEntity<?> earlyLeavingEmployees() {
        return ResponseEntity.ok(service.earlyLeavingEmployees());
    }

    @GetMapping("/overtime/approved")
    public ResponseEntity<?> approvedOvertime() {
        return ResponseEntity.ok(service.approvedOvertime());
    }

    @GetMapping("/overtime/pending")
    public ResponseEntity<?> pendingOvertime() {
        return ResponseEntity.ok(service.pendingOvertime());
    }

    @GetMapping("/regularization/pending")
    public ResponseEntity<?> pendingRegularization() {
        return ResponseEntity.ok(service.pendingRegularization());
    }

    @GetMapping("/regularization/approved")
    public ResponseEntity<?> approvedRegularization() {
        return ResponseEntity.ok(service.approvedRegularization());
    }

    @GetMapping("/today-attendance")
    public ResponseEntity<?> todayAttendance() {
        return ResponseEntity.ok(service.todayAttendance());
    }

    @GetMapping("/gps/live")
    public ResponseEntity<?> liveTracking() {
        return ResponseEntity.ok(service.liveTracking());
    }

    @GetMapping("/gps/history/{employeeId}")
    public ResponseEntity<?> gpsHistory(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.gpsHistory(employeeId));
    }

    //=========================================================
    // DASHBOARD
    //=========================================================

    @GetMapping("/counts")
    public AttendanceDashboardDTO getCounts() {
        return ResponseEntity.ok(service.getCounts()).getBody();
    }

//        //---------------------------------
//        // DAILY ATTENDANCE REPORT
//        //---------------------------------
//
//        @PostMapping("/generate-daily-report")
//        public ResponseEntity<?> generateDailyAttendanceReport() {
//
//            return ResponseEntity.ok(
//                    service.generateDailyAttendanceReport());
//        }
//
//
//        //---------------------------------
//        // MONTHLY ATTENDANCE REPORT
//        //---------------------------------
//
//        @PostMapping("/generate-monthly-report")
//        public ResponseEntity<?> generateMonthlyAttendanceReport() {
//
//            return ResponseEntity.ok(
//                    service.generateMonthlyAttendanceReport());
//        }
//
//
//        //---------------------------------
//        // GET REPORTS
//        //---------------------------------
//
//        @GetMapping("/reports")
//        public ResponseEntity<?> getAllReports() {
//
//            return ResponseEntity.ok(
//                    service.getAllReports());
//        }
//
//
//        @GetMapping("/reports/{employeeId}")
//        public ResponseEntity<?> getEmployeeReports(
//                @PathVariable Long employeeId) {
//
//            return ResponseEntity.ok(
//                    service.getEmployeeReports(employeeId));
//        }

    }



