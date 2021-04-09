package com.softwaretesting.demo.controller;

import com.softwaretesting.demo.controller.request.*;
import com.softwaretesting.demo.domain.*;
import com.softwaretesting.demo.service.JwtUserDetailsService;
import com.softwaretesting.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyl
 */
@RestController
public class Controller {
    private UserService userService;
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public Controller(UserService userService, JwtUserDetailsService jwtUserDetailsService) {
        this.userService = userService;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
//
//        Map<String, Object> response = new HashMap<>();
//        Long id = userService.login(request.getUsername(), request.getPassword(),request.getAuthority(),request.getTreatmentArea());
//        final UserDetails targetUser = jwtUserDetailsService.loadUserByUsername(request.getUsername());
//        Long authorityId=userService.findAuthorityId(request.getUsername());
//        response.put("Id", id);
//        response.put("authorityId",authorityId);
//        response.put("userDetails", targetUser);
//        return ResponseEntity.ok(response);
//    }



}



