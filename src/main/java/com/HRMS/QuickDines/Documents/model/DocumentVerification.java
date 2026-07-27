package com.HRMS.QuickDines.Documents.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DocumentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verifiedBy;

    private String verificationStatus;

    private String remarks;

    private LocalDate verifiedDate;


    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
