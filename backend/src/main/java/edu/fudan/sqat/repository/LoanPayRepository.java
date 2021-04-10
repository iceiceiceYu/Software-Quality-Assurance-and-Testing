package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.LoanPay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPayRepository extends CrudRepository<LoanPay, Long> {
}
