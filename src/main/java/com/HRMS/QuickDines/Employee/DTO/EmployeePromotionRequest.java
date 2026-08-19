package com.HRMS.QuickDines.Employee.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeePromotionRequest {

    private Long previousDesignationId;
    private Long newDesignationId;

    private BigDecimal previousSalary;
    private BigDecimal newSalary;

    private LocalDate promotionDate;

    private Long approvedBy;

    private String reason;
}