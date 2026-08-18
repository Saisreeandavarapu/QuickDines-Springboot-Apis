package com.HRMS.QuickDines.Task.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimesheetRequest {

    private String employeeId;

    private LocalDate workDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer breakMinutes;

    private String projectName;

    private String workDescription;
}