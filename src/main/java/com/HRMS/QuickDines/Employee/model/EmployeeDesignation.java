package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Organization.model.Designation;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class EmployeeDesignation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    private Designation designation;
    private LocalDate promotedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_designation_id")
    private Designation previousDesignation;
    private String salaryGrade;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
