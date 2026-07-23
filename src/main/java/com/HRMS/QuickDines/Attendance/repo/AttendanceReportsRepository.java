package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.model.AttendanceReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceReportsRepository extends JpaRepository<AttendanceReports, Long> {
    Object findByEmployeeId(String employeeId);

    Object findByEmployeeIdAndMonth(String employeeId, String currentMonth);
}
