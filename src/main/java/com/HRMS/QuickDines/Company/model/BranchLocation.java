package com.HRMS.QuickDines.Company.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "branch_locations")
public class BranchLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * branch_locations.branch_id → branches.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;


    @Column(length = 150)
    private String buildingName;

    @Column(length = 50)
    private String floor;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;


    /*
     * DECIMAL(10,8)
     */
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;


    /*
     * DECIMAL(11,8)
     */
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;


    /*
     * DECIMAL(8,2)
     */
    @Column(precision = 8, scale = 2)
    private BigDecimal geoRadius;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}