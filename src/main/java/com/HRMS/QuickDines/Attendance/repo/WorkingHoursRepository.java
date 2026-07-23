package com.HRMS.QuickDines.Attendance.repo;

import com.HRMS.QuickDines.Attendance.model.WorkingHours;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingHoursRepository extends CrudRepository<WorkingHours, Long> {
    Object findByEmployeeId(String employeeId);
}
