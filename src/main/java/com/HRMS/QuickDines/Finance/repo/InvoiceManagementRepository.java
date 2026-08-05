package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.InvoiceManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceManagementRepository extends JpaRepository<InvoiceManagement, Long> {
    Optional<InvoiceManagement> findByInvoiceNumber(String invoiceNumber);

    List<InvoiceManagement> findByCustomerId(Long customerId);

    List<InvoiceManagement> findByVendorId(Long vendorId);
}
