package com.HRMS.QuickDines.Documents.Controller;

import com.HRMS.QuickDines.Documents.Service.DocumentService;
import com.HRMS.QuickDines.Documents.model.DocumentTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    // =========================================================
    // DOCUMENTS
    // =========================================================

    @PostMapping("/upload/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_CREATE')")
    public ResponseEntity<?> uploadDocuments(
            @PathVariable String employeeId,

            @RequestParam(required = false) MultipartFile aadhaar,
            @RequestParam(required = false) MultipartFile panCard,
            @RequestParam(required = false) MultipartFile resume,
            @RequestParam(required = false) MultipartFile ssc,
            @RequestParam(required = false) MultipartFile intermediate,
            @RequestParam(required = false) MultipartFile degree,
            @RequestParam(required = false) MultipartFile pg,
            @RequestParam(required = false) MultipartFile offerLetter,
            @RequestParam(required = false) MultipartFile salarySlip) {

        return ResponseEntity.ok(
                service.uploadDocuments(
                        employeeId,
                        aadhaar,
                        panCard,
                        resume,
                        ssc,
                        intermediate,
                        degree,
                        pg,
                        offerLetter,
                        salarySlip
                )
        );
    }


    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW')")
    public ResponseEntity<?> getDocuments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getDocuments(employeeId)
        );
    }


    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_UPDATE')")
    public ResponseEntity<?> updateDocuments(
            @PathVariable String employeeId,

            @RequestParam(required = false) MultipartFile aadhaar,
            @RequestParam(required = false) MultipartFile panCard,
            @RequestParam(required = false) MultipartFile resume,
            @RequestParam(required = false) MultipartFile ssc,
            @RequestParam(required = false) MultipartFile intermediate,
            @RequestParam(required = false) MultipartFile degree,
            @RequestParam(required = false) MultipartFile pg,
            @RequestParam(required = false) MultipartFile offerLetter,
            @RequestParam(required = false) MultipartFile salarySlip) {

        return ResponseEntity.ok(
                service.updateDocuments(
                        employeeId,
                        aadhaar,
                        panCard,
                        resume,
                        ssc,
                        intermediate,
                        degree,
                        pg,
                        offerLetter,
                        salarySlip
                )
        );
    }


    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_DELETE')")
    public ResponseEntity<?> deleteDocuments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.deleteDocuments(employeeId)
        );
    }


    // =========================================================
    // DOCUMENT TYPES
    // =========================================================

    @PostMapping("/type")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_CREATE')")
    public ResponseEntity<?> createDocumentType(
            @RequestBody DocumentTypes documentTypes) {

        return ResponseEntity.ok(
                service.createDocumentType(documentTypes)
        );
    }


    @GetMapping("/types")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_VIEW')")
    public ResponseEntity<?> getDocumentTypes() {

        return ResponseEntity.ok(
                service.getDocumentTypes()
        );
    }


    @GetMapping("/type/{id}")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_VIEW')")
    public ResponseEntity<?> getDocumentType(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getDocumentType(id)
        );
    }


    @PutMapping("/type/{id}")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_UPDATE')")
    public ResponseEntity<?> updateDocumentType(
            @PathVariable Long id,
            @RequestBody DocumentTypes documentTypes) {

        return ResponseEntity.ok(
                service.updateDocumentType(id, documentTypes)
        );
    }


    @DeleteMapping("/type/{id}")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_DELETE')")
    public ResponseEntity<?> deleteDocumentType(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteDocumentType(id)
        );
    }


    // =========================================================
    // DOCUMENT VERIFICATION
    // =========================================================

    @PostMapping("/verify/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_VERIFY')")
    public ResponseEntity<?> verifyDocument(
            @PathVariable String employeeId,
            @RequestParam String verifiedBy,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                service.verifyDocument(
                        employeeId,
                        verifiedBy,
                        remarks
                )
        );
    }


    @GetMapping("/verification/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_VERIFICATION_VIEW')")
    public ResponseEntity<?> getVerification(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getVerification(employeeId)
        );
    }


    @PutMapping("/reject/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_REJECT')")
    public ResponseEntity<?> rejectDocument(
            @PathVariable String employeeId,
            @RequestParam String verifiedBy,
            @RequestParam String remarks) {

        return ResponseEntity.ok(
                service.rejectDocument(
                        employeeId,
                        verifiedBy,
                        remarks
                )
        );
    }


    // =========================================================
    // REPORTS
    // =========================================================

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('DOCUMENT_PENDING_VIEW')")
    public ResponseEntity<?> getPendingDocuments() {

        return ResponseEntity.ok(
                service.getPendingDocuments()
        );
    }


    @GetMapping("/verified")
    @PreAuthorize("hasAuthority('DOCUMENT_VERIFIED_VIEW')")
    public ResponseEntity<?> getVerifiedDocuments() {

        return ResponseEntity.ok(
                service.getVerifiedDocuments()
        );
    }


    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('DOCUMENT_REJECTED_VIEW')")
    public ResponseEntity<?> getRejectedDocuments() {

        return ResponseEntity.ok(
                service.getRejectedDocuments()
        );
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW_ALL')")
    public ResponseEntity<?> getAllDocuments() {

        return ResponseEntity.ok(
                service.getAllDocuments()
        );
    }


    // =========================================================
    // DOCUMENT COUNT
    // =========================================================

    @GetMapping("/count/pending")
    @PreAuthorize("hasAuthority('DOCUMENT_PENDING_COUNT')")
    public ResponseEntity<?> getPendingCount() {

        return ResponseEntity.ok(
                service.getPendingCount()
        );
    }


    @GetMapping("/count/verified")
    @PreAuthorize("hasAuthority('DOCUMENT_VERIFIED_COUNT')")
    public ResponseEntity<?> getVerifiedCount() {

        return ResponseEntity.ok(
                service.getVerifiedCount()
        );
    }


    @GetMapping("/count/rejected")
    @PreAuthorize("hasAuthority('DOCUMENT_REJECTED_COUNT')")
    public ResponseEntity<?> getRejectedCount() {

        return ResponseEntity.ok(
                service.getRejectedCount()
        );
    }


    @GetMapping("/count/total")
    @PreAuthorize("hasAuthority('DOCUMENT_TOTAL_COUNT')")
    public ResponseEntity<?> getTotalDocumentsCount() {

        return ResponseEntity.ok(
                service.getTotalDocumentsCount()
        );
    }
}