package com.HRMS.QuickDines.Attendance.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shifts")
@Data
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shiftName;

    @Column(unique = true)
    private String shiftCode;
//    GENERAL
//            MORNING
//    EVENING
//            NIGHT

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    private Integer graceTime;
    @Column(precision = 10, scale = 2)
    private BigDecimal workingHours;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL)
    private List<EmployeeShift> employeeShifts;

}