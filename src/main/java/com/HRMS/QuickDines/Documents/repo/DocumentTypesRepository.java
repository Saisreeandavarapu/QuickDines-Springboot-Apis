package com.HRMS.QuickDines.Documents.repo;

import com.HRMS.QuickDines.Documents.model.DocumentTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypesRepository extends JpaRepository<DocumentTypes, Long> {
}
