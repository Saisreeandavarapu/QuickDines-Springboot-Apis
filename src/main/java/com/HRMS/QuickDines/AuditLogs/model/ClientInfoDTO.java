package com.HRMS.QuickDines.AuditLogs.model;

import lombok.Data;

@Data
public class ClientInfoDTO {
    private String ipAddress;
    private String browser;
    private String operatingSystem;
}
