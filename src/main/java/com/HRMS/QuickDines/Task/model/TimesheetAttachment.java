package com.HRMS.QuickDines.Task.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "timesheet_attachments")
public class TimesheetAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheet_id", nullable = false)
    private EmployeeTimesheet timesheet;


    private String fileName;

    private String fileUrl;

    private String fileType;

    private Long fileSize;


    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
