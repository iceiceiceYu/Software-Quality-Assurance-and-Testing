package edu.fudan.sqat.controller;

import edu.fudan.sqat.domain.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class TransactionController {

    @PostMapping("/transaction/all")
    public ResponseEntity<List<Transaction>> all(@RequestBody String username) {

    }
}
