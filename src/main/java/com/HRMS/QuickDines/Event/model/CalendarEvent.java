package com.HRMS.QuickDines.Event.model;

import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Event.Entity.CalendarEventStatus;
import com.HRMS.QuickDines.Event.Entity.CalendarEventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "calendar_events")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // COMPANY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    // =========================
    // BRANCH
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

    // =========================
    // CREATED BY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private Employee createdBy;

    // =========================
    // EVENT DETAILS
    // =========================

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarEventType eventType;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private String location;

    private String meetingLink;

    private Boolean allDay = false;

    // =========================
    // STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarEventStatus status = CalendarEventStatus.SCHEDULED;

    // =========================
    // GOOGLE CALENDAR
    // =========================

    private String googleEventId;

    // =========================
    // PARTICIPANTS
    // =========================

    @OneToMany(
            mappedBy = "calendarEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MeetingSchedule> participants = new ArrayList<>();

    // =========================
    // AUDIT
    // =========================

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}