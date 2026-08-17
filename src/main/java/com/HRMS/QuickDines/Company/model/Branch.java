    package com.HRMS.QuickDines.Company.model;

    import com.HRMS.QuickDines.Employee.model.Employee;
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
    @Table(name = "branches")
    public class Branch {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        /*
         * branches.company_id → companies.id
         */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "company_id", nullable = false)
        @JsonIgnore
        private Company company;

        @Column(length = 20)
        private String branchCode;

        @Column(nullable = false, length = 150)
        private String branchName;

        @Column(length = 50)
        private String branchType;


        /*
         * branches.manager_id → employees.id
         */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "manager_id")
        private Employee manager;


        @Column(length = 150)
        private String email;

        @Column(length = 20)
        private String phone;

        @Column(columnDefinition = "TEXT")
        private String address;

        private String city;

        private String state;

        private String country;

        @Column(length = 20)
        private String postalCode;

        @Column(length = 20)
        private String status;


        /*
         * Branch → Branch Locations
         */
        @OneToMany(mappedBy = "branch",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
        private List<BranchLocation> locations = new ArrayList<>();


        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
    }