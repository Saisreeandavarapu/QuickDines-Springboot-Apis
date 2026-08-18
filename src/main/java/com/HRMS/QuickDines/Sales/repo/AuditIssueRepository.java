package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.model.AuditIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuditIssueRepository extends JpaRepository<AuditIssue, Long> {

    // =====================================================
    // AUDIT
    // =====================================================

    List<AuditIssue> findByAuditReport_Id(
            Long auditId
    );

    // =====================================================
    // STATUS
    // =====================================================

    List<AuditIssue> findByStatus(
            String status
    );

    // =====================================================
    // SEVERITY
    // =====================================================

    List<AuditIssue> findBySeverity(
            String severity
    );

    // =====================================================
    // OPEN
    // =====================================================

    List<AuditIssue> findByStatusIgnoreCase(
            String status
    );

    // =====================================================
    // DUE DATE
    // =====================================================

    List<AuditIssue> findByDueDateBefore(
            LocalDate date
    );

    // =====================================================
    // EMPLOYEE
    // =====================================================

    List<AuditIssue>
    findByAuditReport_Employee_EmployeeId(
            String employeeId
    );

    // =====================================================
    // RESTAURANT
    // =====================================================

    List<AuditIssue>
    findByAuditReport_Restaurant_Id(
            Long restaurantId
    );

    // =====================================================
    // BUS
    // =====================================================

    List<AuditIssue>
    findByAuditReport_BusService_Id(
            Long busId
    );

    // =====================================================
    // EMPLOYEE + STATUS
    // =====================================================

    List<AuditIssue>
    findByAuditReport_Employee_EmployeeIdAndStatus(
            String employeeId,
            String status
    );

    // =====================================================
    // RESTAURANT + STATUS
    // =====================================================

    List<AuditIssue>
    findByAuditReport_Restaurant_IdAndStatus(
            Long restaurantId,
            String status
    );

    // =====================================================
    // BUS + STATUS
    // =====================================================

    List<AuditIssue>
    findByAuditReport_BusService_IdAndStatus(
            Long busId,
            String status
    );
    List<AuditIssue>
    findByDueDateBeforeAndStatusNot(
            LocalDate dueDate,
            String status);
}
