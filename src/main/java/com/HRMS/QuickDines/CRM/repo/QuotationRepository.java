package com.HRMS.QuickDines.CRM.repo;

import com.HRMS.QuickDines.CRM.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation,Long> {
}
