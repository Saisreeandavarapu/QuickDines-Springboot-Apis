package com.HRMS.QuickDines.Attendance.model;


import com.HRMS.QuickDines.Auth.model.Users;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Auth.model.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_shifts")
@Data
public class EmployeeShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private Employee assignedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
