package com.HRMS.QuickDines.Event.Service;

import com.HRMS.QuickDines.Event.Entity.CalendarEventStatus;
import com.HRMS.QuickDines.Event.Entity.CalendarEventType;
import com.HRMS.QuickDines.Event.Entity.EventAttendanceStatus;
import com.HRMS.QuickDines.Event.Entity.InvitationStatus;
import com.HRMS.QuickDines.Event.model.CalendarEvent;
import com.HRMS.QuickDines.Event.repo.CalendarEventRepository;
import com.HRMS.QuickDines.Event.repo.MeetingScheduleRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {

    private final CalendarEventRepository calendarEventRepository;
    private final MeetingScheduleRepository meetingScheduleRepository;
    private final EmployeeRepository employeeRepository;

    // =========================================================
    // CREATE EVENT
    // =========================================================

    public CalendarEvent createEvent(CalendarEvent event) {

        return calendarEventRepository.save(event);
    }

    // =========================================================
    // GET ALL EVENTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getAllEvents() {

        return calendarEventRepository.findAll();
    }

    // =========================================================
    // GET EVENT BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public CalendarEvent getEventById(Long eventId) {

        return calendarEventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Calendar event not found with id: " + eventId));
    }

    // =========================================================
    // UPDATE EVENT
    // =========================================================

    public CalendarEvent updateEvent(Long eventId, CalendarEvent updatedEvent) {

        CalendarEvent existingEvent = calendarEventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Calendar event not found with id: " + eventId));

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setEventType(updatedEvent.getEventType());
        existingEvent.setStartDateTime(updatedEvent.getStartDateTime());
        existingEvent.setEndDateTime(updatedEvent.getEndDateTime());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setStatus(updatedEvent.getStatus());

        return calendarEventRepository.save(existingEvent);
    }

    // =========================================================
    // DELETE EVENT
    // =========================================================

    public void deleteEvent(Long eventId) {

        CalendarEvent event = getEventById(eventId);

        calendarEventRepository.delete(event);
    }

    // =========================================================
    // EVENTS BY DATE
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getEventsByDate(LocalDate date) {

        return calendarEventRepository.findByStartDateTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    // =========================================================
    // EVENTS BY DATE RANGE
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {

        return calendarEventRepository.findByStartDateTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    // =========================================================
    // EVENTS BY TYPE
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getEventsByType(CalendarEventType eventType) {

        return calendarEventRepository.findByEventType(eventType);
    }

    // =========================================================
    // MY EVENTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getMyEvents() {

        Employee employee = getLoggedInEmployee();

        return calendarEventRepository.findEventsByEmployeeId(employee.getEmployeeId());
    }

    // =========================================================
    // MY UPCOMING EVENTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<CalendarEvent> getMyUpcomingEvents() {

        Employee employee = getLoggedInEmployee();

        return calendarEventRepository.findUpcomingEventsByEmployeeId(employee.getEmployeeId());
    }

    // =========================================================
    // ADD SINGLE PARTICIPANT
    // =========================================================

    public Object addParticipant(Long eventId, String employeeId) {

        CalendarEvent event = getEventById(eventId);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        /*
         * Save participant through MeetingSchedule.
         *
         * Your MeetingSchedule entity should contain:
         *
         * CalendarEvent calendarEvent;
         * Employee employee;
         */

        // meetingScheduleRepository.save(...);

        return employee;
    }

    // =========================================================
    // ADD MULTIPLE PARTICIPANTS
    // =========================================================

    public List<Employee> addParticipants(Long eventId, List<String> employeeIds) {

        getEventById(eventId);

        List<Employee> employees = employeeRepository.findByEmployeeIdIn(employeeIds);

        /*
         * Create MeetingSchedule records here
         * for every employee.
         */

        return employees;
    }

    // =========================================================
    // GET PARTICIPANTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Employee> getParticipants(Long eventId) {

        getEventById(eventId);

        return meetingScheduleRepository.findEmployeesByEventId(eventId);
    }

    // =========================================================
    // REMOVE PARTICIPANT
    // =========================================================

    public void removeParticipant(Long eventId, String employeeId) {

        getEventById(eventId);

        meetingScheduleRepository.deleteByEventIdAndEmployeeId(eventId, employeeId);
    }

    // =========================================================
    // MY INVITATIONS
    // =========================================================

    @Transactional(readOnly = true)
    public Object getMyInvitations() {

        Employee employee = getLoggedInEmployee();

        return meetingScheduleRepository.findByEmployee_EmployeeId(employee.getEmployeeId());
    }

    // =========================================================
    // ACCEPT INVITATION
    // =========================================================

    public Object acceptInvitation(Long eventId) {

        Employee employee = getLoggedInEmployee();

        var schedule = meetingScheduleRepository.findByCalendarEvent_IdAndEmployee_EmployeeId(eventId, employee.getEmployeeId()).orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        schedule.setAttendanceStatus(EventAttendanceStatus.valueOf("ACCEPTED"));

        return meetingScheduleRepository.save(schedule);
    }

    // =========================================================
    // DECLINE INVITATION
    // =========================================================

    public Object declineInvitation(Long eventId) {

        Employee employee = getLoggedInEmployee();

        var schedule = meetingScheduleRepository.findByCalendarEvent_IdAndEmployee_EmployeeId(eventId, employee.getEmployeeId()).orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        schedule.setAttendanceStatus(EventAttendanceStatus.valueOf("DECLINED"));

        return meetingScheduleRepository.save(schedule);
    }

    // =========================================================
    // CANCEL EVENT
    // =========================================================

    public CalendarEvent cancelEvent(Long eventId) {

        CalendarEvent event = getEventById(eventId);

        event.setStatus(CalendarEventStatus.valueOf("CANCELLED"));

        return calendarEventRepository.save(event);
    }

    // =========================================================
    // COMPLETE EVENT
    // =========================================================

    public CalendarEvent completeEvent(Long eventId) {

        CalendarEvent event = getEventById(eventId);

        event.setStatus(CalendarEventStatus.valueOf("COMPLETED"));

        return calendarEventRepository.save(event);
    }

    // =========================================================
    // CALENDAR DASHBOARD
    // =========================================================

    @Transactional(readOnly = true)
    public Map<String, Object> getCalendarDashboard() {

        Employee employee = getLoggedInEmployee();

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("todayEvents", calendarEventRepository.findEventsByEmployeeAndDate(employee.getEmployeeId(), LocalDate.now()));

        dashboard.put("upcomingEvents", calendarEventRepository.findUpcomingEventsByEmployeeId(employee.getEmployeeId()));

        dashboard.put("invitations", meetingScheduleRepository.findByEmployee_EmployeeIdAndInvitationStatus(employee.getEmployeeId(), InvitationStatus.valueOf("PENDING")));

        return dashboard;
    }

    // =========================================================
    // GET LOGGED-IN EMPLOYEE
    // =========================================================

    private Employee getLoggedInEmployee() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return employeeRepository.findByEmail(username);
    }
}