package com.HRMS.QuickDines.Documents.Service;

import com.HRMS.QuickDines.AdvanceServices.CloudinaryService;
import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Documents.Entity.VerificationStatus;
import com.HRMS.QuickDines.Documents.model.DocumentTypes;
import com.HRMS.QuickDines.Documents.model.DocumentVerification;
import com.HRMS.QuickDines.Documents.model.Documents;
import com.HRMS.QuickDines.Documents.repo.DocumentTypesRepository;
import com.HRMS.QuickDines.Documents.repo.DocumentVerificationRepository;
import com.HRMS.QuickDines.Documents.repo.DocumentsRepository;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentsRepository documentsRepository;
    private final EmployeeRepository employeeRepository;
    private final CloudinaryService cloudinaryService;
    private final DocumentTypesRepository documentTypesRepository;
    private final DocumentVerificationRepository documentVerificationRepository;

    private final AuditLogsService auditLogsService;
    private final ClientInfoService clientInfoService;


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


    // =========================================================
    // AUDIT + ACTIVITY + SYSTEM LOG
    // =========================================================

    private void logDocumentActivity(
            String employeeId,
            String activityName,
            String description,
            ActivityStatus status) {

        auditLogsService.logActivity(
                employeeId,
                activityName,
                "DOCUMENTS",
                description,
                status,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );
    }


    private void logDocumentAudit(
            String action,
            String referenceId,
            String employeeId,
            String description,
            String oldValue,
            String newValue) {

        AuditActionType actionType;

        switch (action) {

            case "CREATE":
                actionType = AuditActionType.CREATE;
                break;

            case "UPDATE":
                actionType = AuditActionType.UPDATE;
                break;

            case "DELETE":
                actionType = AuditActionType.DELETE;
                break;

            case "APPROVE":
                actionType = AuditActionType.APPROVE;
                break;

            case "REJECT":
                actionType = AuditActionType.REJECT;
                break;

            default:
                actionType = AuditActionType.UPDATE;
                break;
        }

        auditLogsService.createAuditLog(
                "DOCUMENTS",
                referenceId,
                actionType,
                employeeId,
                employeeId,
                description,
                oldValue,
                newValue,
                getIpAddress(),
                getOperatingSystem()
        );
    }


    private void logDocumentSystem(
            String message) {

        auditLogsService.logInfo(
                "DOCUMENTS",
                "DocumentService",
                message
        );
    }


    private void logDocumentError(
            String operation,
            Exception exception) {

        auditLogsService.logError(
                "DOCUMENTS",
                "DocumentService",
                operation + " failed: " +
                        exception.getMessage(),
                exception.toString()
        );
    }


    // =========================================================
    // DOCUMENTS
    // =========================================================

    public String uploadDocuments(
            String employeeId,

            MultipartFile aadhaar,
            MultipartFile panCard,
            MultipartFile resume,
            MultipartFile ssc,
            MultipartFile intermediate,
            MultipartFile degree,
            MultipartFile pg,
            MultipartFile offerLetter,
            MultipartFile salarySlip) {

        try {

            Employee employee =
                    employeeRepository
                            .findById(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employee Not Found"));

            Documents documents = new Documents();

            documents.setEmployee(employee);

            if (aadhaar != null &&
                    !aadhaar.isEmpty()) {

                documents.setAadhaarDocument(
                        cloudinaryService
                                .uploadFile(aadhaar));
            }

            if (panCard != null &&
                    !panCard.isEmpty()) {

                documents.setPanCardDocument(
                        cloudinaryService
                                .uploadFile(panCard));
            }

            if (resume != null &&
                    !resume.isEmpty()) {

                documents.setResumeDocument(
                        cloudinaryService
                                .uploadFile(resume));
            }

            if (ssc != null &&
                    !ssc.isEmpty()) {

                documents.setSscDocument(
                        cloudinaryService
                                .uploadFile(ssc));
            }

            if (intermediate != null &&
                    !intermediate.isEmpty()) {

                documents.setIntermediateDocument(
                        cloudinaryService
                                .uploadFile(intermediate));
            }

            if (degree != null &&
                    !degree.isEmpty()) {

                documents.setDegreeDocument(
                        cloudinaryService
                                .uploadFile(degree));
            }

            if (pg != null &&
                    !pg.isEmpty()) {

                documents.setPgDocument(
                        cloudinaryService
                                .uploadFile(pg));
            }

            if (offerLetter != null &&
                    !offerLetter.isEmpty()) {

                documents.setOfferLetterDocument(
                        cloudinaryService
                                .uploadFile(offerLetter));
            }

            if (salarySlip != null &&
                    !salarySlip.isEmpty()) {

                documents.setSalarySlipDocument(
                        cloudinaryService
                                .uploadFile(salarySlip));
            }

            documents.setStatus("PENDING");

            documentsRepository.save(documents);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents uploaded successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "UPLOAD_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "CREATE",
                    employeeId,
                    performedBy,
                    description,
                    null,
                    "Documents uploaded with PENDING status"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Documents Uploaded Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Upload documents",
                    e
            );

            throw e;
        }
    }


    public Object getDocuments(
            String employeeId) {

        try {

            Object documents =
                    documentsRepository
                            .findByEmployeeEmployeeId(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Documents Not Found"));

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents retrieved successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "GET_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return documents;

        } catch (Exception e) {

            logDocumentError(
                    "Get documents",
                    e
            );

            throw e;
        }
    }


    public String updateDocuments(
            String employeeId,

            MultipartFile aadhaar,
            MultipartFile panCard,
            MultipartFile resume,
            MultipartFile ssc,
            MultipartFile intermediate,
            MultipartFile degree,
            MultipartFile pg,
            MultipartFile offerLetter,
            MultipartFile salarySlip) {

        try {

            Employee employee =
                    employeeRepository
                            .findById(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employee Not Found"));

            Documents documents =
                    documentsRepository
                            .findByEmployeeEmployeeId(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Documents Not Found"));

            if (aadhaar != null &&
                    !aadhaar.isEmpty()) {

                documents.setAadhaarDocument(
                        cloudinaryService
                                .uploadFile(aadhaar));
            }

            if (panCard != null &&
                    !panCard.isEmpty()) {

                documents.setPanCardDocument(
                        cloudinaryService
                                .uploadFile(panCard));
            }

            if (resume != null &&
                    !resume.isEmpty()) {

                documents.setResumeDocument(
                        cloudinaryService
                                .uploadFile(resume));
            }

            if (ssc != null &&
                    !ssc.isEmpty()) {

                documents.setSscDocument(
                        cloudinaryService
                                .uploadFile(ssc));
            }

            if (intermediate != null &&
                    !intermediate.isEmpty()) {

                documents.setIntermediateDocument(
                        cloudinaryService
                                .uploadFile(intermediate));
            }

            if (degree != null &&
                    !degree.isEmpty()) {

                documents.setDegreeDocument(
                        cloudinaryService
                                .uploadFile(degree));
            }

            if (pg != null &&
                    !pg.isEmpty()) {

                documents.setPgDocument(
                        cloudinaryService
                                .uploadFile(pg));
            }

            if (offerLetter != null &&
                    !offerLetter.isEmpty()) {

                documents.setOfferLetterDocument(
                        cloudinaryService
                                .uploadFile(offerLetter));
            }

            if (salarySlip != null &&
                    !salarySlip.isEmpty()) {

                documents.setSalarySlipDocument(
                        cloudinaryService
                                .uploadFile(salarySlip));
            }

            documents.setStatus("PENDING");

            documentsRepository.save(documents);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents updated successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "UPDATE_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "UPDATE",
                    employeeId,
                    performedBy,
                    description,
                    "Existing documents",
                    "Documents updated and status changed to PENDING"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Documents Updated Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Update documents",
                    e
            );

            throw e;
        }
    }


    public String deleteDocuments(
            String employeeId) {

        try {

            Documents documents =
                    documentsRepository
                            .findByEmployeeEmployeeId(
                                    employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Documents Not Found"));

            documentsRepository.delete(documents);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents deleted successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "DELETE_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "DELETE",
                    employeeId,
                    performedBy,
                    description,
                    "Employee documents existed",
                    null
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Documents Deleted Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Delete documents",
                    e
            );

            throw e;
        }
    }


    // =========================================================
    // DOCUMENT TYPES
    // =========================================================

    public String createDocumentType(
            DocumentTypes documentTypes) {

        try {

            documentTypesRepository
                    .save(documentTypes);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Document type created successfully: "
                            + documentTypes.getDocumentName();

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "CREATE_DOCUMENT_TYPE",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "CREATE",
                    documentTypes.getId().toString(),
                    performedBy,
                    description,
                    null,
                    "Document type created"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Document Type Created Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Create document type",
                    e
            );

            throw e;
        }
    }


    public Object getDocumentTypes() {

        try {

            Object result =
                    documentTypesRepository.findAll();

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "All document types retrieved successfully";

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "GET_DOCUMENT_TYPES",
                    description,
                    ActivityStatus.SUCCESS
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description +
                            ". Count: " +
                            documentTypesRepository
                                    .count()
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get document types",
                    e
            );

            throw e;
        }
    }


    public Object getDocumentType(
            Long id) {

        try {

            Object result =
                    documentTypesRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document Type Not Found"));

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Document type retrieved successfully. ID: "
                            + id;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "GET_DOCUMENT_TYPE",
                    description,
                    ActivityStatus.SUCCESS
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get document type",
                    e
            );

            throw e;
        }
    }


    public String updateDocumentType(
            Long id,
            DocumentTypes documentTypes) {

        try {

            DocumentTypes existingType =
                    documentTypesRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document Type Not Found"));

            existingType.setDocumentName(
                    documentTypes.getDocumentName());

            existingType.setDescription(
                    documentTypes.getDescription());

            existingType.setIsMandatory(
                    documentTypes.getIsMandatory());

            existingType.setStatus(
                    documentTypes.getStatus());

            documentTypesRepository
                    .save(existingType);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Document type updated successfully. ID: "
                            + id;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "UPDATE_DOCUMENT_TYPE",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "UPDATE",
                    id.toString(),
                    performedBy,
                    description,
                    "Existing document type",
                    "Document type updated"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Document Type Updated Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Update document type",
                    e
            );

            throw e;
        }
    }


    public String deleteDocumentType(
            Long id) {

        try {

            DocumentTypes documentType =
                    documentTypesRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document Type Not Found"));

            documentTypesRepository
                    .delete(documentType);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Document type deleted successfully. ID: "
                            + id;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "DELETE_DOCUMENT_TYPE",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "DELETE",
                    id.toString(),
                    performedBy,
                    description,
                    "Document type existed",
                    null
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Document Type Deleted Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Delete document type",
                    e
            );

            throw e;
        }
    }


    // =========================================================
    // DOCUMENT VERIFICATION
    // =========================================================

    public String verifyDocument(
            String employeeId,
            String verifiedBy,
            String remarks) {

        try {

            Employee employee =
                    employeeRepository
                            .findById(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employee Not Found"));

            Documents documents =
                    documentsRepository
                            .findByEmployeeEmployeeId(
                                    employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Documents Not Found"));

            documents.setStatus("VERIFIED");

            documentsRepository.save(documents);

            DocumentVerification verification =
                    documentVerificationRepository
                            .findByEmployeeEmployeeId(employeeId)
                            .orElse(new DocumentVerification());

            verification.setEmployee(employee);

            verification.setVerifiedBy(verifiedBy);

            verification.setVerificationStatus(
                    VerificationStatus.valueOf("VERIFIED"));

            verification.setRemarks(remarks);

            verification.setVerifiedDate(
                    LocalDate.now());

            documentVerificationRepository
                    .save(verification);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents verified successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "VERIFY_DOCUMENT",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "APPROVE",
                    employeeId,
                    performedBy,
                    description,
                    "PENDING",
                    "VERIFIED"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Documents Verified Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Verify document",
                    e
            );

            throw e;
        }
    }


    public Object getVerification(
            String employeeId) {

        try {

            Object verification =
                    documentVerificationRepository
                            .findByEmployeeEmployeeId(
                                    employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Verification Details Not Found"));

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Document verification details retrieved successfully for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "GET_DOCUMENT_VERIFICATION",
                    description,
                    ActivityStatus.SUCCESS
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return verification;

        } catch (Exception e) {

            logDocumentError(
                    "Get document verification",
                    e
            );

            throw e;
        }
    }


    public String rejectDocument(
            String employeeId,
            String verifiedBy,
            String remarks) {

        try {

            Employee employee =
                    employeeRepository
                            .findById(employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employee Not Found"));

            Documents documents =
                    documentsRepository
                            .findByEmployeeEmployeeId(
                                    employeeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Documents Not Found"));

            documents.setStatus("REJECTED");

            documentsRepository.save(documents);

            DocumentVerification verification =
                    documentVerificationRepository
                            .findByEmployeeEmployeeId(employeeId)
                            .orElse(new DocumentVerification());

            verification.setEmployee(employee);

            verification.setVerifiedBy(verifiedBy);

            verification.setVerificationStatus(
                    VerificationStatus.valueOf("REJECTED"));

            verification.setRemarks(remarks);

            verification.setVerifiedDate(
                    LocalDate.now());

            documentVerificationRepository
                    .save(verification);

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Documents rejected for employee: "
                            + employeeId;

            // ACTIVITY LOG
            logDocumentActivity(
                    performedBy,
                    "REJECT_DOCUMENT",
                    description,
                    ActivityStatus.SUCCESS
            );

            // AUDIT LOG
            logDocumentAudit(
                    "REJECT",
                    employeeId,
                    performedBy,
                    description,
                    "PENDING",
                    "REJECTED"
            );

            // SYSTEM LOG
            logDocumentSystem(
                    description
            );

            return "Documents Rejected Successfully";

        } catch (Exception e) {

            logDocumentError(
                    "Reject document",
                    e
            );

            throw e;
        }
    }


    // =========================================================
    // REPORTS
    // =========================================================

    public Object getPendingDocuments() {

        try {

            Object result =
                    documentsRepository
                            .findByStatus("PENDING");

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Pending documents retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_PENDING_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get pending documents",
                    e
            );

            throw e;
        }
    }


    public Object getVerifiedDocuments() {

        try {

            Object result =
                    documentsRepository
                            .findByStatus("VERIFIED");

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Verified documents retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_VERIFIED_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get verified documents",
                    e
            );

            throw e;
        }
    }


    public Object getRejectedDocuments() {

        try {

            Object result =
                    documentsRepository
                            .findByStatus("REJECTED");

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Rejected documents retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_REJECTED_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get rejected documents",
                    e
            );

            throw e;
        }
    }


    public Object getAllDocuments() {

        try {

            Object result =
                    documentsRepository.findAll();

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "All documents retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_ALL_DOCUMENTS",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description +
                            ". Count: " +
                            documentsRepository.count()
            );

            return result;

        } catch (Exception e) {

            logDocumentError(
                    "Get all documents",
                    e
            );

            throw e;
        }
    }


    // =========================================================
    // DOCUMENT COUNTS
    // =========================================================

    public Object getPendingCount() {

        try {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "pendingDocuments",
                    documentsRepository
                            .countByStatus("PENDING")
            );

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Pending document count retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_PENDING_DOCUMENT_COUNT",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return response;

        } catch (Exception e) {

            logDocumentError(
                    "Get pending document count",
                    e
            );

            throw e;
        }
    }


    public Object getVerifiedCount() {

        try {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "verifiedDocuments",
                    documentsRepository
                            .countByStatus("VERIFIED")
            );

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Verified document count retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_VERIFIED_DOCUMENT_COUNT",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return response;

        } catch (Exception e) {

            logDocumentError(
                    "Get verified document count",
                    e
            );

            throw e;
        }
    }


    public Object getRejectedCount() {

        try {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "rejectedDocuments",
                    documentsRepository
                            .countByStatus("REJECTED")
            );

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Rejected document count retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_REJECTED_DOCUMENT_COUNT",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return response;

        } catch (Exception e) {

            logDocumentError(
                    "Get rejected document count",
                    e
            );

            throw e;
        }
    }


    public Object getTotalDocumentsCount() {

        try {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "totalDocuments",
                    documentsRepository.count()
            );

            String performedBy =
                    getLoggedInEmployeeId();

            String description =
                    "Total document count retrieved successfully";

            logDocumentActivity(
                    performedBy,
                    "GET_TOTAL_DOCUMENT_COUNT",
                    description,
                    ActivityStatus.SUCCESS
            );

            logDocumentSystem(
                    description
            );

            return response;

        } catch (Exception e) {

            logDocumentError(
                    "Get total document count",
                    e
            );

            throw e;
        }
    }
    // =========================================================
// FILTER BY VERIFICATION STATUS
// =========================================================

    public List<DocumentVerification> getByVerificationStatus(
            VerificationStatus status) {

        if (status == null) {
            throw new RuntimeException(
                    "Verification status is required");
        }

        return documentVerificationRepository
                .findByVerificationStatus(status);
    }
    // =========================================================
// GET DOCUMENT VERIFICATION BY EMPLOYEE ID
// =========================================================

    public DocumentVerification getByEmployeeId(
            String employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee Not Found"));

        return documentVerificationRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Document Verification Not Found"));
    }

}

