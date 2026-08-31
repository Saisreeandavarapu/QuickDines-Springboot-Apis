package com.HRMS.QuickDines.Recruitment.DTO;

import lombok.Data;

@Data
public class JobDescriptionRequest {

    private Long companyId;

    private Long jobOpeningId;

    private String title;

    private String description;

    private String responsibilities;

    private String qualifications;

    private String skills;
}