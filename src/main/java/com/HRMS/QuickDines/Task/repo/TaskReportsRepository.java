package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.model.TaskReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TaskReportsRepository extends JpaRepository<TaskReports, Long> {
    Optional<TaskReports> findByEmployeeEmployeeId(String employeeId);
}
