package com.HRMS.QuickDines.Recruitment.repo;

import com.HRMS.QuickDines.Recruitment.model.CandidateDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDocumentRepository extends JpaRepository<CandidateDocument, Long> {
}
