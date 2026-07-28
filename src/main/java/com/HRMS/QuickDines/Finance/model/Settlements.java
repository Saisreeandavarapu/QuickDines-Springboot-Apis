package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settlements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Double settlementAmount;

    private String settlementType;

    private String settlementStatus;

    private String approvedBy;

    private LocalDate settlementDate;

    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
