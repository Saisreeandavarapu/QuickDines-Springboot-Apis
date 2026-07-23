package com.HRMS.QuickDines.Attendance.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@Data
public class AttendanceReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String month;

    private Integer presentDays;

    private Integer absentDays;

    private Integer paidLeaves;

    private Integer unpaidLeaves;

    private Integer halfDays;

    private Double totalWorkingHours;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
