package com.HRMS.QuickDines.Task.repo;

import com.HRMS.QuickDines.Task.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
}
