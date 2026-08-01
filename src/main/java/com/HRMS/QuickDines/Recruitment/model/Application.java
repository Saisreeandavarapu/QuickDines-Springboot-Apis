package com.HRMS.QuickDines.Recruitment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateName;

    private String email;

    private String mobile;

    private String resumeUrl;

    private String applicationStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobOpening jobOpening;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Interview> interviews;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OfferLetter> offerLetters;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CandidateDocument> candidateDocuments;

}
