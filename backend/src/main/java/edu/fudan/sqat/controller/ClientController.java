package edu.fudan.sqat.controller;

import edu.fudan.sqat.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ClientController {


    @PostMapping("/client/accountInfo")
    public ResponseEntity<List<Account>> accountInfo(@RequestBody String IDCode) {

    }
}
