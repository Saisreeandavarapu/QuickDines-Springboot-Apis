package com.HRMS.QuickDines.Leave.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class LeaveApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate approvedDate;

    private String rejectionReason;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "leave_request_id")
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

}
