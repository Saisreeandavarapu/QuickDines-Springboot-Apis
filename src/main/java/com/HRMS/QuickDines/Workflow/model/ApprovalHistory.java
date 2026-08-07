package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Workflow.Entity.ApprovalAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * FK -> approval_requests.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "approval_request_id",
            nullable = false
    )
    private ApprovalRequest approvalRequest;

    /*
     * FK -> approval_workflow_levels.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workflow_level_id",
            nullable = false
    )
    private ApprovalWorkflowLevel workflowLevel;

    /*
     * FK -> employees.id
     *
     * Employee who approved/rejected/returned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "approver_id",
            nullable = false
    )
    private Employee approver;

    /*
     * Level at which action happened.
     */
    @Column(
            name = "approval_level",
            nullable = false
    )
    private Integer approvalLevel;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "action",
            nullable = false,
            length = 30
    )
    private ApprovalAction action;

    @Column(
            name = "remarks",
            columnDefinition = "TEXT"
    )
    private String remarks;

    @Column(
            name = "action_date",
            nullable = false
    )
    private LocalDateTime actionDate;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (actionDate == null) {
            actionDate = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}