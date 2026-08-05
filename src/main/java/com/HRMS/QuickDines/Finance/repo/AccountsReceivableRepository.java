package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.AccountsReceivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsReceivableRepository extends JpaRepository<AccountsReceivable, Long> {
    Optional<AccountsReceivable> findByInvoiceNumber(String invoiceNumber);
}
