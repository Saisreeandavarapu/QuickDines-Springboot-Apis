package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByModuleName(String moduleName);

    Optional<Permission> findById(Long permissionId);
}
