    package com.HRMS.QuickDines.Auth.model;

    import jakarta.persistence.*;
    import lombok.Data;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;

    @Entity
    @Table(
            name = "role_permissions",
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_role_permission",
                            columnNames = {"role_id", "permission_id"}
                    )
            }
    )
    @Data
    public class RolePermission {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "role_id",
                nullable = false
        )
        private Role role;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "permission_id",
                nullable = false
        )
        private Permission permission;

        @CreationTimestamp
        @Column(
                name = "created_at",
                nullable = false,
                updatable = false
        )
        private LocalDateTime createdAt;
    }