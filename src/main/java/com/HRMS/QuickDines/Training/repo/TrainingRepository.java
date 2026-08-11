package com.HRMS.QuickDines.Training.repo;

import com.HRMS.QuickDines.Training.Entity.TrainingStatus;
import com.HRMS.QuickDines.Training.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByStatus(
            TrainingStatus status);

}
