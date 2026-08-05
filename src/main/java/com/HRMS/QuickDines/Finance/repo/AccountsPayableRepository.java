package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.AccountsPayable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsPayableRepository extends JpaRepository<AccountsPayable, Long> {
    Optional<AccountsPayable> findByInvoiceNumber(String invoiceNumber);
}
