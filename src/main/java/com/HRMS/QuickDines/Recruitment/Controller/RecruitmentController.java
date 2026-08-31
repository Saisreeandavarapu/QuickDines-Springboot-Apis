package com.HRMS.QuickDines.Recruitment.Controller;

import com.HRMS.QuickDines.Recruitment.DTO.*;
import com.HRMS.QuickDines.Recruitment.Entity.ApprovalModule;
import com.HRMS.QuickDines.Recruitment.Entity.InterviewStatus;
import com.HRMS.QuickDines.Recruitment.Entity.JobOpeningStatus;
import com.HRMS.QuickDines.Recruitment.Services.RecruitmentService;
import com.HRMS.QuickDines.Recruitment.model.*;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService service;


    // =========================================================
    // JOB OPENINGS
    // =========================================================

    @PreAuthorize("hasAuthority('JOB_OPENING_CREATE')")
    @PostMapping("/job")
    public ResponseEntity<?> createJobOpening(@RequestBody JobOpening jobOpening) {

        return ResponseEntity.ok(service.createJobOpening(jobOpening));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_READ')")
    @GetMapping("/jobs")
    public ResponseEntity<?> getJobOpenings() {

        return ResponseEntity.ok(service.getJobOpenings());
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_READ')")
    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobOpening(@PathVariable Long id) {

        return ResponseEntity.ok(service.getJobOpening(id));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_UPDATE')")
    @PutMapping("/job/{id}")
    public ResponseEntity<?> updateJobOpening(@PathVariable Long id, @RequestBody JobOpening jobOpening) {

        return ResponseEntity.ok(service.updateJobOpening(id, jobOpening));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_DELETE')")
    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> deleteJobOpening(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteJobOpening(id));
    }


    // =========================================================
    // JOB APPLICATIONS
    // =========================================================

    @PreAuthorize("hasAuthority('JOB_APPLICATION_CREATE')")
    @PostMapping("/application/{jobId}")
    public ResponseEntity<?> createApplication(@PathVariable Long jobId, @RequestBody Application application) {

        return ResponseEntity.ok(service.createApplication(jobId, application));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_READ')")
    @GetMapping("/applications")
    public ResponseEntity<?> getApplications() {

        return ResponseEntity.ok(service.getApplications());
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_READ')")
    @GetMapping("/application/{id}")
    public ResponseEntity<?> getApplication(@PathVariable Long id) {

        return ResponseEntity.ok(service.getApplication(id));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_UPDATE')")
    @PutMapping("/application/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable Long id, @RequestBody Application application) {

        return ResponseEntity.ok(service.updateApplication(id, application));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_DELETE')")
    @DeleteMapping("/application/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteApplication(id));
    }


    // =========================================================
    // INTERVIEWS
    // =========================================================

    @PreAuthorize("hasAuthority('INTERVIEW_CREATE')")
    @PostMapping("/interview/{applicationId}")
    public ResponseEntity<?> createInterview(@PathVariable Long applicationId, @RequestBody Interview interview) {

        return ResponseEntity.ok(service.createInterview(applicationId, interview));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_READ')")
    @GetMapping("/interviews")
    public ResponseEntity<?> getInterviews() {

        return ResponseEntity.ok(service.getInterviews());
    }

    @PreAuthorize("hasAuthority('INTERVIEW_READ')")
    @GetMapping("/interview/{id}")
    public ResponseEntity<?> getInterview(@PathVariable Long id) {

        return ResponseEntity.ok(service.getInterview(id));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    @PutMapping("/interview/{id}")
    public ResponseEntity<?> updateInterview(@PathVariable Long id, @RequestBody Interview interview) {

        return ResponseEntity.ok(service.updateInterview(id, interview));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_DELETE')")
    @DeleteMapping("/interview/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteInterview(id));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_SELECTION_RECOMMEND')")
    @PostMapping("/interview/{id}/recommend")
    public ResponseEntity<?> recommendInterview(@PathVariable Long id) {

        return ResponseEntity.ok(service.recommendInterview(id));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_SELECTION_COORDINATE')")
    @GetMapping("/interview-selection/pending")
    public ResponseEntity<?> getPendingInterviewSelections() {

        return ResponseEntity.ok(service.getPendingInterviewSelections());
    }

    @PreAuthorize("hasAuthority('INTERVIEW_SELECTION_COORDINATE')")
    @PostMapping("/interview/{id}/coordinate")
    public ResponseEntity<?> coordinateInterview(@PathVariable Long id, @RequestBody InterviewCoordinateRequest request) {

        return ResponseEntity.ok(service.coordinateInterview(id, request));
    }


    // =========================================================
    // OFFER LETTERS
    // =========================================================

    @PreAuthorize("hasAuthority('OFFER_LETTER_CREATE')")
    @PostMapping("/offer/{applicationId}")
    public ResponseEntity<?> createOfferLetter(@PathVariable Long applicationId, @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(service.createOfferLetter(applicationId, offerLetter));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_READ')")
    @GetMapping("/offers")
    public ResponseEntity<?> getOfferLetters() {

        return ResponseEntity.ok(service.getOfferLetters());
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_READ')")
    @GetMapping("/offer/{id}")
    public ResponseEntity<?> getOfferLetter(@PathVariable Long id) {

        return ResponseEntity.ok(service.getOfferLetter(id));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_UPDATE')")
    @PutMapping("/offer/{id}")
    public ResponseEntity<?> updateOfferLetter(@PathVariable Long id, @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(service.updateOfferLetter(id, offerLetter));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_DELETE')")
    @DeleteMapping("/offer/{id}")
    public ResponseEntity<?> deleteOfferLetter(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteOfferLetter(id));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_RECOMMEND')")
    @PutMapping("/offer/{id}/recommend")
    public ResponseEntity<?> recommendOfferLetter(@PathVariable Long id) {

        return ResponseEntity.ok(service.recommendOfferLetter(id));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_PREPARE')")
    @PostMapping("/offer/{applicationId}/prepare")
    public ResponseEntity<?> prepareOfferLetter(@PathVariable Long applicationId, @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(service.prepareOfferLetter(applicationId, offerLetter));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_APPROVE')")
    @PutMapping("/offer/{id}/approve")
    public ResponseEntity<?> approveOfferLetter(@PathVariable Long id, @RequestParam(required = false) String reason) {

        return ResponseEntity.ok(service.approveOfferLetter(id, reason));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_APPROVE')")
    @PutMapping("/offer/{id}/reject")
    public ResponseEntity<?> rejectOfferLetter(@PathVariable Long id, @RequestParam String reason) {

        return ResponseEntity.ok(service.rejectOfferLetter(id, reason));
    }


    // =========================================================
    // CANDIDATE DOCUMENTS
    // =========================================================

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_CREATE')")
    @PostMapping(value = "/document/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCandidateDocument(

            @PathVariable Long applicationId,

            @RequestParam("aadhaarDocument") MultipartFile aadhaarDocument,

            @RequestParam("panDocument") MultipartFile panDocument,

            @RequestParam("degreeCertificate") MultipartFile degreeCertificate,

            @RequestParam("resume") MultipartFile resume,

            @RequestParam("verificationStatus") String verificationStatus) throws IOException {

        return ResponseEntity.ok(service.createCandidateDocument(applicationId, aadhaarDocument, panDocument, degreeCertificate, resume, verificationStatus));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_READ')")
    @GetMapping("/documents")
    public ResponseEntity<?> getCandidateDocuments() {

        return ResponseEntity.ok(service.getCandidateDocuments());
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_READ')")
    @GetMapping("/document/{id}")
    public ResponseEntity<?> getCandidateDocument(@PathVariable Long id) {

        return ResponseEntity.ok(service.getCandidateDocument(id));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_UPDATE')")
    @PutMapping(value = "/document/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCandidateDocument(

            @PathVariable Long id,

            @RequestParam(value = "aadhaarDocument", required = false) MultipartFile aadhaarDocument,

            @RequestParam(value = "panDocument", required = false) MultipartFile panDocument,

            @RequestParam(value = "degreeCertificate", required = false) MultipartFile degreeCertificate,

            @RequestParam(value = "resume", required = false) MultipartFile resume,

            @RequestParam("verificationStatus") String verificationStatus) throws IOException {

        return ResponseEntity.ok(service.updateCandidateDocument(id, aadhaarDocument, panDocument, degreeCertificate, resume, verificationStatus));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_DELETE')")
    @DeleteMapping("/document/{id}")
    public ResponseEntity<?> deleteCandidateDocument(@PathVariable Long id) {

        return ResponseEntity.ok(service.deleteCandidateDocument(id));
    }


    // =========================================================
    // RECRUITMENT REPORTS
    // =========================================================

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/pending")
    public ResponseEntity<?> pendingApplications() {

        return ResponseEntity.ok(service.pendingApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/selected")
    public ResponseEntity<?> selectedApplications() {

        return ResponseEntity.ok(service.selectedApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/rejected")
    public ResponseEntity<?> rejectedApplications() {

        return ResponseEntity.ok(service.rejectedApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/interviews/completed")
    public ResponseEntity<?> completedInterviews() {

        return ResponseEntity.ok(service.completedInterviews());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/interviews/pending")
    public ResponseEntity<?> pendingInterviews() {

        return ResponseEntity.ok(service.pendingInterviews());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/offers/accepted")
    public ResponseEntity<?> acceptedOffers() {

        return ResponseEntity.ok(service.acceptedOffers());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/offers/rejected")
    public ResponseEntity<?> rejectedOffers() {

        return ResponseEntity.ok(service.rejectedOffers());
    }


    // =========================================================
    // DASHBOARD COUNTS
    // =========================================================

    @PreAuthorize("hasAuthority('RECRUITMENT_DASHBOARD_READ')")
    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(service.getCounts());
    }
    // =========================================================
// FILTER BY STATUS
// =========================================================

    @GetMapping("/job-openings/status")
    @PreAuthorize("hasAuthority('JOB_OPENING_READ')")
    public ResponseEntity<?> getJobOpeningsByStatus(@RequestParam JobOpeningStatus status) {

        return ResponseEntity.ok(service.getJobOpeningsByStatus(status));
    }
    // =========================================================
// UPDATE JOB OPENING STATUS
// =========================================================

    @PutMapping("/job-opening/{id}/status")
    @PreAuthorize("hasAuthority('JOB_OPENING_UPDATE')")
    public ResponseEntity<?> updateJobOpeningStatus(@PathVariable Long id, @RequestParam JobOpeningStatus status) {

        return ResponseEntity.ok(service.updateJobOpeningStatus(id, status));
    }

    @PostMapping("/interview/{applicationId}/{branchId}")
    @PreAuthorize("hasAuthority('INTERVIEW_CREATE')")
    public ResponseEntity<?> createInterview(@PathVariable Long applicationId, @PathVariable Long branchId, @RequestBody Interview interview) {

        return ResponseEntity.ok(service.createInterview(applicationId,

                interview));
    }

    @PutMapping("/interview/{id}/select")
    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    public ResponseEntity<?> selectCandidate(@PathVariable Long id) {

        return ResponseEntity.ok(service.selectCandidate(id));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('INTERVIEW_READ')")
    public ResponseEntity<?> filterInterviews(

            @RequestParam(required = false) InterviewStatus status,

            @RequestParam(required = false) String startDate,

            @RequestParam(required = false) String endDate) {

        return ResponseEntity.ok(service.filterInterviews(status, startDate, endDate));
    }

    // =====================================================
    // CREATE APPROVAL
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_CREATE')")
    public ResponseEntity<?> createApproval(@RequestBody RecruitmentApprovalRequest request) {

        return ResponseEntity.ok(service.createApproval(request));
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_READ')")
    public ResponseEntity<?> getAllApprovals() {

        return ResponseEntity.ok(service.getAllApprovals());
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_READ')")
    public ResponseEntity<?> getApproval(@PathVariable Long id) {

        return ResponseEntity.ok(service.getApproval(id));
    }


    // =====================================================
    // PENDING APPROVALS
    // =====================================================

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_READ')")
    public ResponseEntity<?> getPendingApprovals() {

        return ResponseEntity.ok(service.getPendingApprovals());
    }


    // =====================================================
    // MY APPROVALS
    // =====================================================

    @GetMapping("/approver/{approverId}")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_READ')")
    public ResponseEntity<?> getMyApprovals(@PathVariable Long approverId) {

        return ResponseEntity.ok(service.getMyApprovals(approverId));
    }


    // =====================================================
    // FILTER BY MODULE
    // =====================================================

    @GetMapping("/module")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_READ')")
    public ResponseEntity<?> getByModule(@RequestParam ApprovalModule module) {

        return ResponseEntity.ok(service.getApprovalsByModule(module));
    }


    // =====================================================
    // PROCESS APPROVAL
    // =====================================================

    @PutMapping("/{id}/process")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_PROCESS')")
    public ResponseEntity<?> processApproval(@PathVariable Long id, @RequestBody ApprovalProcessRequest request) {

        return ResponseEntity.ok(service.processApproval(id, request));
    }

    // =====================================================
    // APPROVE
    // =====================================================

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_PROCESS')")
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestBody(required = false) ApprovalProcessRequest request) {

        return ResponseEntity.ok(service.approveApproval(id, request));
    }


    // =====================================================
    // REJECT
    // =====================================================

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_PROCESS')")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestBody ApprovalProcessRequest request) {

        return ResponseEntity.ok(service.rejectApproval(id, request));
    }


    // =====================================================
    // CANCEL
    // =====================================================

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('RECRUITMENT_APPROVAL_PROCESS')")
    public ResponseEntity<?> cancel(@PathVariable Long id) {

        return ResponseEntity.ok(service.cancelApproval(id));
    }
    // =====================================================
    // JOB DESCRIPTION - MANAGER
    // =====================================================

    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_CREATE')")
    @PostMapping("/job-description")
    public ResponseEntity<?> createJobDescription(@RequestBody JobDescriptionRequest request) {

        return ResponseEntity.ok(service.createJobDescription(request));
    }


    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_READ')")
    @GetMapping("/job-description/{id}")
    public ResponseEntity<?> getJobDescription(@PathVariable Long id) {

        return ResponseEntity.ok(service.getJobDescription(id));
    }


    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_UPDATE')")
    @PutMapping("/job-description/{id}")
    public ResponseEntity<?> updateJobDescription(@PathVariable Long id, @RequestBody JobDescriptionRequest request) {

        return ResponseEntity.ok(service.updateJobDescription(id, request));
    }


    // =====================================================
    // MANAGER → HR
    // =====================================================

    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_SUBMIT')")
    @PostMapping("/job-description/{id}/submit")
    public ResponseEntity<?> submitJobDescription(@PathVariable Long id) {

        return ResponseEntity.ok(service.submitJobDescription(id));
    }


    // =====================================================
    // HR
    // =====================================================

    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_REVIEW')")
    @GetMapping("/job-descriptions/pending-review")
    public ResponseEntity<?> pendingJobDescriptionReviews() {

        return ResponseEntity.ok(service.getPendingJobDescriptionReviews());
    }


    @PreAuthorize("hasAuthority('JOB_DESCRIPTION_REVIEW')")
    @PostMapping("/job-description/{id}/review")
    public ResponseEntity<?> reviewJobDescription(@PathVariable Long id, @RequestBody JobDescriptionReviewRequest request) {

        return ResponseEntity.ok(service.reviewJobDescription(id, request));
    }

    // =====================================================
    // CREATE ONBOARDING
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_CREATE')")
    @PostMapping("/onboarding")
    public ResponseEntity<?> createOnboarding(@RequestBody EmployeeOnboardingRequest request) {

        return ResponseEntity.ok(service.createOnboarding(request));
    }


    // =====================================================
    // GET ONBOARDING
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOnboarding(@PathVariable Long id) {

        return ResponseEntity.ok(service.getOnboarding(id));
    }


    // =====================================================
    // MANAGER - PARTICIPATION
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_PARTICIPATE')")
    @GetMapping("/manager/pending")
    public ResponseEntity<?> getManagerOnboarding() {

        return ResponseEntity.ok(service.getManagerOnboardingRequests());
    }


    // =====================================================
    // MANAGER - COMPLETE PARTICIPATION
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_PARTICIPATE')")
    @PutMapping("/{id}/manager-complete")
    public ResponseEntity<?> managerComplete(@PathVariable Long id) {

        return ResponseEntity.ok(service.managerCompleteOnboarding(id));
    }


    // =====================================================
    // HR - PENDING
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_APPROVE')")
    @GetMapping("/hr/pending")
    public ResponseEntity<?> getHRPending() {

        return ResponseEntity.ok(service.getHRPendingOnboarding());
    }


    // =====================================================
    // HR - APPROVE / COMPLETE
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_APPROVE')")
    @PutMapping("/{id}/hr-complete")
    public ResponseEntity<?> hrComplete(@PathVariable Long id, @RequestBody OnboardingCompletionRequest request) {

        return ResponseEntity.ok(service.completeOnboardingByHR(id, request.getRemarks()));
    }


    // =====================================================
    // HR - COMPLETED
    // =====================================================

    @PreAuthorize("hasAuthority('EMPLOYEE_ONBOARDING_READ')")
    @GetMapping("/hr/completed")
    public ResponseEntity<?> getCompleted() {

        return ResponseEntity.ok(service.getCompletedOnboarding());
    }
}