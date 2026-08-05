package com.HRMS.QuickDines.Attendance.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceDashboardDTO {

    private long totalEmployees;
    private long presentEmployees;
    private long absentEmployees;
    private long lateEmployees;
    private long earlyLeavingEmployees;

    private long todayAttendance;

    private long approvedOvertime;
    private long pendingOvertime;

    private long pendingRegularization;
    private long approvedRegularization;

    private long liveGpsTracking;
}
