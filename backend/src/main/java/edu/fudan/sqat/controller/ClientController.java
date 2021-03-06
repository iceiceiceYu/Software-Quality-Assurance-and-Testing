package edu.fudan.sqat.controller;

import edu.fudan.sqat.controller.request.TransferRequest;
import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client/accountInfo")
    public ResponseEntity<Account> accountInfo(@RequestBody String IDCode) {
        return ResponseEntity.ok(clientService.accountInfo(IDCode));
    }

    @PostMapping("/client/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(clientService.transfer(
                transferRequest.getFromIDCode(),
                transferRequest.getToIDCode(),
                transferRequest.getAmount()));
    }
}
