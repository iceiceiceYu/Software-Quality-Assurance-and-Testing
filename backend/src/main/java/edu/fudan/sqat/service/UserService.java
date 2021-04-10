package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.User;
import edu.fudan.sqat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("username " + username + " not found");
        }

        String correctPassword = user.getPassword();
        if (!correctPassword.equals(password)) {
            throw new Exception("wrong password");
        }

        return user;
    }
}
