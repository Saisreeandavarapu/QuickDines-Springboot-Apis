package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.model.AttendanceApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceApprovalRepository extends JpaRepository<AttendanceApproval, Long> {
}
