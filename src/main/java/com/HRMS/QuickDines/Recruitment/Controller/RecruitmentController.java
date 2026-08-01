package com.HRMS.QuickDines.Recruitment.Controller;

import com.HRMS.QuickDines.Recruitment.Services.RecruitmentService;
import com.HRMS.QuickDines.Recruitment.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService service;

    @PostMapping("/job")
    public ResponseEntity<?> createJobOpening(@RequestBody JobOpening jobOpening) {
        return ResponseEntity.ok(service.createJobOpening(jobOpening));
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getJobOpenings() {
        return ResponseEntity.ok(service.getJobOpenings());
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobOpening(@PathVariable Long id) {
        return ResponseEntity.ok(service.getJobOpening(id));
    }

    @PutMapping("/job/{id}")
    public ResponseEntity<?> updateJobOpening(
            @PathVariable Long id,
            @RequestBody JobOpening jobOpening) {

        return ResponseEntity.ok(service.updateJobOpening(id, jobOpening));
    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> deleteJobOpening(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteJobOpening(id));
    }



    @PostMapping("/application/{jobId}")
    public ResponseEntity<?> createApplication(
            @PathVariable Long jobId,
            @RequestBody Application application) {

        return ResponseEntity.ok(
                service.createApplication(jobId, application));
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getApplications() {

        return ResponseEntity.ok(
                service.getApplications());
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<?> getApplication(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getApplication(id));
    }

    @PutMapping("/application/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @RequestBody Application application) {

        return ResponseEntity.ok(
                service.updateApplication(id, application));
    }

    @DeleteMapping("/application/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteApplication(id));
    }

    //=================================
// INTERVIEWS
//=================================

    @PostMapping("/interview/{applicationId}")
    public ResponseEntity<?> createInterview(
            @PathVariable Long applicationId,
            @RequestBody Interview interview) {

        return ResponseEntity.ok(
                service.createInterview(applicationId, interview));
    }

    @GetMapping("/interviews")
    public ResponseEntity<?> getInterviews() {

        return ResponseEntity.ok(
                service.getInterviews());
    }

    @GetMapping("/interview/{id}")
    public ResponseEntity<?> getInterview(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getInterview(id));
    }

    @PutMapping("/interview/{id}")
    public ResponseEntity<?> updateInterview(
            @PathVariable Long id,
            @RequestBody Interview interview) {

        return ResponseEntity.ok(
                service.updateInterview(id, interview));
    }

    @DeleteMapping("/interview/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteInterview(id));
    }

    //=================================
// OFFER LETTERS
//=================================

    @PostMapping("/offer/{applicationId}")
    public ResponseEntity<?> createOfferLetter(
            @PathVariable Long applicationId,
            @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(
                service.createOfferLetter(applicationId, offerLetter));
    }

    @GetMapping("/offers")
    public ResponseEntity<?> getOfferLetters() {

        return ResponseEntity.ok(
                service.getOfferLetters());
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<?> getOfferLetter(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getOfferLetter(id));
    }

    @PutMapping("/offer/{id}")
    public ResponseEntity<?> updateOfferLetter(
            @PathVariable Long id,
            @RequestBody OfferLetter offerLetter) {

        return ResponseEntity.ok(
                service.updateOfferLetter(id, offerLetter));
    }

    @DeleteMapping("/offer/{id}")
    public ResponseEntity<?> deleteOfferLetter(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteOfferLetter(id));
    }

    //=================================
// CANDIDATE DOCUMENTS
//=================================

    @PostMapping(value = "/document/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCandidateDocument(

            @PathVariable Long applicationId,

            @RequestParam("aadhaarDocument") MultipartFile aadhaarDocument,

            @RequestParam("panDocument") MultipartFile panDocument,

            @RequestParam("degreeCertificate") MultipartFile degreeCertificate,

            @RequestParam("resume") MultipartFile resume,

            @RequestParam("verificationStatus") String verificationStatus) throws IOException {

        return ResponseEntity.ok(
                service.createCandidateDocument(
                        applicationId,
                        aadhaarDocument,
                        panDocument,
                        degreeCertificate,
                        resume,
                        verificationStatus));
    }

    @GetMapping("/documents")
    public ResponseEntity<?> getCandidateDocuments() {

        return ResponseEntity.ok(
                service.getCandidateDocuments());
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<?> getCandidateDocument(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCandidateDocument(id));
    }

    @PutMapping(value = "/document/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCandidateDocument(

            @PathVariable Long id,

            @RequestParam(value = "aadhaarDocument", required = false) MultipartFile aadhaarDocument,

            @RequestParam(value = "panDocument", required = false) MultipartFile panDocument,

            @RequestParam(value = "degreeCertificate", required = false) MultipartFile degreeCertificate,

            @RequestParam(value = "resume", required = false) MultipartFile resume,

            @RequestParam("verificationStatus") String verificationStatus) throws IOException {

        return ResponseEntity.ok(
                service.updateCandidateDocument(
                        id,
                        aadhaarDocument,
                        panDocument,
                        degreeCertificate,
                        resume,
                        verificationStatus));
    }

    @DeleteMapping("/document/{id}")
    public ResponseEntity<?> deleteCandidateDocument(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCandidateDocument(id));
    }

    //=================================
    // REPORTS
    //=================================

    @GetMapping("/applications/pending")
    public ResponseEntity<?> pendingApplications() {
        return ResponseEntity.ok(service.pendingApplications());
    }

    @GetMapping("/applications/selected")
    public ResponseEntity<?> selectedApplications() {
        return ResponseEntity.ok(service.selectedApplications());
    }

    @GetMapping("/applications/rejected")
    public ResponseEntity<?> rejectedApplications() {
        return ResponseEntity.ok(service.rejectedApplications());
    }

    @GetMapping("/interviews/completed")
    public ResponseEntity<?> completedInterviews() {
        return ResponseEntity.ok(service.completedInterviews());
    }

    @GetMapping("/interviews/pending")
    public ResponseEntity<?> pendingInterviews() {
        return ResponseEntity.ok(service.pendingInterviews());
    }

    @GetMapping("/offers/accepted")
    public ResponseEntity<?> acceptedOffers() {
        return ResponseEntity.ok(service.acceptedOffers());
    }

    @GetMapping("/offers/rejected")
    public ResponseEntity<?> rejectedOffers() {
        return ResponseEntity.ok(service.rejectedOffers());
    }

    //=================================
    // DASHBOARD COUNTS
    //=================================

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {
        return ResponseEntity.ok(service.getCounts());
    }

}
