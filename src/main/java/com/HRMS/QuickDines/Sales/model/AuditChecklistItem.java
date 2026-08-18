package com.HRMS.QuickDines.Sales.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_checklist_items")
public class AuditChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_report_id", nullable = false)
    private SalesAuditReports auditReport;

    private String checklistName;

    private String status;

    @Column(length = 1000)
    private String remarks;

    private String photoUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
