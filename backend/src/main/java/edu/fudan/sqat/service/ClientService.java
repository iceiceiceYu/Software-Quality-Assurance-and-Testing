package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Account;
import edu.fudan.sqat.domain.Transaction;
import edu.fudan.sqat.repository.AccountRepository;
import edu.fudan.sqat.repository.ClientRepository;
import edu.fudan.sqat.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClientService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public ClientService(AccountRepository accountRepository, ClientRepository clientRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account accountInfo(String IDCode) {
        if (!isValid(IDCode)) {
            return null;
        }
        return accountRepository.findAccountByIDCode(IDCode);
    }

    public Boolean isValid(String IDCode) {
        return clientRepository.findClientByIDCode(IDCode) != null;
    }

    public String transfer(String fromIDCode, String toIDCode, Double amount) {
        if (!isValid(toIDCode)) {
            return "wrong object";
        }

        Account fromAccount = accountRepository.findAccountByIDCode(fromIDCode);
        Double fromTotal = fromAccount.getTotal();
        if (fromTotal < amount) {
            return "wrong money";
        }

        Account toAccount = accountRepository.findAccountByIDCode(toIDCode);
        Double toTotal = toAccount.getTotal();
        Date date = new Date();

        fromTotal -= amount;
        Transaction fromTransaction = new Transaction(fromAccount, -amount, fromTotal, "Transfer Outlay", date);
        transactionRepository.save(fromTransaction);
        toTotal += amount;
        Transaction toTransaction = new Transaction(toAccount, amount, toTotal, "Transfer Income", date);
        transactionRepository.save(toTransaction);

        fromAccount.setTotal(fromTotal);
        accountRepository.save(fromAccount);
        toAccount.setTotal(toTotal);
        accountRepository.save(toAccount);

        return "success";
    }
}
