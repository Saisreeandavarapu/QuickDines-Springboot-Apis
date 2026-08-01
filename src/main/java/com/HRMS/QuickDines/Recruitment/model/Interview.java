package com.HRMS.QuickDines.Recruitment.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interviewType;

    private String interviewerName;

    private String interviewStatus;

    private String remarks;

    private LocalDateTime interviewDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
