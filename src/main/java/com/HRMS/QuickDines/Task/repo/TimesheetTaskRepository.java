package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.model.TimesheetTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetTaskRepository
        extends JpaRepository<TimesheetTask, Long> {

    List<TimesheetTask> findByTimesheet_Id(Long timesheetId);
}