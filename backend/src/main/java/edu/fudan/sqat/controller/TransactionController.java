package edu.fudan.sqat.controller;

import edu.fudan.sqat.controller.request.TransactionRequest;
import edu.fudan.sqat.domain.Transaction;
import edu.fudan.sqat.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/find")
    public ResponseEntity<List<Transaction>> find(@RequestBody TransactionRequest transactionRequest) throws ParseException {
        return ResponseEntity.ok(transactionService.find(transactionRequest.getStart(), transactionRequest.getEnd()));
    }
}
