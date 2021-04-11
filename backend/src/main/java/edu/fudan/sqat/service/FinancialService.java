package edu.fudan.sqat.service;

import edu.fudan.sqat.domain.*;
import edu.fudan.sqat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FinancialService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final FinancialProductRepository financialProductRepository;
    private final LoanPayRepository loanPayRepository;
    private final LoanRepository loanRepository;
    private final PurchaseRepository purchaseRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public FinancialService(AccountRepository accountRepository,
                            ClientRepository clientRepository,
                            FinancialProductRepository financialProductRepository,
                            LoanPayRepository loanPayRepository,
                            LoanRepository loanRepository,
                            PurchaseRepository purchaseRepository,
                            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.financialProductRepository = financialProductRepository;
        this.loanPayRepository = loanPayRepository;
        this.loanRepository = loanRepository;
        this.purchaseRepository = purchaseRepository;
        this.transactionRepository = transactionRepository;
    }

    public Integer accountLevel(String IDCode) {
        Account account = accountRepository.findAccountByIDCode(IDCode);
        Double total = account.getTotal();
        Long accountId = account.getId();

        List<Loan> loans = (List<Loan>) loanRepository.findLoanByAccountId(accountId);

        Double totalLoan = 0.0;
        for (Loan loan : loans) {
            List<LoanPay> loanPays = loan.getLoanPays();
            Integer stage = loan.getStageCount();

            Double amountPerStage = loanPays.get(1).getAmount();
            LoanPay currLoanPay = loanPays.get(loanPays.size() - 1);
            Double currAmount = currLoanPay.getAmount();
            Integer currStage = currLoanPay.getStage();

            totalLoan += currAmount + (stage - currStage) * amountPerStage;
        }

        if (total - totalLoan > 500000) {
            return 1;
        } else if (total - totalLoan >= 0) {
            return 2;
        } else {
            return 3;
        }
    }

    public List<FinancialProduct> allInfo() {
        return (List<FinancialProduct>) financialProductRepository.findAll();
    }

    public String purchaseProduct(String IDCode, String name, String type, Integer stockAmount, Date date) {
        Account account = accountRepository.findAccountByIDCode(IDCode);

        Double total = account.getTotal();
        Long accountId = account.getId();
        Double fine = checkFine(accountId);

        if (fine > 0) {
            if (total < fine) {
                return "cannot pay fine";
            } else {
                total = total - fine;
                Transaction transaction = new Transaction(account, -fine, total, "Fine Pay Outlay", date);
                transactionRepository.save(transaction);
                account.setTotal(total);
                accountRepository.save(account);
            }
        }

        FinancialProduct financialProduct = financialProductRepository.findFinancialProductByName(name);
        Double price = financialProduct.getPrice();

        if (total < price) {
            return "cannot pay financial product";
        } else {
            total = total - price;
            Transaction transaction = new Transaction(account, -price, total, "Financial Management Outlay", date);
            transactionRepository.save(transaction);
            Purchase purchase = new Purchase(IDCode, name, type, stockAmount, date);
            purchaseRepository.save(purchase);
            account.setTotal(total);
            accountRepository.save(account);
        }
        return "success";
    }

    private Double checkFine(Long accountId) {
        List<Loan> loans = (List<Loan>) loanRepository.findLoanByAccountId(accountId);

        for (Loan loan : loans) {
            List<LoanPay> loanPays = loan.getLoanPays();
            LoanPay currLoanPay = loanPays.get(loanPays.size() - 1);
            if (currLoanPay.getFine() > 0) {
                return currLoanPay.getFine();
            }
        }
        return 0.0;
    }
}
