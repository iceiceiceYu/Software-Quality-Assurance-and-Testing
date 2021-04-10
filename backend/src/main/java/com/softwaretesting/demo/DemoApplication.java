package com.softwaretesting.demo;

import com.softwaretesting.demo.repository.*;
import com.softwaretesting.demo.service.*;
import com.softwaretesting.demo.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, AuthorityRepository authorityRepository,
                                        AuthenticationManager authenticationManager, JwtUserDetailsService jwtUserDetailsService, PasswordEncoder passwordEncoder) {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.
                //Authority adminAuthority = getOrCreateAuthority("Doctor", authorityRepository);
//                getOrCreateAuthority("Head_nurse", authorityRepository);
//                getOrCreateAuthority("Ward_nurse", authorityRepository);
//                getOrCreateAuthority("Emergency_nurse", authorityRepository);


            }



            private Authority getOrCreateAuthority(String authorityText, AuthorityRepository authorityRepository) {
                Authority authority = authorityRepository.findByAuthority(authorityText);
                if (authority == null) {
                    authority = new Authority(authorityText);
                    authorityRepository.save(authority);
                }
                return authority;
            }


        };
    }




}
