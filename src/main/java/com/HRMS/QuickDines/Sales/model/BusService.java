package com.HRMS.QuickDines.Sales.model;
import com.HRMS.QuickDines.Sales.Entity.BusServiceStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class BusService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String ownerName;

    private String mobile;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusServiceStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
