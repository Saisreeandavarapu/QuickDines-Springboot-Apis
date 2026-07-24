package com.HRMS.QuickDines.Leave.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer numberOfDays;

    private String reason;

    private String status;

    private String approvedBy;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

}