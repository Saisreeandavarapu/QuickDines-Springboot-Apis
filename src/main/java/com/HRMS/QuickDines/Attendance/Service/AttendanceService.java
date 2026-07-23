package com.HRMS.QuickDines.Attendance.Service;

import com.HRMS.QuickDines.Attendance.model.Attendance;
import com.HRMS.QuickDines.Attendance.model.GpsTracking;
import com.HRMS.QuickDines.Attendance.model.WorkingHours;
import com.HRMS.QuickDines.Attendance.repo.AttendanceReportsRepository;
import com.HRMS.QuickDines.Attendance.repo.AttendanceRepository;
import com.HRMS.QuickDines.Attendance.repo.GpsTrackingRepository;
import com.HRMS.QuickDines.Attendance.repo.WorkingHoursRepository;
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

}
