package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.Entity.AuditType;
import com.HRMS.QuickDines.Sales.model.AuditSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface AuditScheduleRepository extends JpaRepository<AuditSchedule, Long> {
    // =====================================================
    // EMPLOYEE
    // =====================================================

    List<AuditSchedule> findByEmployee_EmployeeId(
            String employeeId
    );
    List<AuditSchedule>
    findByEmployee_EmployeeIdAndActive(
            String employeeId,
            Boolean active);

    List<AuditSchedule> findByEmployee_EmployeeIdAndActiveTrue(
            String employeeId
    );

    List<AuditSchedule> findByEmployee_EmployeeIdAndActiveFalse(
            String employeeId
    );

    List<AuditSchedule> findByEmployee_EmployeeIdAndNextAuditDateGreaterThanEqual(
            String employeeId,
            LocalDate date
    );

    // =====================================================
    // RESTAURANT
    // =====================================================

    List<AuditSchedule> findByRestaurant_Id(
            Long restaurantId
    );

    // =====================================================
    // BUS
    // =====================================================

    List<AuditSchedule> findByBusService_Id(
            Long busId
    );

    // =====================================================
    // AUDIT TYPE
    // =====================================================

    List<AuditSchedule> findByTargetType(
            AuditType auditType
    );

    // =====================================================
    // SCHEDULE TYPE
    // =====================================================

    List<AuditSchedule> findByScheduleType(
            String scheduleType
    );

    List<AuditSchedule> findByScheduleTypeIgnoreCase(
            String scheduleType
    );

    // =====================================================
    // FREQUENCY
    // =====================================================

    List<AuditSchedule> findByFrequencyDays(
            Integer frequencyDays
    );

    // =====================================================
    // ACTIVE
    // =====================================================

    List<AuditSchedule> findByActiveTrue();

    List<AuditSchedule> findByActiveFalse();

    // =====================================================
    // UPCOMING
    // =====================================================

    List<AuditSchedule> findByNextAuditDateAfter(
            LocalDate date
    );
    List<AuditSchedule>
    findByNextAuditDateGreaterThanEqual(
            LocalDate date);

    // =====================================================
    // TODAY
    // =====================================================

    List<AuditSchedule> findByNextAuditDate(
            LocalDate date
    );

    // =====================================================
    // OVERDUE
    // =====================================================


    List<AuditSchedule>
    findByNextAuditDateBeforeAndActive(
            LocalDate date,
            Boolean active);
    // =====================================================
    // EMPLOYEE + UPCOMING
    // =====================================================

    List<AuditSchedule>
    findByEmployee_EmployeeIdAndNextAuditDateAfterAndActiveTrue(
            String employeeId,
            LocalDate date
    );

    // =====================================================
    // EMPLOYEE + OVERDUE
    // =====================================================

    List<AuditSchedule>
    findByEmployee_EmployeeIdAndNextAuditDateBeforeAndActiveTrue(
            String employeeId,
            LocalDate date
    );
}
