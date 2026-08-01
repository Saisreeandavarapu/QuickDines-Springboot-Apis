package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicationStatus(String applicationStatus);
}
