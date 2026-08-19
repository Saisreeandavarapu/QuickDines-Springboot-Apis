package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

@Data
public class EmployeeLanguageRequest {

    private String languageName;
    private String readLevel;
    private String writeLevel;
    private String speakLevel;
    private Boolean nativeLanguage;
}