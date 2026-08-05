package com.HRMS.QuickDines.Training.Service;

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
import lombok.RequiredArgsConstructor;
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


    //=========================================================
    // TRAININGS
    //=========================================================

    public String createTraining(Training training) {

        trainingRepository.save(training);

        return "Training Created Successfully";
    }


    public List<Training> getAllTrainings() {

        return trainingRepository.findAll();
    }


    public Training getTraining(Long id) {

        return trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Not Found"));
    }


    public String updateTraining(Long id, Training training) {

        Training existing = trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Not Found"));

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

        return "Training Updated Successfully";
    }


    public String deleteTraining(Long id) {

        Training existing = trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Not Found"));

        trainingRepository.delete(existing);

        return "Training Deleted Successfully";
    }


    //=========================================================
    // TRAINING ASSIGNMENTS
    //=========================================================

    public String createAssignment(Long trainingId, String employeeId, TrainingAssignment assignment) {

        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("Training Not Found"));

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        assignment.setTraining(training);

        assignment.setEmployee(employee);

        trainingAssignmentRepository.save(assignment);

        return "Training Assigned Successfully";
    }


    public List<TrainingAssignment> getAllAssignments() {

        return trainingAssignmentRepository.findAll();
    }


    public TrainingAssignment getAssignment(Long id) {

        return trainingAssignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Assignment Not Found"));
    }


    public List<TrainingAssignment> getEmployeeAssignments(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        return trainingAssignmentRepository.findByEmployee(employee);
    }


    public List<TrainingAssignment> getTrainingAssignments(Long trainingId) {

        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("Training Not Found"));

        return trainingAssignmentRepository.findByTraining(training);
    }


    public String updateAssignment(Long id, TrainingAssignment assignment) {

        TrainingAssignment existing = trainingAssignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Assignment Not Found"));

        existing.setAssignedDate(assignment.getAssignedDate());

        existing.setDueDate(assignment.getDueDate());

        existing.setAssignmentStatus(assignment.getAssignmentStatus());

        existing.setRemarks(assignment.getRemarks());

        existing.setAssignedBy(assignment.getAssignedBy());

        trainingAssignmentRepository.save(existing);

        return "Training Assignment Updated Successfully";
    }


    public String deleteAssignment(Long id) {

        TrainingAssignment existing = trainingAssignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Assignment Not Found"));

        trainingAssignmentRepository.delete(existing);

        return "Training Assignment Deleted Successfully";
    }


    //=========================================================
    // COURSE COMPLETION
    //=========================================================

    public String createCompletion(Long assignmentId, CourseCompletion completion) {

        TrainingAssignment assignment = trainingAssignmentRepository.findById(assignmentId).orElseThrow(() -> new RuntimeException("Training Assignment Not Found"));

        completion.setAssignment(assignment);

        completion.setEmployee(assignment.getEmployee());

        courseCompletionRepository.save(completion);

        /*
         * Update assignment status automatically.
         */
        assignment.setAssignmentStatus("COMPLETED");

        trainingAssignmentRepository.save(assignment);

        return "Course Completion Created Successfully";
    }


    public List<CourseCompletion> getAllCompletions() {

        return courseCompletionRepository.findAll();
    }


    public CourseCompletion getCompletion(Long id) {

        return courseCompletionRepository.findById(id).orElseThrow(() -> new RuntimeException("Course Completion Not Found"));
    }


    public List<CourseCompletion> getEmployeeCompletions(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return courseCompletionRepository.findByEmployee(employee);
    }


    public String updateCompletion(Long id, CourseCompletion completion) {

        CourseCompletion existing = courseCompletionRepository.findById(id).orElseThrow(() -> new RuntimeException("Course Completion Not Found"));

        existing.setCompletionDate(completion.getCompletionDate());

        existing.setCompletionPercentage(completion.getCompletionPercentage());

        existing.setAssessmentScore(completion.getAssessmentScore());

        existing.setGrade(completion.getGrade());

        existing.setResult(completion.getResult());

        existing.setRemarks(completion.getRemarks());

        courseCompletionRepository.save(existing);

        return "Course Completion Updated Successfully";
    }


    public String deleteCompletion(Long id) {

        CourseCompletion existing = courseCompletionRepository.findById(id).orElseThrow(() -> new RuntimeException("Course Completion Not Found"));

        courseCompletionRepository.delete(existing);

        return "Course Completion Deleted Successfully";
    }


    //=========================================================
    // TRAINING CERTIFICATES
    //=========================================================

    public String createCertificate(Long completionId, TrainingCertificate certificate) {

        CourseCompletion completion = courseCompletionRepository.findById(completionId).orElseThrow(() -> new RuntimeException("Course Completion Not Found"));

        certificate.setCompletion(completion);

        certificate.setEmployee(completion.getEmployee());

        trainingCertificateRepository.save(certificate);

        return "Training Certificate Created Successfully";
    }


    public List<TrainingCertificate> getAllCertificates() {

        return trainingCertificateRepository.findAll();
    }


    public TrainingCertificate getCertificate(Long id) {

        return trainingCertificateRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Certificate Not Found"));
    }


    public List<TrainingCertificate> getEmployeeCertificates(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));

        return trainingCertificateRepository.findByEmployee(employee);
    }


    public String updateCertificate(Long id, TrainingCertificate certificate) {

        TrainingCertificate existing = trainingCertificateRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Certificate Not Found"));

        existing.setCertificateNumber(certificate.getCertificateNumber());

        existing.setCertificateUrl(certificate.getCertificateUrl());

        existing.setIssueDate(certificate.getIssueDate());

        existing.setExpiryDate(certificate.getExpiryDate());

        existing.setIssuedBy(certificate.getIssuedBy());

        existing.setVerificationCode(certificate.getVerificationCode());

        existing.setStatus(certificate.getStatus());

        trainingCertificateRepository.save(existing);

        return "Training Certificate Updated Successfully";
    }


    public String deleteCertificate(Long id) {

        TrainingCertificate existing = trainingCertificateRepository.findById(id).orElseThrow(() -> new RuntimeException("Training Certificate Not Found"));
        trainingCertificateRepository.delete(existing);

        return "Training Certificate Deleted Successfully";
    }
}