package com.HRMS.QuickDines.Administration.repo;

import com.HRMS.QuickDines.Administration.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,Long> {
}
