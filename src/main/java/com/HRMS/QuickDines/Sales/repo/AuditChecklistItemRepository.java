package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.model.AuditChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditChecklistItemRepository extends JpaRepository<AuditChecklistItem, Long> {
    // =====================================================
    // AUDIT
    // =====================================================

    List<AuditChecklistItem> findByAuditReport_Id(
            Long auditId
    );

    // =====================================================
    // STATUS
    // =====================================================

    List<AuditChecklistItem> findByStatus(
            String status
    );

    // =====================================================
    // AUDIT + STATUS
    // =====================================================

    List<AuditChecklistItem> findByAuditReport_IdAndStatus(
            Long auditId,
            String status
    );

    // =====================================================
    // CHECKLIST NAME
    // =====================================================

    List<AuditChecklistItem> findByChecklistNameIgnoreCase(
            String checklistName
    );
}
