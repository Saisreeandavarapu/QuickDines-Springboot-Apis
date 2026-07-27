package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.model.TaskAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssignmentsRepository extends JpaRepository<TaskAssignments, Long> {
    List<TaskAssignments> findByAssignedToEmployeeId(String employeeId);
    List<TaskAssignments> findByAssignedToEmployeeIdAndTaskStatus(String employeeId, String status);
    Long countByAssignedToEmployeeEmployeeIdAndTaskStatus(String employeeId, String status);

}
