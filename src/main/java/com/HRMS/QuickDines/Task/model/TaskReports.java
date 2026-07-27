package com.HRMS.QuickDines.Task.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class TaskReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    private Integer completedTasks;

    private Integer pendingTasks;

    private Double performancePercentage;


    @CreationTimestamp
    private LocalDateTime createdAt;

}
