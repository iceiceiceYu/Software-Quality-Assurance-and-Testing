package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.repository.AccountRepository;
import edu.fudan.sqat.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public Account accountInfo(String IDCode) {
        if (!isValid(IDCode)) {
            return null;
        }
        return accountRepository.findAccountByIDCode(IDCode);
    }

    private Boolean isValid(String IDCode) {
        return clientRepository.findClientByIDCode(IDCode) != null;
    }
}
