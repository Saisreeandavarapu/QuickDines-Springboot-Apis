package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.Entity.AuditApprovalStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditStatus;
import com.HRMS.QuickDines.Sales.Entity.AuditType;
import com.HRMS.QuickDines.Sales.model.SalesAuditReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesAuditReportsRepository extends JpaRepository<SalesAuditReports, Long> {
    // =====================================================
    // EMPLOYEE
    // =====================================================

    List<SalesAuditReports> findByEmployee_EmployeeId(String employeeId);

    List<SalesAuditReports> findByEmployee_EmployeeIdAndAuditStatus(
            String employeeId,
            AuditStatus auditStatus
    );

    List<SalesAuditReports> findByEmployee_EmployeeIdAndAuditDate(
            String employeeId,
            LocalDate auditDate
    );
    List<SalesAuditReports>
    findByEmployee_EmployeeIdAndAuditDateGreaterThanEqual(
            String employeeId,
            LocalDate auditDate);
    List<SalesAuditReports> findByEmployee_EmployeeIdAndOverdue(
            String employeeId,
            Boolean overdue);

    // =====================================================
    // TYPE
    // =====================================================

    List<SalesAuditReports> findByAuditType(
            AuditType auditType
    );

    // =====================================================
    // RESTAURANT
    // =====================================================

    List<SalesAuditReports> findByRestaurant_Id(
            Long restaurantId
    );

    List<SalesAuditReports> findTopByRestaurant_IdOrderByAuditDateDesc(
            Long restaurantId
    );

    List<SalesAuditReports> findByRestaurant_IdOrderByAuditDateDesc(
            Long restaurantId
    );
    List<SalesAuditReports>
    findByRestaurant_IdAndOverdue(
            Long restaurantId,
            Boolean overdue);

    // =====================================================
    // BUS
    // =====================================================

    List<SalesAuditReports> findByBusService_Id(
            Long busId
    );

    List<SalesAuditReports> findTopByBusService_IdOrderByAuditDateDesc(
            Long busId
    );

    List<SalesAuditReports> findByBusService_IdOrderByAuditDateDesc(
            Long busId
    );
    List<SalesAuditReports>
    findByBusService_IdAndOverdue(
            Long busId,
            Boolean overdue);
    // =====================================================
    // STATUS
    // =====================================================

    List<SalesAuditReports> findByAuditStatus(
            AuditStatus status
    );

    List<SalesAuditReports> findByEmployee_EmployeeIdAndAuditStatusNot(
            String employeeId,
            AuditStatus status
    );

    // =====================================================
    // APPROVAL
    // =====================================================

    List<SalesAuditReports> findByApprovalStatus(
            AuditApprovalStatus approvalStatus
    );

    // =====================================================
    // OVERDUE
    // =====================================================

    List<SalesAuditReports> findByOverdueTrue();

    List<SalesAuditReports> findByEmployee_EmployeeIdAndOverdueTrue(
            String employeeId
    );

    List<SalesAuditReports> findByRestaurant_IdAndOverdueTrue(
            Long restaurantId
    );

    List<SalesAuditReports> findByBusService_IdAndOverdueTrue(
            Long busId
    );

    // =====================================================
    // DATE
    // =====================================================

    List<SalesAuditReports> findByAuditDate(
            LocalDate auditDate
    );

    // =====================================================
    // APPROVAL + STATUS
    // =====================================================

    List<SalesAuditReports> findByApprovalStatusAndAuditStatus(
            AuditApprovalStatus approvalStatus,
            AuditStatus auditStatus
    );
}
