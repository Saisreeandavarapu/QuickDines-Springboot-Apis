package com.HRMS.QuickDines.Documents.Service;

import com.HRMS.QuickDines.AdvanceServices.CloudinaryService;
import com.HRMS.QuickDines.Documents.model.DocumentTypes;
import com.HRMS.QuickDines.Documents.model.DocumentVerification;
import com.HRMS.QuickDines.Documents.model.Documents;
import com.HRMS.QuickDines.Documents.repo.DocumentTypesRepository;
import com.HRMS.QuickDines.Documents.repo.DocumentVerificationRepository;
import com.HRMS.QuickDines.Documents.repo.DocumentsRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentsRepository documentsRepository;
    private final EmployeeRepository employeeRepository;
    private final CloudinaryService cloudinaryService;
    private final DocumentTypesRepository documentTypesRepository;
    private final DocumentVerificationRepository documentVerificationRepository;
    //=================================
    // DOCUMENTS
    //=================================

    public String uploadDocuments(String employeeId,

            MultipartFile aadhaar,
            MultipartFile panCard,
            MultipartFile resume,
            MultipartFile ssc,
            MultipartFile intermediate,
            MultipartFile degree,
            MultipartFile pg,
            MultipartFile offerLetter,
            MultipartFile salarySlip) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        Documents documents = new Documents();

        documents.setEmployee(employee);

        if (aadhaar != null && !aadhaar.isEmpty()) {
            documents.setAadhaarDocument(
                    cloudinaryService.uploadFile(aadhaar));
        }

        if (panCard != null && !panCard.isEmpty()) {
            documents.setPanCardDocument(
                    cloudinaryService.uploadFile(panCard));
        }

        if (resume != null && !resume.isEmpty()) {
            documents.setResumeDocument(
                    cloudinaryService.uploadFile(resume));
        }

        if (ssc != null && !ssc.isEmpty()) {
            documents.setSscDocument(
                    cloudinaryService.uploadFile(ssc));
        }

        if (intermediate != null && !intermediate.isEmpty()) {
            documents.setIntermediateDocument(
                    cloudinaryService.uploadFile(intermediate));
        }

        if (degree != null && !degree.isEmpty()) {
            documents.setDegreeDocument(
                    cloudinaryService.uploadFile(degree));
        }

        if (pg != null && !pg.isEmpty()) {
            documents.setPgDocument(
                    cloudinaryService.uploadFile(pg));
        }

        if (offerLetter != null && !offerLetter.isEmpty()) {
            documents.setOfferLetterDocument(
                    cloudinaryService.uploadFile(offerLetter));
        }

        if (salarySlip != null && !salarySlip.isEmpty()) {
            documents.setSalarySlipDocument(
                    cloudinaryService.uploadFile(salarySlip));
        }

        documents.setStatus("PENDING");

        documentsRepository.save(documents);

        return "Documents Uploaded Successfully";
    }

    public Object getDocuments(String employeeId) {

        return documentsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Documents Not Found"));
    }

    public String updateDocuments(String employeeId,

            MultipartFile aadhaar,

            MultipartFile panCard,

            MultipartFile resume,

            MultipartFile ssc,

            MultipartFile intermediate,

            MultipartFile degree,

            MultipartFile pg,

            MultipartFile offerLetter,

            MultipartFile salarySlip) {


        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));


        Documents documents = documentsRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Documents Not Found"));


        if (aadhaar != null && !aadhaar.isEmpty()) {

            documents.setAadhaarDocument(
                    cloudinaryService.uploadFile(aadhaar));
        }


        if (panCard != null && !panCard.isEmpty()) {

            documents.setPanCardDocument(
                    cloudinaryService.uploadFile(panCard));
        }


        if (resume != null && !resume.isEmpty()) {

            documents.setResumeDocument(
                    cloudinaryService.uploadFile(resume));
        }


        if (ssc != null && !ssc.isEmpty()) {

            documents.setSscDocument(
                    cloudinaryService.uploadFile(ssc));
        }


        if (intermediate != null && !intermediate.isEmpty()) {

            documents.setIntermediateDocument(
                    cloudinaryService.uploadFile(intermediate));
        }


        if (degree != null && !degree.isEmpty()) {

            documents.setDegreeDocument(
                    cloudinaryService.uploadFile(degree));
        }


        if (pg != null && !pg.isEmpty()) {

            documents.setPgDocument(
                    cloudinaryService.uploadFile(pg));
        }


        if (offerLetter != null && !offerLetter.isEmpty()) {

            documents.setOfferLetterDocument(
                    cloudinaryService.uploadFile(offerLetter));
        }


        if (salarySlip != null && !salarySlip.isEmpty()) {

            documents.setSalarySlipDocument(
                    cloudinaryService.uploadFile(salarySlip));
        }


        documents.setStatus("PENDING");

        documentsRepository.save(documents);


        return "Documents Updated Successfully";
    }

    public String deleteDocuments(String employeeId) {

        Documents documents = documentsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() ->
                                new RuntimeException("Documents Not Found"));
        documentsRepository.delete(documents);

        return "Documents Deleted Successfully";
    }



    //=================================
// DOCUMENT TYPES
//=================================

    public String createDocumentType(DocumentTypes documentTypes){
        documentTypesRepository.save(documentTypes);
        return "Document Type Created Successfully";
    }


    public Object getDocumentTypes(){
        return documentTypesRepository.findAll();
    }


    public Object getDocumentType(Long id){
        return documentTypesRepository.findById(id).orElseThrow(() -> new RuntimeException("Document Type Not Found"));
    }


    public String updateDocumentType(Long id, DocumentTypes documentTypes){

        DocumentTypes existingType = documentTypesRepository.findById(id).orElseThrow(() -> new RuntimeException("Document Type Not Found"));

        existingType.setDocumentName(
                documentTypes.getDocumentName());

        existingType.setDescription(
                documentTypes.getDescription());

        existingType.setIsMandatory(
                documentTypes.getIsMandatory());

        existingType.setStatus(
                documentTypes.getStatus());

        documentTypesRepository.save(existingType);

        return "Document Type Updated Successfully";
    }


    public String deleteDocumentType(Long id){

        DocumentTypes documentType = documentTypesRepository.findById(id).orElseThrow(() -> new RuntimeException("Document Type Not Found"));
        documentTypesRepository.delete(documentType);
        return "Document Type Deleted Successfully";
    }



    //=================================
    // DOCUMENT VERIFICATION
    //=================================

    public String verifyDocument(String employeeId, String verifiedBy, String remarks){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Documents documents = documentsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Documents Not Found"));

        documents.setStatus("VERIFIED");

        documentsRepository.save(documents);


        DocumentVerification verification = documentVerificationRepository.findByEmployeeEmployeeId(employeeId)
                        .orElse(new DocumentVerification());
        verification.setEmployee(employee);

        verification.setVerifiedBy(verifiedBy);

        verification.setVerificationStatus("VERIFIED");

        verification.setRemarks(remarks);

        verification.setVerifiedDate(LocalDate.now());

        documentVerificationRepository.save(verification);

        return "Documents Verified Successfully";
    }


    public Object getVerification(String employeeId){

        return documentVerificationRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() ->
                        new RuntimeException("Verification Details Not Found"));
    }


    public String rejectDocument(String employeeId, String verifiedBy, String remarks){

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Documents documents = documentsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() ->
                        new RuntimeException("Documents Not Found"));

        documents.setStatus("REJECTED");

        documentsRepository.save(documents);


        DocumentVerification verification = documentVerificationRepository.findByEmployeeEmployeeId(employeeId).orElse(new DocumentVerification());

        verification.setEmployee(employee);

        verification.setVerifiedBy(verifiedBy);

        verification.setVerificationStatus("REJECTED");

        verification.setRemarks(remarks);

        verification.setVerifiedDate(LocalDate.now());

        documentVerificationRepository.save(verification);

        return "Documents Rejected Successfully";
    }



//=================================
// REPORTS
//=================================

    public Object getPendingDocuments(){

        return documentsRepository.findByStatus("PENDING");
    }


    public Object getVerifiedDocuments(){

        return documentsRepository.findByStatus("VERIFIED");
    }


    public Object getRejectedDocuments(){

        return documentsRepository.findByStatus("REJECTED");
    }


    public Object getAllDocuments(){

        return documentsRepository.findAll();
    }

    //=================================
// DOCUMENT COUNTS
//=================================

    public Object getPendingCount() {

        Map<String, Object> response = new HashMap<>();

        response.put("pendingDocuments",
                documentsRepository.countByStatus("PENDING"));

        return response;
    }


    public Object getVerifiedCount() {

        Map<String, Object> response = new HashMap<>();

        response.put("verifiedDocuments",
                documentsRepository.countByStatus("VERIFIED"));

        return response;
    }


    public Object getRejectedCount() {

        Map<String, Object> response = new HashMap<>();

        response.put("rejectedDocuments",
                documentsRepository.countByStatus("REJECTED"));

        return response;
    }


    public Object getTotalDocumentsCount() {

        Map<String, Object> response = new HashMap<>();

        response.put("totalDocuments",
                documentsRepository.count());

        return response;
    }

}
