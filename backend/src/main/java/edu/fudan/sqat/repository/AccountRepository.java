package edu.fudan.sqat.repository;

import edu.fudan.sqat.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zyl
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
