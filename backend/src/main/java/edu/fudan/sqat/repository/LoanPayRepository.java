package edu.fudan.sqat.repository;


import edu.fudan.sqat.domain.LoanPay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPayRepository extends CrudRepository<LoanPay, Long> {
    List<LoanPay> findByFineAfterPaidAndMoneyPaid(Double fineAfterPaid,Double moneyPaid);
}
