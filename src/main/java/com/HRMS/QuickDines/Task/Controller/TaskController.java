package com.HRMS.QuickDines.Task.Controller;

import com.HRMS.QuickDines.Task.Service.TaskService;
import com.HRMS.QuickDines.Task.model.TaskAssignments;
import com.HRMS.QuickDines.Task.model.Tasks;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;


    //=================================
// TASKS
//=================================

    @PostMapping("/create")
    public ResponseEntity<?> createTask(
            @RequestBody Tasks task){

        return ResponseEntity.ok(service.createTask(task));
    }


    @GetMapping
    public ResponseEntity<?> getAllTasks(){

        return ResponseEntity.ok(service.getAllTasks());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getTask(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody Tasks task){

        return ResponseEntity.ok(service.updateTask(id, task));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long id){

        return ResponseEntity.ok(service.deleteTask(id));
    }



    //=================================
// TASK ASSIGNMENTS
//=================================

    @PostMapping("/assign/{taskId}")
    public ResponseEntity<?> assignTask(
            @PathVariable Long taskId,
            @RequestBody TaskAssignments assignment){

        return ResponseEntity.ok(service.assignTask(taskId, assignment));
    }


    @GetMapping("/assigned")
    public ResponseEntity<?> getAssignedTasks(){

        return ResponseEntity.ok(service.getAssignedTasks());
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeTasks(
            @PathVariable String employeeId){

        return ResponseEntity.ok(service.getEmployeeTasks(employeeId));
    }


    @PutMapping("/update-status/{taskId}")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam String status){

        return ResponseEntity.ok(service.updateTaskStatus(taskId, status));
    }


    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.deleteAssignment(id));
    }



    //=================================
// TASK REPORTS
//=================================

    @PostMapping("/report/{employeeId}")
    public ResponseEntity<?> generateTaskReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.generateTaskReport(employeeId));
    }


    @GetMapping("/report/{employeeId}")
    public ResponseEntity<?> getTaskReport(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(service.getTaskReport(employeeId));
    }


    @GetMapping("/performance-report")
    public ResponseEntity<?> getPerformanceReport() {

        return ResponseEntity.ok(service.getPerformanceReport());
    }

    //=================================
// TASK STATUS MANAGEMENT
//=================================

    @PostMapping("/start/{taskId}")
    public ResponseEntity<?> startTask(
            @PathVariable Long taskId){

        return ResponseEntity.ok(service.startTask(taskId));
    }


    @PostMapping("/complete/{taskId}")
    public ResponseEntity<?> completeTask(
            @PathVariable Long taskId){

        return ResponseEntity.ok(service.completeTask(taskId));
    }


    @PostMapping("/on-hold/{taskId}")
    public ResponseEntity<?> holdTask(
            @PathVariable Long taskId){

        return ResponseEntity.ok(service.holdTask(taskId));
    }


    @PostMapping("/reject/{taskId}")
    public ResponseEntity<?> rejectTask(
            @PathVariable Long taskId){

        return ResponseEntity.ok(service.rejectTask(taskId));
    }

}
