package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.Entity.OvertimeStatus;
import com.HRMS.QuickDines.Attendance.model.OvertimeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Long> {
    List<OvertimeRequest> findByStatus(
            OvertimeStatus status);

}
