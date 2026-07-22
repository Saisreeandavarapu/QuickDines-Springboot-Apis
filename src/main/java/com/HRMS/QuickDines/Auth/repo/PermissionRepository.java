package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByModuleName(String moduleName);
}
