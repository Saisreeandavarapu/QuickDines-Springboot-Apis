package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.model.GpsTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GpsTrackingRepository extends JpaRepository<GpsTracking, Long> {
    Optional<GpsTracking> findTopByEmployeeIdOrderByIdDesc(Long employeeId);

    Object findByEmployeeId(String employeeId);

}
