package com.HRMS.QuickDines.Workflow.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Workflow.Entity.ApprovalRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "approval_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalRequest {

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
     * FK -> employees.id
     *
     * Employee who submitted the request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_employee_id")
    private Employee approverEmployee;

    /*
     * LEAVE
     * EXPENSE
     * LOAN
     * etc.
     */
    @Column(
            name = "request_type",
            nullable = false,
            length = 100
    )
    private String requestType;

    /*
     * ID of the actual business record.
     *
     * Example:
     *
     * requestType = LEAVE
     * referenceId = 500
     *
     * Then:
     *
     * leave_requests.id = 500
     */
    @Column(
            name = "reference_id",
            nullable = false
    )
    private Long referenceId;

    /*
     * Current workflow level.
     *
     * Example:
     *
     * 1 = Manager
     * 2 = HR
     * 3 = Admin
     */
    @Column(
            name = "current_level",
            nullable = false
    )
    @Builder.Default
    private Integer currentLevel = 1;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private ApprovalRequestStatus status =
            ApprovalRequestStatus.PENDING;

    @Column(
            name = "remarks",
            columnDefinition = "TEXT"
    )
    private String remarks;

    @Column(
            name = "submitted_date",
            nullable = false
    )
    private LocalDateTime submittedDate;

    /*
     * One request can have multiple history records.
     */
    @OneToMany(
            mappedBy = "approvalRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("actionDate ASC")
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

        if (submittedDate == null) {
            submittedDate = now;
        }

        if (currentLevel == null) {
            currentLevel = 1;
        }

        if (status == null) {
            status = ApprovalRequestStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    /*
     * Helper method
     */
    public void addApprovalHistory(ApprovalHistory history) {

        approvalHistory.add(history);
        history.setApprovalRequest(this);
    }

    /*
     * Helper method
     */
    public void removeApprovalHistory(ApprovalHistory history) {

        approvalHistory.remove(history);
        history.setApprovalRequest(null);
    }
}