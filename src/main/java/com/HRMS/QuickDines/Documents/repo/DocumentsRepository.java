package com.HRMS.QuickDines.Documents.repo;

import com.HRMS.QuickDines.Documents.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    Optional<Documents> findByEmployeeEmployeeId(String employeeId);
    List<Documents> findByStatus(String status);
    Long countByStatus(String status);
}
