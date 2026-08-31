package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OvertimeApprovalRequest {

    /*
     * APPROVE
     * REJECT
     */
    private String action;

    /*
     * Required when REJECT
     * Optional when APPROVE
     */
    private String reason;

    /*
     * Required when APPROVE
     *
     * Manager can approve equal or
     * less than requested hours.
     */
    private BigDecimal approvedHours;
}