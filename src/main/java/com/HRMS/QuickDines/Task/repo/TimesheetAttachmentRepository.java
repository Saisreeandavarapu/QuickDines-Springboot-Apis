package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.model.TimesheetAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetAttachmentRepository
        extends JpaRepository<TimesheetAttachment, Long> {

    List<TimesheetAttachment> findByTimesheet_Id(Long timesheetId);
}