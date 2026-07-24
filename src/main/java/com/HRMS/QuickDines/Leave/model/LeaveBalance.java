package com.HRMS.QuickDines.Leave.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalLeaves;

    private Integer usedLeaves;

    private Integer availableLeaves;

    private Integer paidLeaves;

    private Integer unpaidLeaves;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
