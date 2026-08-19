package com.HRMS.QuickDines.Auth.repo;

import com.HRMS.QuickDines.Auth.model.UserDevice;
import com.HRMS.QuickDines.Employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {


    Optional<Object> findById(Long id);

    List<UserDevice> findByEmployee(String employeeId);

    Optional<UserDevice> findByEmployeeAndDeviceId(Employee employee, String deviceId);
}
