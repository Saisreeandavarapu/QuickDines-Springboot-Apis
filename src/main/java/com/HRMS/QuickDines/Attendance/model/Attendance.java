package com.HRMS.QuickDines.Attendance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private Double totalHours;

    private String attendanceStatus;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
