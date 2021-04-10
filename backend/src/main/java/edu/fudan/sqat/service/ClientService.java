package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Client;
import edu.fudan.sqat.repository.AccountRepository;
import edu.fudan.sqat.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    // TODO：从List<Account>到Account 只有一个Account
    public Account accountInfo(String IDCode) throws Exception {
        if (!isValid(IDCode)) {
            throw new Exception("this client doesn't have an account in our system, or you may input a wrong IDCode");
        }
        return accountRepository.findAccountByIDCode(IDCode);
    }

    private Boolean isValid(String IDCode) {
        return clientRepository.findClientByIDCode(IDCode) != null;
    }
}
