package com.HRMS.QuickDines.Training.Controller;

import com.HRMS.QuickDines.Training.Entity.AssignmentStatus;
import com.HRMS.QuickDines.Training.Entity.TrainingStatus;
import com.HRMS.QuickDines.Training.Service.TrainingService;
import com.HRMS.QuickDines.Training.model.CourseCompletion;
import com.HRMS.QuickDines.Training.model.Training;
import com.HRMS.QuickDines.Training.model.TrainingAssignment;
import com.HRMS.QuickDines.Training.model.TrainingCertificate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService service;


    //=========================================================
    // TRAININGS
    //=========================================================

    @PreAuthorize("hasAuthority('TRAINING_CREATE')")
    @PostMapping("/create")
    public ResponseEntity<?> createTraining(
            @RequestBody Training training) {

        return ResponseEntity.ok(
                service.createTraining(training));
    }

    @PreAuthorize("hasAuthority('TRAINING_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllTrainings() {

        return ResponseEntity.ok(
                service.getAllTrainings());
    }

    @PreAuthorize("hasAuthority('TRAINING_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTraining(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTraining(id));
    }

    @PreAuthorize("hasAuthority('TRAINING_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(
            @PathVariable Long id,
            @RequestBody Training training) {

        return ResponseEntity.ok(
                service.updateTraining(id, training));
    }

    @PreAuthorize("hasAuthority('TRAINING_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTraining(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTraining(id));
    }


    //=========================================================
    // TRAINING ASSIGNMENTS
    //=========================================================

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_CREATE')")
    @PostMapping("/assignment/{trainingId}/{employeeId}")
    public ResponseEntity<?> createAssignment(
            @PathVariable Long trainingId,
            @PathVariable String employeeId,
            @RequestBody TrainingAssignment assignment) {

        return ResponseEntity.ok(
                service.createAssignment(
                        trainingId,
                        employeeId,
                        assignment));
    }

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_READ')")
    @GetMapping("/assignments")
    public ResponseEntity<?> getAllAssignments() {

        return ResponseEntity.ok(
                service.getAllAssignments());
    }

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_READ')")
    @GetMapping("/assignment/{id}")
    public ResponseEntity<?> getAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAssignment(id));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_TRAINING_READ')")
    @GetMapping("/employee/{employeeId}/assignments")
    public ResponseEntity<?> getEmployeeAssignments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeAssignments(employeeId));
    }

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_READ')")
    @GetMapping("/training/{trainingId}/assignments")
    public ResponseEntity<?> getTrainingAssignments(
            @PathVariable Long trainingId) {

        return ResponseEntity.ok(
                service.getTrainingAssignments(trainingId));
    }

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_UPDATE')")
    @PutMapping("/assignment/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @RequestBody TrainingAssignment assignment) {

        return ResponseEntity.ok(
                service.updateAssignment(id, assignment));
    }

    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_DELETE')")
    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAssignment(id));
    }


    //=========================================================
    // COURSE COMPLETION
    //=========================================================

    @PreAuthorize("hasAuthority('COURSE_COMPLETION_CREATE')")
    @PostMapping("/completion/{assignmentId}")
    public ResponseEntity<?> createCompletion(
            @PathVariable Long assignmentId,
            @RequestBody CourseCompletion completion) {

        return ResponseEntity.ok(
                service.createCompletion(
                        assignmentId,
                        completion));
    }

    @PreAuthorize("hasAuthority('COURSE_COMPLETION_READ')")
    @GetMapping("/completions")
    public ResponseEntity<?> getAllCompletions() {

        return ResponseEntity.ok(
                service.getAllCompletions());
    }

    @PreAuthorize("hasAuthority('COURSE_COMPLETION_READ')")
    @GetMapping("/completion/{id}")
    public ResponseEntity<?> getCompletion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCompletion(id));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_COMPLETION_READ')")
    @GetMapping("/employee/{employeeId}/completions")
    public ResponseEntity<?> getEmployeeCompletions(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeCompletions(employeeId));
    }

    @PreAuthorize("hasAuthority('COURSE_COMPLETION_UPDATE')")
    @PutMapping("/completion/{id}")
    public ResponseEntity<?> updateCompletion(
            @PathVariable Long id,
            @RequestBody CourseCompletion completion) {

        return ResponseEntity.ok(
                service.updateCompletion(id, completion));
    }

    @PreAuthorize("hasAuthority('COURSE_COMPLETION_DELETE')")
    @DeleteMapping("/completion/{id}")
    public ResponseEntity<?> deleteCompletion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCompletion(id));
    }


    //=========================================================
    // TRAINING CERTIFICATES
    //=========================================================

    @PreAuthorize("hasAuthority('TRAINING_CERTIFICATE_CREATE')")
    @PostMapping("/certificate/{completionId}")
    public ResponseEntity<?> createCertificate(
            @PathVariable Long completionId,
            @RequestBody TrainingCertificate certificate) {

        return ResponseEntity.ok(
                service.createCertificate(
                        completionId,
                        certificate));
    }

    @PreAuthorize("hasAuthority('TRAINING_CERTIFICATE_READ')")
    @GetMapping("/certificates")
    public ResponseEntity<?> getAllCertificates() {

        return ResponseEntity.ok(
                service.getAllCertificates());
    }

    @PreAuthorize("hasAuthority('TRAINING_CERTIFICATE_READ')")
    @GetMapping("/certificate/{id}")
    public ResponseEntity<?> getCertificate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCertificate(id));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE_CERTIFICATE_READ')")
    @GetMapping("/employee/{employeeId}/certificates")
    public ResponseEntity<?> getEmployeeCertificates(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeCertificates(employeeId));
    }

    @PreAuthorize("hasAuthority('TRAINING_CERTIFICATE_UPDATE')")
    @PutMapping("/certificate/{id}")
    public ResponseEntity<?> updateCertificate(
            @PathVariable Long id,
            @RequestBody TrainingCertificate certificate) {

        return ResponseEntity.ok(
                service.updateCertificate(id, certificate));
    }

    @PreAuthorize("hasAuthority('TRAINING_CERTIFICATE_DELETE')")
    @DeleteMapping("/certificate/{id}")
    public ResponseEntity<?> deleteCertificate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCertificate(id));
    }
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('TRAINING_READ')")
    public ResponseEntity<?> getTrainingsByStatus(
            @PathVariable TrainingStatus status) {

        return ResponseEntity.ok(
                service
                        .getTrainingsByStatus(status));
    }

    @GetMapping("/filter/trainings")
    @PreAuthorize("hasAuthority('TRAINING_READ')")
    public ResponseEntity<?> filterTrainings(
            @RequestParam(required = false)
            TrainingStatus status) {

        return ResponseEntity.ok(
                service.filterTrainings(status));
    }
    @GetMapping("/filter/assignments")
    @PreAuthorize("hasAuthority('TRAINING_ASSIGNMENT_READ')")
    public ResponseEntity<?> filterAssignments(
            @RequestParam(required = false)
            AssignmentStatus status) {

        return ResponseEntity.ok(
                service
                        .filterAssignments(status));
    }
}
