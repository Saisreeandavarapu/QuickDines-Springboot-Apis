package com.HRMS.QuickDines.Recruitment.Services;

import com.HRMS.QuickDines.AdvanceServices.EmailService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Auth.model.Role;
import com.HRMS.QuickDines.Company.model.Branch;
import com.HRMS.QuickDines.Company.model.Company;
import com.HRMS.QuickDines.Company.repo.BranchRepository;
import com.HRMS.QuickDines.Company.repo.CompanyRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Recruitment.DTO.*;
import com.HRMS.QuickDines.Recruitment.Entity.*;
import com.HRMS.QuickDines.Recruitment.model.*;
import com.HRMS.QuickDines.Recruitment.repo.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final ApplicationRepository applicationRepository;
    private final BranchRepository branchRepository;
    private final EmailService emailService;
    private final JobOpeningRepository jobOpeningRepository;
    private final CandidateDocumentRepository candidateDocumentRepository;
    private final InterviewRepository interviewRepository;
    private final OfferLetterRepository offerLetterRepository;
    private final EmployeeRepository employeeRepository;
    private final RecruitmentApprovalRepository recruitmentApprovalRepository;
    private final CompanyRepository companyRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    @Autowired
    private Cloudinary cloudinary;
    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


// =========================================================
// CONVERT OBJECT TO JSON
// =========================================================

    private String convertToJson(Object object) {

        try {

            if (object == null) {
                return null;
            }

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to convert data to JSON", e);
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService.getClientInfo().getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService.getClientInfo().getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService.getClientInfo().getOperatingSystem();
        } catch (Exception e) {
            return null;
        }
    }

//=================================
// JOB OPENINGS
//=================================

    public String createJobOpening(JobOpening jobOpening) {

        jobOpeningRepository.save(jobOpening);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("JOB_OPENING", String.valueOf(jobOpening.getId()), performedBy, jobOpening.getId().toString(), "Job Opening created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_JOB_OPENING", "RECRUITMENT", "Job Opening created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Job Opening created successfully");

        return "Job Opening Created Successfully";
    }


    public Object getJobOpenings() {

        return jobOpeningRepository.findAll();
    }


    public Object getJobOpening(Long id) {

        return jobOpeningRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));
    }


    public String updateJobOpening(Long id, JobOpening jobOpening) {

        JobOpening existingJob = jobOpeningRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        String oldValue = convertToJson(existingJob);

        existingJob.setDesignation(jobOpening.getDesignation());
        existingJob.setDepartment(jobOpening.getDepartment());
        existingJob.setExperienceRequired(jobOpening.getExperienceRequired());
        existingJob.setSalaryPackage(jobOpening.getSalaryPackage());
        existingJob.setOpenings(jobOpening.getOpenings());
        existingJob.setStatus(jobOpening.getStatus());

        jobOpeningRepository.save(existingJob);

        String newValue = convertToJson(existingJob);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("JOB_OPENING", String.valueOf(id), performedBy, null, "Job Opening updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_JOB_OPENING", "RECRUITMENT", "Job Opening updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Job Opening updated successfully");

        return "Job Opening Updated Successfully";
    }


    public String deleteJobOpening(Long id) {

        JobOpening existingJob = jobOpeningRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        String deletedValue = convertToJson(existingJob);

        jobOpeningRepository.delete(existingJob);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog("JOB_OPENING", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingJob.getId().toString(), "Job Opening deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_JOB_OPENING", "RECRUITMENT", "Job Opening deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Job Opening deleted successfully");

        return "Job Opening Deleted Successfully";
    }


//=================================
// APPLICATIONS
//=================================

    public String createApplication(Long jobId, Application application) {

        JobOpening jobOpening = jobOpeningRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        application.setJobOpening(jobOpening);

        applicationRepository.save(application);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("APPLICATION", String.valueOf(application.getId()), performedBy, application.getId().toString(), "Application created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_APPLICATION", "RECRUITMENT", "Application created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Application created successfully");

        return "Application Created Successfully";
    }


    public Object getApplications() {

        return applicationRepository.findAll();
    }


    public Object getApplication(Long id) {

        return applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Application Not Found"));
    }


    public String updateApplication(Long id, Application application) {

        Application existingApplication = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Application Not Found"));

        String oldValue = convertToJson(existingApplication);

        existingApplication.setCandidateName(application.getCandidateName());

        existingApplication.setEmail(application.getEmail());

        existingApplication.setMobile(application.getMobile());

        existingApplication.setResumeUrl(application.getResumeUrl());

        existingApplication.setApplicationStatus(application.getApplicationStatus());

        applicationRepository.save(existingApplication);

        String newValue = convertToJson(existingApplication);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("APPLICATION", String.valueOf(id), performedBy, null, "Application updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_APPLICATION", "RECRUITMENT", "Application updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Application updated successfully");

        return "Application Updated Successfully";
    }


    public String deleteApplication(Long id) {

        Application existingApplication = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Application Not Found"));

        String deletedValue = convertToJson(existingApplication);

        applicationRepository.delete(existingApplication);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog("APPLICATION", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, existingApplication.getId().toString(), "Application deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_APPLICATION", "RECRUITMENT", "Application deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Application deleted successfully");

        return "Application Deleted Successfully";
    }


//=================================
// INTERVIEWS
//=================================

    public String createInterview(Long applicationId, Interview interview) {

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application Not Found"));


        interview.setApplication(application);


        if (interview.getInterviewStatus() == null) {

            interview.setInterviewStatus(InterviewStatus.SCHEDULED);
        }

        if (interview.getInterviewDate() == null) {

            throw new RuntimeException("Interview date and time is required");
        }

        Interview saved = interviewRepository.save(interview);

        // Send interview email
        emailService.sendInterviewEmail(saved);


        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("INTERVIEW", String.valueOf(interview.getId()), performedBy, String.valueOf(applicationId), "Interview scheduled successfully");

        auditLogsService.logActivity(performedBy, "CREATE_INTERVIEW", "RECRUITMENT", "Interview scheduled successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Interview scheduled successfully");

        return "Interview Scheduled Successfully";
    }


    public Object getInterviews() {

        return interviewRepository.findAll();
    }


    public Object getInterview(Long id) {

        return interviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Interview Not Found"));
    }


    public String updateInterview(Long id, Interview interview) {

        Interview existingInterview = interviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Interview Not Found"));

        String oldValue = convertToJson(existingInterview);

        existingInterview.setInterviewType(interview.getInterviewType());

        existingInterview.setInterviewerName(interview.getInterviewerName());

        existingInterview.setInterviewStatus(interview.getInterviewStatus());

        existingInterview.setRemarks(interview.getRemarks());

        existingInterview.setInterviewDate(interview.getInterviewDate());

        interviewRepository.save(existingInterview);

        String newValue = convertToJson(existingInterview);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("INTERVIEW", String.valueOf(id), performedBy, String.valueOf(existingInterview.getApplication().getId()), "Interview updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_INTERVIEW", "RECRUITMENT", "Interview updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Interview updated successfully");

        return "Interview Updated Successfully";
    }


    public String deleteInterview(Long id) {

        Interview existingInterview = interviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Interview Not Found"));

        String deletedValue = convertToJson(existingInterview);

        String performedBy = getLoggedInEmployeeId();

        String applicationId = existingInterview.getApplication() != null ? existingInterview.getApplication().getId().toString() : null;

        interviewRepository.delete(existingInterview);

        auditLogsService.createAuditLog("INTERVIEW", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, applicationId, "Interview deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_INTERVIEW", "RECRUITMENT", "Interview deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Interview deleted successfully");

        return "Interview Deleted Successfully";
    }

    @Transactional
    public String recommendInterview(Long interviewId) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(() -> new RuntimeException("Interview Not Found"));

        Application application = interview.getApplication();

        if (application == null) {
            throw new RuntimeException("Application Not Found");
        }

        String employeeId = getLoggedInEmployeeId();

        Employee manager = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Employee hr = findHRForCompany(application.getJobOpening().getCompany());

        // =====================================================
        // CREATE NEXT WORKFLOW STEP
        // =====================================================

        RecruitmentApproval approval = new RecruitmentApproval();

        approval.setCompany(application.getJobOpening().getCompany());

        approval.setModule(ApprovalModule.INTERVIEW_SELECTION);

        approval.setApplication(application);

        approval.setJobOpening(application.getJobOpening());

        approval.setRequestedBy(manager);

        approval.setApprover(hr);

        approval.setApprovalLevel(2);

        approval.setAction(ApprovalAction.COORDINATE);

        approval.setStatus(ApprovalStatus.PENDING);

        approval.setReason("Manager recommended candidate. HR coordination required.");

        recruitmentApprovalRepository.save(approval);

        // =====================================================
        // UPDATE INTERVIEW STATUS
        // =====================================================

        interview.setInterviewStatus(InterviewStatus.RECOMMENDED);

        interviewRepository.save(interview);

        return "Candidate Recommended Successfully. HR Coordination Pending.";
    }

    public List<RecruitmentApproval> getPendingInterviewSelections() {

        return recruitmentApprovalRepository.findByModuleAndStatus(ApprovalModule.INTERVIEW_SELECTION, ApprovalStatus.PENDING);
    }

    @Transactional
    public String coordinateInterview(Long interviewId, InterviewCoordinateRequest request) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(() -> new RuntimeException("Interview Not Found"));

        Application application = interview.getApplication();

        RecruitmentApproval approval = recruitmentApprovalRepository.findByModuleAndActionAndStatusAndApplication_Id(ApprovalModule.INTERVIEW_SELECTION, ApprovalAction.COORDINATE, ApprovalStatus.PENDING, application.getId()).orElseThrow(() -> new RuntimeException("Interview coordination approval not found"));

        String employeeId = getLoggedInEmployeeId();

        Employee hr = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("HR Employee Not Found"));

        // =====================================================
        // VERIFY HR
        // =====================================================

        if (!approval.getApprover().getEmployeeId().equals(hr.getEmployeeId())) {

            throw new RuntimeException("You are not authorized to coordinate this interview");
        }

        // =====================================================
        // UPDATE INTERVIEW
        // =====================================================

        interview.setInterviewType(request.getInterviewType());

        interview.setInterviewerName(request.getInterviewerName());

        interview.setInterviewDate(request.getInterviewDate());

        interview.setRemarks(request.getRemarks());

        interview.setInterviewStatus(InterviewStatus.SCHEDULED);

        interviewRepository.save(interview);

        // =====================================================
        // COMPLETE APPROVAL
        // =====================================================

        approval.setProcessedBy(hr);

        approval.setProcessedAt(LocalDateTime.now());

        approval.setStatus(ApprovalStatus.COMPLETED);

        approval.setReason(request.getRemarks());

        recruitmentApprovalRepository.save(approval);

        return "Interview Coordinated Successfully";
    }
//=================================
// OFFER LETTERS
//=================================

    public String createOfferLetter(Long applicationId, OfferLetter offerLetter) {

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application Not Found"));

        offerLetter.setApplication(application);

        offerLetterRepository.save(offerLetter);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("OFFER_LETTER", String.valueOf(offerLetter.getId()), performedBy, String.valueOf(applicationId), "Offer letter created successfully");

        auditLogsService.logActivity(performedBy, "CREATE_OFFER_LETTER", "RECRUITMENT", "Offer letter created successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Offer letter created successfully");

        return "Offer Letter Created Successfully";
    }


    public Object getOfferLetters() {

        return offerLetterRepository.findAll();
    }


    public Object getOfferLetter(Long id) {

        return offerLetterRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));
    }


    public String updateOfferLetter(Long id, OfferLetter offerLetter) {

        OfferLetter existingOffer = offerLetterRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));

        String oldValue = convertToJson(existingOffer);

        existingOffer.setDesignation(offerLetter.getDesignation());

        existingOffer.setOfferedSalary(offerLetter.getOfferedSalary());

        existingOffer.setJoiningDate(offerLetter.getJoiningDate());

        existingOffer.setOfferStatus(offerLetter.getOfferStatus());

        offerLetterRepository.save(existingOffer);

        String newValue = convertToJson(existingOffer);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate("OFFER_LETTER", String.valueOf(id), performedBy, String.valueOf(existingOffer.getApplication().getId()), "Offer letter updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_OFFER_LETTER", "RECRUITMENT", "Offer letter updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Offer letter updated successfully");

        return "Offer Letter Updated Successfully";
    }


    public String deleteOfferLetter(Long id) {

        OfferLetter existingOffer = offerLetterRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));

        String deletedValue = convertToJson(existingOffer);

        String performedBy = getLoggedInEmployeeId();

        String applicationId = existingOffer.getApplication() != null ? existingOffer.getApplication().getId().toString() : null;

        offerLetterRepository.delete(existingOffer);

        auditLogsService.createAuditLog("OFFER_LETTER", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, applicationId, "Offer letter deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_OFFER_LETTER", "RECRUITMENT", "Offer letter deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Offer letter deleted successfully");

        return "Offer Letter Deleted Successfully";
    }

//=================================
// CANDIDATE DOCUMENTS
//=================================

    public String createCandidateDocument(Long applicationId, MultipartFile aadhaarDocument, MultipartFile panDocument, MultipartFile degreeCertificate, MultipartFile resume, String verificationStatus) throws IOException {

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application Not Found"));

        CandidateDocument candidateDocument = new CandidateDocument();

        candidateDocument.setApplication(application);

        Map aadhaar = cloudinary.uploader().upload(aadhaarDocument.getBytes(), ObjectUtils.emptyMap());

        candidateDocument.setAadhaarDocument(aadhaar.get("secure_url").toString());

        Map pan = cloudinary.uploader().upload(panDocument.getBytes(), ObjectUtils.emptyMap());

        candidateDocument.setPanDocument(pan.get("secure_url").toString());

        Map degree = cloudinary.uploader().upload(degreeCertificate.getBytes(), ObjectUtils.emptyMap());

        candidateDocument.setDegreeCertificate(degree.get("secure_url").toString());

        Map resumeFile = cloudinary.uploader().upload(resume.getBytes(), ObjectUtils.emptyMap());

        candidateDocument.setResume(resumeFile.get("secure_url").toString());

        candidateDocument.setVerificationStatus(verificationStatus);

        candidateDocumentRepository.save(candidateDocument);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate("CANDIDATE_DOCUMENT", String.valueOf(candidateDocument.getId()), performedBy, String.valueOf(applicationId), "Candidate documents uploaded successfully");

        auditLogsService.logActivity(performedBy, "CREATE_CANDIDATE_DOCUMENT", "RECRUITMENT", "Candidate documents uploaded successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Candidate documents uploaded successfully");

        return "Candidate Documents Uploaded Successfully";
    }


    public Object getCandidateDocuments() {

        return candidateDocumentRepository.findAll();
    }


    public Object getCandidateDocument(Long id) {

        return candidateDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));
    }


    public String updateCandidateDocument(Long id, MultipartFile aadhaarDocument, MultipartFile panDocument, MultipartFile degreeCertificate, MultipartFile resume, String verificationStatus) throws IOException {

        CandidateDocument existingDocument = candidateDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));

        String oldValue = convertToJson(existingDocument);

        if (aadhaarDocument != null && !aadhaarDocument.isEmpty()) {

            Map upload = cloudinary.uploader().upload(aadhaarDocument.getBytes(), ObjectUtils.emptyMap());

            existingDocument.setAadhaarDocument(upload.get("secure_url").toString());
        }

        if (panDocument != null && !panDocument.isEmpty()) {

            Map upload = cloudinary.uploader().upload(panDocument.getBytes(), ObjectUtils.emptyMap());

            existingDocument.setPanDocument(upload.get("secure_url").toString());
        }

        if (degreeCertificate != null && !degreeCertificate.isEmpty()) {

            Map upload = cloudinary.uploader().upload(degreeCertificate.getBytes(), ObjectUtils.emptyMap());

            existingDocument.setDegreeCertificate(upload.get("secure_url").toString());
        }

        if (resume != null && !resume.isEmpty()) {

            Map upload = cloudinary.uploader().upload(resume.getBytes(), ObjectUtils.emptyMap());

            existingDocument.setResume(upload.get("secure_url").toString());
        }

        existingDocument.setVerificationStatus(verificationStatus);

        candidateDocumentRepository.save(existingDocument);

        String newValue = convertToJson(existingDocument);

        String performedBy = getLoggedInEmployeeId();

        String applicationId = existingDocument.getApplication() != null ? existingDocument.getApplication().getId().toString() : null;

        auditLogsService.logUpdate("CANDIDATE_DOCUMENT", String.valueOf(id), performedBy, applicationId, "Candidate documents updated successfully", oldValue, newValue);

        auditLogsService.logActivity(performedBy, "UPDATE_CANDIDATE_DOCUMENT", "RECRUITMENT", "Candidate documents updated successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Candidate documents updated successfully");

        return "Candidate Documents Updated Successfully";
    }


    public String deleteCandidateDocument(Long id) {

        CandidateDocument existingDocument = candidateDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));

        String deletedValue = convertToJson(existingDocument);

        String performedBy = getLoggedInEmployeeId();

        String applicationId = existingDocument.getApplication() != null ? existingDocument.getApplication().getId().toString() : null;

        candidateDocumentRepository.delete(existingDocument);

        auditLogsService.createAuditLog("CANDIDATE_DOCUMENT", String.valueOf(id), com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE, performedBy, applicationId, "Candidate documents deleted successfully", deletedValue, null, getIpAddress(), getOperatingSystem());

        auditLogsService.logActivity(performedBy, "DELETE_CANDIDATE_DOCUMENT", "RECRUITMENT", "Candidate documents deleted successfully", ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Candidate documents deleted successfully");

        return "Candidate Document Deleted Successfully";
    }


    //=================================
// REPORTS
//=================================

    public Object pendingApplications() {

        return applicationRepository.findByApplicationStatus("PENDING");
    }

    public Object selectedApplications() {

        return applicationRepository.findByApplicationStatus("SELECTED");
    }

    public Object rejectedApplications() {

        return applicationRepository.findByApplicationStatus("REJECTED");
    }

    public Object completedInterviews() {

        return interviewRepository.findByInterviewStatus("COMPLETED");
    }

    public Object pendingInterviews() {

        return interviewRepository.findByInterviewStatus("PENDING");
    }

    public Object acceptedOffers() {

        return offerLetterRepository.findByOfferStatus("ACCEPTED");
    }

    public Object rejectedOffers() {

        return offerLetterRepository.findByOfferStatus("REJECTED");
    }

    //=================================
// DASHBOARD COUNTS
//=================================

    public Object getCounts() {

        Map<String, Object> counts = new HashMap<>();

        counts.put("Total Job Openings", jobOpeningRepository.count());
        counts.put("Total Applications", applicationRepository.count());
        counts.put("Total Interviews", interviewRepository.count());
        counts.put("Total Offer Letters", offerLetterRepository.count());
        counts.put("Total Candidate Documents", candidateDocumentRepository.count());

        counts.put("Pending Applications", applicationRepository.findByApplicationStatus("PENDING").size());

        counts.put("Selected Applications", applicationRepository.findByApplicationStatus("SELECTED").size());

        counts.put("Rejected Applications", applicationRepository.findByApplicationStatus("REJECTED").size());

        counts.put("Completed Interviews", interviewRepository.findByInterviewStatus("COMPLETED").size());

        counts.put("Pending Interviews", interviewRepository.findByInterviewStatus("PENDING").size());

        counts.put("Accepted Offers", offerLetterRepository.findByOfferStatus("ACCEPTED").size());

        counts.put("Rejected Offers", offerLetterRepository.findByOfferStatus("REJECTED").size());

        return counts;
    }

    public List<JobOpening> getJobOpeningsByStatus(JobOpeningStatus status) {

        return jobOpeningRepository.findByStatus(status);
    }
    // =========================================================
// UPDATE JOB OPENING STATUS
// =========================================================

    public String updateJobOpeningStatus(Long id, JobOpeningStatus status) {

        JobOpening jobOpening = jobOpeningRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        if (status == null) {
            throw new RuntimeException("Status is required");
        }

        jobOpening.setStatus(status);

        jobOpeningRepository.save(jobOpening);

        return "Job Opening status updated to " + status;
    }

    public String selectCandidate(Long interviewId) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(() -> new RuntimeException("Interview Not Found"));

        interview.setInterviewStatus(InterviewStatus.SELECTED);

        Interview selected = interviewRepository.save(interview);

        emailService.sendSelectionEmail(selected);

        return "Candidate Selected Successfully";
    }

    public List<Interview> filterInterviews(InterviewStatus status, String startDate, String endDate) {

        if (status != null && startDate != null && endDate != null) {

            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();

            LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

            return interviewRepository.findByInterviewStatusAndInterviewDateBetween(status, start, end);
        }

        if (status != null) {

            return interviewRepository.findByInterviewStatus(status);
        }

        if (startDate != null && endDate != null) {

            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();

            LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

            return interviewRepository.findByInterviewDateBetween(start, end);
        }

        return interviewRepository.findAll();
    }

    public String processApproval(Long approvalId, ApprovalProcessRequest request) {

        RecruitmentApproval approval = recruitmentApprovalRepository.findById(approvalId).orElseThrow(() -> new RuntimeException("Approval Request Not Found"));


        if (approval.getStatus() == ApprovalStatus.APPROVED) {

            throw new RuntimeException("Approval already completed");
        }


        if (approval.getStatus() == ApprovalStatus.REJECTED) {

            throw new RuntimeException("Approval already rejected");
        }


        approval.setStatus(request.getStatus());

        approval.setReason(request.getReason());

        approval.setProcessedAt(LocalDateTime.now());


        // =====================================================
        // LOGGED-IN EMPLOYEE
        // =====================================================

        /*
         * Replace this with your existing
         * getLoggedInEmployeeId() implementation.
         */

        String employeeId = SecurityContextHolder.getContext().getAuthentication().getName();


        Employee processedBy = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Logged-in Employee Not Found"));


        approval.setProcessedBy(processedBy);


        recruitmentApprovalRepository.save(approval);


        return "Approval processed successfully";
    }

    // =====================================================
    // CREATE APPROVAL REQUEST
    // =====================================================

    public String createApproval(RecruitmentApprovalRequest request) {

        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company Not Found"));


        Employee requestedBy = employeeRepository.findById(request.getRequestedById()).orElseThrow(() -> new RuntimeException("Requested Employee Not Found"));


        RecruitmentApproval approval = new RecruitmentApproval();

        approval.setCompany(company);

        approval.setModule(request.getModule());

        approval.setRequestedBy(requestedBy);

        approval.setApprovalLevel(request.getApprovalLevel());

        approval.setAction(request.getAction());

        approval.setReason(request.getReason());

        approval.setStatus(ApprovalStatus.PENDING);


        // =================================================
        // EMPLOYEE
        // =================================================

        if (request.getEmployeeId() != null) {

            Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new RuntimeException("Employee Not Found"));

            approval.setEmployee(employee);
        }


        // =================================================
        // APPROVER
        // =================================================

        if (request.getApproverId() != null) {

            Employee approver = employeeRepository.findById(request.getApproverId()).orElseThrow(() -> new RuntimeException("Approver Not Found"));

            approval.setApprover(approver);
        }


        // =================================================
        // APPLICATION
        // =================================================

        if (request.getApplicationId() != null) {

            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new RuntimeException("Application Not Found"));

            approval.setApplication(application);
        }


        // =================================================
        // JOB OPENING
        // =================================================

        if (request.getJobOpeningId() != null) {

            JobOpening jobOpening = jobOpeningRepository.findById(request.getJobOpeningId()).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

            approval.setJobOpening(jobOpening);
        }


        recruitmentApprovalRepository.save(approval);

        return "Approval Request Created Successfully";
    }

    public List<RecruitmentApproval> getAllApprovals() {

        return recruitmentApprovalRepository.findAll();
    }

    public RecruitmentApproval getApproval(Long id) {

        return recruitmentApprovalRepository.findById(id).orElseThrow(() -> new RuntimeException("Approval Request Not Found"));
    }

    public List<RecruitmentApproval> getPendingApprovals() {

        return recruitmentApprovalRepository.findByStatus(ApprovalStatus.PENDING);
    }

    public List<RecruitmentApproval> getMyApprovals(Long approverId) {

        return recruitmentApprovalRepository.findByApproverIdAndStatus(approverId, ApprovalStatus.PENDING);
    }

    public List<RecruitmentApproval> getApprovalsByModule(ApprovalModule module) {

        return recruitmentApprovalRepository.findByModule(module);
    }

    // =====================================================
    // GET PENDING BY MODULE
    // =====================================================

    public List<RecruitmentApproval> getPendingByModule(ApprovalModule module) {

        return recruitmentApprovalRepository.findByModuleAndStatus(module, ApprovalStatus.PENDING);
    }


    // =====================================================
    // APPROVE
    // =====================================================

    public String approveApproval(Long approvalId, ApprovalProcessRequest request) {

        String employeeId = getLoggedInEmployeeId();


        RecruitmentApproval approval = recruitmentApprovalRepository.findByIdAndApproverEmployeeId(approvalId, employeeId).orElseThrow(() -> new RuntimeException("Approval not found or you are not the assigned approver"));


        if (approval.getStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Approval is already processed");
        }


        Employee processor = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));


        approval.setStatus(ApprovalStatus.APPROVED);

        approval.setProcessedBy(processor);

        approval.setProcessedAt(LocalDateTime.now());

        if (request != null) {

            approval.setReason(request.getReason());
        }


        recruitmentApprovalRepository.save(approval);


        auditLogsService.logActivity(employeeId, "APPROVE_RECRUITMENT_APPROVAL", "RECRUITMENT", "Recruitment approval approved. Approval ID: " + approvalId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        return "Recruitment approval approved successfully";
    }


    // =====================================================
    // REJECT
    // =====================================================

    public String rejectApproval(Long approvalId, ApprovalProcessRequest request) {

        String employeeId = getLoggedInEmployeeId();


        RecruitmentApproval approval = recruitmentApprovalRepository.findByIdAndApproverEmployeeId(approvalId, employeeId).orElseThrow(() -> new RuntimeException("Approval not found or you are not the assigned approver"));


        if (approval.getStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Approval is already processed");
        }


        if (request == null || request.getReason() == null || request.getReason().isBlank()) {

            throw new RuntimeException("Rejection reason is required");
        }


        Employee processor = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));


        approval.setStatus(ApprovalStatus.REJECTED);

        approval.setProcessedBy(processor);

        approval.setProcessedAt(LocalDateTime.now());

        approval.setReason(request.getReason());


        recruitmentApprovalRepository.save(approval);


        auditLogsService.logActivity(employeeId, "REJECT_RECRUITMENT_APPROVAL", "RECRUITMENT", "Recruitment approval rejected. Approval ID: " + approvalId, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());


        return "Recruitment approval rejected successfully";
    }


    // =====================================================
    // CANCEL
    // =====================================================

    public String cancelApproval(Long approvalId) {

        RecruitmentApproval approval = getApproval(approvalId);


        if (approval.getStatus() != ApprovalStatus.PENDING) {

            throw new RuntimeException("Only pending approval can be cancelled");
        }


        approval.setStatus(ApprovalStatus.CANCELLED);

        recruitmentApprovalRepository.save(approval);


        return "Recruitment approval cancelled successfully";
    }

    public String createJobDescription(JobDescriptionRequest request) {

        JobDescription jobDescription = new JobDescription();

        // Set company
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company Not Found"));

        jobDescription.setCompany(company);

        // Set job opening
        JobOpening jobOpening = jobOpeningRepository.findById(request.getJobOpeningId()).orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        jobDescription.setJobOpening(jobOpening);

        jobDescription.setTitle(request.getTitle());
        jobDescription.setDescription(request.getDescription());
        jobDescription.setResponsibilities(request.getResponsibilities());
        jobDescription.setQualifications(request.getQualifications());
        jobDescription.setSkills(request.getSkills());

        // Logged-in manager
        String employeeId = getLoggedInEmployeeId();

        Employee manager = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        jobDescription.setDefinedBy(manager);

        jobDescription.setStatus(JobDescriptionStatus.DRAFT);

        jobDescriptionRepository.save(jobDescription);

        return "Job Description Created Successfully";
    }

    @Transactional
    public String submitJobDescription(Long id) {

        JobDescription jobDescription = jobDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Description Not Found"));

        // =====================================================
        // VALIDATE STATUS
        // =====================================================

        if (jobDescription.getStatus() != JobDescriptionStatus.DRAFT) {

            throw new RuntimeException("Only draft Job Description can be submitted");
        }

        // =====================================================
        // CHANGE STATUS
        // =====================================================

        jobDescription.setStatus(JobDescriptionStatus.PENDING_REVIEW);

        jobDescriptionRepository.save(jobDescription);

        // =====================================================
        // FIND HR
        // =====================================================

        Employee hr = findHRForCompany(jobDescription.getCompany());

        // =====================================================
        // CREATE APPROVAL
        // =====================================================

        RecruitmentApproval approval = new RecruitmentApproval();

        approval.setCompany(jobDescription.getCompany());

        approval.setModule(ApprovalModule.JOB_DESCRIPTION);

        approval.setJobOpening(jobDescription.getJobOpening());

        approval.setRequestedBy(jobDescription.getDefinedBy());

        approval.setApprover(hr);

        approval.setApprovalLevel(1);

        approval.setAction(ApprovalAction.REVIEW);

        approval.setStatus(ApprovalStatus.PENDING);

        approval.setReason("Job Description submitted for HR review");

        recruitmentApprovalRepository.save(approval);

        return "Job Description Submitted For HR Review";
    }


    private Employee findHRForCompany(Company company) {

        return employeeRepository.findFirstByCompanyAndRole(company, "HR").orElseThrow(() -> new RuntimeException("HR not found for this company"));
    }

    public List<JobDescription> getPendingJobDescriptionReviews() {

        return jobDescriptionRepository.findByStatus(JobDescriptionStatus.PENDING_REVIEW);
    }

    @Transactional
    public String reviewJobDescription(Long id, JobDescriptionReviewRequest request) {

        JobDescription jobDescription = jobDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Description Not Found"));

        if (jobDescription.getStatus() != JobDescriptionStatus.PENDING_REVIEW) {

            throw new RuntimeException("Job Description is not pending review");
        }

        String employeeId = getLoggedInEmployeeId();

        Employee hr = employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("HR Employee Not Found"));

        RecruitmentApproval approval = recruitmentApprovalRepository.findByJobOpeningAndModuleAndStatus(jobDescription.getJobOpening(), ApprovalModule.JOB_DESCRIPTION, ApprovalStatus.PENDING).orElseThrow(() -> new RuntimeException("Approval Request Not Found"));

        // Make sure current logged-in HR is the approver
        if (!approval.getApprover().getEmployeeId().equals(employeeId)) {

            throw new RuntimeException("You are not authorized to review this Job Description");
        }

        approval.setProcessedBy(hr);

        approval.setProcessedAt(LocalDateTime.now());

        approval.setReason(request.getReason());

        if (request.isApproved()) {

            jobDescription.setStatus(JobDescriptionStatus.APPROVED);

            jobDescription.setReviewedBy(hr);

            jobDescription.setReviewedAt(LocalDateTime.now());

            approval.setStatus(ApprovalStatus.APPROVED);

            jobDescriptionRepository.save(jobDescription);
            recruitmentApprovalRepository.save(approval);

            return "Job Description Approved Successfully";

        } else {

            jobDescription.setStatus(JobDescriptionStatus.REJECTED);

            jobDescriptionRepository.save(jobDescription);

            approval.setStatus(ApprovalStatus.REJECTED);

            recruitmentApprovalRepository.save(approval);

            return "Job Description Rejected";
        }
    }

    public JobDescription getJobDescription(Long id) {

        return jobDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Description Not Found"));
    }

    @Transactional
    public String updateJobDescription(Long id, JobDescriptionRequest request) {

        JobDescription existing = jobDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Description Not Found"));

        // =====================================================
        // ONLY DRAFT CAN BE UPDATED
        // =====================================================

        if (existing.getStatus() != JobDescriptionStatus.DRAFT) {

            throw new RuntimeException("Only DRAFT Job Description can be updated");
        }

        // =====================================================
        // UPDATE DETAILS
        // =====================================================

        existing.setTitle(request.getTitle());

        existing.setDescription(request.getDescription());

        existing.setResponsibilities(request.getResponsibilities());

        existing.setQualifications(request.getQualifications());

        existing.setSkills(request.getSkills());

        jobDescriptionRepository.save(existing);

        // =====================================================
        // AUDIT
        // =====================================================

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logActivity(performedBy, "UPDATE_JOB_DESCRIPTION", "RECRUITMENT", "Job Description updated successfully. ID: " + id, ActivityStatus.SUCCESS, getIpAddress(), getBrowser(), getOperatingSystem());

        auditLogsService.logInfo("RECRUITMENT", "RecruitmentService", "Job Description updated successfully. ID: " + id);

        return "Job Description Updated Successfully";
    }
}
