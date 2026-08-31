package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeShiftRequest {

    private Long shiftId;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isCurrent;

    private String assignedBy;
}