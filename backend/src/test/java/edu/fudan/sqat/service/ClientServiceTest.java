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
        Client client = new Client("zzz123", "name", "male", 18);
        clientRepository.save(client);
        Account account = new Account("zzz123");
        accountRepository.save(account);
        clientService.accountInfo("zzz23");
        assertNotNull(clientService.accountInfo("zzz123"));
        assertNull(clientService.accountInfo("0"));
    }

    @Test
    void isValid() {
        assertTrue(clientService.isValid("12345"));
        assertFalse(clientService.isValid("0"));
    }

    @Test
    void transfer() {
        Client fromClient = new Client("999", "name999", "male", 99);
        Client toClient = new Client("998", "name998", "male", 98);
        clientRepository.save(fromClient);
        clientRepository.save(toClient);
        // 1. from账户钱不够
        Account account =new Account("999",1000.0);
        Account account2 =new Account("998",1000.0);
        accountRepository.save(account);
        accountRepository.save(account2);
        account = accountRepository.findAccountByIDCode("zzz");
        assertEquals(clientService.transfer("999", "998", 1200.0), "wrong money");
        // 2. to账户不存在
        assertEquals(clientService.transfer("999", "997", 1200.0), "wrong object");
        // 3. 正常

        assertEquals(clientService.transfer("999", "998", 800.0), "success");

    }
}
