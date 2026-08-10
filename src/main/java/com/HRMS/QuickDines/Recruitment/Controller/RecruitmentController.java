package com.HRMS.QuickDines.Recruitment.Controller;

import com.HRMS.QuickDines.Recruitment.Services.RecruitmentService;
import com.HRMS.QuickDines.Recruitment.model.*;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createJobOpening(
            @RequestBody JobOpening jobOpening) {

        return ResponseEntity.ok(
                service.createJobOpening(jobOpening));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_READ')")
    @GetMapping("/jobs")
    public ResponseEntity<?> getJobOpenings() {

        return ResponseEntity.ok(
                service.getJobOpenings());
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_READ')")
    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobOpening(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getJobOpening(id));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_UPDATE')")
    @PutMapping("/job/{id}")
    public ResponseEntity<?> updateJobOpening(
            @PathVariable Long id,
            @RequestBody JobOpening jobOpening) {

        return ResponseEntity.ok(
                service.updateJobOpening(id, jobOpening));
    }

    @PreAuthorize("hasAuthority('JOB_OPENING_DELETE')")
    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> deleteJobOpening(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteJobOpening(id));
    }


    // =========================================================
    // JOB APPLICATIONS
    // =========================================================

    @PreAuthorize("hasAuthority('JOB_APPLICATION_CREATE')")
    @PostMapping("/application/{jobId}")
    public ResponseEntity<?> createApplication(
            @PathVariable Long jobId,
            @RequestBody Application application) {

        return ResponseEntity.ok(
                service.createApplication(jobId, application));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_READ')")
    @GetMapping("/applications")
    public ResponseEntity<?> getApplications() {

        return ResponseEntity.ok(
                service.getApplications());
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_READ')")
    @GetMapping("/application/{id}")
    public ResponseEntity<?> getApplication(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getApplication(id));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_UPDATE')")
    @PutMapping("/application/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @RequestBody Application application) {

        return ResponseEntity.ok(
                service.updateApplication(id, application));
    }

    @PreAuthorize("hasAuthority('JOB_APPLICATION_DELETE')")
    @DeleteMapping("/application/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteApplication(id));
    }


    // =========================================================
    // INTERVIEWS
    // =========================================================

    @PreAuthorize("hasAuthority('INTERVIEW_CREATE')")
    @PostMapping("/interview/{applicationId}")
    public ResponseEntity<?> createInterview(
            @PathVariable Long applicationId,
            @RequestBody Interview interview) {

        return ResponseEntity.ok(
                service.createInterview(
                        applicationId,
                        interview));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_READ')")
    @GetMapping("/interviews")
    public ResponseEntity<?> getInterviews() {

        return ResponseEntity.ok(
                service.getInterviews());
    }

    @PreAuthorize("hasAuthority('INTERVIEW_READ')")
    @GetMapping("/interview/{id}")
    public ResponseEntity<?> getInterview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInterview(id));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_UPDATE')")
    @PutMapping("/interview/{id}")
    public ResponseEntity<?> updateInterview(
            @PathVariable Long id,
            @RequestBody Interview interview) {

        return ResponseEntity.ok(
                service.updateInterview(id, interview));
    }

    @PreAuthorize("hasAuthority('INTERVIEW_DELETE')")
    @DeleteMapping("/interview/{id}")
    public ResponseEntity<?> deleteInterview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInterview(id));
    }


    // =========================================================
    // OFFER LETTERS
    // =========================================================

    @PreAuthorize("hasAuthority('OFFER_LETTER_CREATE')")
    @PostMapping("/offer/{applicationId}")
    public ResponseEntity<?> createOfferLetter(
            @PathVariable Long applicationId,
            @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(
                service.createOfferLetter(
                        applicationId,
                        offerLetter));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_READ')")
    @GetMapping("/offers")
    public ResponseEntity<?> getOfferLetters() {

        return ResponseEntity.ok(
                service.getOfferLetters());
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_READ')")
    @GetMapping("/offer/{id}")
    public ResponseEntity<?> getOfferLetter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getOfferLetter(id));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_UPDATE')")
    @PutMapping("/offer/{id}")
    public ResponseEntity<?> updateOfferLetter(
            @PathVariable Long id,
            @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(
                service.updateOfferLetter(
                        id,
                        offerLetter));
    }

    @PreAuthorize("hasAuthority('OFFER_LETTER_DELETE')")
    @DeleteMapping("/offer/{id}")
    public ResponseEntity<?> deleteOfferLetter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteOfferLetter(id));
    }


    // =========================================================
    // CANDIDATE DOCUMENTS
    // =========================================================

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_CREATE')")
    @PostMapping(
            value = "/document/{applicationId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCandidateDocument(

            @PathVariable Long applicationId,

            @RequestParam("aadhaarDocument")
            MultipartFile aadhaarDocument,

            @RequestParam("panDocument")
            MultipartFile panDocument,

            @RequestParam("degreeCertificate")
            MultipartFile degreeCertificate,

            @RequestParam("resume")
            MultipartFile resume,

            @RequestParam("verificationStatus")
            String verificationStatus) throws IOException {

        return ResponseEntity.ok(
                service.createCandidateDocument(
                        applicationId,
                        aadhaarDocument,
                        panDocument,
                        degreeCertificate,
                        resume,
                        verificationStatus));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_READ')")
    @GetMapping("/documents")
    public ResponseEntity<?> getCandidateDocuments() {

        return ResponseEntity.ok(
                service.getCandidateDocuments());
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_READ')")
    @GetMapping("/document/{id}")
    public ResponseEntity<?> getCandidateDocument(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCandidateDocument(id));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_UPDATE')")
    @PutMapping(
            value = "/document/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCandidateDocument(

            @PathVariable Long id,

            @RequestParam(
                    value = "aadhaarDocument",
                    required = false)
            MultipartFile aadhaarDocument,

            @RequestParam(
                    value = "panDocument",
                    required = false)
            MultipartFile panDocument,

            @RequestParam(
                    value = "degreeCertificate",
                    required = false)
            MultipartFile degreeCertificate,

            @RequestParam(
                    value = "resume",
                    required = false)
            MultipartFile resume,

            @RequestParam("verificationStatus")
            String verificationStatus) throws IOException {

        return ResponseEntity.ok(
                service.updateCandidateDocument(
                        id,
                        aadhaarDocument,
                        panDocument,
                        degreeCertificate,
                        resume,
                        verificationStatus));
    }

    @PreAuthorize("hasAuthority('CANDIDATE_DOCUMENT_DELETE')")
    @DeleteMapping("/document/{id}")
    public ResponseEntity<?> deleteCandidateDocument(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCandidateDocument(id));
    }


    // =========================================================
    // RECRUITMENT REPORTS
    // =========================================================

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/pending")
    public ResponseEntity<?> pendingApplications() {

        return ResponseEntity.ok(
                service.pendingApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/selected")
    public ResponseEntity<?> selectedApplications() {

        return ResponseEntity.ok(
                service.selectedApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/applications/rejected")
    public ResponseEntity<?> rejectedApplications() {

        return ResponseEntity.ok(
                service.rejectedApplications());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/interviews/completed")
    public ResponseEntity<?> completedInterviews() {

        return ResponseEntity.ok(
                service.completedInterviews());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/interviews/pending")
    public ResponseEntity<?> pendingInterviews() {

        return ResponseEntity.ok(
                service.pendingInterviews());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/offers/accepted")
    public ResponseEntity<?> acceptedOffers() {

        return ResponseEntity.ok(
                service.acceptedOffers());
    }

    @PreAuthorize("hasAuthority('RECRUITMENT_REPORT_READ')")
    @GetMapping("/offers/rejected")
    public ResponseEntity<?> rejectedOffers() {

        return ResponseEntity.ok(
                service.rejectedOffers());
    }


    // =========================================================
    // DASHBOARD COUNTS
    // =========================================================

    @PreAuthorize("hasAuthority('RECRUITMENT_DASHBOARD_READ')")
    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {

        return ResponseEntity.ok(
                service.getCounts());
    }
}