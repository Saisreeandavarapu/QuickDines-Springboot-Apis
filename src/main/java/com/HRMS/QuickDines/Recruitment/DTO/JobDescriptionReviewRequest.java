package com.HRMS.QuickDines.Recruitment.DTO;

import lombok.Data;

@Data
public class JobDescriptionReviewRequest {

    private boolean approved;

    private String reason;
}