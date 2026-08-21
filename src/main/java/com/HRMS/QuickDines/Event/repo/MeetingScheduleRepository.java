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
public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule,Long> {
    @Query("""
        SELECT ms.employee
        FROM MeetingSchedule ms
        WHERE ms.calendarEvent.id = :eventId
        """)
    List<Employee> findEmployeesByEventId(
            @Param("eventId") Long eventId
    );

    List<MeetingSchedule> findByEmployeeId(
            Long employeeId
    );

    List<MeetingSchedule> findByEmployeeIdAndStatus(
            String employeeId,
            String status
    );

    Optional<MeetingSchedule> findByEventIdAndEmployeeId(
            Long eventId,
            String employeeId
    );

    @Modifying
    @Query("""
        DELETE FROM MeetingSchedule ms
        WHERE ms.calendarEvent.id = :eventId
        AND ms.employee.id = :employeeId
        """)
    void deleteByEventIdAndEmployeeId(
            @Param("eventId") Long eventId,
            @Param("employeeId") String employeeId
    );
}
