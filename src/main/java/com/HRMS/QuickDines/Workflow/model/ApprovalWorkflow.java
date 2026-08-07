package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Workflow.Entity.WorkflowStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "approval_workflows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_name", nullable = false, length = 150)
    private String workflowName;

    @Column(name = "workflow_type", nullable = false, length = 100)
    private String workflowType;

    /*
     * FK -> companies.id
     *
     * Change this to @ManyToOne Company
     * if Company entity already exists.
     */
    @Column(name = "company_id")
    private Long companyId;

    /*
     * FK -> departments.id
     *
     * Change this to @ManyToOne Department
     * if Department entity already exists.
     */
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "total_levels", nullable = false)
    private Integer totalLevels;

    @Column(name = "auto_approve", nullable = false)
    @Builder.Default
    private Boolean autoApprove = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private WorkflowStatus status = WorkflowStatus.ACTIVE;

    /*
     * FK -> employees.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    /*
     * One Workflow can have multiple levels.
     */
    @OneToMany(
            mappedBy = "workflow",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("levelNumber ASC")
    @Builder.Default
    private List<ApprovalWorkflowLevel> levels = new ArrayList<>();

    /*
     * One Workflow can have multiple requests.
     */
    @OneToMany(
            mappedBy = "workflow",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<ApprovalRequest> approvalRequests = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = WorkflowStatus.ACTIVE;
        }

        if (autoApprove == null) {
            autoApprove = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    /*
     * Helper method
     */
    public void addLevel(ApprovalWorkflowLevel level) {

        levels.add(level);
        level.setWorkflow(this);
    }

    /*
     * Helper method
     */
    public void removeLevel(ApprovalWorkflowLevel level) {

        levels.remove(level);
        level.setWorkflow(null);
    }
}