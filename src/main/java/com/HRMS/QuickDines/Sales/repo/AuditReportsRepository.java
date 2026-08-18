package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.Entity.AuditApprovalStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditType;
import com.HRMS.QuickDines.Sales.model.AuditChecklistItem;
import com.HRMS.QuickDines.Sales.model.AuditReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuditReportsRepository extends JpaRepository<AuditReports, Long> {
    // =====================================================
    // EMPLOYEE
    // =====================================================

    List<AuditReports> findByEmployee_EmployeeId(String employeeId);

    List<AuditReports> findByEmployee_EmployeeIdAndAuditStatus(
            String employeeId,
            AuditStatus auditStatus
    );

    List<AuditReports> findByEmployee_EmployeeIdAndAuditDate(
            String employeeId,
            LocalDate auditDate
    );
    List<AuditReports>
    findByEmployee_EmployeeIdAndAuditDateGreaterThanEqual(
            String employeeId,
            LocalDate auditDate);
    List<AuditReports> findByEmployee_EmployeeIdAndOverdue(
            String employeeId,
            Boolean overdue);

    // =====================================================
    // TYPE
    // =====================================================

    List<AuditReports> findByAuditType(
            AuditType auditType
    );

    // =====================================================
    // RESTAURANT
    // =====================================================

    List<AuditReports> findByRestaurant_Id(
            Long restaurantId
    );

    List<AuditReports> findTopByRestaurant_IdOrderByAuditDateDesc(
            Long restaurantId
    );

    List<AuditReports> findByRestaurant_IdOrderByAuditDateDesc(
            Long restaurantId
    );
    List<AuditReports>
    findByRestaurant_IdAndOverdue(
            Long restaurantId,
            Boolean overdue);

    // =====================================================
    // BUS
    // =====================================================

    List<AuditReports> findByBusService_Id(
            Long busId
    );

    List<AuditReports> findTopByBusService_IdOrderByAuditDateDesc(
            Long busId
    );

    List<AuditReports> findByBusService_IdOrderByAuditDateDesc(
            Long busId
    );
    List<AuditReports>
    findByBusService_IdAndOverdue(
            Long busId,
            Boolean overdue);
    // =====================================================
    // STATUS
    // =====================================================

    List<AuditReports> findByAuditStatus(
            AuditStatus status
    );

    List<AuditReports> findByEmployee_EmployeeIdAndAuditStatusNot(
            String employeeId,
            AuditStatus status
    );

    // =====================================================
    // APPROVAL
    // =====================================================

    List<AuditReports> findByApprovalStatus(
            AuditApprovalStatus approvalStatus
    );

    // =====================================================
    // OVERDUE
    // =====================================================

    List<AuditReports> findByOverdueTrue();

    List<AuditReports> findByEmployee_EmployeeIdAndOverdueTrue(
            String employeeId
    );

    List<AuditReports> findByRestaurant_IdAndOverdueTrue(
            Long restaurantId
    );

    List<AuditReports> findByBusService_IdAndOverdueTrue(
            Long busId
    );

    // =====================================================
    // DATE
    // =====================================================

    List<AuditReports> findByAuditDate(
            LocalDate auditDate
    );

    // =====================================================
    // APPROVAL + STATUS
    // =====================================================

    List<AuditReports> findByApprovalStatusAndAuditStatus(
            AuditApprovalStatus approvalStatus,
            AuditStatus auditStatus
    );
}
