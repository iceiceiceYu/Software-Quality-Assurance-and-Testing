package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
    Loan findLoanById(Long id);

    Iterable<Loan> findLoanByAccountId(Long accountId);

    Iterable<Loan> findLoanByIsPaidOff(Boolean isPaidOff);
}
