package com.HRMS.QuickDines.Recruitment.Services;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Recruitment.model.*;
import com.HRMS.QuickDines.Recruitment.repo.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

private final ApplicationRepository applicationRepository;
private final JobOpeningRepository jobOpeningRepository;
private final CandidateDocumentRepository candidateDocumentRepository;
private final InterviewRepository interviewRepository;
private final OfferLetterRepository offerLetterRepository;
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

            throw new RuntimeException(
                    "Unable to convert data to JSON",
                    e
            );
        }
    }


// =========================================================
// LOGGED-IN EMPLOYEE
// =========================================================

    private String getLoggedInEmployeeId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return authentication.getName();
    }


// =========================================================
// CLIENT INFORMATION
// =========================================================

    private String getIpAddress() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getIpAddress();
        } catch (Exception e) {
            return null;
        }
    }


    private String getBrowser() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getBrowser();
        } catch (Exception e) {
            return null;
        }
    }


    private String getOperatingSystem() {

        try {
            return clientInfoService
                    .getClientInfo()
                    .getOperatingSystem();
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

        auditLogsService.logCreate(
                "JOB_OPENING",
                String.valueOf(jobOpening.getId()),
                performedBy,
                jobOpening.getId().toString(),
                "Job Opening created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_JOB_OPENING",
                "RECRUITMENT",
                "Job Opening created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Job Opening created successfully"
        );

        return "Job Opening Created Successfully";
    }


    public Object getJobOpenings() {

        return jobOpeningRepository.findAll();
    }


    public Object getJobOpening(Long id) {

        return jobOpeningRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Opening Not Found"));
    }


    public String updateJobOpening(Long id, JobOpening jobOpening) {

        JobOpening existingJob = jobOpeningRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Opening Not Found"));

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

        auditLogsService.logUpdate(
                "JOB_OPENING",
                String.valueOf(id),
                performedBy,
                null,
                "Job Opening updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_JOB_OPENING",
                "RECRUITMENT",
                "Job Opening updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Job Opening updated successfully"
        );

        return "Job Opening Updated Successfully";
    }


    public String deleteJobOpening(Long id) {

        JobOpening existingJob = jobOpeningRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Opening Not Found"));

        String deletedValue = convertToJson(existingJob);

        jobOpeningRepository.delete(existingJob);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog(
                "JOB_OPENING",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingJob.getId().toString(),
                "Job Opening deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_JOB_OPENING",
                "RECRUITMENT",
                "Job Opening deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Job Opening deleted successfully"
        );

        return "Job Opening Deleted Successfully";
    }



//=================================
// APPLICATIONS
//=================================

    public String createApplication(
            Long jobId,
            Application application) {

        JobOpening jobOpening = jobOpeningRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job Opening Not Found"));

        application.setJobOpening(jobOpening);

        applicationRepository.save(application);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "APPLICATION",
                String.valueOf(application.getId()),
                performedBy,
                application.getId().toString(),
                "Application created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_APPLICATION",
                "RECRUITMENT",
                "Application created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Application created successfully"
        );

        return "Application Created Successfully";
    }


    public Object getApplications() {

        return applicationRepository.findAll();
    }


    public Object getApplication(Long id) {

        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application Not Found"));
    }


    public String updateApplication(
            Long id,
            Application application) {

        Application existingApplication =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Application Not Found"));

        String oldValue =
                convertToJson(existingApplication);

        existingApplication.setCandidateName(
                application.getCandidateName());

        existingApplication.setEmail(
                application.getEmail());

        existingApplication.setMobile(
                application.getMobile());

        existingApplication.setResumeUrl(
                application.getResumeUrl());

        existingApplication.setApplicationStatus(
                application.getApplicationStatus());

        applicationRepository.save(existingApplication);

        String newValue =
                convertToJson(existingApplication);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "APPLICATION",
                String.valueOf(id),
                performedBy,
                null,
                "Application updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_APPLICATION",
                "RECRUITMENT",
                "Application updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Application updated successfully"
        );

        return "Application Updated Successfully";
    }


    public String deleteApplication(Long id) {

        Application existingApplication =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Application Not Found"));

        String deletedValue =
                convertToJson(existingApplication);

        applicationRepository.delete(existingApplication);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.createAuditLog(
                "APPLICATION",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existingApplication.getId().toString(),
                "Application deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_APPLICATION",
                "RECRUITMENT",
                "Application deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Application deleted successfully"
        );

        return "Application Deleted Successfully";
    }



//=================================
// INTERVIEWS
//=================================

    public String createInterview(Long applicationId, Interview interview) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application Not Found"));

        interview.setApplication(application);

        interviewRepository.save(interview);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "INTERVIEW",
                String.valueOf(interview.getId()),
                performedBy,
                String.valueOf(applicationId),
                "Interview scheduled successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_INTERVIEW",
                "RECRUITMENT",
                "Interview scheduled successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Interview scheduled successfully"
        );

        return "Interview Scheduled Successfully";
    }


    public Object getInterviews() {

        return interviewRepository.findAll();
    }


    public Object getInterview(Long id) {

        return interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview Not Found"));
    }


    public String updateInterview(Long id, Interview interview) {

        Interview existingInterview =
                interviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Interview Not Found"));

        String oldValue =
                convertToJson(existingInterview);

        existingInterview.setInterviewType(
                interview.getInterviewType());

        existingInterview.setInterviewerName(
                interview.getInterviewerName());

        existingInterview.setInterviewStatus(
                interview.getInterviewStatus());

        existingInterview.setRemarks(
                interview.getRemarks());

        existingInterview.setInterviewDate(
                interview.getInterviewDate());

        interviewRepository.save(existingInterview);

        String newValue =
                convertToJson(existingInterview);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "INTERVIEW",
                String.valueOf(id),
                performedBy,
                String.valueOf(
                        existingInterview.getApplication().getId()),
                "Interview updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_INTERVIEW",
                "RECRUITMENT",
                "Interview updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Interview updated successfully"
        );

        return "Interview Updated Successfully";
    }


    public String deleteInterview(Long id) {

        Interview existingInterview =
                interviewRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Interview Not Found"));

        String deletedValue =
                convertToJson(existingInterview);

        String performedBy =
                getLoggedInEmployeeId();

        String applicationId =
                existingInterview.getApplication() != null
                        ? existingInterview.getApplication()
                        .getId()
                        .toString()
                        : null;

        interviewRepository.delete(existingInterview);

        auditLogsService.createAuditLog(
                "INTERVIEW",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                applicationId,
                "Interview deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_INTERVIEW",
                "RECRUITMENT",
                "Interview deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Interview deleted successfully"
        );

        return "Interview Deleted Successfully";
    }



//=================================
// OFFER LETTERS
//=================================

    public String createOfferLetter(
            Long applicationId,
            OfferLetter offerLetter) {

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application Not Found"));

        offerLetter.setApplication(application);

        offerLetterRepository.save(offerLetter);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "OFFER_LETTER",
                String.valueOf(offerLetter.getId()),
                performedBy,
                String.valueOf(applicationId),
                "Offer letter created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_OFFER_LETTER",
                "RECRUITMENT",
                "Offer letter created successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Offer letter created successfully"
        );

        return "Offer Letter Created Successfully";
    }


    public Object getOfferLetters() {

        return offerLetterRepository.findAll();
    }


    public Object getOfferLetter(Long id) {

        return offerLetterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Offer Letter Not Found"));
    }


    public String updateOfferLetter(
            Long id,
            OfferLetter offerLetter) {

        OfferLetter existingOffer =
                offerLetterRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Offer Letter Not Found"));

        String oldValue =
                convertToJson(existingOffer);

        existingOffer.setDesignation(
                offerLetter.getDesignation());

        existingOffer.setOfferedSalary(
                offerLetter.getOfferedSalary());

        existingOffer.setJoiningDate(
                offerLetter.getJoiningDate());

        existingOffer.setOfferStatus(
                offerLetter.getOfferStatus());

        offerLetterRepository.save(existingOffer);

        String newValue =
                convertToJson(existingOffer);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "OFFER_LETTER",
                String.valueOf(id),
                performedBy,
                String.valueOf(
                        existingOffer.getApplication().getId()),
                "Offer letter updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_OFFER_LETTER",
                "RECRUITMENT",
                "Offer letter updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Offer letter updated successfully"
        );

        return "Offer Letter Updated Successfully";
    }


    public String deleteOfferLetter(Long id) {

        OfferLetter existingOffer =
                offerLetterRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Offer Letter Not Found"));

        String deletedValue =
                convertToJson(existingOffer);

        String performedBy =
                getLoggedInEmployeeId();

        String applicationId =
                existingOffer.getApplication() != null
                        ? existingOffer.getApplication()
                        .getId()
                        .toString()
                        : null;

        offerLetterRepository.delete(existingOffer);

        auditLogsService.createAuditLog(
                "OFFER_LETTER",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                applicationId,
                "Offer letter deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_OFFER_LETTER",
                "RECRUITMENT",
                "Offer letter deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Offer letter deleted successfully"
        );

        return "Offer Letter Deleted Successfully";
    }

//=================================
// CANDIDATE DOCUMENTS
//=================================

    public String createCandidateDocument(
            Long applicationId,
            MultipartFile aadhaarDocument,
            MultipartFile panDocument,
            MultipartFile degreeCertificate,
            MultipartFile resume,
            String verificationStatus) throws IOException {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application Not Found"));

        CandidateDocument candidateDocument =
                new CandidateDocument();

        candidateDocument.setApplication(application);

        Map aadhaar = cloudinary.uploader().upload(
                aadhaarDocument.getBytes(),
                ObjectUtils.emptyMap());

        candidateDocument.setAadhaarDocument(
                aadhaar.get("secure_url").toString());

        Map pan = cloudinary.uploader().upload(
                panDocument.getBytes(),
                ObjectUtils.emptyMap());

        candidateDocument.setPanDocument(
                pan.get("secure_url").toString());

        Map degree = cloudinary.uploader().upload(
                degreeCertificate.getBytes(),
                ObjectUtils.emptyMap());

        candidateDocument.setDegreeCertificate(
                degree.get("secure_url").toString());

        Map resumeFile = cloudinary.uploader().upload(
                resume.getBytes(),
                ObjectUtils.emptyMap());

        candidateDocument.setResume(
                resumeFile.get("secure_url").toString());

        candidateDocument.setVerificationStatus(
                verificationStatus);

        candidateDocumentRepository.save(candidateDocument);

        String performedBy =
                getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "CANDIDATE_DOCUMENT",
                String.valueOf(candidateDocument.getId()),
                performedBy,
                String.valueOf(applicationId),
                "Candidate documents uploaded successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_CANDIDATE_DOCUMENT",
                "RECRUITMENT",
                "Candidate documents uploaded successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Candidate documents uploaded successfully"
        );

        return "Candidate Documents Uploaded Successfully";
    }


    public Object getCandidateDocuments() {

        return candidateDocumentRepository.findAll();
    }


    public Object getCandidateDocument(Long id) {

        return candidateDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Candidate Document Not Found"));
    }


    public String updateCandidateDocument(
            Long id,
            MultipartFile aadhaarDocument,
            MultipartFile panDocument,
            MultipartFile degreeCertificate,
            MultipartFile resume,
            String verificationStatus) throws IOException {

        CandidateDocument existingDocument =
                candidateDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate Document Not Found"));

        String oldValue =
                convertToJson(existingDocument);

        if (aadhaarDocument != null &&
                !aadhaarDocument.isEmpty()) {

            Map upload = cloudinary.uploader().upload(
                    aadhaarDocument.getBytes(),
                    ObjectUtils.emptyMap());

            existingDocument.setAadhaarDocument(
                    upload.get("secure_url").toString());
        }

        if (panDocument != null &&
                !panDocument.isEmpty()) {

            Map upload = cloudinary.uploader().upload(
                    panDocument.getBytes(),
                    ObjectUtils.emptyMap());

            existingDocument.setPanDocument(
                    upload.get("secure_url").toString());
        }

        if (degreeCertificate != null &&
                !degreeCertificate.isEmpty()) {

            Map upload = cloudinary.uploader().upload(
                    degreeCertificate.getBytes(),
                    ObjectUtils.emptyMap());

            existingDocument.setDegreeCertificate(
                    upload.get("secure_url").toString());
        }

        if (resume != null &&
                !resume.isEmpty()) {

            Map upload = cloudinary.uploader().upload(
                    resume.getBytes(),
                    ObjectUtils.emptyMap());

            existingDocument.setResume(
                    upload.get("secure_url").toString());
        }

        existingDocument.setVerificationStatus(
                verificationStatus);

        candidateDocumentRepository.save(existingDocument);

        String newValue =
                convertToJson(existingDocument);

        String performedBy =
                getLoggedInEmployeeId();

        String applicationId =
                existingDocument.getApplication() != null
                        ? existingDocument.getApplication()
                        .getId()
                        .toString()
                        : null;

        auditLogsService.logUpdate(
                "CANDIDATE_DOCUMENT",
                String.valueOf(id),
                performedBy,
                applicationId,
                "Candidate documents updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_CANDIDATE_DOCUMENT",
                "RECRUITMENT",
                "Candidate documents updated successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Candidate documents updated successfully"
        );

        return "Candidate Documents Updated Successfully";
    }


    public String deleteCandidateDocument(Long id) {

        CandidateDocument existingDocument =
                candidateDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate Document Not Found"));

        String deletedValue =
                convertToJson(existingDocument);

        String performedBy =
                getLoggedInEmployeeId();

        String applicationId =
                existingDocument.getApplication() != null
                        ? existingDocument.getApplication()
                        .getId()
                        .toString()
                        : null;

        candidateDocumentRepository.delete(existingDocument);

        auditLogsService.createAuditLog(
                "CANDIDATE_DOCUMENT",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                applicationId,
                "Candidate documents deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_CANDIDATE_DOCUMENT",
                "RECRUITMENT",
                "Candidate documents deleted successfully",
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "RECRUITMENT",
                "RecruitmentService",
                "Candidate documents deleted successfully"
        );

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

        counts.put("Pending Applications",
                applicationRepository.findByApplicationStatus("PENDING").size());

        counts.put("Selected Applications",
                applicationRepository.findByApplicationStatus("SELECTED").size());

        counts.put("Rejected Applications",
                applicationRepository.findByApplicationStatus("REJECTED").size());

        counts.put("Completed Interviews",
                interviewRepository.findByInterviewStatus("COMPLETED").size());

        counts.put("Pending Interviews",
                interviewRepository.findByInterviewStatus("PENDING").size());

        counts.put("Accepted Offers",
                offerLetterRepository.findByOfferStatus("ACCEPTED").size());

        counts.put("Rejected Offers",
                offerLetterRepository.findByOfferStatus("REJECTED").size());

        return counts;
    }
}
