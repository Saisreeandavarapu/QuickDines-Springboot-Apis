package com.HRMS.QuickDines.Event.repo;

import com.HRMS.QuickDines.Event.model.CalendarEvent;
import com.HRMS.QuickDines.Event.Entity.CalendarEventType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository
        extends JpaRepository<CalendarEvent, Long> {

    List<CalendarEvent> findByEventType(
            CalendarEventType eventType
    );

    List<CalendarEvent> findByStartDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
    SELECT DISTINCT ce
    FROM CalendarEvent ce
    JOIN ce.participants ms
    WHERE ms.employee.id = :employeeId
""")
    List<CalendarEvent> findEventsByEmployeeId(String employeeId);

    @Query("""
        SELECT DISTINCT ce
        FROM CalendarEvent ce
        JOIN MeetingSchedule ms
            ON ms.calendarEvent.id = ce.id
        WHERE ms.employee.id = :employeeId
        AND ce.startDateTime >= CURRENT_TIMESTAMP
        ORDER BY ce.startDateTime ASC
        """)
    List<CalendarEvent> findUpcomingEventsByEmployeeId(
            @Param("employeeId") String employeeId
    );

    @Query("""
        SELECT DISTINCT ce
        FROM CalendarEvent ce
        JOIN MeetingSchedule ms
            ON ms.calendarEvent.id = ce.id
        WHERE ms.employee.id = :employeeId
        AND ce.startDateTime >= :startDate
        AND ce.startDateTime < :endDate
        ORDER BY ce.startDateTime ASC
        """)
    List<CalendarEvent> findEventsByEmployeeAndDate(
            @Param("employeeId") String employeeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    default List<CalendarEvent> findEventsByEmployeeAndDate(
            String employeeId,
            LocalDate date
    ) {
        return findEventsByEmployeeAndDate(
                employeeId,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }
}