package edu.fudan.sqat.service;

import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepayService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final LoanPayRepository loanPayRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public RepayService(AccountRepository accountRepository,
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

    public List<Loan> loanInfo(Long id) {
        return null;
    }


    public String repayment(RepaymentRequest repaymentRequest) {
        return null;
    }

    public String autoRepayment() {
        return null;
    }
}
