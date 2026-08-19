package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.Entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    Optional<Object> findByEmail(String email);

    Optional<Object> findById(String userId);
    List<Users> findByStatus(UserStatus status);

   // List<Users> findByEmployeeId(String employeeId);

    List<Users> findByRole(String role);

    List<Users> findByStatusAndRole(
            UserStatus status,
            String role);

}
