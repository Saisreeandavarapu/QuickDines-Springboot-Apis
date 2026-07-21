package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
