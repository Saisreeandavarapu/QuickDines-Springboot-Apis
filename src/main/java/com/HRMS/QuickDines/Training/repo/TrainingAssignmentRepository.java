package com.HRMS.QuickDines.Training.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Training.model.Training;
import com.HRMS.QuickDines.Training.model.TrainingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingAssignmentRepository extends JpaRepository<TrainingAssignment,Long> {
    List<TrainingAssignment> findByEmployee(Employee employee);

    List<TrainingAssignment> findByTraining(Training training);
}
