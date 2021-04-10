package com.softwaretesting.demo.repository;


import com.softwaretesting.demo.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author zyl
 */
@Repository
public interface UserRepository extends CrudRepository<Client, Long> {
    Client findByName(String username);
//    User findById(Long uid);


}
