package com.HRMS.QuickDines.Task.DTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimesheetTaskRequest {

    private String projectName;

    private String taskName;

    private String workDescription;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer breakMinutes;

    private String taskStatus;
}