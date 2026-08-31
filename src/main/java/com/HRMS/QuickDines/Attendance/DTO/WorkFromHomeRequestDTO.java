package com.HRMS.QuickDines.Attendance.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkFromHomeRequestDTO {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;
}