package com.HRMS.QuickDines.Documents.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class DocumentTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;

    private String description;

    private Boolean isMandatory;

    private String status;


    @CreationTimestamp
    private LocalDateTime createdAt;

}
