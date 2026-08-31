package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.Entity.ApprovalModule;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Recruitment.Entity.JobDescriptionStatus;
import com.HRMS.QuickDines.Recruitment.model.JobDescription;
import com.HRMS.QuickDines.Recruitment.model.JobOpening;
import com.HRMS.QuickDines.Recruitment.model.RecruitmentApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobDescriptionRepository
        extends JpaRepository<JobDescription, Long> {

    List<JobDescription> findByStatus(JobDescriptionStatus status);

    List<JobDescription> findByDefinedBy_EmployeeId(String employeeId);

    List<JobDescription> findByReviewedBy_EmployeeId(String employeeId);


}