package com.softwaretesting.demo.repository;


import com.softwaretesting.demo.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zyl
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
