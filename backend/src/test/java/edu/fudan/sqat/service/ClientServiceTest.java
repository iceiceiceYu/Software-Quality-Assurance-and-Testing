package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Client;
import edu.fudan.sqat.repository.AccountRepository;
import edu.fudan.sqat.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void accountInfo() {
        Client client =new Client("123","name","male",18);
        clientRepository.save(client);
        Account account =new Account("123");
        accountRepository.save(account);
        clientService.accountInfo("123");
        assertNotNull(clientService.accountInfo("123"));
        assertNull(clientService.accountInfo("0"));
    }

    @Test
    void transfer() {
    }
}
