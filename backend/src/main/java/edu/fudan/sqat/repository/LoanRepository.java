package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findByAccountId(Long accountId);
    List<Loan> findByIsPaidOff(Boolean isPaidOff);
}
