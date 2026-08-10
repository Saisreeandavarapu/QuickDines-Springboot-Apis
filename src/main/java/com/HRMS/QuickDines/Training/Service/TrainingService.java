package com.HRMS.QuickDines.Training.Service;

import com.HRMS.QuickDines.AuditLogs.Entity.ActivityStatus;
import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.Service.ClientInfoService;
import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Training.model.CourseCompletion;
import com.HRMS.QuickDines.Training.model.Training;
import com.HRMS.QuickDines.Training.model.TrainingAssignment;
import com.HRMS.QuickDines.Training.model.TrainingCertificate;
import com.HRMS.QuickDines.Training.repo.CourseCompletionRepository;
import com.HRMS.QuickDines.Training.repo.TrainingAssignmentRepository;
import com.HRMS.QuickDines.Training.repo.TrainingCertificateRepository;
import com.HRMS.QuickDines.Training.repo.TrainingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingAssignmentRepository trainingAssignmentRepository;
    private final CourseCompletionRepository courseCompletionRepository;
    private final TrainingCertificateRepository trainingCertificateRepository;
    private final EmployeeRepository employeeRepository;
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



//=========================================================
// TRAININGS
//=========================================================

    public String createTraining(Training training) {

        trainingRepository.save(training);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "TRAINING",
                String.valueOf(training.getId()),
                performedBy,
                String.valueOf(training.getId()),
                "Training created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_TRAINING",
                "TRAINING",
                "Training created successfully: "
                        + training.getTrainingTitle(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training created successfully: "
                        + training.getTrainingTitle()
        );

        return "Training Created Successfully";
    }


    public List<Training> getAllTrainings() {

        return trainingRepository.findAll();
    }


    public Training getTraining(Long id) {

        return trainingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Training Not Found"));
    }


    public String updateTraining(Long id, Training training) {

        Training existing = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Training Not Found"));

        // Capture old value before update
        String oldValue = convertToJson(existing);

        existing.setTrainingCode(training.getTrainingCode());
        existing.setTrainingTitle(training.getTrainingTitle());
        existing.setTrainingCategory(training.getTrainingCategory());
        existing.setTrainerName(training.getTrainerName());
        existing.setTrainingMode(training.getTrainingMode());
        existing.setStartDate(training.getStartDate());
        existing.setEndDate(training.getEndDate());
        existing.setDurationHours(training.getDurationHours());
        existing.setLocation(training.getLocation());
        existing.setDescription(training.getDescription());
        existing.setStatus(training.getStatus());
        existing.setCreatedBy(training.getCreatedBy());

        trainingRepository.save(existing);

        // Capture new value after update
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TRAINING",
                String.valueOf(id),
                performedBy,
                String.valueOf(id),
                "Training updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TRAINING",
                "TRAINING",
                "Training updated successfully: "
                        + existing.getTrainingTitle(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training updated successfully. Training ID: "
                        + id
        );

        return "Training Updated Successfully";
    }


    public String deleteTraining(Long id) {

        Training existing = trainingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Training Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        trainingRepository.delete(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog(
                "TRAINING",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Training deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TRAINING",
                "TRAINING",
                "Training deleted successfully: "
                        + existing.getTrainingTitle(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training deleted successfully. Training ID: "
                        + id
        );

        return "Training Deleted Successfully";
    }


//=========================================================
// TRAINING ASSIGNMENTS
//=========================================================

    public String createAssignment(
            Long trainingId,
            String employeeId,
            TrainingAssignment assignment) {

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() ->
                        new RuntimeException("Training Not Found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        assignment.setTraining(training);
        assignment.setEmployee(employee);

        trainingAssignmentRepository.save(assignment);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logCreate(
                "TRAINING",
                String.valueOf(assignment.getId()),
                performedBy,
                employeeId,
                "Training assigned successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "ASSIGN_TRAINING",
                "TRAINING",
                "Training '"
                        + training.getTrainingTitle()
                        + "' assigned to employee: "
                        + employeeId,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training assigned successfully to employee: "
                        + employeeId
        );

        return "Training Assigned Successfully";
    }


    public List<TrainingAssignment> getAllAssignments() {

        return trainingAssignmentRepository.findAll();
    }


    public TrainingAssignment getAssignment(Long id) {

        return trainingAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Training Assignment Not Found"));
    }


    public List<TrainingAssignment> getEmployeeAssignments(
            String employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee Not Found"));

        return trainingAssignmentRepository.findByEmployee(employee);
    }


    public List<TrainingAssignment> getTrainingAssignments(
            Long trainingId) {

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() ->
                        new RuntimeException("Training Not Found"));

        return trainingAssignmentRepository.findByTraining(training);
    }


    public String updateAssignment(
            Long id,
            TrainingAssignment assignment) {

        TrainingAssignment existing =
                trainingAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Training Assignment Not Found"));

        // Capture old value
        String oldValue = convertToJson(existing);

        existing.setAssignedDate(
                assignment.getAssignedDate());

        existing.setDueDate(
                assignment.getDueDate());

        existing.setAssignmentStatus(
                assignment.getAssignmentStatus());

        existing.setRemarks(
                assignment.getRemarks());

        existing.setAssignedBy(
                assignment.getAssignedBy());

        trainingAssignmentRepository.save(existing);

        // Capture new value
        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TRAINING",
                String.valueOf(id),
                performedBy,
                String.valueOf(id),
                "Training assignment updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TRAINING_ASSIGNMENT",
                "TRAINING",
                "Training assignment updated successfully. "
                        + "Assignment ID: "
                        + id,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training assignment updated successfully. "
                        + "Assignment ID: "
                        + id
        );

        return "Training Assignment Updated Successfully";
    }


    public String deleteAssignment(Long id) {

        TrainingAssignment existing =
                trainingAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Training Assignment Not Found"));

        // Capture deleted value before deletion
        String deletedValue = convertToJson(existing);

        trainingAssignmentRepository.delete(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.createAuditLog(
                "TRAINING",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                String.valueOf(id),
                "Training assignment deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TRAINING_ASSIGNMENT",
                "TRAINING",
                "Training assignment deleted successfully. "
                        + "Assignment ID: "
                        + id,
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training assignment deleted successfully. "
                        + "Assignment ID: "
                        + id
        );

        return "Training Assignment Deleted Successfully";
    }




//=========================================================
// COURSE COMPLETION
//=========================================================

    public String createCompletion(Long assignmentId, CourseCompletion completion) {

        TrainingAssignment assignment =
                trainingAssignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new RuntimeException("Training Assignment Not Found"));

        completion.setAssignment(assignment);
        completion.setEmployee(assignment.getEmployee());

        courseCompletionRepository.save(completion);

        // Update assignment status automatically
        assignment.setAssignmentStatus("COMPLETED");
        trainingAssignmentRepository.save(assignment);

        String performedBy = getLoggedInEmployeeId();

        String newValue = convertToJson(completion);

        auditLogsService.logCreate(
                "TRAINING",
                String.valueOf(completion.getId()),
                performedBy,
                completion.getEmployee().getEmployeeId(),
                "Course Completion created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_COURSE_COMPLETION",
                "TRAINING",
                "Course Completion created successfully for employee: "
                        + completion.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Course Completion created successfully for employee: "
                        + completion.getEmployee().getEmployeeId()
        );

        return "Course Completion Created Successfully";
    }


    public List<CourseCompletion> getAllCompletions() {

        return courseCompletionRepository.findAll();
    }


    public CourseCompletion getCompletion(Long id) {

        return courseCompletionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Course Completion Not Found"));
    }


    public List<CourseCompletion> getEmployeeCompletions(String employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException("Employee Not Found"));

        return courseCompletionRepository.findByEmployee(employee);
    }


    public String updateCompletion(
            Long id,
            CourseCompletion completion) {

        CourseCompletion existing =
                courseCompletionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Course Completion Not Found"));

        String oldValue = convertToJson(existing);

        existing.setCompletionDate(
                completion.getCompletionDate());

        existing.setCompletionPercentage(
                completion.getCompletionPercentage());

        existing.setAssessmentScore(
                completion.getAssessmentScore());

        existing.setGrade(
                completion.getGrade());

        existing.setResult(
                completion.getResult());

        existing.setRemarks(
                completion.getRemarks());

        courseCompletionRepository.save(existing);

        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TRAINING",
                String.valueOf(id),
                performedBy,
                existing.getEmployee().getEmployeeId(),
                "Course Completion updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_COURSE_COMPLETION",
                "TRAINING",
                "Course Completion updated successfully for employee: "
                        + existing.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Course Completion updated successfully"
        );

        return "Course Completion Updated Successfully";
    }


    public String deleteCompletion(Long id) {

        CourseCompletion existing =
                courseCompletionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Course Completion Not Found"));

        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        courseCompletionRepository.delete(existing);

        auditLogsService.createAuditLog(
                "TRAINING",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existing.getEmployee().getEmployeeId(),
                "Course Completion deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_COURSE_COMPLETION",
                "TRAINING",
                "Course Completion deleted successfully for employee: "
                        + existing.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Course Completion deleted successfully"
        );

        return "Course Completion Deleted Successfully";
    }


//=========================================================
// TRAINING CERTIFICATES
//=========================================================

    public String createCertificate(
            Long completionId,
            TrainingCertificate certificate) {

        CourseCompletion completion =
                courseCompletionRepository.findById(completionId)
                        .orElseThrow(() ->
                                new RuntimeException("Course Completion Not Found"));

        certificate.setCompletion(completion);
        certificate.setEmployee(completion.getEmployee());

        trainingCertificateRepository.save(certificate);

        String performedBy = getLoggedInEmployeeId();

        String newValue = convertToJson(certificate);

        auditLogsService.logCreate(
                "TRAINING",
                String.valueOf(certificate.getId()),
                performedBy,
                certificate.getEmployee().getEmployeeId(),
                "Training Certificate created successfully"
        );

        auditLogsService.logActivity(
                performedBy,
                "CREATE_TRAINING_CERTIFICATE",
                "TRAINING",
                "Training Certificate created successfully for employee: "
                        + certificate.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training Certificate created successfully for employee: "
                        + certificate.getEmployee().getEmployeeId()
        );

        return "Training Certificate Created Successfully";
    }


    public List<TrainingCertificate> getAllCertificates() {

        return trainingCertificateRepository.findAll();
    }


    public TrainingCertificate getCertificate(Long id) {

        return trainingCertificateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Training Certificate Not Found"));
    }


    public List<TrainingCertificate> getEmployeeCertificates(
            String employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException("Employee Not Found"));

        return trainingCertificateRepository.findByEmployee(employee);
    }


    public String updateCertificate(
            Long id,
            TrainingCertificate certificate) {

        TrainingCertificate existing =
                trainingCertificateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Training Certificate Not Found"));

        String oldValue = convertToJson(existing);

        existing.setCertificateNumber(
                certificate.getCertificateNumber());

        existing.setCertificateUrl(
                certificate.getCertificateUrl());

        existing.setIssueDate(
                certificate.getIssueDate());

        existing.setExpiryDate(
                certificate.getExpiryDate());

        existing.setIssuedBy(
                certificate.getIssuedBy());

        existing.setVerificationCode(
                certificate.getVerificationCode());

        existing.setStatus(
                certificate.getStatus());

        trainingCertificateRepository.save(existing);

        String newValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        auditLogsService.logUpdate(
                "TRAINING",
                String.valueOf(id),
                performedBy,
                existing.getEmployee().getEmployeeId(),
                "Training Certificate updated successfully",
                oldValue,
                newValue
        );

        auditLogsService.logActivity(
                performedBy,
                "UPDATE_TRAINING_CERTIFICATE",
                "TRAINING",
                "Training Certificate updated successfully for employee: "
                        + existing.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training Certificate updated successfully"
        );

        return "Training Certificate Updated Successfully";
    }


    public String deleteCertificate(Long id) {

        TrainingCertificate existing =
                trainingCertificateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Training Certificate Not Found"));

        String deletedValue = convertToJson(existing);

        String performedBy = getLoggedInEmployeeId();

        trainingCertificateRepository.delete(existing);

        auditLogsService.createAuditLog(
                "TRAINING",
                String.valueOf(id),
                com.HRMS.QuickDines.AuditLogs.Entity.AuditActionType.DELETE,
                performedBy,
                existing.getEmployee().getEmployeeId(),
                "Training Certificate deleted successfully",
                deletedValue,
                null,
                getIpAddress(),
                getOperatingSystem()
        );

        auditLogsService.logActivity(
                performedBy,
                "DELETE_TRAINING_CERTIFICATE",
                "TRAINING",
                "Training Certificate deleted successfully for employee: "
                        + existing.getEmployee().getEmployeeId(),
                ActivityStatus.SUCCESS,
                getIpAddress(),
                getBrowser(),
                getOperatingSystem()
        );

        auditLogsService.logInfo(
                "TRAINING",
                "TrainingService",
                "Training Certificate deleted successfully"
        );

        return "Training Certificate Deleted Successfully";
    }

}