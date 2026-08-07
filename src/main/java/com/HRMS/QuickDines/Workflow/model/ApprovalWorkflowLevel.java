package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "approval_workflow_levels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_workflow_level",
                        columnNames = {"workflow_id", "level_number"}
                )
        }
)
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

    /*
     * 1, 2, 3...
     */
    @Column(
            name = "level_number",
            nullable = false
    )
    private Integer levelNumber;

    /*
     * Example:
     *
     * Manager Approval
     * HR Approval
     * Admin Approval
     */
    @Column(
            name = "level_name",
            nullable = false,
            length = 100
    )
    private String levelName;

    /*
     * FK -> roles.id
     *
     * Example:
     *
     * role.id = 2
     * role.roleName = MANAGER
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_role_id")
    private Role approverRole;

    /*
     * Whether this approval level is mandatory.
     */
    @Column(
            name = "is_required",
            nullable = false
    )
    @Builder.Default
    private Boolean required = true;

    /*
     * ACTIVE / INACTIVE
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
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
    private List<ApprovalHistory> approvalHistory =
            new ArrayList<>();

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