package com.HRMS.QuickDines.Finance.repo;

import com.HRMS.QuickDines.Finance.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByTransactionStatus(String success);

    Long countByTransactionStatus(String success);
}
