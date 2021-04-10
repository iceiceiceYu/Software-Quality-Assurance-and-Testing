package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Iterable<Transaction> findAll();

    Iterable<Transaction> findTransactionByTimeBetween(Date start, Date end);
}
