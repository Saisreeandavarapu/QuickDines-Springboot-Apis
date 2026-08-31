package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.Entity.ApprovalAction;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalModule;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalStatus;
import com.HRMS.QuickDines.Recruitment.model.JobOpening;
import com.HRMS.QuickDines.Recruitment.model.RecruitmentApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitmentApprovalRepository
        extends JpaRepository<RecruitmentApproval, Long> {

    List<RecruitmentApproval> findByCompanyId(Long companyId);

    List<RecruitmentApproval> findByModule(ApprovalModule module);

    List<RecruitmentApproval> findByStatus(ApprovalStatus status);

    List<RecruitmentApproval> findByApproverId(Long approverId);

    List<RecruitmentApproval> findByRequestedById(Long employeeId);

    List<RecruitmentApproval> findByEmployeeId(Long employeeId);

    List<RecruitmentApproval> findByApplicationId(Long applicationId);

    List<RecruitmentApproval> findByJobOpeningId(Long jobOpeningId);

    List<RecruitmentApproval> findByModuleAndStatus(
            ApprovalModule module,
            ApprovalStatus status
    );

    List<RecruitmentApproval> findByApproverIdAndStatus(
            Long approverId,
            ApprovalStatus status
    );
    Optional<RecruitmentApproval> findByIdAndApproverEmployeeId(
            Long id,
            String employeeId
    );
    Optional<RecruitmentApproval>
    findByJobOpeningAndModuleAndStatus(
            JobOpening jobOpening,
            ApprovalModule module,
            ApprovalStatus status
    );
    Optional<RecruitmentApproval>
    findByModuleAndActionAndStatusAndApplication_Id(
            ApprovalModule module,
            ApprovalAction action,
            ApprovalStatus status,
            Long applicationId
    );
}