package com.HRMS.QuickDines.Training.repo;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Training.model.CourseCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCompletionRepository extends JpaRepository<CourseCompletion,Long> {
    List<CourseCompletion> findByEmployee(Employee employee);
}
