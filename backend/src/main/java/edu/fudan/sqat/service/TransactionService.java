package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.Transaction;
import edu.fudan.sqat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final LoanPayRepository loanPayRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(AccountRepository accountRepository,
                              ClientRepository clientRepository,
                              LoanPayRepository loanPayRepository,
                              LoanRepository loanRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.loanPayRepository = loanPayRepository;
        this.loanRepository = loanRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> find(Date start, Date end) {
        if (start == null && end == null) {
            return findAll();
        } else {
            return findBetween(start, end);
        }
    }

    private List<Transaction> findAll() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    private List<Transaction> findBetween(Date start, Date end) {
        return (List<Transaction>) transactionRepository.findTransactionByTimeBetween(start, end);
    }
}
