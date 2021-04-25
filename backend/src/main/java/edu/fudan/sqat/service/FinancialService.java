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
    private final FinancialProductRepository financialProductRepository;
    private final LoanRepository loanRepository;
    private final PurchaseRepository purchaseRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public FinancialService(AccountRepository accountRepository,
                            FinancialProductRepository financialProductRepository,
                            LoanRepository loanRepository,
                            PurchaseRepository purchaseRepository,
                            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.financialProductRepository = financialProductRepository;
        this.loanRepository = loanRepository;
        this.purchaseRepository = purchaseRepository;
        this.transactionRepository = transactionRepository;
    }

    public String increase() {
        List<Purchase> purchases = (List<Purchase>) purchaseRepository.findAll();
        for (Purchase purchase : purchases) {
            String type = purchase.getType();
            Double capital = purchase.getCapital();
            Double profit = purchase.getProfit();
            double gain = 0.0, modify;
            String source = "Financial Management Income";

            switch (type) {
                case "stock":
                    modify = ((int) (Math.random() * 21) - 10) / 10.0;
                    gain = (capital + profit) * modify;
                    profit += gain;
                    if (modify < 0) {
                        source = "Financial Management Outlay";
                    }
                    break;
                case "fund":
                    modify = 0.1;
                    gain = (capital + profit) * modify;
                    profit += gain;
                    break;
                case "deposit":
                    modify = 0.05;
                    gain = (capital + profit) * modify;
                    profit += gain;
                    break;
                default:
                    continue;
            }
            purchase.setProfit(profit);
            purchaseRepository.save(purchase);

            Account account = accountRepository.findAccountByIDCode(purchase.getIDCode());
            Double total = account.getTotal();
            total += gain;

            Transaction transaction = new Transaction(account, gain, total, source, new Date());
            transactionRepository.save(transaction);
            account.setTotal(total);
            accountRepository.save(account);
        }
        return "success";
    }

    public List<Purchase> purchaseInfo(String IDCode) {
        return (List<Purchase>) purchaseRepository.findPurchaseByIDCode(IDCode);
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

            Double amountPerStage = loanPays.get(0).getAmount();
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
                total -= fine;
                Transaction transaction = new Transaction(account, -fine, total, "Fine Pay Outlay", date);
                transactionRepository.save(transaction);
                account.setTotal(total);
                accountRepository.save(account);
            }
        }

        FinancialProduct financialProduct = financialProductRepository.findFinancialProductByName(name);
        Double price = financialProduct.getPrice();
        if (type.equals("stock")) {
            price *= stockAmount;
        }

        if (total < price) {
            return "cannot pay financial product";
        } else {
            total -= price;
            Transaction transaction = new Transaction(account, -price, total, "Financial Management Outlay", date);
            transactionRepository.save(transaction);
            Purchase purchase = new Purchase(IDCode, name, type, stockAmount, date, price, 0.0);
            purchaseRepository.save(purchase);
            account.setTotal(total);
            accountRepository.save(account);
        }
        return "success";
    }

    public Double checkFine(Long accountId) {
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
