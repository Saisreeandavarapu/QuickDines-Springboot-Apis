package com.HRMS.QuickDines.Task.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Task.model.TaskAssignments;
import com.HRMS.QuickDines.Task.model.TaskReports;
import com.HRMS.QuickDines.Task.model.Tasks;
import com.HRMS.QuickDines.Task.repo.TaskAssignmentsRepository;
import com.HRMS.QuickDines.Task.repo.TaskReportsRepository;
import com.HRMS.QuickDines.Task.repo.TasksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TasksRepository tasksRepository;
    private final TaskAssignmentsRepository taskAssignmentsRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskReportsRepository taskReportsRepository;

    //=================================
// TASKS
//=================================

    public String createTask(Tasks task){
        task.setStatus("PENDING");

        tasksRepository.save(task);

        return "Task Created Successfully";
    }

    public List<Tasks> getAllTasks(){

        return tasksRepository.findAll();
    }

    public Tasks getTask(Long id){
        return tasksRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));
    }

    public String updateTask(Long id, Tasks task){

        Tasks existingTask = tasksRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));

        existingTask.setTaskName(task.getTaskName());

        existingTask.setDescription(task.getDescription());

        existingTask.setPriority(task.getPriority());

        existingTask.setDeadline(task.getDeadline());

        existingTask.setStatus(task.getStatus());
        tasksRepository.save(existingTask);

        return "Task Updated Successfully";
    }

    public String deleteTask(Long id){

        Tasks task = tasksRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Not Found"));
        tasksRepository.delete(task);

        return "Task Deleted Successfully";
    }



    //=================================
// TASK ASSIGNMENTS
//=================================

    public String assignTask(Long taskId, TaskAssignments assignment){

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));
        Employee assignedBy = employeeRepository.findById(assignment.getAssignedBy().getEmployeeId()).orElseThrow(() -> new RuntimeException("Assigned By Employee Not Found"));
        Employee assignedTo = employeeRepository.findById(assignment.getAssignedTo().getEmployeeId()).orElseThrow(() -> new RuntimeException("Assigned To Employee Not Found"));

        assignment.setTask(task);
        assignment.setAssignedBy(assignedBy);
        assignment.setAssignedTo(assignedTo);
        assignment.setAssignedDate(LocalDate.now());

        taskAssignmentsRepository.save(assignment);
        task.setStatus("ASSIGNED");
        tasksRepository.save(task);


        return "Task Assigned Successfully";
    }

    public List<TaskAssignments> getAssignedTasks(){
        return taskAssignmentsRepository.findAll();
    }

    public List<TaskAssignments> getEmployeeTasks(String employeeId){

        return taskAssignmentsRepository.findByAssignedToEmployeeId(employeeId);
    }
    public String updateTaskStatus(
            Long taskId,
            String status){

        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Task Not Found"));


        task.setStatus(status);

        tasksRepository.save(task);


        return "Task Status Updated Successfully";
    }

    public String deleteAssignment(Long id){
        TaskAssignments assignment = taskAssignmentsRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Assignment Not Found"));
        taskAssignmentsRepository.delete(assignment);


        return "Task Assignment Deleted Successfully";
    }



    //=================================
// TASK REPORTS
//=================================

    public String generateTaskReport(String employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee Not Found"));
        List<TaskAssignments> assignments =taskAssignmentsRepository.findByAssignedToEmployeeId(employeeId);
        Integer completedTasks = 0;
        Integer pendingTasks = 0;

        for (TaskAssignments assignment : assignments) {

            String status = assignment.getTask().getStatus();
            if ("COMPLETED".equalsIgnoreCase(status)) {
                completedTasks++;
            } else {
                pendingTasks++;
            }

        }
        Integer totalTasks = completedTasks + pendingTasks;
        Double performancePercentage = 0.0;

        if (totalTasks > 0) {
            performancePercentage = ((double) completedTasks / totalTasks) * 100;
        }

        TaskReports report = new TaskReports();

        report.setEmployee(employee);
        report.setCompletedTasks(completedTasks);
        report.setPendingTasks(pendingTasks);
        report.setPerformancePercentage(performancePercentage);
        taskReportsRepository.save(report);
        return "Task Report Generated Successfully";
    }

    public Object getTaskReport(String employeeId) {

        return taskReportsRepository.findByEmployeeEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Task Report Not Found"));
    }

    public Object getPerformanceReport() {
        return taskReportsRepository.findAll();
    }


    public String startTask(Long taskId){

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));
        task.setStatus("IN_PROGRESS");

        tasksRepository.save(task);

        return "Task Started Successfully";
    }


    public String completeTask(Long taskId){

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        task.setStatus("COMPLETED");

        tasksRepository.save(task);

        return "Task Completed Successfully";
    }
    public String holdTask(Long taskId){

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException(                 "Task Not Found"));

        task.setStatus("ON_HOLD");

        tasksRepository.save(task);

        return "Task Put On Hold Successfully";
    }
    public String rejectTask(Long taskId){

        Tasks task = tasksRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

        task.setStatus("REJECTED");

        tasksRepository.save(task);

        return "Task Rejected Successfully";
    }
}