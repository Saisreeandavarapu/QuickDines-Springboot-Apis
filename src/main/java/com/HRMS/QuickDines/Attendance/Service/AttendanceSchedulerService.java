package com.HRMS.QuickDines.Attendance.Service;

import com.HRMS.QuickDines.Attendance.model.Attendance;
import com.HRMS.QuickDines.Attendance.model.AttendanceReports;
import com.HRMS.QuickDines.Attendance.repo.AttendanceReportsRepository;
import com.HRMS.QuickDines.Attendance.repo.AttendanceRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AttendanceSchedulerService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    //private final LeaveRepository leaveRepository;
    private final AttendanceReportsRepository attendanceReportsRepository;


    // Every day at 07:00 PM
    @Scheduled(cron = "0 0 19 * * ?")
    public String markAbsentEmployees() {
        LocalDate today = LocalDate.now();
        // Fetch all active employees
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {

            // Check whether today's attendance already exists
            boolean attendanceExists = attendanceRepository.existsByEmployeeAndCreatedAtBetween(
                    employee,
                    today.atStartOfDay(),
                    today.atTime(23, 59, 59));

            // If attendance is already marked, skip
            if (attendanceExists) {
                continue;
            }

            // Create attendance record
            Attendance attendance = new Attendance();

            attendance.setEmployee(employee);
            attendance.setAttendanceStatus("ABSENT");
            attendance.setRemarks(
                    "Employee did not mark attendance.");
            attendance.setTotalHours(0.0);

            // Save attendance
            attendanceRepository.save(attendance);

        }
        return "Attendance Marked Successfully";
    }


    // Every day at 12:10 AM
    @Scheduled(cron = "0 10 0 * * ?")
    public String markHoliday() {
//        LocalDate today = LocalDate.now();
//        // Check today's holiday
//        Optional<Holiday> holiday = holidayRepository.findByHolidayDate(today);
//        // If today is not a holiday
//        if (holiday.isEmpty()) {
//            return;
//        }
//        // Fetch all employees
//        List<Employee> employees = employeeRepository.findAll();
//
//
//        // Create attendance records
//        for (Employee employee : employees) {
//
//            boolean exists = attendanceRepository.existsByEmployeeAndCreatedAtBetween(
//                                    employee,
//                                    today.atStartOfDay(),
//                                    today.atTime(23, 59, 59));
//
//            // Skip if attendance already exists
//            if (exists) {
//                continue;
//            }
//
//
//            Attendance attendance = new Attendance();
//
//            attendance.setEmployee(employee);
//            attendance.setAttendanceStatus("HOLIDAY");
//            attendance.setRemarks(
//                    holiday.get().getHolidayName());
//
//            attendance.setTotalHours(0.0);
//
////            attendanceRepository.save(attendance);
//      return "";
//    }
        return "Attendance Marked Successfully";
   }


    // Runs every day at 12:05 AM
    @Scheduled(cron = "0 5 0 * * ?")
    public String markWeekend() {

        // Get today's day
        DayOfWeek day = LocalDate.now().getDayOfWeek();

        // Check whether it is Saturday or Sunday
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {

            // Fetch all employees
            List<Employee> employees = employeeRepository.findAll();

            for (Employee employee : employees) {

                // Check whether attendance already exists
                boolean exists = attendanceRepository.existsByEmployeeAndCreatedAtBetween(employee, LocalDate.now().atStartOfDay(),
                        LocalDate.now().atTime(23, 59, 59));

                // Create attendance only if not present
                if (!exists) {
                    Attendance attendance = new Attendance();

                    attendance.setEmployee(employee);
                    attendance.setAttendanceStatus("WEEKEND");
                    attendance.setRemarks("Weekend");
                    attendance.setTotalHours(0.0);

                    attendanceRepository.save(attendance);
                }
            }
        }
        return "Attendance Marked Successfully";
    }


    // Every day at 12:15 AM
    @Scheduled(cron = "0 15 0 * * ?")
    public String markLeave( String EmployeeId) {
        return "Attendance Marked Successfully";
    }


    //--------------------------------------------------
// DAILY ATTENDANCE REPORT GENERATION
//--------------------------------------------------

// Every day at 11:00 PM

    @Scheduled(cron = "0 0 23 * * ?")
    public String generateDailyAttendanceReports() {

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {

            List<Attendance> attendances =
                    attendanceRepository.findByEmployee(employee);

            int presentDays = 0;
            int absentDays = 0;
            int paidLeaves = 0;
            int unpaidLeaves = 0;
            double totalWorkingHours = 0.0;

            for (Attendance attendance : attendances) {

                if ("PRESENT".equalsIgnoreCase(attendance.getAttendanceStatus())) {
                    presentDays++;
                    if (attendance.getTotalHours() != null) {
                        totalWorkingHours += attendance.getTotalHours();
                    }
                } else if ("ABSENT".equalsIgnoreCase(attendance.getAttendanceStatus())) {
                    absentDays++;
                } else if ("PAID_LEAVE".equalsIgnoreCase(attendance.getAttendanceStatus())) {
                    paidLeaves++;
                } else if ("UNPAID_LEAVE".equalsIgnoreCase(attendance.getAttendanceStatus())) {
                    unpaidLeaves++;
                }
            }
            AttendanceReports report = new AttendanceReports();

            report.setEmployee(employee);
            report.setMonth(YearMonth.now().toString());
            report.setPresentDays(presentDays);
            report.setAbsentDays(absentDays);
            report.setPaidLeaves(paidLeaves);
            report.setUnpaidLeaves(unpaidLeaves);
            report.setTotalWorkingHours(totalWorkingHours);
            attendanceReportsRepository.save(report);

        }
        return "Attendance Report Saved Successfully";

    }
}



