package com.HRMS.QuickDines.Attendance.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double expectedHours;

    private Double completedHours;

    private Double overtimeHours;

    private Double breakHours;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
