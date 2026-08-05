package com.HRMS.QuickDines.Training.Controller;

import com.HRMS.QuickDines.Training.Service.TrainingService;
import com.HRMS.QuickDines.Training.model.CourseCompletion;
import com.HRMS.QuickDines.Training.model.Training;
import com.HRMS.QuickDines.Training.model.TrainingAssignment;
import com.HRMS.QuickDines.Training.model.TrainingCertificate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService service;


    //=========================================================
    // TRAININGS
    //=========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createTraining(
            @RequestBody Training training) {

        return ResponseEntity.ok(
                service.createTraining(training));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllTrainings() {

        return ResponseEntity.ok(
                service.getAllTrainings());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTraining(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTraining(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(
            @PathVariable Long id,
            @RequestBody Training training) {

        return ResponseEntity.ok(
                service.updateTraining(id, training));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTraining(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteTraining(id));
    }


    //=========================================================
    // TRAINING ASSIGNMENTS
    //=========================================================

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


    @GetMapping("/assignments")
    public ResponseEntity<?> getAllAssignments() {

        return ResponseEntity.ok(
                service.getAllAssignments());
    }


    @GetMapping("/assignment/{id}")
    public ResponseEntity<?> getAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getAssignment(id));
    }


    @GetMapping("/employee/{employeeId}/assignments")
    public ResponseEntity<?> getEmployeeAssignments(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeAssignments(employeeId));
    }


    @GetMapping("/training/{trainingId}/assignments")
    public ResponseEntity<?> getTrainingAssignments(
            @PathVariable Long trainingId) {

        return ResponseEntity.ok(
                service.getTrainingAssignments(trainingId));
    }


    @PutMapping("/assignment/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @RequestBody TrainingAssignment assignment) {

        return ResponseEntity.ok(
                service.updateAssignment(id, assignment));
    }


    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteAssignment(id));
    }


    //=========================================================
    // COURSE COMPLETION
    //=========================================================

    @PostMapping("/completion/{assignmentId}")
    public ResponseEntity<?> createCompletion(
            @PathVariable Long assignmentId,
            @RequestBody CourseCompletion completion) {

        return ResponseEntity.ok(
                service.createCompletion(
                        assignmentId,
                        completion));
    }


    @GetMapping("/completions")
    public ResponseEntity<?> getAllCompletions() {

        return ResponseEntity.ok(
                service.getAllCompletions());
    }


    @GetMapping("/completion/{id}")
    public ResponseEntity<?> getCompletion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCompletion(id));
    }


    @GetMapping("/employee/{employeeId}/completions")
    public ResponseEntity<?> getEmployeeCompletions(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeCompletions(employeeId));
    }


    @PutMapping("/completion/{id}")
    public ResponseEntity<?> updateCompletion(
            @PathVariable Long id,
            @RequestBody CourseCompletion completion) {

        return ResponseEntity.ok(
                service.updateCompletion(id, completion));
    }


    @DeleteMapping("/completion/{id}")
    public ResponseEntity<?> deleteCompletion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCompletion(id));
    }


    //=========================================================
    // TRAINING CERTIFICATES
    //=========================================================

    @PostMapping("/certificate/{completionId}")
    public ResponseEntity<?> createCertificate(
            @PathVariable Long completionId,
            @RequestBody TrainingCertificate certificate) {

        return ResponseEntity.ok(
                service.createCertificate(
                        completionId,
                        certificate));
    }


    @GetMapping("/certificates")
    public ResponseEntity<?> getAllCertificates() {

        return ResponseEntity.ok(
                service.getAllCertificates());
    }


    @GetMapping("/certificate/{id}")
    public ResponseEntity<?> getCertificate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCertificate(id));
    }


    @GetMapping("/employee/{employeeId}/certificates")
    public ResponseEntity<?> getEmployeeCertificates(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                service.getEmployeeCertificates(employeeId));
    }


    @PutMapping("/certificate/{id}")
    public ResponseEntity<?> updateCertificate(
            @PathVariable Long id,
            @RequestBody TrainingCertificate certificate) {

        return ResponseEntity.ok(
                service.updateCertificate(id, certificate));
    }


    @DeleteMapping("/certificate/{id}")
    public ResponseEntity<?> deleteCertificate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.deleteCertificate(id));
    }
}