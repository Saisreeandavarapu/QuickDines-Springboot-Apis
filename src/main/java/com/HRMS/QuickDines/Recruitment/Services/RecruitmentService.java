package com.HRMS.QuickDines.Recruitment.Services;

import com.HRMS.QuickDines.Recruitment.model.*;
import com.HRMS.QuickDines.Recruitment.repo.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    //=================================
// JOB OPENINGS
//=================================

    public String createJobOpening(JobOpening jobOpening) {

        jobOpeningRepository.save(jobOpening);

        return "Job Opening Created Successfully";
    }

    public Object getJobOpenings() {

        return jobOpeningRepository.findAll();
    }

    public Object getJobOpening(Long id) {

        return jobOpeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Opening Not Found"));
    }

    public String updateJobOpening(Long id, JobOpening jobOpening) {

        JobOpening existingJob = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        existingJob.setDesignation(jobOpening.getDesignation());
        existingJob.setDepartment(jobOpening.getDepartment());
        existingJob.setExperienceRequired(jobOpening.getExperienceRequired());
        existingJob.setSalaryPackage(jobOpening.getSalaryPackage());
        existingJob.setOpenings(jobOpening.getOpenings());
        existingJob.setStatus(jobOpening.getStatus());

        jobOpeningRepository.save(existingJob);

        return "Job Opening Updated Successfully";
    }

    public String deleteJobOpening(Long id) {

        JobOpening existingJob = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        jobOpeningRepository.delete(existingJob);

        return "Job Opening Deleted Successfully";
    }

    //=================================
// APPLICATIONS
//=================================

    public String createApplication(Long jobId, Application application) {

        JobOpening jobOpening = jobOpeningRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job Opening Not Found"));

        application.setJobOpening(jobOpening);

        applicationRepository.save(application);

        return "Application Created Successfully";
    }

    public Object getApplications() {

        return applicationRepository.findAll();
    }

    public Object getApplication(Long id) {

        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));
    }

    public String updateApplication(Long id, Application application) {

        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        existingApplication.setCandidateName(application.getCandidateName());
        existingApplication.setEmail(application.getEmail());
        existingApplication.setMobile(application.getMobile());
        existingApplication.setResumeUrl(application.getResumeUrl());
        existingApplication.setApplicationStatus(application.getApplicationStatus());

        applicationRepository.save(existingApplication);

        return "Application Updated Successfully";
    }

    public String deleteApplication(Long id) {

        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        applicationRepository.delete(existingApplication);

        return "Application Deleted Successfully";
    }

    //=================================
// INTERVIEWS
//=================================

    public String createInterview(Long applicationId, Interview interview) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        interview.setApplication(application);

        interviewRepository.save(interview);

        return "Interview Scheduled Successfully";
    }

    public Object getInterviews() {

        return interviewRepository.findAll();
    }

    public Object getInterview(Long id) {

        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview Not Found"));
    }

    public String updateInterview(Long id, Interview interview) {

        Interview existingInterview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview Not Found"));

        existingInterview.setInterviewType(interview.getInterviewType());
        existingInterview.setInterviewerName(interview.getInterviewerName());
        existingInterview.setInterviewStatus(interview.getInterviewStatus());
        existingInterview.setRemarks(interview.getRemarks());
        existingInterview.setInterviewDate(interview.getInterviewDate());

        interviewRepository.save(existingInterview);

        return "Interview Updated Successfully";
    }

    public String deleteInterview(Long id) {

        Interview existingInterview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview Not Found"));

        interviewRepository.delete(existingInterview);

        return "Interview Deleted Successfully";
    }

    //=================================
// OFFER LETTERS
//=================================

    public String createOfferLetter(Long applicationId, OfferLetter offerLetter) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

        offerLetter.setApplication(application);

        offerLetterRepository.save(offerLetter);

        return "Offer Letter Created Successfully";
    }

    public Object getOfferLetters() {

        return offerLetterRepository.findAll();
    }

    public Object getOfferLetter(Long id) {

        return offerLetterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));
    }

    public String updateOfferLetter(Long id, OfferLetter offerLetter) {

        OfferLetter existingOffer = offerLetterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));

        existingOffer.setDesignation(offerLetter.getDesignation());
        existingOffer.setOfferedSalary(offerLetter.getOfferedSalary());
        existingOffer.setJoiningDate(offerLetter.getJoiningDate());
        existingOffer.setOfferStatus(offerLetter.getOfferStatus());

        offerLetterRepository.save(existingOffer);

        return "Offer Letter Updated Successfully";
    }

    public String deleteOfferLetter(Long id) {

        OfferLetter existingOffer = offerLetterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer Letter Not Found"));

        offerLetterRepository.delete(existingOffer);

        return "Offer Letter Deleted Successfully";
    }

    //=================================
// CANDIDATE DOCUMENTS
//=================================

    public String createCandidateDocument(Long applicationId,
                                          MultipartFile aadhaarDocument,
                                          MultipartFile panDocument,
                                          MultipartFile degreeCertificate,
                                          MultipartFile resume,
                                          String verificationStatus) throws IOException {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found"));

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

        return "Candidate Documents Uploaded Successfully";
    }

    public Object getCandidateDocuments() {

        return candidateDocumentRepository.findAll();
    }

    public Object getCandidateDocument(Long id) {

        return candidateDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));
    }

    public String updateCandidateDocument(Long id,
                                          MultipartFile aadhaarDocument,
                                          MultipartFile panDocument,
                                          MultipartFile degreeCertificate,
                                          MultipartFile resume,
                                          String verificationStatus) throws IOException {

        CandidateDocument existingDocument = candidateDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));

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

        return "Candidate Documents Updated Successfully";
    }

    public String deleteCandidateDocument(Long id) {

        CandidateDocument existingDocument = candidateDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate Document Not Found"));

        candidateDocumentRepository.delete(existingDocument);

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
