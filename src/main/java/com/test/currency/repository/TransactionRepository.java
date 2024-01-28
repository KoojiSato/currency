package com.test.currency.repository;

import com.test.currency.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransactionDate(Date transactionDate);

    @Query("SELECT t FROM Transaction t " +
            " WHERE t.transactionDate <= :endDay " +
            " AND t.transactionDate >= :startDay" +
            " AND t.clientId = :clientId")
    List<Transaction> findByTransactionDateClient(@Param("startDay") Date startDay,@Param("endDay") Date endDay, @Param("clientId")Long clientId);

    @Query("SELECT t FROM Transaction t " +
            " WHERE t.transactionDate <= :endDay " +
            " AND t.transactionDate >= :startDay ")
    List<Transaction> findByTransactionDate(@Param("startDay") Date startDay,@Param("endDay") Date endDay);


}
