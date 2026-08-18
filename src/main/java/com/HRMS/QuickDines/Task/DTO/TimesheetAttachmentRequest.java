package com.HRMS.QuickDines.Task.DTO;

import lombok.Data;

@Data
public class TimesheetAttachmentRequest {

    private String fileName;

    private String fileUrl;

    private String fileType;

    private Long fileSize;
}