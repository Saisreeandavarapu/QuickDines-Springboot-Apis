package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByInterviewStatus(String interviewStatus);

}
