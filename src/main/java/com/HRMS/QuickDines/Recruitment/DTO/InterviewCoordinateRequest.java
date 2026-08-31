package com.HRMS.QuickDines.Recruitment.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewCoordinateRequest {

    private String interviewType;

    private String interviewerName;

    private LocalDateTime interviewDate;

    private String remarks;
}