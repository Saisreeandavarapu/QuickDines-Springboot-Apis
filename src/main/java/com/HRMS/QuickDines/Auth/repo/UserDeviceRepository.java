package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {
}
