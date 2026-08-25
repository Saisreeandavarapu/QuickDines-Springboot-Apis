package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckInResponse {

    private String message;

    private String employeeId;
    private String employeeName;

    private Long attendanceId;

    private String attendanceStatus;

    private LocalDateTime loginTime;

    private Boolean late;

    private String shiftName;
    private String shiftCode;

    private String shiftStartTime;
    private String shiftEndTime;

    private Long gpsTrackingId;

    private String latitude;
    private String longitude;

    private String trackingStatus;
    private String deviceName;
}