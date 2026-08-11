package com.HRMS.QuickDines.Sales.model;

import com.HRMS.QuickDines.Sales.Entity.RestaurantStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;

    private String ownerName;

    @Column(unique = true)
    private String email;

    private String mobile;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
