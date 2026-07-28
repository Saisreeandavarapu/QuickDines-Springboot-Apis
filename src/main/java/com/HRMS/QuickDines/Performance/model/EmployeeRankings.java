package com.HRMS.QuickDines.Performance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeRankings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rankPosition;

    private Integer departmentRank;

    private Double performancePercentage;

    private Integer rewardsPoints;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
