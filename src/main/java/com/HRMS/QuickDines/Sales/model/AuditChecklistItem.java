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
@Table(name = "audit_checklist_items")
public class AuditChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_report_id", nullable = false)
    private AuditReports auditReport;

    private String checklistName;

    private String status;

    @Column(length = 1000)
    private String remarks;

    private String photoUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
