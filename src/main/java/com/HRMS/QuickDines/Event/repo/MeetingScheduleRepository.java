package com.HRMS.QuickDines.Event.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Event.model.MeetingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingScheduleRepository
        extends JpaRepository<MeetingSchedule, Long> {

    // =====================================================
    // FIND EMPLOYEES BY EVENT
    // =====================================================

    @Query("""
        SELECT ms.employee
        FROM MeetingSchedule ms
        WHERE ms.calendarEvent.id = :eventId
        """)
    List<Employee> findEmployeesByEventId(
            @Param("eventId") Long eventId
    );


    // =====================================================
    // FIND BY EMPLOYEE EMPLOYEE-ID
    // =====================================================

    List<MeetingSchedule> findByEmployee_EmployeeId(
            String employeeId
    );


    // =====================================================
    // FIND BY EMPLOYEE + INVITATION STATUS
    // =====================================================

    List<MeetingSchedule> findByEmployee_EmployeeIdAndInvitationStatus(
            String employeeId,
            com.HRMS.QuickDines.Event.Entity.InvitationStatus invitationStatus
    );


    // =====================================================
    // FIND PARTICIPANT BY EVENT + EMPLOYEE
    // =====================================================

    Optional<MeetingSchedule> findByCalendarEvent_IdAndEmployee_EmployeeId(
            Long eventId,
            String employeeId
    );


    // =====================================================
    // DELETE PARTICIPANT FROM EVENT
    // =====================================================

    @Modifying
    @Query("""
        DELETE FROM MeetingSchedule ms
        WHERE ms.calendarEvent.id = :eventId
        AND ms.employee.employeeId = :employeeId
        """)
    void deleteByEventIdAndEmployeeId(
            @Param("eventId") Long eventId,
            @Param("employeeId") String employeeId
    );
}