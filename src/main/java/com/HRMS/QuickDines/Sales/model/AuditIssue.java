package com.HRMS.QuickDines.Sales.model;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Sales.Entity.AuditApprovalStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditStatus;
import com.HRMS.QuickDines.Sales.model.Restaurant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_issues")
public class AuditIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_report_id", nullable = false)
    private AuditReports auditReport;

    private String issueTitle;

    @Column(length = 2000)
    private String description;

    private String severity;

    private String status;

    private LocalDate dueDate;

    private String evidenceUrl;

    @Column(length = 2000)
    private String resolutionRemarks;

    private LocalDate resolvedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
