package com.HRMS.QuickDines.Recruitment.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class CandidateDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aadhaarDocument;

    private String panDocument;

    private String degreeCertificate;

    private String resume;

    private String verificationStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
