package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    Optional<Object> findByEmail(String email);

    Optional<Object> findById(String userId);
}
