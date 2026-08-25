package com.HRMS.QuickDines.Attendance.model;

import com.HRMS.QuickDines.Attendance.Entity.AttendanceStatus;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Organization.model.Department;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;
    @Column(precision = 10, scale = 2)
    private BigDecimal totalHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;
    private Boolean late;

    private Boolean earlyLeaving;


    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
