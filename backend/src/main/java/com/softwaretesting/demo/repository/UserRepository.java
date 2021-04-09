package com.softwaretesting.demo.repository;


import com.softwaretesting.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author zyl
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String username);
//    User findById(Long uid);

}
