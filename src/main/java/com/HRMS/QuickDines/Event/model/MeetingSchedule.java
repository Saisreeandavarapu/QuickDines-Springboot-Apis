package com.HRMS.QuickDines.Event.model;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Event.Entity.EventAttendanceStatus;
import com.HRMS.QuickDines.Event.Entity.InvitationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "calendar_event_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"calendar_event_id", "employee_id"}
                )
        }
)
public class MeetingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // CALENDAR EVENT
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private CalendarEvent event;

    // =========================
    // EMPLOYEE
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // =========================
    // INVITATION
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus invitationStatus = InvitationStatus.INVITED;

    // =========================
    // ATTENDANCE
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventAttendanceStatus attendanceStatus =
            EventAttendanceStatus.PENDING;

    // =========================
    // NOTIFICATION
    // =========================

    private Boolean notificationSent = false;

    private Boolean reminderSent = false;

    private LocalDateTime responseAt;

    // =========================
    // AUDIT
    // =========================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}