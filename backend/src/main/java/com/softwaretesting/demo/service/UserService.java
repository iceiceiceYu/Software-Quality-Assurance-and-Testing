package com.softwaretesting.demo.service;

//import com.softwaretesting.demo.exception.*;
import com.softwaretesting.demo.domain.Client;
import com.softwaretesting.demo.repository.*;
import com.softwaretesting.demo.security.jwt.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private AuthenticationManager authenticationManager;
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;


    private PasswordEncoder passwordEncoder;


    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,
                       AuthenticationManager authenticationManager, JwtUserDetailsService jwtUserDetailsService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.passwordEncoder = passwordEncoder;

    }

    public String login(String username, String password) throws UsernameNotFoundException {
        Client user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User: '" + username + "' not found.");
        } else {
            String correctPassword = user.getPassword();
            if (password.equals(correctPassword)) {
                return jwtTokenUtil.generateToken(user);
            } else {
                return "密码错误！";
            }
        }
    }


}
