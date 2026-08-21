package com.HRMS.QuickDines.Event.Controller;

import com.HRMS.QuickDines.Event.model.CalendarEvent;
import com.HRMS.QuickDines.Event.Entity.CalendarEventType;
import com.HRMS.QuickDines.Event.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    // =========================================================
    // CREATE EVENT
    // =========================================================

    @PostMapping("/events")
    @PreAuthorize("hasAuthority('CALENDAR_CREATE')")
    public ResponseEntity<CalendarEvent> createEvent(
            @RequestBody CalendarEvent event) {

        return ResponseEntity.ok(
                calendarService.createEvent(event)
        );
    }

    // =========================================================
    // GET ALL EVENTS
    // =========================================================

    @GetMapping("/events")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getAllEvents() {

        return ResponseEntity.ok(
                calendarService.getAllEvents()
        );
    }

    // =========================================================
    // GET EVENT BY ID
    // =========================================================

    @GetMapping("/events/{eventId}")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<CalendarEvent> getEventById(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.getEventById(eventId)
        );
    }

    // =========================================================
    // UPDATE EVENT
    // =========================================================

    @PutMapping("/events/{eventId}")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<CalendarEvent> updateEvent(
            @PathVariable Long eventId,
            @RequestBody CalendarEvent event) {

        return ResponseEntity.ok(
                calendarService.updateEvent(eventId, event)
        );
    }

    // =========================================================
    // DELETE EVENT
    // =========================================================

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasAuthority('CALENDAR_DELETE')")
    public ResponseEntity<String> deleteEvent(
            @PathVariable Long eventId) {

        calendarService.deleteEvent(eventId);

        return ResponseEntity.ok(
                "Calendar event deleted successfully"
        );
    }

    // =========================================================
    // EVENTS BY DATE
    // =========================================================

    @GetMapping("/events/date/{date}")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getEventsByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                calendarService.getEventsByDate(date)
        );
    }

    // =========================================================
    // EVENTS BY DATE RANGE
    // =========================================================

    @GetMapping("/events/range")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getEventsByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                calendarService.getEventsByDateRange(
                        startDate,
                        endDate
                )
        );
    }

    // =========================================================
    // EVENTS BY TYPE
    // =========================================================

    @GetMapping("/events/type/{eventType}")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getEventsByType(
            @PathVariable CalendarEventType eventType) {

        return ResponseEntity.ok(
                calendarService.getEventsByType(eventType)
        );
    }

    // =========================================================
    // LOGGED-IN EMPLOYEE EVENTS
    // =========================================================

    @GetMapping("/my-events")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getMyEvents() {

        return ResponseEntity.ok(
                calendarService.getMyEvents()
        );
    }

    // =========================================================
    // LOGGED-IN EMPLOYEE UPCOMING EVENTS
    // =========================================================

    @GetMapping("/my-events/upcoming")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<List<CalendarEvent>> getMyUpcomingEvents() {

        return ResponseEntity.ok(
                calendarService.getMyUpcomingEvents()
        );
    }

    // =========================================================
    // ADD SINGLE PARTICIPANT
    // =========================================================

    @PostMapping("/events/{eventId}/participants")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<?> addParticipant(
            @PathVariable Long eventId,
            @RequestParam Long employeeId) {

        return ResponseEntity.ok(
                calendarService.addParticipant(
                        eventId,
                        employeeId
                )
        );
    }

    // =========================================================
    // ADD MULTIPLE PARTICIPANTS
    // =========================================================

    @PostMapping("/events/{eventId}/participants/bulk")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<?> addParticipants(
            @PathVariable Long eventId,
            @RequestBody List<Long> employeeIds) {

        return ResponseEntity.ok(
                calendarService.addParticipants(
                        eventId,
                        employeeIds
                )
        );
    }

    // =========================================================
    // GET PARTICIPANTS
    // =========================================================

    @GetMapping("/events/{eventId}/participants")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<?> getParticipants(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.getParticipants(eventId)
        );
    }

    // =========================================================
    // REMOVE PARTICIPANT
    // =========================================================

    @DeleteMapping("/events/{eventId}/participants/{employeeId}")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<String> removeParticipant(
            @PathVariable Long eventId,
            @PathVariable Long employeeId) {

        calendarService.removeParticipant(
                eventId,
                employeeId
        );

        return ResponseEntity.ok(
                "Participant removed successfully"
        );
    }

    // =========================================================
    // MY INVITATIONS
    // =========================================================

    @GetMapping("/my-invitations")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<?> getMyInvitations() {

        return ResponseEntity.ok(
                calendarService.getMyInvitations()
        );
    }

    // =========================================================
    // ACCEPT INVITATION
    // =========================================================

    @PatchMapping("/events/{eventId}/accept")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.acceptInvitation(eventId)
        );
    }

    // =========================================================
    // DECLINE INVITATION
    // =========================================================

    @PatchMapping("/events/{eventId}/decline")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<?> declineInvitation(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.declineInvitation(eventId)
        );
    }

    // =========================================================
    // CANCEL EVENT
    // =========================================================

    @PatchMapping("/events/{eventId}/cancel")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<?> cancelEvent(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.cancelEvent(eventId)
        );
    }

    // =========================================================
    // COMPLETE EVENT
    // =========================================================

    @PatchMapping("/events/{eventId}/complete")
    @PreAuthorize("hasAuthority('CALENDAR_UPDATE')")
    public ResponseEntity<?> completeEvent(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                calendarService.completeEvent(eventId)
        );
    }

    // =========================================================
    // CALENDAR DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('CALENDAR_READ')")
    public ResponseEntity<Map<String, Object>> getCalendarDashboard() {

        return ResponseEntity.ok(
                calendarService.getCalendarDashboard()
        );
    }
}