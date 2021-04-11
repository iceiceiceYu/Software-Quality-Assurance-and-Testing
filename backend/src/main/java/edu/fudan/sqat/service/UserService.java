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

    public User login(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }

        String correctPassword = user.getPassword();
        if (!correctPassword.equals(password)) {
            return null;
        }

        return user;
    }
}
