package com.HRMS.QuickDines.Attendance.Controller;

import com.HRMS.QuickDines.Attendance.Service.AttendanceSchedulerService;
import com.HRMS.QuickDines.Attendance.Service.AttendanceService;
import com.HRMS.QuickDines.Attendance.model.Attendance;
import com.HRMS.QuickDines.Attendance.model.GpsTracking;
import com.HRMS.QuickDines.Attendance.model.WorkingHours;
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



