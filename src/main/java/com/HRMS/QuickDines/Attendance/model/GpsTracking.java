package com.HRMS.QuickDines.Attendance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class GpsTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String latitude;

    private String longitude;

    private String loginLocation;

    private String logoutLocation;

    private String trackingStatus;

    private String deviceName;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

}
