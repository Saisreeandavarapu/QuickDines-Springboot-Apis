package com.HRMS.QuickDines.Leave.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "leave_approvals")
public class LeaveApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate approvedDate;

    private String rejectionReason;

    /*
     * PENDING
     * APPROVED
     * REJECTED
     */
    private String status;

    /*
     * HR
     * SALES_MANAGER
     * SUPER_ADMIN
     */
    private String approvalRole;

    /*
     * 1 = first approval
     * 2 = second approval
     */
    private Integer approvalOrder;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "leave_request_id",
            nullable = false
    )
    private LeaveRequest leaveRequest;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;
}