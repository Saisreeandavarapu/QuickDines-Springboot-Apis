package com.HRMS.QuickDines.Attendance.DTO;
import lombok.Data;

@Data
public class AttendanceApprovalRequest {

    /*
     * APPROVE
     * REJECT
     */
    private String action;

    /*
     * Required when action = REJECT
     * Optional when action = APPROVE
     */
    private String reason;
}

