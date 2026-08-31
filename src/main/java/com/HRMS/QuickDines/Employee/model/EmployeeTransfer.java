package com.HRMS.QuickDines.Employee.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Employee.Entity.EmployeeTransferStatus;
import com.HRMS.QuickDines.Organization.model.Department;
import com.HRMS.QuickDines.Organization.model.Team;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "employee_transfers")
public class EmployeeTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // EMPLOYEE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // =====================================================
    // FROM
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_department_id")
    private Department fromDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_branch_id")
    private Branch fromBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_team_id")
    private Team fromTeam;

    // =====================================================
    // TO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_department_id")
    private Department toDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_branch_id")
    private Branch toBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_team_id")
    private Team toTeam;

    // =====================================================
    // TRANSFER
    // =====================================================

    private LocalDate transferDate;

    @Column(length = 1000)
    private String reason;

    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeTransferStatus status;

    // =====================================================
    // MANAGER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    private LocalDateTime managerActionAt;

    @Column(length = 1000)
    private String managerRemarks;

    // =====================================================
    // HR
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private Employee hr;

    private LocalDateTime hrActionAt;

    @Column(length = 1000)
    private String hrRemarks;

    // =====================================================
    // SUPER ADMIN / MANAGEMENT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "management_approved_by")
    private Employee managementApprovedBy;

    private LocalDateTime managementApprovalAt;

    @Column(length = 1000)
    private String managementRemarks;

    // =====================================================
    // FINAL APPROVED BY
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    // =====================================================
    // AUDIT
    // =====================================================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}