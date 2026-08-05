package com.HRMS.QuickDines.Company.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String companyCode;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(length = 200)
    private String legalName;

    @Column(length = 20)
    private String gstNumber;

    @Column(length = 20)
    private String panNumber;

    @Column(length = 30)
    private String cinNumber;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String website;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String state;

    private String country;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 255)
    private String logoUrl;

    @Column(length = 20)
    private String status;


    /*
     * created_by → users.id
     *
     * Replace User with your actual User entity.
     */
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "created_by")
    // private User createdBy;


    /*
     * Company → Branches
     */
    @OneToMany(mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Branch> branches = new ArrayList<>();


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}