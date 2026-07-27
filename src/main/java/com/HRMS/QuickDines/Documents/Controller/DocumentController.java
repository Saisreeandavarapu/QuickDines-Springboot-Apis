package com.HRMS.QuickDines.Documents.Controller;

import com.HRMS.QuickDines.Documents.Service.DocumentService;
import com.HRMS.QuickDines.Documents.model.DocumentTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    //=================================
// DOCUMENTS
//=================================

    @PostMapping("/upload/{employeeId}")
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

        return ResponseEntity.ok(service.uploadDocuments(
                        employeeId,
                        aadhaar,
                        panCard,
                        resume,
                        ssc,
                        intermediate,
                        degree,
                        pg,
                        offerLetter,
                        salarySlip));
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getDocuments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getDocuments(employeeId));
    }


    @PutMapping("/{employeeId}")
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

        return ResponseEntity.ok(service.updateDocuments(
                        employeeId,
                        aadhaar,
                        panCard,
                        resume,
                        ssc,
                        intermediate,
                        degree,
                        pg,
                        offerLetter,
                        salarySlip));
    }


    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteDocuments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.deleteDocuments(employeeId));
    }


    //=================================
// DOCUMENT TYPES
//=================================

    @PostMapping("/type")
    public ResponseEntity<?> createDocumentType(
            @RequestBody DocumentTypes documentTypes){

        return ResponseEntity.ok(service.createDocumentType(documentTypes));
    }


    @GetMapping("/types")
    public ResponseEntity<?> getDocumentTypes(){

        return ResponseEntity.ok(service.getDocumentTypes());
    }


    @GetMapping("/type/{id}")
    public ResponseEntity<?> getDocumentType(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getDocumentType(id));
    }


    @PutMapping("/type/{id}")
    public ResponseEntity<?> updateDocumentType(
            @PathVariable Long id,
            @RequestBody DocumentTypes documentTypes){

        return ResponseEntity.ok(service.updateDocumentType(id, documentTypes));
    }


    @DeleteMapping("/type/{id}")
    public ResponseEntity<?> deleteDocumentType(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteDocumentType(id));
    }



    //=================================
// DOCUMENT VERIFICATION
//=================================

    @PostMapping("/verify/{employeeId}")
    public ResponseEntity<?> verifyDocument(
            @PathVariable String employeeId,
            @RequestParam String verifiedBy,
            @RequestParam(required = false) String remarks){

        return ResponseEntity.ok(
                service.verifyDocument(
                        employeeId,
                        verifiedBy,
                        remarks));
    }


    @GetMapping("/verification/{employeeId}")
    public ResponseEntity<?> getVerification(
            @PathVariable String employeeId){

        return ResponseEntity.ok(
                service.getVerification(employeeId));
    }


    @PutMapping("/reject/{employeeId}")
    public ResponseEntity<?> rejectDocument(
            @PathVariable String employeeId,
            @RequestParam String verifiedBy,
            @RequestParam String remarks){

        return ResponseEntity.ok(
                service.rejectDocument(
                        employeeId,
                        verifiedBy,
                        remarks));
    }


    //=================================
// REPORTS
//=================================

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingDocuments(){

        return ResponseEntity.ok(service.getPendingDocuments());
    }


    @GetMapping("/verified")
    public ResponseEntity<?> getVerifiedDocuments(){

        return ResponseEntity.ok(service.getVerifiedDocuments());
    }


    @GetMapping("/rejected")
    public ResponseEntity<?> getRejectedDocuments(){

        return ResponseEntity.ok(service.getRejectedDocuments());
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllDocuments(){

        return ResponseEntity.ok(service.getAllDocuments());
    }

    //=================================
// DOCUMENT COUNT
//=================================

    @GetMapping("/count/pending")
    public ResponseEntity<?> getPendingCount() {

        return ResponseEntity.ok(
                service.getPendingCount());
    }


    @GetMapping("/count/verified")
    public ResponseEntity<?> getVerifiedCount() {

        return ResponseEntity.ok(
                service.getVerifiedCount());
    }


    @GetMapping("/count/rejected")
    public ResponseEntity<?> getRejectedCount() {

        return ResponseEntity.ok(
                service.getRejectedCount());
    }


    @GetMapping("/count/total")
    public ResponseEntity<?> getTotalDocumentsCount() {

        return ResponseEntity.ok(
                service.getTotalDocumentsCount());
    }
}
