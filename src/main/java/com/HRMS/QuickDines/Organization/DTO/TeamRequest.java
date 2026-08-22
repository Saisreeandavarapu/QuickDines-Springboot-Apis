package com.HRMS.QuickDines.Organization.DTO;

import lombok.Data;

@Data
public class TeamRequest {

    private String teamName;

    private String teamLead;

    private Integer numberOfMembers;

    private String description;

    private String status;

    private Long companyId;

    private Long branchId;

    private Long departmentId;
}