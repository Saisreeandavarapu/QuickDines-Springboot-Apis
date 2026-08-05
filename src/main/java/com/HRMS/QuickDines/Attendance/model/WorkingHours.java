package com.HRMS.QuickDines.Attendance.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 10, scale = 2)
    private BigDecimal expectedHours;
    @Column(precision = 10, scale = 2)
    private BigDecimal completedHours;
    @Column(precision = 10, scale = 2)
    private BigDecimal overtimeHours;
    @Column(precision = 10, scale = 2)
    private BigDecimal breakHours;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
