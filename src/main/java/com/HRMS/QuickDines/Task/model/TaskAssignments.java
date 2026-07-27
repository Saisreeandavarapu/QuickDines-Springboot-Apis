package com.HRMS.QuickDines.Task.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class TaskAssignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;


    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private Employee assignedBy;


    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;


    private LocalDate assignedDate;

    private String remarks;


    @CreationTimestamp
    private LocalDateTime createdAt;

}
