package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Data;

@Data
public class CheckInRequest {

    private String latitude;
    private String longitude;
    private String deviceName;
}
