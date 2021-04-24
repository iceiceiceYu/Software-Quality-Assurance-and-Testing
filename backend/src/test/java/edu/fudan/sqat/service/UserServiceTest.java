package edu.fudan.sqat.service;


import edu.fudan.sqat.domain.User;

import edu.fudan.sqat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void login(){
        User user=new User("zhangsan","123456","zhangnimasan");
        userRepository.save(user);
        assertNotNull(userService.login("zhangsan","123456"));
        assertNull(userService.login("lisi","lisi"));
        assertNull(userService.login("zhangsan","lisi"));
    }
}
