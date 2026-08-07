package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import com.HRMS.QuickDines.Workflow.model.ApprovalHistory;
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
     *
     * Example:
     *
     * Leave Approval Workflow
     *        |
     *        ├── Level 1
     *        ├── Level 2
     *        └── Level 3
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workflow_id",
            nullable = false
    )
    private ApprovalWorkflow workflow;


    /*
     * Approval level number.
     *
     * Example:
     *
     * 1 = Manager
     * 2 = HR
     * 3 = Admin
     */
    @Column(
            name = "level_number",
            nullable = false
    )
    private Integer levelNumber;


    /*
     * Name displayed for this approval level.
     *
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
     * The Role determines who can approve this level.
     *
     * Example:
     *
     * role_id = 2
     * role_name = HR
     *
     * Therefore:
     *
     * Level 2 -> HR role
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "approver_role_id",
            nullable = false
    )
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
     * One workflow level can have
     * multiple approval history records.
     */
    @OneToMany(
            mappedBy = "workflowLevel",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<ApprovalHistory> approvalHistory =
            new ArrayList<>();


    /*
     * Created date.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    /*
     * Updated date.
     */
    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;


    /*
     * Automatically executed before INSERT.
     */
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


    /*
     * Automatically executed before UPDATE.
     */
    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }


    /*
     * Helper method.
     */
    public void addApprovalHistory(
            ApprovalHistory history) {

        approvalHistory.add(history);

        history.setWorkflowLevel(this);
    }


    /*
     * Helper method.
     */
    public void removeApprovalHistory(
            ApprovalHistory history) {

        approvalHistory.remove(history);

        history.setWorkflowLevel(null);
    }
}