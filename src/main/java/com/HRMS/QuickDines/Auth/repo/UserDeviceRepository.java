package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.UserDevice;
import com.HRMS.QuickDines.Auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {
    Optional<Object> findTopByUsersOrderByIdDesc(Users user);

    Optional<Object> findById(Long id);
}
