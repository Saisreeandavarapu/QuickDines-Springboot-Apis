package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckInResponse {

    // =====================================================
    // BASIC RESPONSE
    // =====================================================

    private String message;

    private String employeeId;

    private String employeeName;


    // =====================================================
    // ATTENDANCE
    // =====================================================

    private Long attendanceId;

    private String attendanceStatus;

    private LocalDateTime loginTime;

    private Boolean late;


    // =====================================================
    // SHIFT
    // =====================================================

    private String shiftName;

    private String shiftCode;

    private String shiftStartTime;

    private String shiftEndTime;


    // =====================================================
    // GPS TRACKING
    // =====================================================

    private Long gpsTrackingId;

    private String latitude;

    private String longitude;

    private String trackingStatus;

    private String deviceName;


    // =====================================================
    // MANAGER APPROVAL
    // =====================================================

    private Boolean approvalRequired;

    private Long approvalId;

    private String approvalStatus;

    private String managerId;

    private String managerName;
}