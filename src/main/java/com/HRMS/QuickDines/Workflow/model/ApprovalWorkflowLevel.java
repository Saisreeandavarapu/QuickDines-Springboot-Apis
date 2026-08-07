package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "approval_workflow_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalWorkflowLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * FK -> approval_workflows.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workflow_id",
            nullable = false
    )
    private ApprovalWorkflow workflow;

    @Column(name = "level_number", nullable = false)
    private Integer levelNumber;

    @Column(name = "level_name", nullable = false, length = 100)
    private String levelName;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "approver_role_name")
    private Role approverType;

    /*
     * FK -> roles.id
     *
     * Used when:
     *
     * approverType = ROLE
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_role_id")
    private Role approverRole;

    /*
     * FK -> employees.id
     *
     * Used when:
     *
     * approverType = EMPLOYEE
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_employee_id")
    private Employee approverEmployee;

    @Column(name = "is_required", nullable = false)
    @Builder.Default
    private Boolean required = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private WorkflowStatus status = WorkflowStatus.ACTIVE;

    /*
     * One level can have multiple approval history records.
     */
    @OneToMany(
            mappedBy = "workflowLevel",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<ApprovalHistory> approvalHistory = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (required == null) {
            required = true;
        }

        if (status == null) {
            status = WorkflowStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}